package uk.co.shadeddimensions.enhancedportals.tileentity;

import net.minecraft.entity.player.EntityPlayer;
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

    public void actionPerformed(int id, String string, EntityPlayer player)
    {

    }

    public void actionPerformed(int id, int data, EntityPlayer player)
    {

    }

    public void buttonPressed(int button, EntityPlayer player)
    {

    }
}
