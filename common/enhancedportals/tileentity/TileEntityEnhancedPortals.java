package enhancedportals.tileentity;

import net.minecraft.tileentity.TileEntity;
import enhancedportals.network.packet.PacketData;

public abstract class TileEntityEnhancedPortals extends TileEntity
{
    public abstract PacketData getPacketData();

    public abstract void parsePacketData(PacketData data);
}
