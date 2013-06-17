package enhancedportals.network.packet;

import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import enhancedcore.packet.PacketHelper;
import enhancedcore.world.WorldLocation;
import enhancedportals.EnhancedPortals;
import enhancedportals.lib.BlockIds;
import enhancedportals.tileentity.TileEntityDialDevice;
import enhancedportals.tileentity.TileEntityDialDeviceBasic;

public class PacketDialRequest extends PacketEnhancedPortals
{
    int xCoord, yCoord, zCoord, dimension;
    String network;

    public PacketDialRequest()
    {

    }

    public PacketDialRequest(TileEntity dialDevice, String diallingNetwork)
    {
        xCoord = dialDevice.xCoord;
        yCoord = dialDevice.yCoord;
        zCoord = dialDevice.zCoord;
        dimension = dialDevice.worldObj.provider.dimensionId;
        network = diallingNetwork;
    }

    @Override
    public PacketEnhancedPortals consumePacket(DataInputStream stream) throws IOException
    {
        xCoord = stream.readInt();
        yCoord = stream.readInt();
        zCoord = stream.readInt();
        dimension = stream.readInt();
        network = stream.readUTF();

        return this;
    }

    @Override
    public void execute(INetworkManager network, EntityPlayer player)
    {
        World world = EnhancedPortals.proxy.getWorld(dimension);

        if (!world.isRemote)
        {
            WorldLocation loc = new WorldLocation(xCoord, yCoord, zCoord, world);

            if (loc.getBlockId() == BlockIds.DialDeviceBasic)
            {
                if (loc.getTileEntity() instanceof TileEntityDialDeviceBasic)
                {
                    TileEntityDialDeviceBasic device = (TileEntityDialDeviceBasic) loc.getTileEntity();

                    device.processDiallingRequest(this.network, player);
                }
            }
            else if (loc.getBlockId() == BlockIds.DialDevice)
            {
                if (loc.getTileEntity() instanceof TileEntityDialDevice)
                {
                    TileEntityDialDevice device = (TileEntityDialDevice) loc.getTileEntity();

                    device.processDiallingRequest(Integer.parseInt(this.network), player);
                }
            }
        }
    }

    @Override
    public byte[] generatePacket(Object... data)
    {
        return PacketHelper.getByteArray(xCoord, yCoord, zCoord, dimension, network);
    }
}
