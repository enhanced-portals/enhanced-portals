package enhancedportals.network.packet;

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
    int    xCoord, yCoord, zCoord, dimension;
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
    public PacketEnhancedPortals consumePacket(byte[] data)
    {
        try
        {
            Object[] objArray = PacketHelper.getObjects(data, "I", "I", "I", "I", "S");

            if (objArray != null)
            {
                xCoord = Integer.parseInt(objArray[0].toString());
                yCoord = Integer.parseInt(objArray[1].toString());
                zCoord = Integer.parseInt(objArray[2].toString());
                dimension = Integer.parseInt(objArray[3].toString());
                network = objArray[4].toString();

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
            else if (loc.getBlockId() == BlockIds.DialHomeDevice)
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
