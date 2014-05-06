package enhancedportals.portal;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import enhancedportals.tileentity.portal.TileController;
import enhancedportals.tileentity.portal.TileDiallingDevice;
import enhancedportals.tileentity.portal.TileModuleManipulator;
import enhancedportals.tileentity.portal.TileNetworkInterface;
import enhancedportals.tileentity.portal.TilePortalPart;
import enhancedportals.tileentity.portal.TileProgrammableInterface;
import enhancedportals.utility.WorldUtils;

public class PortalUtils
{
    static final int MAXIMUM_CHANCES = 10;

    /***
     * Adds all the touching blocks to the processing queue.
     */
    static void addNearbyBlocks(World world, ChunkCoordinates w, int portalDirection, Queue<ChunkCoordinates> q)
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

    public static ArrayList<ChunkCoordinates> getAllPortalComponents(TileController controller) throws PortalException
    {
        ArrayList<ChunkCoordinates> portalComponents = new ArrayList<ChunkCoordinates>();
        Queue<ChunkCoordinates> toProcess = new LinkedList<ChunkCoordinates>();
        Queue<ChunkCoordinates> portalBlocks = getGhostedPortalBlocks(controller);
        toProcess.add(controller.getChunkCoordinates());

        if (portalBlocks.isEmpty())
        {
            throw new PortalException("couldNotCreatePortalHere");
        }

        System.out.println("Parsing portal components with " + portalBlocks.size() + " portal blocks.");

        boolean program = false, mod = false, dialler = false, network = false;

        while (!toProcess.isEmpty())
        {
            ChunkCoordinates c = toProcess.remove();

            if (!portalComponents.contains(c))
            {
                TileEntity t = WorldUtils.getTileEntity(controller.getWorldObj(), c);

                if (portalBlocks.contains(c) || t instanceof TilePortalPart)
                {
                    if (t instanceof TileNetworkInterface)
                    {
                        if (dialler)
                        {
                            throw new PortalException("dialAndNetwork");
                        }

                        network = true;
                    }
                    else if (t instanceof TileDiallingDevice)
                    {
                        if (network)
                        {
                            throw new PortalException("dialAndNetwork");
                        }

                        dialler = true;
                    }

                    else if (t instanceof TileProgrammableInterface)
                    {
                        if (!program)
                        {
                            program = true;
                        }
                        else
                        {
                            throw new PortalException("multipleProgram");
                        }
                    }
                    else if (t instanceof TileModuleManipulator)
                    {
                        if (!mod)
                        {
                            mod = true;
                        }
                        else
                        {
                            throw new PortalException("multipleMod");
                        }
                    }

                    portalComponents.add(c);
                    addNearbyBlocks(controller.getWorldObj(), c, 0, toProcess);

                    if (controller.portalType >= 4)
                    {
                        addNearbyBlocks(controller.getWorldObj(), c, controller.portalType, toProcess); // Adds diagonals for those that require it
                    }
                }
            }
        }

        if (portalComponents.isEmpty())
        {
            throw new PortalException("unknown");
        }

        System.out.println("Done. Returning " + portalComponents.size() + " portal components.");

        return portalComponents;
    }

    static Queue<ChunkCoordinates> getGhostedPortalBlocks(TileController controller)
    {
        for (int j = 0; j < 6; j++)
        {
            for (int i = 1; i < 6; i++)
            {
                Queue<ChunkCoordinates> portalBlocks = getGhostedPortalBlocks(controller.getWorldObj(), WorldUtils.getChunkCoordinatesOffset(controller.getChunkCoordinates(), ForgeDirection.getOrientation(j)), i);

                if (!portalBlocks.isEmpty())
                {
                    controller.portalType = i;
                    return portalBlocks;
                }
            }
        }

        return new LinkedList<ChunkCoordinates>();
    }

    static Queue<ChunkCoordinates> getGhostedPortalBlocks(World world, ChunkCoordinates start, int portalType)
    {
        Queue<ChunkCoordinates> portalBlocks = new LinkedList<ChunkCoordinates>();
        Queue<ChunkCoordinates> toProcess = new LinkedList<ChunkCoordinates>();
        int chances = 0;
        toProcess.add(start);

        while (!toProcess.isEmpty())
        {
            ChunkCoordinates c = toProcess.remove();

            if (!portalBlocks.contains(c))
            {
                if (WorldUtils.isAirBlock(world, c))
                {
                    int sides = getGhostedSides(world, c, portalBlocks, portalType);

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
                        portalBlocks.add(c);
                        addNearbyBlocks(world, c, portalType, toProcess);
                    }
                }
                else if (!isPortalPart(world, c))
                {
                    return new LinkedList<ChunkCoordinates>();
                }
            }
        }

        return portalBlocks;
    }

    static int getGhostedSides(World world, ChunkCoordinates block, Queue<ChunkCoordinates> portalBlocks, int portalType)
    {
        int sides = 0;
        Queue<ChunkCoordinates> neighbors = new LinkedList<ChunkCoordinates>();
        addNearbyBlocks(world, block, portalType, neighbors);

        for (ChunkCoordinates c : neighbors)
        {
            if (portalBlocks.contains(c) || isPortalPart(world, c))
            {
                sides++;
            }
        }

        return sides;
    }

    static boolean isPortalPart(World world, ChunkCoordinates c)
    {
        TileEntity tile = WorldUtils.getTileEntity(world, c);
        return tile != null && tile instanceof TilePortalPart;
    }

    public static boolean netherCreatePortal(World world, ChunkCoordinates w, int portalDirection)
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
                    int sides = netherGetSides(world, c, portalDirection);

                    if (sides < 2)
                    {
                        if (chances < MAXIMUM_CHANCES)
                        {
                            chances++;
                            sides += 2;
                        }
                        else
                        {
                            netherRemoveFailedPortal(world, processed);
                            return false;
                        }
                    }

                    if (sides >= 2)
                    {
                        processed.add(c);
                        world.setBlock(c.posX, c.posY, c.posZ, Blocks.portal, 0, 2);
                        addNearbyBlocks(world, c, portalDirection, toProcess);
                    }
                }
                else if (!netherIsPortalPart(world, c.posX, c.posY, c.posZ))
                {
                    netherRemoveFailedPortal(world, processed);
                    return false;
                }
            }
        }

        return true;
    }

    static int netherGetSides(World world, ChunkCoordinates w, int portalDirection)
    {
        int sides = 0;
        Queue<ChunkCoordinates> neighbors = new LinkedList<ChunkCoordinates>();
        addNearbyBlocks(world, w, portalDirection, neighbors);

        for (ChunkCoordinates c : neighbors)
        {
            if (netherIsPortalPart(world, c.posX, c.posY, c.posZ))
            {
                sides++;
            }
        }

        return sides;
    }

    static boolean netherIsPortalPart(Block id)
    {
        return id == Blocks.portal || id == Blocks.obsidian;
    }

    static boolean netherIsPortalPart(World world, int x, int y, int z)
    {
        return netherIsPortalPart(world.getBlock(x, y, z));
    }

    static void netherRemoveFailedPortal(World world, Queue<ChunkCoordinates> processed)
    {
        while (!processed.isEmpty())
        {
            ChunkCoordinates c = processed.remove();
            world.setBlockToAir(c.posX, c.posY, c.posZ);
        }
    }
}
