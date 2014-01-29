package uk.co.shadeddimensions.ep3.portal;

import java.util.LinkedList;
import java.util.Queue;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import uk.co.shadeddimensions.ep3.lib.Localization;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.tileentity.TilePortal;
import uk.co.shadeddimensions.ep3.tileentity.TilePortalPart;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileBiometricIdentifier;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileDiallingDevice;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileModuleManipulator;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileNetworkInterface;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;

public class PortalUtils
{
    static final int MAXIMUM_CHANCES = 10;

    /***
     * Adds all the touching blocks to the processing queue.
     */
    public static void addNearbyBlocks(World world, ChunkCoordinates w, int portalDirection, Queue<ChunkCoordinates> q)
    {
        if (portalDirection == 4)
        {
            q.add(new ChunkCoordinates(w.posX, w.posY + 1, w.posZ)); // Up
            q.add(new ChunkCoordinates(w.posX, w.posY - 1, w.posZ)); // Down

            q.add(new ChunkCoordinates(w.posX + 1, w.posY, w.posZ - 1)); // North East
            q.add(new ChunkCoordinates(w.posX - 1, w.posY, w.posZ + 1)); // South West
        }
        else if (portalDirection == 5)
        {
            q.add(new ChunkCoordinates(w.posX, w.posY + 1, w.posZ)); // Up
            q.add(new ChunkCoordinates(w.posX, w.posY - 1, w.posZ)); // Down

            q.add(new ChunkCoordinates(w.posX - 1, w.posY, w.posZ - 1)); // North West
            q.add(new ChunkCoordinates(w.posX + 1, w.posY, w.posZ + 1)); // South East
        }
        else
        {
            for (int i = 0; i < 6; i++)
            {
                if (portalDirection == 1 && (i == 2 || i == 3))
                {
                    continue;
                }
                else if (portalDirection == 2 && (i == 4 || i == 5))
                {
                    continue;
                }
                else if (portalDirection == 3 && (i == 0 || i == 1))
                {
                    continue;
                }

                ForgeDirection d = ForgeDirection.getOrientation(i);
                q.add(new ChunkCoordinates(w.posX + d.offsetX, w.posY + d.offsetY, w.posZ + d.offsetZ));
            }
        }
    }

    /***
     * Creates a portal at the specified location.
     */
    @Deprecated
    public static boolean createPortalAt(World world, ChunkCoordinates w, int portalDirection)
    {
        Queue<ChunkCoordinates> processed = new LinkedList<ChunkCoordinates>();
        Queue<ChunkCoordinates> toProcess = new LinkedList<ChunkCoordinates>();
        int chances = 0;

        toProcess.add(w);

        while (!toProcess.isEmpty())
        {
            ChunkCoordinates c = toProcess.remove();

            if (!processed.contains(c))
            {
                if (world.isAirBlock(c.posX, c.posY, c.posZ))
                {
                    int sides = getAllSides(world, c, portalDirection);

                    if (sides < 2)
                    {
                        if (chances < MAXIMUM_CHANCES)
                        {
                            chances++;
                            sides += 2;
                        }
                        else
                        {
                            removeFailedPortal(world, processed);
                            return false;
                        }
                    }

                    if (sides >= 2)
                    {
                        processed.add(c);
                        world.setBlock(c.posX, c.posY, c.posZ, CommonProxy.blockPortal.blockID, portalDirection, 2);
                        addNearbyBlocks(world, c, portalDirection, toProcess);
                    }
                }
                else if (!isPortalPart(world, c.posX, c.posY, c.posZ))
                {
                    removeFailedPortal(world, processed);
                    return false;
                }
            }
        }

        return true;
    }

