package enhancedportals.tileentity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import enhancedportals.utility.WorldCoordinates;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;

public class TileEP extends TileEntity
{
    @Override
    public boolean canUpdate()
    {
        return false;
    }

    public ChunkCoordinates getChunkCoordinates()
    {
        return new ChunkCoordinates(xCoord, yCoord, zCoord);
    }

    public WorldCoordinates getWorldCoordinates()
    {
        return new WorldCoordinates(getChunkCoordinates(), worldObj.provider.dimensionId);
    }
    
    public void packetGuiFill(DataOutputStream stream) throws IOException
    {

    }

    public void packetGuiUse(DataInputStream stream) throws IOException
    {

    }
}
