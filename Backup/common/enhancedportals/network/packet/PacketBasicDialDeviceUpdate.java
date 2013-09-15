package enhancedportals.network.packet;

import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import enhancedcore.packet.PacketHelper;
import enhancedcore.world.BlockPosition;
import enhancedcore.world.WorldHelper;
import enhancedportals.tileentity.TileEntityDialDeviceBasic;

public class PacketBasicDialDeviceUpdate extends PacketEnhancedPortals
{
    int dimension;
    BlockPosition position;
    boolean isActive;

    public PacketBasicDialDeviceUpdate()
    {

    }

    public PacketBasicDialDeviceUpdate(TileEntityDialDeviceBasic device)
    {
        position = device.getBlockPosition();
        dimension = device.worldObj.provider.dimensionId;
        isActive = device.active;
    }

    @Override
    public PacketEnhancedPortals consumePacket(DataInputStream stream) throws IOException
    {
        position = BlockPosition.getBlockPosition(stream);
        dimension = stream.readInt();
        isActive = stream.readBoolean();

        return this;
    }

    @Override
    public void execute(INetworkManager network, EntityPlayer player)
    {
        if (WorldHelper.getTileEntity(dimension, position) instanceof TileEntityDialDeviceBasic)
        {
            ((TileEntityDialDeviceBasic) WorldHelper.getTileEntity(dimension, position)).active = isActive;
        }
    }

    @Override
    public byte[] generatePacket(Object... data)
    {
        return PacketHelper.getByteArray(position, dimension, isActive);
    }
}
