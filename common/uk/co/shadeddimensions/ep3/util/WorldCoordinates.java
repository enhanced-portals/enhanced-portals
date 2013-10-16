package uk.co.shadeddimensions.ep3.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.DimensionManager;

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
}
