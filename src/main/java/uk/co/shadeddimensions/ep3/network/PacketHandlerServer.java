package uk.co.shadeddimensions.ep3.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import uk.co.shadeddimensions.ep3.network.packet.PacketEnhancedPortals;
import uk.co.shadeddimensions.ep3.network.packet.PacketTileGui;
import uk.co.shadeddimensions.ep3.tileentity.TileEP;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class PacketHandlerServer implements IPacketHandler
{
    public static void sendPacketToAllAround(TileEntity tile, Packet250CustomPayload packet)
    {
        PacketDispatcher.sendPacketToAllAround(tile.xCoord + 0.5, tile.yCoord + 0.5, tile.zCoord + 0.5, 128, tile.worldObj.provider.dimensionId, packet);
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
            p.serverPacket(manager, p, player);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void sendGuiPacketToPlayer(TileEP tile, EntityPlayer player)
    {
        PacketDispatcher.sendPacketToPlayer(new PacketTileGui(tile).getPacket(), (Player) player);
    }
    
    public static void sendGuiPacketToPlayer(TileEP tile, Player player)
    {
        PacketDispatcher.sendPacketToPlayer(new PacketTileGui(tile).getPacket(), player);
    }

    //public static void sendGuiPacketToAllAround(TileEP tile)
    //{
    //    sendPacketToAllAround(tile, packet);
    //}
}
