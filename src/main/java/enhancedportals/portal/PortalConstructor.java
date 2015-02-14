package enhancedportals.portal;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import enhancedportals.IPortalPart;
import enhancedportals.network.ProxyCommon;
import enhancedportals.tile.TileFrame;
import enhancedportals.tile.TileFrameController;
import enhancedportals.tile.TileFrameDialDevice;
import enhancedportals.tile.TileFrameNetworkInterface;
import enhancedportals.tile.TileFramePortalManipulator;
import enhancedportals.tile.TileFrameRedstoneInterface;
import enhancedportals.tile.TileFrameTransferEnergy;
import enhancedportals.tile.TileFrameTransferFluid;
import enhancedportals.tile.TileFrameTransferItem;
import enhancedportals.tile.TilePortal;
import enhancedportals.util.GeneralUtils;

public class PortalConstructor
{
    static final int MAXIMUM_CHANCES = 40;
    int portalType;

    ArrayList<TilePortal> portal = new ArrayList<TilePortal>();
    ArrayList<TileFrame> frame = new ArrayList<TileFrame>();
    ArrayList<TileFrameRedstoneInterface> redstone = new ArrayList<TileFrameRedstoneInterface>();
    ArrayList<TileFrameDialDevice> dial = new ArrayList<TileFrameDialDevice>();
    ArrayList<TileFrameNetworkInterface> network = new ArrayList<TileFrameNetworkInterface>();
    TileFramePortalManipulator manip = null;
    ArrayList<TileFrameTransferEnergy> energy = new ArrayList<TileFrameTransferEnergy>();
    ArrayList<TileFrameTransferFluid> fluid = new ArrayList<TileFrameTransferFluid>();
    ArrayList<TileFrameTransferItem> item = new ArrayList<TileFrameTransferItem>();

    public void perform(TileFrameController controller) throws Exception
    {
        Queue<ChunkCoordinates> portals = getAllPortalComponents(controller);

        for (ChunkCoordinates c : portals)
        {
            controller.getWorldObj().setBlock(c.posX, c.posY, c.posZ, ProxyCommon.portal, portalType, 3);
            TilePortal p = (TilePortal) controller.getWorldObj().getTileEntity(c.posX, c.posY, c.posZ);
            portal.add(p);
        }

        throw new Exception("Portals: " + portal.size() + ", Frames: " + frame.size() + ", Dial: " + dial.size() + ", Network: " + network.size() + ", Redstone: " + redstone.size());
    }

    void addNearbyBlocks(World world, ChunkCoordinates w, int portalDirection, Queue<ChunkCoordinates> q)
    {
        for (int i = 0; i < 6; i++) // Loop through the different directions for portalDirection 1-3.
        {
            if (portalDirection == 1 && (i == 2 || i == 3)) // Skip North (2) and South (3)
            {
                continue;
            }
            else if (portalDirection == 2 && (i == 4 || i == 5)) // Skip West (4) and East (5)
            {
                continue;
            }
            else if (portalDirection == 3 && (i == 0 || i == 1)) // Skip Up (0) and Down (1)
            {
                continue;
            }

            ForgeDirection d = ForgeDirection.getOrientation(i);
            q.add(new ChunkCoordinates(w.posX + d.offsetX, w.posY + d.offsetY, w.posZ + d.offsetZ));
        }
    }

