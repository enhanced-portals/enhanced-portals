package enhancedportals.network.packet;

import java.io.DataInputStream;

import enhancedportals.lib.PacketIds;
import enhancedportals.tileentity.TileEntityEnhancedPortals;

public class PacketGui extends PacketUpdate
{
    public PacketGui()
    {
        isChunkPacket = false;
    }
    
    public PacketGui(boolean requesting, boolean state, int id)
    {
        this();
        packetData = new PacketData();
        packetData.integerData = new int[] { requesting ? 1 : 0, state ? 1 : 0, id };
    }
    
    public PacketGui(boolean requesting, boolean state, int id, TileEntityEnhancedPortals portalModifier)
    {
        this();
        packetData = new PacketData();
        packetData.integerData = new int[] { requesting ? 1 : 0, state ? 1 : 0, id };
        xCoord = portalModifier.xCoord;
        yCoord = portalModifier.yCoord;
        zCoord = portalModifier.zCoord;
        dimension = portalModifier.worldObj.provider.dimensionId;
    }
    
    public PacketGui(DataInputStream stream)
    {
        super(stream);
    }

    @Override
    public int getPacketID()
    {
        return PacketIds.Gui;
    }
}
