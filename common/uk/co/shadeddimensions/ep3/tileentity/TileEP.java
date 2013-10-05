package uk.co.shadeddimensions.ep3.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;

public class TileEP extends TileEntity
{
    public void actionPerformed(int id, int data, EntityPlayer player)
    {

    }

    public void actionPerformed(int id, String string, EntityPlayer player)
    {

    }

    public void buttonPressed(int button, EntityPlayer player)
    {

    }

    public ChunkCoordinates getChunkCoordinates()
    {
        return new ChunkCoordinates(xCoord, yCoord, zCoord);
    }
}
