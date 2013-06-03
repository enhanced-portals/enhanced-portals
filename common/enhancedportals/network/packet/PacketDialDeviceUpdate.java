package enhancedportals.network.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;

public class PacketDialDeviceUpdate extends PacketEnhancedPortals
{
    int xCoord, yCoord, zCoord, dimension;
    
    public PacketDialDeviceUpdate()
    {
        // TODO Auto-generated constructor stub
    }

    @Override
    public PacketEnhancedPortals consumePacket(byte[] data)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void execute(INetworkManager network, EntityPlayer player)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public byte[] generatePacket(Object... data)
    {
        // TODO Auto-generated method stub
        return null;
    }
}