    Queue<ChunkCoordinates> getAllPortalComponents(TileFrameController controller) throws Exception
    {
        World world = controller.getWorldObj();
        ArrayList<ChunkCoordinates> portalComponents = new ArrayList<ChunkCoordinates>();
        Queue<ChunkCoordinates> toProcess = new LinkedList<ChunkCoordinates>();
        Queue<ChunkCoordinates> portalBlocks = getGhostedPortalBlocks(controller);
        toProcess.add(controller.getChunkCoordinates());

        if (portalBlocks.isEmpty()) { throw new Exception("couldNotCreatePortalHere"); }

        while (!toProcess.isEmpty())
        {
            ChunkCoordinates c = toProcess.remove();

            if (!portalComponents.contains(c))
            {
                TileEntity t = world.getTileEntity(c.posX, c.posY, c.posZ);

                if (portalBlocks.contains(c) || t instanceof IPortalPart) // Add all components to the lists, make sure everything fits the rules.
                {
                    if (t instanceof TileFrameController)
                    {
                        if (!((TileFrameController) t).getChunkCoordinates().equals(controller.getChunkCoordinates())) { throw new Exception("two controllers"); }
                    }
                    else if (t instanceof TileFrameNetworkInterface)
                    {
                        if (!dial.isEmpty()) { throw new Exception("dialAndNetwork"); }
                        network.add((TileFrameNetworkInterface) t);
                    }
                    else if (t instanceof TileFrameDialDevice)
                    {
                        if (!network.isEmpty()) { throw new Exception("dialAndNetwork"); }
                        dial.add((TileFrameDialDevice) t);
                    }
                    else if (t instanceof TileFramePortalManipulator)
                    {
                        if (manip != null) { throw new Exception("multipleManip"); }
                        manip = (TileFramePortalManipulator) t;
                    }
                    else if (t instanceof TileFrameRedstoneInterface)
                    {
                        redstone.add((TileFrameRedstoneInterface) t);
                    }
                    else if (t instanceof TileFrameTransferEnergy)
                    {
                        energy.add((TileFrameTransferEnergy) t);
                    }
                    else if (t instanceof TileFrameTransferFluid)
                    {
                        fluid.add((TileFrameTransferFluid) t);
                    }
                    else if (t instanceof TileFrameTransferItem)
                    {
                        item.add((TileFrameTransferItem) t);
                    }
                    else if (t instanceof TileFrame) // Needs to be last otherwise it will catch all frame parts
                    {
                        frame.add((TileFrame) t);
                    }

                    portalComponents.add(c);
                    addNearbyBlocks(controller.getWorldObj(), c, 0, toProcess);
                }
            }
        }

        if (portalComponents.isEmpty()) { throw new Exception("unknown"); }
        return portalBlocks;
    }

    Queue<ChunkCoordinates> getGhostedPortalBlocks(TileFrameController controller)
    {
        for (int j = 0; j < 6; j++)
        {
            for (int i = 1; i < 6; i++)
            {
                // Forge directions: Down, Up, North, South, West, East
                // Get Controller and cycle through forge directions from the coord.
                ChunkCoordinates c = GeneralUtils.offset(controller.getChunkCoordinates(), ForgeDirection.getOrientation(j));
                // portalBlocks = (the world controller is in, the offset from the controller we're exploring, 1-5)
                Queue<ChunkCoordinates> portalBlocks = getGhostedPortalBlocks(controller.getWorldObj(), c, i);

                if (!portalBlocks.isEmpty())
                {
                    portalType = i;
                    return portalBlocks;
                }
            }
        }

        return new LinkedList<ChunkCoordinates>();
    }

    Queue<ChunkCoordinates> getGhostedPortalBlocks(World world, ChunkCoordinates start, int portalType)
    {
        Queue<ChunkCoordinates> portalBlocks = new LinkedList<ChunkCoordinates>();
        Queue<ChunkCoordinates> toProcess = new LinkedList<ChunkCoordinates>();
        int chances = 0;
        // Start is the offset block from the controller.
        toProcess.add(start);

        while (!toProcess.isEmpty())
        {
            // c is now the offset block (start).
            ChunkCoordinates c = toProcess.remove();
            // Pass as long as portalBlocks does not already contain the offset block from the controller.
            if (!portalBlocks.contains(c))
            {
                // Check if the coords of the offset block happens to be an air block.
                if (world.isAirBlock(c.posX, c.posY, c.posZ))
                {
                    // sides = (world that the controller is in, current list of portalBlocks, 1-5)
                    // Returns the number of portal frame blocks and items already in portalBlocks.
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
                else if (!isPortalPart(world, c)) { return new LinkedList<ChunkCoordinates>(); }
            }
        }

        return portalBlocks;
    }

    int getGhostedSides(World world, ChunkCoordinates block, Queue<ChunkCoordinates> portalBlocks, int portalType)
    {
        int sides = 0;
        Queue<ChunkCoordinates> neighbors = new LinkedList<ChunkCoordinates>();
        // (world controller is in, the offset block from controller, 1-5, blank linkedList neighbors)
        // if portalDirection = 1, then add up, down, west, east
        // if portalDirection = 2, then add up, down, north, south
        // if portalDirection = 3, then add north, south, west, east
        // if portalDirection = 4, then add up, down, north-east, south-west
        // if portalDirection = 5, then add up, down, north-west, south-east
        addNearbyBlocks(world, block, portalType, neighbors);

        // Go through all neighbor blocks.
        for (ChunkCoordinates c : neighbors)
        {
            if (portalBlocks.contains(c) || isPortalPart(world, c))
            {
                sides++;
            }
        }

        return sides;
    }

    boolean isPortalPart(World world, ChunkCoordinates c)
    {
        TileEntity tile = world.getTileEntity(c.posX, c.posY, c.posZ);
        return tile != null && tile instanceof IPortalPart;
    }
}
