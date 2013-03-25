package alz.mods.enhancedportals.networking;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import alz.mods.enhancedportals.client.ClientProxy;
import alz.mods.enhancedportals.reference.Reference;
import alz.mods.enhancedportals.tileentity.TileEntityPortalModifier;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler
{
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
	{
		if (!packet.channel.equals(Reference.MOD_ID))
			return;
		
		DataInputStream dataStream = new DataInputStream(new ByteArrayInputStream(packet.data));
				
		try
		{
			int packetID = dataStream.read();
						
			if (packetID == Reference.Networking.TileEntityUpdate)
			{
				byte packetType = dataStream.readByte();
				
				PacketTileUpdate packetUpdate = new PacketTileUpdate();
				packetUpdate.getPacketData(dataStream);
				onTileEntityUpdate(packetUpdate, packetType);
			}
			else if (packetID == Reference.Networking.DataRequest)
			{
				PacketDataRequest packetData = new PacketDataRequest();
				packetData.getPacketData(dataStream);
				onDataRequest(packetData, player);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void onTileEntityUpdate(PacketTileUpdate packet, byte type)
	{
		if (type == 1)
		{
			ClientProxy.OnTileUpdate(packet, type);
			return;
		}
		
		World world = Reference.LinkData.serverInstance.worldServerForDimension(packet.dimension);
				
		if (world.getBlockId(packet.xCoord, packet.yCoord, packet.zCoord) == Reference.BlockIDs.PortalModifier && world.blockHasTileEntity(packet.xCoord, packet.yCoord, packet.zCoord))
		{
			TileEntityPortalModifier modifier = (TileEntityPortalModifier) world.getBlockTileEntity(packet.xCoord, packet.yCoord, packet.zCoord);
			
			modifier.parseUpdatePacket(packet);			
			Reference.LinkData.sendUpdatePacketToNearbyClients(modifier);				
			Reference.LinkData.AddToFrequency(packet.data[0], packet.xCoord, packet.yCoord, packet.zCoord, packet.dimension);
		}
	}
	
	private void onDataRequest(PacketDataRequest packet, Player player)
	{
		if (Reference.LinkData == null)
			return;
		
		World world = Reference.LinkData.serverInstance.worldServerForDimension(packet.dimension);
		
		if (world.getBlockId(packet.xCoord, packet.yCoord, packet.zCoord) == Reference.BlockIDs.PortalModifier && world.blockHasTileEntity(packet.xCoord, packet.yCoord, packet.zCoord))
		{
			TileEntityPortalModifier modifier = (TileEntityPortalModifier) world.getBlockTileEntity(packet.xCoord, packet.yCoord, packet.zCoord);
			
			Reference.LinkData.sendUpdatePacketToPlayer(modifier, player);
		}
	}
}
