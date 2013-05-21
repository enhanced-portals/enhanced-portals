package enhancedportals.network.packet;

import java.io.DataInputStream;

import net.minecraft.tileentity.TileEntity;
import enhancedportals.lib.PacketIds;

public class PacketNetworkData extends PacketUpdate
{
    public PacketNetworkData()
    {
        super();
    }

    public PacketNetworkData(DataInputStream stream)
    {
        super(stream);
    }

    public PacketNetworkData(TileEntity entity, int id, String name, String network)
    {
        xCoord = entity.xCoord;
        yCoord = entity.yCoord;
        zCoord = entity.zCoord;
        dimension = entity.worldObj.provider.dimensionId;

        packetData = new PacketData(new int[] { 0, id }, new byte[0], new String[] { name, network });
    }

    public PacketNetworkData(TileEntity entity, String name, String network, String text, byte thick, boolean particles, boolean sound)
    {
        xCoord = entity.xCoord;
        yCoord = entity.yCoord;
        zCoord = entity.zCoord;
        dimension = entity.worldObj.provider.dimensionId;

        packetData = new PacketData(new int[] { 1 }, new byte[] { thick, (byte) (particles ? 1 : 0), (byte) (sound ? 1 : 0) }, new String[] { name, network, text });
    }

    @Override
    public int getPacketID()
    {
        return PacketIds.NetworkData;
    }
}
