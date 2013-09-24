package uk.co.shadeddimensions.enhancedportals.util;

import net.minecraft.util.ChunkCoordinates;

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
}
