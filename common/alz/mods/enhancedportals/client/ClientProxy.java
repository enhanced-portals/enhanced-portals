package alz.mods.enhancedportals.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import alz.mods.enhancedportals.EnhancedPortals;
import alz.mods.enhancedportals.common.CommonProxy;
import alz.mods.enhancedportals.networking.PacketDataRequest;
import alz.mods.enhancedportals.networking.PacketGuiRequest;
import alz.mods.enhancedportals.networking.PacketTileUpdate;
import alz.mods.enhancedportals.reference.Reference;
import alz.mods.enhancedportals.tileentity.TileEntityPortalModifier;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.PacketDispatcher;

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
	
	public static void OpenGuiFromLocal(EntityPlayer player, TileEntity tileEntity, int guiID)
	{
		PacketDispatcher.sendPacketToServer(new PacketGuiRequest(guiID, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord).getPacket());		
		player.openGui(EnhancedPortals.instance, guiID, tileEntity.worldObj, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
	}
}
