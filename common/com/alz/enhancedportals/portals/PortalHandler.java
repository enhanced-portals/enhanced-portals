package com.alz.enhancedportals.portals;

import java.util.LinkedList;
import java.util.Queue;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import com.alz.enhancedportals.core.tileentity.TileEntityPortalModifier;
import com.alz.enhancedportals.helpers.WorldLocation;
import com.alz.enhancedportals.reference.BlockIds;
import com.alz.enhancedportals.reference.Reference;
import com.alz.enhancedportals.core.tileentity.TileEntityNetherPortal;

public class PortalHandler
{
    private static final int MAX_CHANCES = 10;
    
    public static boolean isBlockFrame(int id, boolean includePortal)
    {
        return true;
    }
    
    public static boolean isBlockRemovable(int id)
    {
        return true;
    }
    
    public static void floodUpdateTexture(WorldLocation location, PortalTexture newTexture, PortalTexture oldTexture, boolean updateModifiers)
    {
        if (location.getBlockId() != BlockIds.NETHER_PORTAL || oldTexture == null || oldTexture == PortalTexture.UNKNOWN || newTexture == oldTexture)
        {
            return;
        }
        
        PortalShape shape = PortalShape.getPortalShape(location);
        Queue<WorldLocation> queue = new LinkedList<WorldLocation>();
        queue.add(location);
        
        while (!queue.isEmpty())
        {
            WorldLocation current = queue.remove();
            int currentBlockID = current.getBlockId();

            if (currentBlockID == BlockIds.NETHER_PORTAL)
            {
                PortalTexture texture = ((TileEntityNetherPortal) current.getTileEntity()).PortalTexture;

                if (texture == oldTexture)
                {
                    updateTexture(current, newTexture, oldTexture);
                    queue = updateQueue(queue, current, shape);
                }
            }
            else if (updateModifiers && currentBlockID == BlockIds.PORTAL_MODIFIER)
            {
                TileEntityPortalModifier modifier = (TileEntityPortalModifier) current.getTileEntity();

                if (modifier.getTexture() == oldTexture) // This should only affect modifiers if they're the same texture as what's being replaced
                {
                    modifier.setTexture(newTexture);

                    if (current.getWorld().isRemote)
                    {
                        current.markBlockForUpdate();
                    }
                    else
                    {
                        Reference.NetworkManager.sendUpdatePacketToNearbyClients(modifier);
                    }
                }
            }
        }
    }
    
    private static Queue<WorldLocation> updateQueue(Queue<WorldLocation> queue, WorldLocation location, PortalShape shape)
    {
        if (shape == PortalShape.X)
        {
            queue.add(location.getOffset(ForgeDirection.UP));
            queue.add(location.getOffset(ForgeDirection.DOWN));
            queue.add(location.getOffset(ForgeDirection.EAST));
            queue.add(location.getOffset(ForgeDirection.WEST));
        }
        else if (shape == PortalShape.Z)
        {
            queue.add(location.getOffset(ForgeDirection.UP));
            queue.add(location.getOffset(ForgeDirection.DOWN));
            queue.add(location.getOffset(ForgeDirection.NORTH));
            queue.add(location.getOffset(ForgeDirection.SOUTH));
        }
        else if (shape == PortalShape.HORIZONTAL)
        {
            queue.add(location.getOffset(ForgeDirection.NORTH));
            queue.add(location.getOffset(ForgeDirection.SOUTH));
            queue.add(location.getOffset(ForgeDirection.EAST));
            queue.add(location.getOffset(ForgeDirection.WEST));
        }
        else if (shape == PortalShape.UNKNOWN) // used for deconstructing portals
        {
            queue.add(location.getOffset(ForgeDirection.UP));
            queue.add(location.getOffset(ForgeDirection.DOWN));
            queue.add(location.getOffset(ForgeDirection.NORTH));
            queue.add(location.getOffset(ForgeDirection.SOUTH));
            queue.add(location.getOffset(ForgeDirection.EAST));
            queue.add(location.getOffset(ForgeDirection.WEST));
        }

        return queue;
    }

    public static void updateTexture(WorldLocation location, PortalTexture newTexture, PortalTexture oldTexture)
    {
        if (location.getBlockId() != BlockIds.NETHER_PORTAL || oldTexture == null || oldTexture == PortalTexture.UNKNOWN)
        {
            return;
        }
        
        TileEntityNetherPortal netherPortal = (TileEntityNetherPortal) location.getTileEntity();
        
        if (netherPortal.PortalTexture != oldTexture)
        {
            return;
        }
        
        netherPortal.PortalTexture = newTexture;
        
        if (location.getWorld().isRemote)
        {
            location.markBlockForUpdate();
        }
        else
        {
            if (location.getMetadata() == 1)
            {
                Reference.NetworkManager.sendUpdatePacketToNearbyClients(netherPortal);
            }
        }
    }

    public static void removePortal(WorldLocation worldLocation, PortalShape unknown)
    {
        // TODO Auto-generated method stub
        
    }

    public static boolean createPortal(WorldLocation worldLocation)
    {
        // TODO Auto-generated method stub
        return false;
    }

    public static boolean isValidExitPortal(World world, WorldLocation selectedExit, TileEntityPortalModifier modifier, Entity entity, boolean b)
    {
        // TODO Auto-generated method stub
        return false;
    }

    public static void createPortalAround(World world, int x, int y, int z, EntityPlayer player)
    {
        // TODO Auto-generated method stub
        
    }

    public static boolean createPortalAroundBlock(WorldLocation worldLocation)
    {
        // TODO Auto-generated method stub
        return false;
    }
}
