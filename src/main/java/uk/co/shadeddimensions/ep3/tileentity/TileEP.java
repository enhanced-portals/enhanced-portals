package uk.co.shadeddimensions.ep3.tileentity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import uk.co.shadeddimensions.ep3.network.PacketHandlerClient;
import uk.co.shadeddimensions.ep3.util.WorldCoordinates;

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
    
    /**
     * Should only be used for data that needs to be displayed ONLY in the GUI. If it needs to be displayed in-world as well as the GUI, use packetFill/packetUse.
     * @param stream
     * @throws IOException
     */
    public void packetGuiFill(DataOutputStream stream) throws IOException
    {

    }

    /**
     * Should only be used for data that needs to be displayed ONLY in the GUI. If it needs to be displayed in-world as well as the GUI, use packetFill/packetUse.
     * @param stream
     * @throws IOException
     */
    public void packetGuiUse(DataInputStream stream) throws IOException
    {

    }

    /**
     * Called when the player sends a GUI packet to the server.
     * @param tag
     * @param player
     */
    public void packetGui(NBTTagCompound tag, EntityPlayer player)
    {
        
    }
}
