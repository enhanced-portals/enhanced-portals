package enhancedportals.network.packet;

import java.io.DataInputStream;

import net.minecraft.tileentity.TileEntity;
import enhancedportals.lib.PacketIds;

public class PacketUpgrade extends PacketUpdate
{
    public PacketUpgrade()
    {
    }

    public PacketUpgrade(DataInputStream stream)
    {
        super(stream);
    }
    
    public PacketUpgrade(int x, int y, int z, int dim, PacketData data)
    {
        super(x, y, z, dim, data);
    }

    public PacketUpgrade(TileEntity tileEntity, int upgrade, boolean state)
    {
        super(tileEntity, null);
        
        packetData = new PacketData();
        packetData.integerData = new int[] { upgrade, state ? 1 : 0 };
    }

    public void setData(int upgrade, boolean state)
    {
        packetData = new PacketData();
        packetData.integerData = new int[] { upgrade, state ? 1 : 0 };
    }
    
    @Override
    public int getPacketID()
    {
        return PacketIds.PortalModifierUpgrade;
    }
}
