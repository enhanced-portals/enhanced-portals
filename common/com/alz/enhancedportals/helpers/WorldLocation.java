package com.alz.enhancedportals.helpers;

import com.alz.enhancedportals.EnhancedPortals;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class WorldLocation
{
    public int xCoord, yCoord, zCoord, dimension;
    
    public static boolean equals(WorldLocation location, WorldLocation location2)
    {
        return location2.xCoord == location.xCoord && location2.yCoord == location.yCoord && location2.zCoord == location.zCoord && location2.dimension == location.dimension;
    }
    
    public boolean equals(WorldLocation loc)
    {
        return equals(this, loc);
    }
    
    public WorldLocation()
    {
        
    }
    
    public WorldLocation(int x, int y, int z)
    {
        xCoord = x;
        yCoord = y;
        zCoord = z;
    }
    
    public WorldLocation(int x, int y, int z, World world)
    {
        xCoord = x;
        yCoord = y;
        zCoord = z;
        dimension = world.provider.dimensionId;
    }
    
    public WorldLocation(int x, int y, int z, IBlockAccess blockAccess)
    {
        xCoord = x;
        yCoord = y;
        zCoord = z;
        dimension = 0; // TODO
    }
    
    public World getWorld()
    {
        return EnhancedPortals.proxy.getWorldForDimension(dimension);
    }
    
    public int getBlockId()
    {
        return getWorld().getBlockId(xCoord, yCoord, zCoord);
    }
    
    public int getMetadata()
    {
        return getWorld().getBlockMetadata(xCoord, yCoord, zCoord);
    }
    
    public TileEntity getTileEntity()
    {
        return getWorld().getBlockTileEntity(xCoord, yCoord, zCoord);
    }
    
    public void markBlockForUpdate()
    {
        getWorld().markBlockForUpdate(xCoord, yCoord, zCoord);
    }
    
    public void setBlock(int id)
    {
        getWorld().setBlock(xCoord, yCoord, zCoord, id);
    }
    
    public void setBlockToAir()
    {
        getWorld().setBlockToAir(xCoord, yCoord, zCoord);
    }

    public WorldLocation getOffset(ForgeDirection direction)
    {
        WorldLocation newLocation = new WorldLocation();
        newLocation.dimension = dimension;
        newLocation.xCoord = xCoord + direction.offsetX;
        newLocation.yCoord = yCoord + direction.offsetY;
        newLocation.zCoord = zCoord + direction.offsetZ;

        return newLocation;
    }
}
