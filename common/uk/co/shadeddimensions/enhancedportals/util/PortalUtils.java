package uk.co.shadeddimensions.enhancedportals.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import net.minecraft.block.Block;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeDirection;
import uk.co.shadeddimensions.enhancedportals.multipart.MultipartUtils;
import uk.co.shadeddimensions.enhancedportals.network.CommonProxy;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortal;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrame;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameController;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameRedstone;

public class PortalUtils
{
    private static boolean controllerLinkCreatePortal(WorldServer world, int x, int y, int z)
    {
        for (int i = 0; i < 6; i++)
        {
            ForgeDirection d = ForgeDirection.getOrientation(i);
            
            if (world.isAirBlock(x + d.offsetX, y + d.offsetY, z + d.offsetZ) || Block.blocksList[world.getBlockId(x + d.offsetX, y + d.offsetY, z + d.offsetZ)].isBlockReplaceable(world, x + d.offsetX, y + d.offsetY, z + d.offsetZ))
            {
                for (int j = 1; j < 4; j++)
                {
                    if (createPortal(world, x + d.offsetX, y + d.offsetY, z + d.offsetZ, 6 + j))
                    {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    /***
     * Links the controller to all surrounding portal blocks
     * @return Returns state. 0 - Success, 1 - Unknown Error, 2 - Not controller, 3 - Another controller found.
     */
    public static byte linkPortalController(WorldServer world, int x, int y, int z)
    {
        TilePortalFrame frame = (TilePortalFrame) world.getBlockTileEntity(x, y, z);
        
        if (frame instanceof TilePortalFrameController)
        {
            if (!controllerLinkCreatePortal(world, x, y, z))
            {
                return 4;
            }
            
            TilePortalFrameController controller = (TilePortalFrameController) frame;
            ChunkCoordinates control = new ChunkCoordinates(x, y, z);
            
            List<ChunkCoordinates> backupPortalFrame = duplicateList(controller.portalFrame);
            List<ChunkCoordinates> backupPortalFrameRedstone = duplicateList(controller.portalFrameRedstone);
            List<ChunkCoordinates> backupPortalBlocks = duplicateList(controller.portalBlocks);
                        
            controller.portalFrame = new ArrayList<ChunkCoordinates>();
            controller.portalFrameRedstone = new ArrayList<ChunkCoordinates>();
            controller.portalBlocks = new ArrayList<ChunkCoordinates>();
            
            Queue<ChunkCoordinates> toProcess = new LinkedList<ChunkCoordinates>();
            Queue<ChunkCoordinates> processed = new LinkedList<ChunkCoordinates>();
            processed.add(control);
            addTouchingBlocks(control, toProcess, 0);
            
            while (!toProcess.isEmpty())
            {
                ChunkCoordinates c = toProcess.remove();
                
                if (!processed.contains(c))
                {
                    int id = world.getBlockId(c.posX, c.posY, c.posZ);
                    
                    if (id == CommonProxy.blockFrame.blockID)
                    {
                        TilePortalFrame f = (TilePortalFrame) world.getBlockTileEntity(c.posX, c.posY, c.posZ);
                        
                        if (f instanceof TilePortalFrameController)
                        {
                            controller.portalFrame = backupPortalFrame;
                            controller.portalFrameRedstone = backupPortalFrameRedstone;
                            controller.portalBlocks = backupPortalBlocks;
                            return 3;
                        }
                        else
                        {
                            if (f instanceof TilePortalFrameRedstone)
                            {
                                controller.portalFrameRedstone.add(c);
                            }
                            
                            f.controller = control;
                            controller.portalFrame.add(c);
                            processed.add(c);
                            addTouchingBlocks(c, toProcess, 0);
                        }
                    }
                    else if (id == CommonProxy.blockPortal.blockID)
                    {
                        TilePortal portal = (TilePortal) world.getBlockTileEntity(c.posX, c.posY, c.posZ);
                        
                        portal.controller = control;                        
                        controller.portalBlocks.add(c);
                        processed.add(c);
                        addTouchingBlocks(c, toProcess, 0);
                    }
                }
            }
        }
        else
        {
            return 2;
        }
        
        return 0;
    }
    
    
    /***
     * Creates a new portal.
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
    
    private static boolean createPortal(WorldServer world, int x, int y, int z, int meta)
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

    private static List<ChunkCoordinates> duplicateList(List<ChunkCoordinates> queue)
    {
        List<ChunkCoordinates> q = new ArrayList<ChunkCoordinates>();

        for (ChunkCoordinates c : queue)
        {
            q.add(c);
        }

        return q;
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
        if (meta >= 6)
        {
            meta -= 6;
        }
        
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
        if (meta >= 6)
        {
            meta -= 6;
        }
        
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
        if (meta >= 6)
        {
            meta -= 6;
        }
        
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
}
