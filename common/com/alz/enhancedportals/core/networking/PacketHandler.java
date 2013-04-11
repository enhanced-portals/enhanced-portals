package com.alz.enhancedportals.core.networking;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.logging.Level;

import com.alz.enhancedportals.core.networking.packets.PacketRequestSync;
import com.alz.enhancedportals.core.networking.packets.PacketTileEntityUpdate;
import com.alz.enhancedportals.core.tileentity.TileEntityEnhancedPortals;
import com.alz.enhancedportals.reference.Log;
import com.alz.enhancedportals.reference.PacketIds;
import com.alz.enhancedportals.reference.Reference;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

public class PacketHandler implements IPacketHandler
{
    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
    {
        if (packet.channel != Reference.MOD_ID)
        {
            return;
        }
        
        if (FMLCommonHandler.instance().getSide() == Side.SERVER)
        {
            handlePacket(packet, player);
        }
        else if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
        {
            ClientPacketHandler.handlePacket(packet);
        }
    }
    
    private void handlePacket(Packet250CustomPayload packet, Player player)
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
                case PacketIds.TILE_ENTITY_UPDATE: // The server shouldn't recieve this packet, let's ignore it
                {
                    // Invalid packet
                    return;
                }
                case PacketIds.REQUEST_DATA:
                {
                    PacketRequestSync packetSync = new PacketRequestSync();
                    packetSync.readPacketData(stream);
                    handlePacketRequestSync(packetSync, player);
                };
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void handlePacketRequestSync(PacketRequestSync packetSync, Player player)
    {
        WorldServer world = MinecraftServer.getServer().worldServerForDimension(packetSync.Dimension);
        TileEntity tileEntity = world.getBlockTileEntity(packetSync.xCoord, packetSync.yCoord, packetSync.zCoord);
        
        if (tileEntity != null && tileEntity instanceof TileEntityEnhancedPortals)
        {
            PacketTileEntityUpdate packetUpdate = ((TileEntityEnhancedPortals)tileEntity).getUpdateData();
            
            PacketDispatcher.sendPacketToPlayer(packetUpdate.getPacket(), player);
        }
        else
        {
            Log.log(Level.WARNING, String.format("Tile entity not found or was of unexpected type. %s, %s, %s", packetSync.xCoord, packetSync.yCoord, packetSync.zCoord));
        }
    }
}
