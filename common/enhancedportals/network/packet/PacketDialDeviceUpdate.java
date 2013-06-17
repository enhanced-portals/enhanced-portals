package enhancedportals.network.packet;

import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.world.World;
import enhancedcore.packet.PacketHelper;
import enhancedportals.EnhancedPortals;
import enhancedportals.tileentity.TileEntityDialDevice;

public class PacketDialDeviceUpdate extends PacketEnhancedPortals
{
    int xCoord, yCoord, zCoord, dimension;
    boolean active;
    int tickTimer;

    public PacketDialDeviceUpdate()
    {

    }

    public PacketDialDeviceUpdate(TileEntityDialDevice device)
    {
        xCoord = device.xCoord;
        yCoord = device.yCoord;
        zCoord = device.zCoord;
        dimension = device.worldObj.provider.dimensionId;
        active = device.active;
        tickTimer = device.tickTimer;
    }

    @Override
    public PacketEnhancedPortals consumePacket(DataInputStream stream) throws IOException
    {
        xCoord = stream.readInt();
        yCoord = stream.readInt();
        zCoord = stream.readInt();
        dimension = stream.readInt();
        active = stream.readBoolean();
        tickTimer = stream.readInt();

        return this;
    }

    @Override
    public void execute(INetworkManager network, EntityPlayer player)
    {
        World world = EnhancedPortals.proxy.getWorld(dimension);

        if (world.getBlockTileEntity(xCoord, yCoord, zCoord) instanceof TileEntityDialDevice)
        {
            TileEntityDialDevice dial = (TileEntityDialDevice) world.getBlockTileEntity(xCoord, yCoord, zCoord);

            dial.active = active;
            dial.tickTimer = tickTimer;
        }
    }

    @Override
    public byte[] generatePacket(Object... data)
    {
        return PacketHelper.getByteArray(xCoord, yCoord, zCoord, dimension, active, tickTimer);
    }
}
