package com.alz.enhancedportals.core.networking;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.logging.Level;

import com.alz.enhancedportals.core.networking.packets.PacketTileEntityUpdate;
import com.alz.enhancedportals.core.tileentity.TileEntityEnhancedPortals;
import com.alz.enhancedportals.reference.Log;
import com.alz.enhancedportals.reference.PacketIds;

import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;

public class ClientPacketHandler
{
    public static void handlePacket(Packet250CustomPayload packet)
    {
        DataInputStream stream = new DataInputStream(new ByteArrayInputStream(packet.data));
        byte packetID = -1;
        
        try
        {
            packetID = stream.readByte();
            
            switch (packetID)
            {
                default:
                case -1:
                case PacketIds.REQUEST_DATA: // The client shouldn't recieve this packet.
                {
                    // Invalid packet
                    return;
                }
                case PacketIds.TILE_ENTITY_UPDATE:
                {
                    PacketTileEntityUpdate packetUpdate = new PacketTileEntityUpdate();
                    packetUpdate.readPacketData(stream);
                    handleTileEntityUpdate(packetUpdate);                    
                };
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private static void handleTileEntityUpdate(PacketTileEntityUpdate packetUpdate)
    {
        WorldServer world = MinecraftServer.getServer().worldServerForDimension(packetUpdate.Dimension);
        TileEntity tileEntity = world.getBlockTileEntity(packetUpdate.xCoord, packetUpdate.yCoord, packetUpdate.zCoord);
        
        if (tileEntity != null && tileEntity instanceof TileEntityEnhancedPortals)
        {
            ((TileEntityEnhancedPortals)tileEntity).parseTileData(packetUpdate);
        }
        else
        {
            Log.log(Level.WARNING, String.format("Tile entity not found or was of unexpected type. %s, %s, %s", packetUpdate.xCoord, packetUpdate.yCoord, packetUpdate.zCoord));
        }
    }
}
