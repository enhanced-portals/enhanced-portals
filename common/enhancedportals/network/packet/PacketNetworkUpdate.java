package enhancedportals.network.packet;

import java.io.DataInputStream;

import enhancedportals.lib.PacketIds;
import enhancedportals.tileentity.TileEntityPortalModifier;

public class PacketNetworkUpdate extends PacketTEUpdate
{
    public PacketNetworkUpdate()
    {
        super();
    }

    public PacketNetworkUpdate(DataInputStream stream)
    {
        super(stream);
    }

    public PacketNetworkUpdate(TileEntityPortalModifier tileEntity)
    {
        super();

        xCoord = tileEntity.xCoord;
        yCoord = tileEntity.yCoord;
        zCoord = tileEntity.zCoord;
        dimension = tileEntity.worldObj.provider.dimensionId;
        packetData = new PacketData(new int[] {}, new String[] { tileEntity.network });
        isChunkPacket = true;
    }

    @Override
    public int getPacketID()
    {
        return PacketIds.NetworkUpdate;
    }
}
