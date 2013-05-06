package enhancedportals.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import enhancedportals.EnhancedPortals;
import enhancedportals.lib.PacketIds;
import enhancedportals.lib.Reference;
import enhancedportals.network.packet.PacketRequestSync;
import enhancedportals.network.packet.PacketTEUpdate;

public class PacketHandler implements IPacketHandler
{
    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
    {
        if (packet.channel != Reference.MOD_ID)
        {
            return;
        }

        DataInputStream stream = new DataInputStream(new ByteArrayInputStream(packet.data));
        byte packetID = -1;

        try
        {
            packetID = stream.readByte();
         
            System.out.println("Recieved packet of ID " + packetID);
            
            if (packetID == PacketIds.TileEntityUpdate)
            {
                EnhancedPortals.proxy.parseTileEntityUpdate(new PacketTEUpdate(stream));
            }
            else if (packetID == PacketIds.RequestSync)
            {                
                EnhancedPortals.proxy.parseRequestSync(new PacketRequestSync(stream), player);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
