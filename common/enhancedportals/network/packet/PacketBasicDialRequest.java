package enhancedportals.network.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.world.World;
import enhancedcore.packet.PacketHelper;
import enhancedcore.world.WorldLocation;
import enhancedportals.EnhancedPortals;
import enhancedportals.lib.BlockIds;
import enhancedportals.tileentity.TileEntityDialDeviceBasic;

public class PacketBasicDialRequest extends PacketEnhancedPortals
{
    int    xCoord, yCoord, zCoord, dimension;
    String network;

    public PacketBasicDialRequest()
    {

    }

    public PacketBasicDialRequest(TileEntityDialDeviceBasic dialDevice, String diallingNetwork)
    {
        xCoord = dialDevice.xCoord;
        yCoord = dialDevice.yCoord;
        zCoord = dialDevice.zCoord;
        dimension = dialDevice.worldObj.provider.dimensionId;
        network = diallingNetwork;
    }

    @Override
    public PacketEnhancedPortals consumePacket(byte[] data)
    {
        try
        {
            Object[] objArray = PacketHelper.getObjects(data, "I", "I", "I", "I", "S");

            if (objArray != null)
            {
                xCoord = (int) objArray[0];
                yCoord = (int) objArray[1];
                zCoord = (int) objArray[2];
                dimension = (int) objArray[3];
                network = (String) objArray[4];

                return this;
            }
            else
            {
                return null;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void execute(INetworkManager network, EntityPlayer player)
    {
        World world = EnhancedPortals.proxy.getWorld(dimension);

        if (!world.isRemote)
        {
            WorldLocation loc = new WorldLocation(xCoord, yCoord, zCoord, world);

            if (loc.getBlockId() == BlockIds.DialHomeDeviceBasic)
            {
                if (loc.getTileEntity() instanceof TileEntityDialDeviceBasic)
                {
                    TileEntityDialDeviceBasic device = (TileEntityDialDeviceBasic) loc.getTileEntity();

                    device.processDiallingRequest(this.network, player);
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