    /***
     * Creates a portal from the specified portal controller.
     */
    public static boolean createPortalFrom(TilePortalController controller)
    {
        if (controller.worldObj.isRemote || controller.isPortalActive || controller.processing || !controller.isFullyInitialized())
        {
            return false;
        }

        for (ChunkCoordinates w : controller.blockManager.getPortals())
        {
            if (!controller.worldObj.isAirBlock(w.posX, w.posY, w.posZ))
            {
                if (!CommonProxy.portalsDestroyBlocks)
                {
                    return false;
                }

                int id = controller.worldObj.getBlockId(w.posX, w.posY, w.posZ), metadata = controller.worldObj.getBlockMetadata(w.posX, w.posY, w.posZ);

                if (id == Block.bedrock.blockID) // Stop users from being able to break out of the world with portals
                {
                    return false;
                }
                else
                {
                    controller.worldObj.playAuxSFX(2001, w.posX, w.posY, w.posZ, id + (metadata << 12));
                    Block.blocksList[id].dropBlockAsItem(controller.worldObj, w.posX, w.posY, w.posZ, metadata, 0);
                    controller.worldObj.setBlockToAir(w.posX, w.posY, w.posZ);
                }
            }
        }

        for (ChunkCoordinates w : controller.blockManager.getPortals())
        {
            controller.worldObj.setBlock(w.posX, w.posY, w.posZ, CommonProxy.blockPortal.blockID, controller.portalType, 2);

            TilePortal portal = (TilePortal) controller.worldObj.getBlockTileEntity(w.posX, w.posY, w.posZ);
            portal.portalController = controller.getWorldCoordinates();
        }

        for (ChunkCoordinates w : controller.blockManager.getPortals())
        {
            CommonProxy.sendUpdatePacketToAllAround((TilePortal) controller.worldObj.getBlockTileEntity(w.posX, w.posY, w.posZ));
        }

        //CommonProxy.sendPacketToAllAround(controller, new PacketPortalCreated(controller).getPacket());
        controller.setPortalActive(true);
        return true;
    }

    public static Queue<ChunkCoordinates> findAllAttachedPortalParts(TilePortalController controller, Queue<ChunkCoordinates> portalBlocks, EntityPlayer player)
    {
        Queue<ChunkCoordinates> portalParts = new LinkedList<ChunkCoordinates>();
        Queue<ChunkCoordinates> toProcess = new LinkedList<ChunkCoordinates>();
        Queue<ChunkCoordinates> processed = new LinkedList<ChunkCoordinates>();
        toProcess.add(controller.getChunkCoordinates());

        int biometricCounter = 0, dialCounter = 0, networkCounter = 0, moduleCounter = 0;

        while (!toProcess.isEmpty())
        {
            ChunkCoordinates c = toProcess.remove();

            if (!processed.contains(c))
            {
                processed.add(c);
                TileEntity t = controller.worldObj.getBlockTileEntity(c.posX, c.posY, c.posZ);

                if (portalBlocks.contains(c) || t instanceof TilePortalPart)
                {
                    if (t != null)
                    {
                        if (!(t instanceof TilePortalController) && ((TilePortalPart) t).getPortalController() != null)
                        {
                            continue;
                        }
                        
                        if (t instanceof TileNetworkInterface)
                        {
                            if (networkCounter == 1)
                            {
                                player.sendChatToPlayer(ChatMessageComponent.createFromText(Localization.getChatString("multipleNetworkInterfaces")));
                                return null;
                            }
                            else if (dialCounter == 1)
                            {
                                player.sendChatToPlayer(ChatMessageComponent.createFromText(Localization.getChatString("dialDeviceAndNetworkInterface")));
                                return null;
                            }

                            networkCounter++;
                        }
                        else if (t instanceof TileDiallingDevice)
                        {
                            if (dialCounter == 1)
                            {
                                player.sendChatToPlayer(ChatMessageComponent.createFromText(Localization.getChatString("multipleDiallingDevices")));
                                return null;
                            }
                            else if (networkCounter == 1)
                            {
                                player.sendChatToPlayer(ChatMessageComponent.createFromText(Localization.getChatString("dialDeviceAndNetworkInterface")));
                                return null;
                            }

                            dialCounter++;
                        }
                        else if (t instanceof TileBiometricIdentifier)
                        {
                            if (biometricCounter == 1)
                            {
                                player.sendChatToPlayer(ChatMessageComponent.createFromText(Localization.getChatString("multipleBiometricIdentifiers")));
                                return null;
                            }

                            biometricCounter++;
                        }
                        else if (t instanceof TileModuleManipulator)
                        {
                            if (moduleCounter == 1)
                            {
                                player.sendChatToPlayer(ChatMessageComponent.createFromText(Localization.getChatString("multipleModuleManipulators")));
                                return null;
                            }

                            moduleCounter++;
                        }
                        else if (t instanceof TilePortalController && processed.size() > 1)
                        {
                            player.sendChatToPlayer(ChatMessageComponent.createFromText(Localization.getChatString("multiplePortalControllers")));
                            return null;
                        }
                    }

                    portalParts.add(c);
                    addNearbyBlocks(controller.worldObj, c, controller.portalType, toProcess);
                    addNearbyBlocks(controller.worldObj, c, 0, toProcess);
                }
            }
        }

        return portalParts;
    }

