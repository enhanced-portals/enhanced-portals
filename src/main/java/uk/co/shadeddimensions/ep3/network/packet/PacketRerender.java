package uk.co.shadeddimensions.ep3.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import cpw.mods.fml.common.network.Player;

public class PacketRerender extends PacketEnhancedPortals
{
    int posX, posY, posZ;

    public PacketRerender()
    {
        
    }
    
    public PacketRerender(int x, int y, int z)
    {
        posX = x;
        posY = y;
        posZ = z;
    }

    @Override
    public void readPacketData(DataInputStream stream) throws IOException
    {
        posX = stream.readInt();
        posY = stream.readInt();
        posZ = stream.readInt();
    }

    @Override
    public void writePacketData(DataOutputStream stream) throws IOException
    {
        stream.writeInt(posX);
        stream.writeInt(posY);
        stream.writeInt(posZ);
    }
    
    @Override
    public void clientPacket(INetworkManager manager, PacketEnhancedPortals packet, Player player)
    {
        ((EntityPlayer) player).worldObj.markBlockForRenderUpdate(posX, posY, posZ);
    }
}
