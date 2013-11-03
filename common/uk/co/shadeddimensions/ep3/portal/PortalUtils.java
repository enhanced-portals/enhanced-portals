package uk.co.shadeddimensions.ep3.portal;

import java.util.LinkedList;
import java.util.Queue;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.tileentity.TilePortal;
import uk.co.shadeddimensions.ep3.tileentity.TilePortalPart;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;
import uk.co.shadeddimensions.ep3.util.WorldCoordinates;

public class PortalUtils
{
    static final int MAXIMUM_CHANCES = 10;
    
    /***
     * Checks to see if the specified block is a portal part.
     */
    private static boolean isPortalPart(World world, int x, int y, int z)
    {
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        
        return isPortalPart(tile);
    }
    
    /***
     * Checks to see if the specified block is a portal part.
     */
    private static boolean isPortalPart(TileEntity tile)
    {
        return tile != null && tile instanceof TilePortalPart;
    }
    
    /***
     * Creates a portal from the specified portal controller.
     */
    public static boolean createPortalFrom(TilePortalController controller)
    {
        if (CommonProxy.isClient() || controller.isPortalActive || controller.processing || !controller.hasConfigured)
        {
            return false;
        }

        for (WorldCoordinates w : controller.portals)
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
        
        for (WorldCoordinates w : controller.portals)
        {
            controller.worldObj.setBlock(w.posX, w.posY, w.posZ, CommonProxy.blockPortal.blockID, controller.portalType, 2);
            
            TilePortal portal = (TilePortal) controller.worldObj.getBlockTileEntity(w.posX, w.posY, w.posZ);
            portal.portalController = controller.getWorldCoordinates();
        }
        
        for (WorldCoordinates w : controller.portals)
        {
            CommonProxy.sendUpdatePacketToAllAround((TilePortal) controller.worldObj.getBlockTileEntity(w.posX, w.posY, w.posZ));
        }
        
        //CommonProxy.sendPacketToAllAround(controller, new PacketPortalCreated(controller).getPacket());
        controller.setPortalActive(true);
        return true;
    }
    
    /***
     * Creates a portal at the specified location.
     */
    public static boolean createPortalAt(WorldCoordinates w, int portalDirection)
    {
        World world = w.getWorld();
        Queue<WorldCoordinates> processed = new LinkedList<WorldCoordinates>();
        Queue<WorldCoordinates> toProcess = new LinkedList<WorldCoordinates>();
        int chances = 0;
 
        toProcess.add(w);
        
        while (!toProcess.isEmpty())
        {
            WorldCoordinates c = toProcess.remove();
            
            if (!processed.contains(c))
            {
                if (world.isAirBlock(c.posX, c.posY, c.posZ))
                {
                    int sides = getAllSides(c, portalDirection);
                    
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
                        addNearbyBlocks(c, portalDirection, toProcess);
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
     * Ghosts a portal at the specified location.
     */
    public static Queue<WorldCoordinates> ghostPortalAt(WorldCoordinates w, int portalDirection)
    {
        World world = w.getWorld();
        Queue<WorldCoordinates> processed = new LinkedList<WorldCoordinates>();
        Queue<WorldCoordinates> toProcess = new LinkedList<WorldCoordinates>();
        Queue<WorldCoordinates> ghostedPortals = new LinkedList<WorldCoordinates>();
        int chances = 0;
 
        toProcess.add(w);
        
        while (!toProcess.isEmpty())
        {
            WorldCoordinates c = toProcess.remove();
            
            if (!processed.contains(c))
            {
                if (world.isAirBlock(c.posX, c.posY, c.posZ))
                {
                    int sides = getAllGhostedSides(c, portalDirection, ghostedPortals);
                    
                    if (sides < 2)
                    {
                        if (chances < MAXIMUM_CHANCES)
                        {
                            chances++;
                            sides += 2;
                        }
                        else
                        {
                            return new LinkedList<WorldCoordinates>();
                        }
                    }
                    
                    if (sides >= 2)
                    {
                        processed.add(c);
                        ghostedPortals.add(c);
                        addNearbyBlocks(c, portalDirection, toProcess);
                    }
                }
                else if (!isPortalPart(world, c.posX, c.posY, c.posZ))
                {
                    return new LinkedList<WorldCoordinates>();
                }
            }
        }
        
        return ghostedPortals;
    }
    
    /***
     * Counts the sides that are touching portal parts.
     */
    private static int getAllSides(WorldCoordinates w, int portalDirection)
    {
        int sides = 0;
        
        for (int i = 0; i < 6; i++)
        {
            WorldCoordinates c = w.offset(ForgeDirection.getOrientation(i));
            
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
            
            if (isPortalPart(c.getBlockTileEntity()))
            {
                sides++;
            }
        }
        
        return sides;
    }
    
    /***
     * Counts the sides that are touching ghosted portal parts.
     */
    private static int getAllGhostedSides(WorldCoordinates w, int portalDirection, Queue<WorldCoordinates> ghostedParts)
    {
        int sides = 0;
        
        for (int i = 0; i < 6; i++)
        {
            WorldCoordinates c = w.offset(ForgeDirection.getOrientation(i));
            
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
            
            if (ghostedParts.contains(c) || isPortalPart(c.getBlockTileEntity()))
            {
                sides++;
            }
        }
        
        return sides;
    }
    
    /***
     * Adds all the touching blocks to the processing queue.
     */
    public static void addNearbyBlocks(WorldCoordinates w, int portalDirection, Queue<WorldCoordinates> q)
    {
        for (int i = 0; i < 6; i++)
        {
            ForgeDirection d = ForgeDirection.getOrientation(i);

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

            q.add(w.offset(d));
        }
    }
    
    /***
     * Removes an active portal from the specified portal controller.
     */
    public static void removePortalFrom(TilePortalController controller)
    {
        if (CommonProxy.isClient() || !controller.isPortalActive || controller.processing || !controller.hasConfigured)
        {
            return;
        }
        
        for (WorldCoordinates w : controller.portals)
        {
            controller.worldObj.setBlockToAir(w.posX, w.posY, w.posZ);
        }
        
        controller.setPortalActive(false);
    }
    
    private static void removeFailedPortal(World world, Queue<WorldCoordinates> processed)
    {
        while (!processed.isEmpty())
        {
            WorldCoordinates c = processed.remove();            
            world.setBlockToAir(c.posX, c.posY, c.posZ);
        }
    }    
}