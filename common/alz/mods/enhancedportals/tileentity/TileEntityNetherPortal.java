package alz.mods.enhancedportals.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import alz.mods.enhancedportals.client.ClientProxy;
import alz.mods.enhancedportals.common.WorldLocation;
import alz.mods.enhancedportals.networking.ITileEntityNetworking;
import alz.mods.enhancedportals.networking.PacketTileUpdate;
import alz.mods.enhancedportals.portals.PortalHandler;
import alz.mods.enhancedportals.portals.PortalTexture;

public class TileEntityNetherPortal extends TileEntity implements ITileEntityNetworking
{
    public PortalTexture Texture;

    public TileEntityNetherPortal()
    {
        Texture = PortalTexture.PURPLE;
    }

    @Override
    public PacketTileUpdate getUpdatePacket()
    {
        PacketTileUpdate packet = new PacketTileUpdate();
        packet.xCoord = xCoord;
        packet.yCoord = yCoord;
        packet.zCoord = zCoord;
        packet.Dimension = worldObj.provider.dimensionId;
        packet.data = new int[] { Texture.ordinal() };

        return packet;
    }

    @Override
    public void parseUpdatePacket(PacketTileUpdate packet)
    {
        PortalHandler.Data.floodUpdateTexture(new WorldLocation(worldObj, xCoord, yCoord, zCoord), PortalTexture.getPortalTexture(packet.data[0]), Texture, true);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);

        Texture = PortalTexture.getPortalTexture(tagCompound.getInteger("Texture"));
    }

    @Override
    public void validate()
    {
        super.validate();

        if (worldObj.isRemote && getBlockMetadata() == 1)
        {
            ClientProxy.RequestTileData(this);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);

        tagCompound.setInteger("Texture", Texture.ordinal());
    }
}
