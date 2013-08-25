package uk.co.shadeddimensions.enhancedportals.util;

import java.util.LinkedList;
import java.util.Queue;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeDirection;
import uk.co.shadeddimensions.enhancedportals.multipart.MultipartUtils;
import uk.co.shadeddimensions.enhancedportals.multipart.PortalPart;
import uk.co.shadeddimensions.enhancedportals.network.CommonProxy;
import uk.co.shadeddimensions.enhancedportals.tileentity.TileEP;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalController;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrame;
import codechicken.multipart.TileMultipart;
import cpw.mods.fml.common.Loader;

public class PortalUtils
{
    /***
     * Creates a new portal. Uses any data in the frame if applicable.
     */
    public static boolean createPortal(WorldServer world, int x, int y, int z)
    {
        for (int i = 1; i < 4; i++)
        {
            if (createPortal(world, x, y, z, i))
            {
                return true;
            }
        }

        return false;
    }

    /***
     * Links all surrounding portal frame blocks to the controller
     */
    public static boolean linkController(WorldServer world, int x, int y, int z)
    {
        ChunkCoordinates linkLocation = new ChunkCoordinates(x, y, z);

        if (world.getBlockId(x, y, z) == CommonProxy.blockFrame.blockID)
        {
            for (int i = 0; i < 6; i++)
            {
                ForgeDirection d = ForgeDirection.getOrientation(i);
                ChunkCoordinates c = offsetCoordinate(new ChunkCoordinates(x, y, z), d);

                if (world.getBlockId(c.posX, c.posY, c.posZ) == CommonProxy.blockPortal.blockID)
                {
                    x = c.posX;
                    y = c.posY;
                    z = c.posZ;
                    break;
                }
            }
        }

        if (world.getBlockId(x, y, z) == CommonProxy.blockFrame.blockID)
        {
            return false; // Second check to make sure we now have a portal block selected
        }

        Queue<ChunkCoordinates> toProcess = new LinkedList<ChunkCoordinates>();
        Queue<ChunkCoordinates> portalBlocks = new LinkedList<ChunkCoordinates>();
        Queue<ChunkCoordinates> processed = new LinkedList<ChunkCoordinates>();
        toProcess.add(new ChunkCoordinates(x, y, z));
        int meta = world.getBlockMetadata(x, y, z);

        while (!toProcess.isEmpty())
        {
            ChunkCoordinates c = toProcess.remove();
            int id = world.getBlockId(c.posX, c.posY, c.posZ);

            if (!processed.contains(c) && (id == CommonProxy.blockFrame.blockID || id == CommonProxy.blockPortal.blockID))
            {
                if (id == CommonProxy.blockFrame.blockID)
                {
                    TileEP tile = (TileEP) world.getBlockTileEntity(c.posX, c.posY, c.posZ);

                    if (tile instanceof TilePortalFrame)
                    {
                        TilePortalFrame frame = (TilePortalFrame) tile;

                        if (!frame.checkController())
                        {
                            frame.controller = linkLocation;
                            CommonProxy.sendUpdatePacketToAllAround(frame); // TODO
                        }
                    }
                    else if (tile instanceof TilePortalController) // We found an existing controller within this portal frame, update to that controller
                    {
                        linkLocation = new ChunkCoordinates(tile.xCoord, tile.yCoord, tile.zCoord);
                        Queue<ChunkCoordinates> proc = duplicateQueue(processed);
                        proc.add(c);

                        // Go back and clean up your mess before continuing

                        while (!proc.isEmpty())
                        {
                            ChunkCoordinates cc = proc.remove();                            
                            TileEP t = (TileEP) world.getBlockTileEntity(cc.posX, cc.posY, cc.posZ);

                            if (t instanceof TilePortalFrame)
                            {
                                TilePortalFrame frame = (TilePortalFrame) t;

                                if (!frame.checkController())
                                {
                                    frame.controller = linkLocation;
                                    CommonProxy.sendUpdatePacketToAllAround(frame); // TODO
                                }
                            }
                        }
                    }
                }
                else
                {
                    portalBlocks.add(c);
                    addTouchingBlocks(c, toProcess, meta); // We don't want to follow the frame out to another portal..
                }

                processed.add(c);
            }
        }

        if (processed.size() == 0)
        {
            return false;
        }

        TileEP tile = (TileEP) world.getBlockTileEntity(linkLocation.posX, linkLocation.posY, linkLocation.posZ);

        if (!(tile instanceof TilePortalController))
        {
            world.setBlock(linkLocation.posX, linkLocation.posY, linkLocation.posZ, CommonProxy.blockFrame.blockID, 1, 3);
        }

        return true;
    }

