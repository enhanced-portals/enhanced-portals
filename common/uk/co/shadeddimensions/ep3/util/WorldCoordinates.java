package uk.co.shadeddimensions.ep3.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeDirection;

public class WorldCoordinates extends ChunkCoordinates
{
    public int dimension;

    public WorldCoordinates()
    {

    }

    public WorldCoordinates(ChunkCoordinates chunkCoordinates, int dim)
    {
        super(chunkCoordinates);
        dimension = dim;
    }

    public WorldCoordinates(int x, int y, int z, int dim)
    {
        super(x, y, z);
        dimension = dim;
    }

    public WorldCoordinates(WorldCoordinates coord)
    {
        super(coord.posX, coord.posY, coord.posZ);
        dimension = coord.dimension;
    }
    
    public int getBlockId()
    {
        return DimensionManager.getWorld(dimension).getBlockId(posX, posY, posZ);
    }
    
    public int getBlockMetadata()
    {
        return DimensionManager.getWorld(dimension).getBlockMetadata(posX, posY, posZ);
    }
    
    public TileEntity getBlockTileEntity()
    {
        return DimensionManager.getWorld(dimension).getBlockTileEntity(posX, posY, posZ);
    }

    public WorldCoordinates offset(ForgeDirection orientation)
    {
        return new WorldCoordinates(posX + orientation.offsetX, posY + orientation.offsetY, posZ + orientation.offsetZ, dimension);
    }

    public World getWorld()
    {
        return DimensionManager.getWorld(dimension);
    }
    
    @Override
    public String toString()
    {
        return String.format("WorldCoordinates (X %s, Y %s, Z %s, D %s)", posX, posY, posZ, dimension);
    }
}
