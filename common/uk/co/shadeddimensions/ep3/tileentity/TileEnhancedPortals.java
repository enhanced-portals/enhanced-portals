package uk.co.shadeddimensions.ep3.tileentity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.ForgeDirection;
import uk.co.shadeddimensions.ep3.network.ClientProxy;
import uk.co.shadeddimensions.ep3.util.GuiPayload;
import uk.co.shadeddimensions.ep3.util.WorldCoordinates;

public class TileEnhancedPortals extends TileEntity
{
    @Override
    public boolean canUpdate()
    {
        return false;
    }

    public void fillPacket(DataOutputStream stream) throws IOException
    {

    }

    public ChunkCoordinates getChunkCoordinates()
    {
        return new ChunkCoordinates(xCoord, yCoord, zCoord);
    }

    protected byte getHighestPowerState()
    {
        byte current = 0;

        for (int i = 0; i < 6; i++)
        {
            ForgeDirection d = ForgeDirection.getOrientation(i);
            byte c = (byte) worldObj.getIndirectPowerLevelTo(xCoord + d.offsetX, yCoord + d.offsetY, zCoord + d.offsetZ, i);

            if (c > current)
            {
                current = c;
            }
        }

        return current;
    }

    public WorldCoordinates getWorldCoordinates()
    {
        return new WorldCoordinates(xCoord, yCoord, zCoord, worldObj.provider.dimensionId);
    }

    public void guiActionPerformed(GuiPayload payload, EntityPlayer player)
    {

    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
    }

    public void usePacket(DataInputStream stream) throws IOException
    {

    }

    @Override
    public void validate()
    {
        super.validate();

        if (worldObj.isRemote)
        {
            ClientProxy.requestTileData(this);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
    }
}
