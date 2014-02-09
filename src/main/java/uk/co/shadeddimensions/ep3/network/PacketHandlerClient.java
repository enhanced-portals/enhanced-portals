package uk.co.shadeddimensions.ep3.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import uk.co.shadeddimensions.ep3.network.packet.PacketEnhancedPortals;
import uk.co.shadeddimensions.ep3.network.packet.PacketGuiData;
import uk.co.shadeddimensions.ep3.network.packet.PacketRequestData;
import uk.co.shadeddimensions.ep3.tileentity.TileEP;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class PacketHandlerClient implements IPacketHandler
{
    public static void requestTileData(TileEP tile)
    {
        PacketDispatcher.sendPacketToServer(new PacketRequestData(tile).getPacket());
    }
    
    public static void sendGuiPacket(NBTTagCompound tag)
    {
        PacketDispatcher.sendPacketToServer(new PacketGuiData(tag).getPacket());
    }
    
    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
    {
        DataInputStream stream = new DataInputStream(new ByteArrayInputStream(packet.data));
        byte packetType = -1;

        try
        {
            packetType = stream.readByte();
            PacketEnhancedPortals p = PacketEnhancedPortals.getPacket(packetType);
            p.readPacketData(stream);
            p.clientPacket(manager, p, player);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