    /***
     * Counts the sides that are touching ghosted portal parts.
     */
    private static int getAllGhostedSides(World world, ChunkCoordinates w, int portalDirection, Queue<ChunkCoordinates> ghostedParts)
    {
        int sides = 0;
        Queue<ChunkCoordinates> neighbors = new LinkedList<ChunkCoordinates>();
        addNearbyBlocks(world, w, portalDirection, neighbors);

        for (ChunkCoordinates c : neighbors)
        {
            if (ghostedParts.contains(c) || isPortalPart(world.getBlockTileEntity(c.posX, c.posY, c.posZ)))
            {
                sides++;
            }
        }

        return sides;
    }

    /***
     * Counts the sides that are touching portal parts.
     */
    private static int getAllSides(World world, ChunkCoordinates w, int portalDirection)
    {
        int sides = 0;
        Queue<ChunkCoordinates> neighbors = new LinkedList<ChunkCoordinates>();
        addNearbyBlocks(world, w, portalDirection, neighbors);

        for (ChunkCoordinates c : neighbors)
        {
            if (isPortalPart(world.getBlockTileEntity(c.posX, c.posY, c.posZ)))
            {
                sides++;
            }
        }

        return sides;
    }

    public static Queue<ChunkCoordinates> getGhostedPortals(TilePortalController c)
    {
        for (int j = 0; j < 6; j++)
        {
            for (int i = 1; i < 6; i++)
            {
                Queue<ChunkCoordinates> portalBlocks = ghostPortalAt(c.worldObj, c.getWorldCoordinates().offset(ForgeDirection.getOrientation(j)), i);

                if (!portalBlocks.isEmpty())
                {
                    c.portalType = (byte) i;
                    return portalBlocks;
                }
            }
        }

        return new LinkedList<ChunkCoordinates>();
    }

    /***
     * Ghosts a portal at the specified location.
     */
    public static Queue<ChunkCoordinates> ghostPortalAt(World world, ChunkCoordinates w, int portalDirection)
    {
        Queue<ChunkCoordinates> processed = new LinkedList<ChunkCoordinates>();
        Queue<ChunkCoordinates> toProcess = new LinkedList<ChunkCoordinates>();
        Queue<ChunkCoordinates> ghostedPortals = new LinkedList<ChunkCoordinates>();
        int chances = 0;

        toProcess.add(w);

        while (!toProcess.isEmpty())
        {
            ChunkCoordinates c = toProcess.remove();

            if (!processed.contains(c))
            {
                if (world.isAirBlock(c.posX, c.posY, c.posZ))
                {
                    int sides = getAllGhostedSides(world, c, portalDirection, ghostedPortals);

                    if (sides < 2)
                    {
                        if (chances < MAXIMUM_CHANCES)
                        {
                            chances++;
                            sides += 2;
                        }
                        else
                        {
                            return new LinkedList<ChunkCoordinates>();
                        }
                    }

                    if (sides >= 2)
                    {
                        processed.add(c);
                        ghostedPortals.add(c);
                        addNearbyBlocks(world, c, portalDirection, toProcess);
                    }
                }
                else if (!isPortalPart(world, c.posX, c.posY, c.posZ))
                {
                    return new LinkedList<ChunkCoordinates>();
                }
            }
        }

        return ghostedPortals;
    }

