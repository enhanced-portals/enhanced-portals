package enhancedportals.network.packet;

import java.io.DataInputStream;

import net.minecraft.tileentity.TileEntity;
import enhancedportals.lib.PacketIds;
import enhancedportals.portal.PortalTexture;

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

    public PacketNetworkData(TileEntity entity, String name, String network, PortalTexture text, byte thick, boolean particles, boolean sound)
    {
        xCoord = entity.xCoord;
        yCoord = entity.yCoord;
        zCoord = entity.zCoord;
        dimension = entity.worldObj.provider.dimensionId;

        packetData = new PacketData(new int[] { 1, text.colour, text.blockID, text.metaData }, new byte[] { thick, (byte) (particles ? 1 : 0), (byte) (sound ? 1 : 0) }, new String[] { name, network, text.liquidID });
    }

    @Override
    public int getPacketID()
    {
        return PacketIds.NetworkData;
    }
}
