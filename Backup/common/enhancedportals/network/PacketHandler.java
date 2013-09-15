package enhancedportals.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import enhancedportals.network.packet.PacketEnhancedPortals;

public class PacketHandler implements IPacketHandler
{
    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
    {
        PacketEnhancedPortals pckt = PacketEnhancedPortals.readPacket(manager, packet.data);

        if (pckt == null)
        {
            return;
        }

        pckt.execute(manager, (EntityPlayer) player);
    }
}
