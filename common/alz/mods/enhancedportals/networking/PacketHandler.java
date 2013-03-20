package alz.mods.enhancedportals.networking;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import alz.mods.enhancedportals.reference.Reference;
import alz.mods.enhancedportals.tileentity.TileEntityPortalModifier;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;
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
			byte packetType = dataStream.readByte();
			
			if (packetID == Reference.Networking.TileEntityUpdate)
			{
				PacketTileUpdate packetUpdate = new PacketTileUpdate();
				packetUpdate.getPacketData(dataStream);
				onTileEntityUpdate(packetUpdate, packetType);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void onTileEntityUpdate(PacketTileUpdate packet, byte type)
	{
		World world = null;
		
		if (type == 0)
			world = Reference.LinkData.serverInstance.worldServerForDimension(packet.dimension);
		else if (type == 1)
			world = FMLClientHandler.instance().getClient().theWorld;
				
		if (world.getBlockId(packet.xCoord, packet.yCoord, packet.zCoord) == Reference.BlockIDs.PortalModifier && world.blockHasTileEntity(packet.xCoord, packet.yCoord, packet.zCoord))
		{
			TileEntityPortalModifier modifier = (TileEntityPortalModifier) world.getBlockTileEntity(packet.xCoord, packet.yCoord, packet.zCoord);
			
			modifier.parseUpdatePacket(packet);
			
			if (type == 0)
			{
				Reference.LinkData.sendUpdatePacketToNearbyClients(modifier);
				
				Reference.LinkData.AddToFrequency(packet.data[0], packet.xCoord, packet.yCoord, packet.zCoord, packet.dimension);
			}
		}
	}
}
