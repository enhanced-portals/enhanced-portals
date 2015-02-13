package enhancedportals.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import enhancedportals.util.DimensionCoordinates;

public class TileEP extends TileEntity
{
    public boolean onBlockActivated(EntityPlayer player)
    {
        return false;
    }

    public ChunkCoordinates getChunkCoordinates()
    {
        return new ChunkCoordinates(xCoord, yCoord, zCoord);
    }

    public DimensionCoordinates getDimensionCoordinates()
    {
        return new DimensionCoordinates(xCoord, yCoord, zCoord, worldObj.provider.dimensionId);
    }

    public void onPreDestroy()
    {

    }
}
