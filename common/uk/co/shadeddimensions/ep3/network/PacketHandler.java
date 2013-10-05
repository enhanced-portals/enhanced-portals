package uk.co.shadeddimensions.ep3.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import uk.co.shadeddimensions.ep3.network.packet.MainPacket;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler
{
    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
    {
        MainPacket pckt = MainPacket.readPacket(manager, packet.data);

        if (pckt == null)
        {
            return;
        }

        pckt.execute(manager, (EntityPlayer) player);
    }
}