    private static boolean createPortal(WorldServer world, int x, int y, int z, int meta)
    {
        if (Loader.isModLoaded("ForgeMultipart"))
        {
            return createPortalMultipart(world, x, y, z, meta);
        }
        else
        {
            return createPortalDefault(world, x, y, z, meta);
        }
    }
    
    private static boolean createPortalMultipart(WorldServer world, int x, int y, int z, int meta)
    {
        int USED_CHANCES = 0, MAX_CHANCES = 16;
        Queue<ChunkCoordinates> toProcess = new LinkedList<ChunkCoordinates>();
        Queue<ChunkCoordinates> addedBlocks = new LinkedList<ChunkCoordinates>();
        toProcess.add(new ChunkCoordinates(x, y, z));

        while (!toProcess.isEmpty())
        {
            ChunkCoordinates cur = toProcess.remove();

            if (!addedBlocks.contains(cur) && (world.isAirBlock(cur.posX, cur.posY, cur.posZ) || Block.blocksList[world.getBlockId(cur.posX, cur.posY, cur.posZ)].isBlockReplaceable(world, cur.posX, cur.posY, cur.posZ) || world.getBlockTileEntity(cur.posX, cur.posY, cur.posZ) instanceof TileMultipart))
            {
                int sides = getSides(world, cur, meta);

                if (sides == -1)
                {
                    removePortal(world, addedBlocks);
                    return false;
                }
                else if (sides < 2 && USED_CHANCES < MAX_CHANCES)
                {
                    USED_CHANCES++;
                    sides += 2;
                }

                if (sides >= 2)
                {
                    TileEntity t = world.getBlockTileEntity(cur.posX, cur.posY, cur.posZ);
                    
                    if (t != null && t instanceof TileMultipart)
                    {
                        if (!MultipartUtils.doesMultipartContainPortal((TileMultipart) t))
                        {
                            MultipartUtils.addPortalToPart((TileMultipart) t, meta);
                        }
                    }
                    else
                    {
                        world.setBlock(cur.posX, cur.posY, cur.posZ, CommonProxy.blockPortal.blockID, meta, 2);
                    }
                    
                    addedBlocks.add(cur);
                    addTouchingBlocks(cur, toProcess, meta);
                }
            }
        }

        Queue<ChunkCoordinates> portalBlocks = duplicateQueue(addedBlocks);

        while (!portalBlocks.isEmpty())
        {
            ChunkCoordinates cur = portalBlocks.remove();

            if (!checkTension(world, meta, cur))
            {
                removePortal(world, addedBlocks);
                return false;
            }
        }

        return true;
    }
    
