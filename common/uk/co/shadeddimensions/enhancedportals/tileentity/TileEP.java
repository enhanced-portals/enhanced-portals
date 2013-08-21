package uk.co.shadeddimensions.enhancedportals.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Icon;

public class TileEP extends TileEntity
{
    public Icon getTexture(int side, int renderpass)
    {
        return null;
    }
    
    public ChunkCoordinates getChunkCoordinates()
    {
        return new ChunkCoordinates(xCoord, yCoord, zCoord);
    }
}
