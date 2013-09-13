package uk.co.shadeddimensions.enhancedportals.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class PacketGuiRequest extends MainPacket
{
    int id;

    public PacketGuiRequest()
    {

    }

    public PacketGuiRequest(int guiID)
    {
        id = guiID;
    }

    @Override
    public MainPacket consumePacket(DataInputStream stream) throws IOException
    {
        id = stream.readInt();

        return this;
    }

    @Override
    public void execute(INetworkManager network, EntityPlayer player)
    {
        boolean validRequest = false, sendPacket = player instanceof EntityPlayerMP;
        // Container c = player.openContainer;

        if (validRequest && sendPacket)
        {
            PacketDispatcher.sendPacketToPlayer(MainPacket.makePacket(this), (Player) player);
        }
    }

    @Override
    public void generatePacket(DataOutputStream stream) throws IOException
    {
        stream.writeInt(id);
    }
}