    private static boolean createPortalDefault(WorldServer world, int x, int y, int z, int meta)
    {
        int USED_CHANCES = 0, MAX_CHANCES = 16;
        Queue<ChunkCoordinates> toProcess = new LinkedList<ChunkCoordinates>();
        Queue<ChunkCoordinates> addedBlocks = new LinkedList<ChunkCoordinates>();
        toProcess.add(new ChunkCoordinates(x, y, z));

        while (!toProcess.isEmpty())
        {
            ChunkCoordinates cur = toProcess.remove();

            if (!addedBlocks.contains(cur) && (world.isAirBlock(cur.posX, cur.posY, cur.posZ) || Block.blocksList[world.getBlockId(cur.posX, cur.posY, cur.posZ)].isBlockReplaceable(world, cur.posX, cur.posY, cur.posZ)))
            {
                int sides = getSides(world, cur, meta);

                if (sides == -1)
                {
                    removePortal(world, addedBlocks);
                    return false;
                }
                else if (sides < 2 && USED_CHANCES < MAX_CHANCES)
                {
                    USED_CHANCES++;
                    sides += 2;
                }

                if (sides >= 2)
                {
                    addedBlocks.add(cur);
                    world.setBlock(cur.posX, cur.posY, cur.posZ, CommonProxy.blockPortal.blockID, meta, 2);
                    addTouchingBlocks(cur, toProcess, meta);
                }
            }
        }

        Queue<ChunkCoordinates> portalBlocks = duplicateQueue(addedBlocks);

        while (!portalBlocks.isEmpty())
        {
            ChunkCoordinates cur = portalBlocks.remove();

            if (!checkTension(world, meta, cur))
            {
                removePortal(world, addedBlocks);
                return false;
            }
        }

        return true;
    }

    private static Queue<ChunkCoordinates> duplicateQueue(Queue<ChunkCoordinates> queue)
    {
        Queue<ChunkCoordinates> q = new LinkedList<ChunkCoordinates>();

        for (ChunkCoordinates c : queue)
        {
            q.add(c);
        }

        return q;
    }

    private static ChunkCoordinates offsetCoordinate(ChunkCoordinates coord, ForgeDirection dir)
    {
        return new ChunkCoordinates(coord.posX + dir.offsetX, coord.posY + dir.offsetY, coord.posZ + dir.offsetZ);
    }

    /***
     * Removes a portal at the specified location.
     */
    public static void removePortal(WorldServer world, int x, int y, int z, int meta)
    {
        Queue<ChunkCoordinates> toProcess = new LinkedList<ChunkCoordinates>();
        toProcess.add(new ChunkCoordinates(x, y, z));

        while (!toProcess.isEmpty())
        {
            ChunkCoordinates cur = toProcess.remove();
            int id = world.getBlockId(cur.posX, cur.posY, cur.posZ);
            
            if (id == CommonProxy.blockPortal.blockID)
            {
                world.setBlockToAir(cur.posX, cur.posY, cur.posZ);
                addTouchingBlocks(cur, toProcess, meta);
            }
            else if (id == CommonProxy.multiPartID)
            {
                if (MultipartUtils.doesMultipartContainPortal(world, cur.posX, cur.posY, cur.posZ))
                {
                    MultipartUtils.removePortalFromPart(world, cur.posX, cur.posY, cur.posZ);
                    addTouchingBlocks(cur, toProcess, meta);
                }
            }
        }
    }

    /***
     * Removes all portals around the specified location.
     */    
    public static void removePortalAround(WorldServer world, int x, int y, int z)
    {
        for (int i = 0; i < 6; i++)
        {
            ForgeDirection d = ForgeDirection.getOrientation(i);
            ChunkCoordinates c = offsetCoordinate(new ChunkCoordinates(x, y, z), d);
            int id = world.getBlockId(c.posX, c.posY, c.posZ);
            
            if (id == CommonProxy.blockPortal.blockID)
            {
                removePortal(world, c.posX, c.posY, c.posZ, world.getBlockMetadata(c.posX, c.posY, c.posZ));
            }
            else if (id == CommonProxy.multiPartID)
            {                
                TileMultipart p = (TileMultipart) world.getBlockTileEntity(c.posX, c.posY, c.posZ);
                PortalPart part = MultipartUtils.getPortalPart(p);
                
                if (part != null)
                {
                    removePortal(world, c.posX, c.posY, c.posZ, part.getMetadata());
                }
            }
        }
    }

