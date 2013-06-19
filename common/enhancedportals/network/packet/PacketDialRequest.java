package enhancedportals.network.packet;

import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.world.World;
import enhancedcore.packet.PacketHelper;
import enhancedcore.world.BlockPosition;
import enhancedcore.world.WorldHelper;
import enhancedportals.EnhancedPortals;
import enhancedportals.lib.BlockIds;
import enhancedportals.tileentity.TileEntityDialDevice;
import enhancedportals.tileentity.TileEntityDialDeviceBasic;
import enhancedportals.tileentity.TileEntityEnhancedPortals;

public class PacketDialRequest extends PacketEnhancedPortals
{
    int dimension;
    BlockPosition position;
    String network;

    public PacketDialRequest()
    {

    }

    public PacketDialRequest(TileEntityEnhancedPortals dialDevice, String diallingNetwork)
    {
        position = dialDevice.getBlockPosition();
        dimension = dialDevice.worldObj.provider.dimensionId;
        network = diallingNetwork;
    }

    @Override
    public PacketEnhancedPortals consumePacket(DataInputStream stream) throws IOException
    {
        position = BlockPosition.getBlockPosition(stream);
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
            if (WorldHelper.getBlockId(world, position) == BlockIds.DialDeviceBasic)
            {
                if (WorldHelper.getTileEntity(world, position) instanceof TileEntityDialDeviceBasic)
                {
                    TileEntityDialDeviceBasic device = (TileEntityDialDeviceBasic) WorldHelper.getTileEntity(world, position);

                    device.processDiallingRequest(this.network, player);
                }
            }
            else if (WorldHelper.getBlockId(world, position) == BlockIds.DialDevice)
            {
                if (WorldHelper.getTileEntity(world, position) instanceof TileEntityDialDevice)
                {
                    TileEntityDialDevice device = (TileEntityDialDevice) WorldHelper.getTileEntity(world, position);

                    device.processDiallingRequest(Integer.parseInt(this.network), player);
                }
            }
        }
    }

    @Override
    public byte[] generatePacket(Object... data)
    {
        return PacketHelper.getByteArray(position, dimension, network);
    }
}
