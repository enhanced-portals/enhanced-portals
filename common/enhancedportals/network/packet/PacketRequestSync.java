package enhancedportals.network.packet;

import java.io.DataInputStream;

import enhancedportals.lib.PacketIds;
import enhancedportals.tileentity.TileEntityEnhancedPortals;

public class PacketRequestSync extends PacketUpdate
{
    public PacketRequestSync()
    {
        packetData = null;
        isChunkPacket = false;
    }

    public PacketRequestSync(DataInputStream stream)
    {
        super(stream);
    }

    public PacketRequestSync(TileEntityEnhancedPortals tileEntity)
    {
        super();

        xCoord = tileEntity.xCoord;
        yCoord = tileEntity.yCoord;
        zCoord = tileEntity.zCoord;
        dimension = tileEntity.worldObj.provider.dimensionId;
    }

    @Override
    public int getPacketID()
    {
        return PacketIds.RequestSync;
    }
}
