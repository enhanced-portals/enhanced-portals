package enhancedportals.network.packet;

import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import enhancedcore.packet.PacketHelper;
import enhancedcore.world.BlockPosition;
import enhancedcore.world.WorldHelper;
import enhancedportals.tileentity.TileEntityDialDevice;

public class PacketDialDeviceUpdate extends PacketEnhancedPortals
{
    int dimension;
    BlockPosition position;
    boolean active;
    int tickTimer;

    public PacketDialDeviceUpdate()
    {

    }

    public PacketDialDeviceUpdate(TileEntityDialDevice device)
    {
        position = device.getBlockPosition();
        dimension = device.worldObj.provider.dimensionId;
        active = device.active;
        tickTimer = device.tickTimer;
    }

    @Override
    public PacketEnhancedPortals consumePacket(DataInputStream stream) throws IOException
    {
        position = BlockPosition.getBlockPosition(stream);
        dimension = stream.readInt();
        active = stream.readBoolean();
        tickTimer = stream.readInt();

        return this;
    }

    @Override
    public void execute(INetworkManager network, EntityPlayer player)
    {
        if (WorldHelper.getTileEntity(dimension, position) instanceof TileEntityDialDevice)
        {
            TileEntityDialDevice dial = (TileEntityDialDevice) WorldHelper.getTileEntity(dimension, position);

            dial.active = active;
            dial.tickTimer = tickTimer;
        }
    }

    @Override
    public byte[] generatePacket(Object... data)
    {
        return PacketHelper.getByteArray(position, dimension, active, tickTimer);
    }
}
