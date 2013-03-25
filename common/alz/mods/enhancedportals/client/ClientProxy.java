package alz.mods.enhancedportals.client;

import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import alz.mods.enhancedportals.common.CommonProxy;
import alz.mods.enhancedportals.networking.PacketDataRequest;
import alz.mods.enhancedportals.networking.PacketTileUpdate;
import alz.mods.enhancedportals.reference.Reference;
import alz.mods.enhancedportals.tileentity.TileEntityPortalModifier;

public class ClientProxy extends CommonProxy
{	
	public static void SendBlockUpdate(TileEntityPortalModifier modifier)
	{
		PacketDispatcher.sendPacketToServer(modifier.getUpdatePacket().getClientPacket());
	}
	
	public static void OnTileUpdate(PacketTileUpdate packet, byte type)
	{
		if (type == 0)
			return;
		
		World world = FMLClientHandler.instance().getClient().theWorld;
				
		if (world.getBlockId(packet.xCoord, packet.yCoord, packet.zCoord) == Reference.BlockIDs.PortalModifier && world.blockHasTileEntity(packet.xCoord, packet.yCoord, packet.zCoord))
		{
			TileEntityPortalModifier modifier = (TileEntityPortalModifier) world.getBlockTileEntity(packet.xCoord, packet.yCoord, packet.zCoord);
			
			modifier.parseUpdatePacket(packet);
		}
	}
	
	public static void RequestTileData(TileEntityPortalModifier modifier)
	{
		PacketDispatcher.sendPacketToServer(new PacketDataRequest(modifier).getPacket());
	}
}
