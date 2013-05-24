package enhancedportals.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.WorldServer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import enhancedportals.network.packet.PacketEnhancedPortals;

public class ServerPacketHandler implements IPacketHandler
{
    public WorldServer getWorldForDimension(int dim)
    {
        return FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(dim);
    }

    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
    {
        PacketEnhancedPortals pckt = PacketEnhancedPortals.readPacket(manager, packet.data);

        if (pckt == null)
        {
            return;
        }

        System.out.println("SERVER processing packet");
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
            else if (packetID == PacketIds.RequestSync)
            {
                parseRequestSync(new PacketRequestSync(stream), player);
            }
            else if (packetID == PacketIds.Gui)
            {
                parseGui(new PacketGui(stream), player);
            }
            else if (packetID == PacketIds.NetworkUpdate)
            {
                parseNetwork(new PacketNetworkUpdate(stream));
            }
            else if (packetID == PacketIds.PortalModifierUpgrade)
            {
                parseUpgrade(new PacketUpgrade(stream), player);
            }
            else if (packetID == PacketIds.DialDeviceRequest)
            {
                parseDialRequest(new PacketDialRequest(stream), player);
            }
            else if (packetID == PacketIds.NetworkData)
            {
                parseNetworkData(new PacketNetworkData(stream));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }*/
    }
}
