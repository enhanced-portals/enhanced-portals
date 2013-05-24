package enhancedportals.network;

import java.util.logging.Level;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import enhancedportals.lib.Reference;
import enhancedportals.network.packet.PacketEnhancedPortals;

public class ClientPacketHandler implements IPacketHandler
{
    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
    {
        PacketEnhancedPortals pckt = PacketEnhancedPortals.readPacket(manager, packet.data);

        if (pckt == null)
        {
            Reference.log.log(Level.WARNING, "Recieved unknown packet.");
            return;
        }

        pckt.execute(manager, (EntityPlayer) player);

        /*if (!packet.channel.equals(Reference.MOD_ID))
        {
            return;
        }

        DataInputStream stream = new DataInputStream(new ByteArrayInputStream(packet.data));
        byte packetID = -1;

        try
        {
            packetID = stream.readByte();

            if (packetID == PacketIds.TileEntityUpdate)
            {
                parseTileEntityUpdate(new PacketTEUpdate(stream));
            }
            else if (packetID == PacketIds.Gui)
            {
                parseGui(new PacketGui(stream));
            }
            else if (packetID == PacketIds.NetworkUpdate)
            {
                parseNetwork(new PacketNetworkUpdate(stream));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }*/
    }
}
