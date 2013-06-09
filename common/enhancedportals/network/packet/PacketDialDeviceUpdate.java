package enhancedportals.network.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.world.World;
import enhancedcore.packet.PacketHelper;
import enhancedportals.EnhancedPortals;
import enhancedportals.tileentity.TileEntityDialDevice;

public class PacketDialDeviceUpdate extends PacketEnhancedPortals
{
    int     xCoord, yCoord, zCoord, dimension;
    boolean active;
    int     tickTimer;

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
    public PacketEnhancedPortals consumePacket(byte[] data)
    {
        try
        {
            Object[] objArray = PacketHelper.getObjects(data, "I", "I", "I", "I", "B", "I");

            if (objArray != null)
            {
                xCoord = Integer.parseInt(objArray[0].toString());
                yCoord = Integer.parseInt(objArray[1].toString());
                zCoord = Integer.parseInt(objArray[2].toString());
                dimension = Integer.parseInt(objArray[3].toString());
                active = Boolean.parseBoolean(objArray[4].toString());
                tickTimer = Integer.parseInt(objArray[5].toString());

                return this;
            }
            else
            {
                return null;
            }
        }
        catch (Exception e)
        {
            return null;
        }
    }

    @Override
    public void execute(INetworkManager network, EntityPlayer player)
    {
        World world = EnhancedPortals.proxy.getWorld(dimension);

        if (world.getBlockTileEntity(xCoord, yCoord, zCoord) instanceof TileEntityDialDevice)
        {
            ((TileEntityDialDevice) world.getBlockTileEntity(xCoord, yCoord, zCoord)).active = active;
            ((TileEntityDialDevice) world.getBlockTileEntity(xCoord, yCoord, zCoord)).tickTimer = tickTimer;
        }
    }

    @Override
    public byte[] generatePacket(Object... data)
    {
        return PacketHelper.getByteArray(xCoord, yCoord, zCoord, dimension, active, tickTimer);
    }
}
