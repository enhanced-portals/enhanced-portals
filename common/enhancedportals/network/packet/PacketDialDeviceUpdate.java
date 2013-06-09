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
    public PacketEnhancedPortals consumePacket(byte[] data)
    {
        try
        {
            Object[] objArray = PacketHelper.getObjects(data, "I", "I", "I", "I", "B", "I");

            if (objArray != null)
            {
                xCoord = (int) objArray[0];
                yCoord = (int) objArray[1];
                zCoord = (int) objArray[2];
                dimension = (int) objArray[3];
                active = (boolean) objArray[4];
                tickTimer = (int) objArray[5];

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