    /***
     * Checks to see if the specified block is a portal part.
     */
    public static boolean isPortalPart(TileEntity tile)
    {
        return tile != null && tile instanceof TilePortalPart;
    }

    /***
     * Checks to see if the specified block is a portal part.
     */
    public static boolean isPortalPart(World world, int x, int y, int z)
    {
        return isPortalPart(world.getBlockTileEntity(x, y, z));
    }

    private static void removeFailedPortal(World world, Queue<ChunkCoordinates> processed)
    {
        while (!processed.isEmpty())
        {
            ChunkCoordinates c = processed.remove();
            world.setBlockToAir(c.posX, c.posY, c.posZ);
        }
    }

    /***
     * Removes an active portal from the specified portal controller.
     */
    public static void removePortalFrom(TilePortalController controller)
    {
        if (controller.worldObj.isRemote || !controller.isPortalActive || controller.processing || !controller.isFullyInitialized())
        {
            return;
        }

        for (ChunkCoordinates w : controller.blockManager.getPortals())
        {
            controller.worldObj.setBlockToAir(w.posX, w.posY, w.posZ);
        }

        controller.setPortalActive(false);
    }

    /**
     * Creates a nether portal from the specified Fire block.
     * @param world
     * @param x
     * @param y
     * @param z
     */
    public static boolean createNetherPortalFrom(World world, int x, int y, int z)
    {
        world.setBlockToAir(x, y, z);
        
        for (int i = 1; i < 4; i++)
        {
            if (createNetherPortal(world, new ChunkCoordinates(x, y, z), i))
            {
                return true;
            }
        }
        
        return false;
    }

    static boolean isNetherPortalPart(World world, int x, int y, int z)
    {
        return isNetherPortalPart(world.getBlockId(x, y, z));
    }
    
    public static boolean isNetherPortalPart(int id)
    {
        return id == Block.portal.blockID || id == Block.obsidian.blockID;
    }
    
    static int getNetherSides(World world, ChunkCoordinates w, int portalDirection)
    {
        int sides = 0;
        Queue<ChunkCoordinates> neighbors = new LinkedList<ChunkCoordinates>();
        addNearbyBlocks(world, w, portalDirection, neighbors);

        for (ChunkCoordinates c : neighbors)
        {
            if (isNetherPortalPart(world, c.posX, c.posY, c.posZ))
            {
                sides++;
            }
        }

        return sides;
    }
    
    static boolean createNetherPortal(World world, ChunkCoordinates w, int portalDirection)
    {
        Queue<ChunkCoordinates> processed = new LinkedList<ChunkCoordinates>();
        Queue<ChunkCoordinates> toProcess = new LinkedList<ChunkCoordinates>();
        int chances = 0;
        toProcess.add(w);

        while (!toProcess.isEmpty())
        {
            ChunkCoordinates c = toProcess.remove();

            if (!processed.contains(c))
            {
                if (world.isAirBlock(c.posX, c.posY, c.posZ))
                {
                    int sides = getNetherSides(world, c, portalDirection);

                    if (sides < 2)
                    {
                        if (chances < MAXIMUM_CHANCES)
                        {
                            chances++;
                            sides += 2;
                        }
                        else
                        {
                            removeFailedPortal(world, processed);
                            return false;
                        }
                    }

                    if (sides >= 2)
                    {
                        processed.add(c);
                        world.setBlock(c.posX, c.posY, c.posZ, Block.portal.blockID, 0, 2);
                        addNearbyBlocks(world, c, portalDirection, toProcess);
                    }
                }
                else if (!isNetherPortalPart(world, c.posX, c.posY, c.posZ))
                {
                    removeFailedPortal(world, processed);
                    return false;
                }
            }
        }

        return true;
    }
}