    private static void removePortal(WorldServer world, Queue<ChunkCoordinates> blocks)
    {
        while (!blocks.isEmpty())
        {
            ChunkCoordinates cur = blocks.remove();
            int id = world.getBlockId(cur.posX, cur.posY, cur.posZ);
            
            if (id == CommonProxy.blockPortal.blockID)
            {
                world.setBlockToAir(cur.posX, cur.posY, cur.posZ);
            }
            else if (id == CommonProxy.multiPartID)
            {
                MultipartUtils.removePortalFromPart(world, cur.posX, cur.posY, cur.posZ);
            }
        }
    }

    private static void addTouchingBlocks(ChunkCoordinates c, Queue<ChunkCoordinates> queue, int meta)
    {
        for (int i = 0; i < 6; i++)
        {
            ForgeDirection d = ForgeDirection.getOrientation(i);

            if (meta == 1 && (i == 2 || i == 3))
            {
                continue;
            }
            else if (meta == 2 && (i == 4 || i == 5))
            {
                continue;
            }
            else if (meta == 3 && (i == 0 || i == 1))
            {
                continue;
            }

            queue.add(offsetCoordinate(c, d));
        }
    }
    
    private static boolean checkTension(WorldServer world, int meta, ChunkCoordinates c)
    {
        int[] blockIDs = new int[6];

        for (int i = 0; i < 6; i++)
        {
            ForgeDirection d = ForgeDirection.getOrientation(i);
            ChunkCoordinates c2 = offsetCoordinate(c, d);

            blockIDs[i] = world.getBlockId(c2.posX, c2.posY, c2.posZ);
        }

        if (meta == 1)
        {
            return isValidPortalPart(blockIDs[0]) && isValidPortalPart(blockIDs[1]) && isValidPortalPart(blockIDs[4]) && isValidPortalPart(blockIDs[5]);
        }
        else if (meta == 2)
        {
            return isValidPortalPart(blockIDs[0]) && isValidPortalPart(blockIDs[1]) && isValidPortalPart(blockIDs[2]) && isValidPortalPart(blockIDs[3]);
        }
        else if (meta == 3)
        {
            return isValidPortalPart(blockIDs[2]) && isValidPortalPart(blockIDs[3]) && isValidPortalPart(blockIDs[4]) && isValidPortalPart(blockIDs[5]);
        }

        return false;
    }

    private static int getSides(WorldServer world, ChunkCoordinates coord, int meta)
    {
        int counter = 0;

        for (int i = 0; i < 6; i++)
        {
            ChunkCoordinates c = offsetCoordinate(coord, ForgeDirection.getOrientation(i));
            int id = world.getBlockId(c.posX, c.posY, c.posZ);

            if (isValidPortalPart(id))
            {
                counter++;
            }
            else if (id != 0)
            {
                if (meta == 1 && (i == 2 || i == 3))
                {
                    continue;
                }
                else if (meta == 2 && (i == 4 || i == 5))
                {
                    continue;
                }
                else if (meta == 3 && (i == 0 || i == 1))
                {
                    continue;
                }

                return -1;
            }
        }

        return counter;
    }

    private static boolean isValidPortalPart(int id)
    {
        return id == CommonProxy.blockFrame.blockID || id == CommonProxy.blockPortal.blockID || id == CommonProxy.multiPartID;
    }

    public static boolean findNearbyPortalBlock(WorldServer world, int x, int y, int z)
    {
        for (int i = 0; i < 6; i++)
        {
            ForgeDirection d = ForgeDirection.getOrientation(i);
            ChunkCoordinates c = offsetCoordinate(new ChunkCoordinates(x, y, z), d);

            if (world.getBlockId(c.posX, c.posY, c.posZ) == CommonProxy.blockPortal.blockID)
            {
                return true;
            }
        }

        return false;
    }
}
