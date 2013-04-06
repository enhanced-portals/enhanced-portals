package alz.mods.enhancedportals.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import alz.mods.enhancedportals.EnhancedPortals;
import alz.mods.enhancedportals.common.CommonProxy;
import alz.mods.enhancedportals.networking.ITileEntityNetworking;
import alz.mods.enhancedportals.networking.PacketAddPortalData;
import alz.mods.enhancedportals.networking.PacketAllPortalData;
import alz.mods.enhancedportals.networking.PacketDataRequest;
import alz.mods.enhancedportals.networking.PacketGuiRequest;
import alz.mods.enhancedportals.networking.PacketTileUpdate;
import alz.mods.enhancedportals.portals.PortalData;
import alz.mods.enhancedportals.reference.Reference;
import alz.mods.enhancedportals.tileentity.TileEntityDialDevice;
import alz.mods.enhancedportals.tileentity.TileEntityNetherPortal;
import alz.mods.enhancedportals.tileentity.TileEntityPortalModifier;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.PacketDispatcher;

public class ClientProxy extends CommonProxy
{
	public static void SendBlockUpdate(ITileEntityNetworking tileEntity)
	{
		PacketDispatcher.sendPacketToServer(tileEntity.getUpdatePacket().getClientPacket());
	}

	public static void OnTileUpdate(PacketTileUpdate packet, byte type)
	{
		if (type == 0)
		{
			return;
		}
		
		World world = FMLClientHandler.instance().getClient().theWorld;

		if (world.blockHasTileEntity(packet.xCoord, packet.yCoord, packet.zCoord))
		{
			TileEntity tileEntity = world.getBlockTileEntity(packet.xCoord, packet.yCoord, packet.zCoord);
			
			if (tileEntity instanceof TileEntityPortalModifier)
			{
				TileEntityPortalModifier modifier = (TileEntityPortalModifier) tileEntity;
	
				modifier.parseUpdatePacket(packet);
			}
			else if (tileEntity instanceof TileEntityNetherPortal)
			{
				TileEntityNetherPortal netherPortal = (TileEntityNetherPortal) tileEntity;
				
				netherPortal.parseUpdatePacket(packet);
			}
		}
	}

	public static void RequestTileData(TileEntity tileEntity)
	{
		PacketDispatcher.sendPacketToServer(new PacketDataRequest(tileEntity).getClientPacket());
	}
	
	public static void OpenGuiFromLocal(EntityPlayer player, TileEntity tileEntity, int guiID)
	{
		PacketDispatcher.sendPacketToServer(new PacketGuiRequest(guiID, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord).getClientPacket());		
		player.openGui(EnhancedPortals.instance, guiID, tileEntity.worldObj, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
	}

	public static void SendNewPortalData(TileEntityDialDevice dialDevice, String text)
	{
		PacketAddPortalData packet = new PacketAddPortalData();
		packet.portalData = new PortalData();
		packet.portalData.DisplayName = text;
		packet.Dimension = dialDevice.worldObj.provider.dimensionId;
		packet.xCoord = dialDevice.xCoord;
		packet.yCoord = dialDevice.yCoord;
		packet.zCoord = dialDevice.zCoord;
		
		PacketDispatcher.sendPacketToServer(packet.getClientPacket());
	}

	public static void onAllPacketData(PacketAllPortalData packetPortal)
	{
		World world = FMLClientHandler.instance().getClient().theWorld;
				
		if (world.provider.dimensionId != packetPortal.Dimension)
		{
			return;
		}
		
		if (world.getBlockId(packetPortal.xCoord, packetPortal.yCoord, packetPortal.zCoord) == Reference.BlockIDs.DialDevice)
		{
			TileEntityDialDevice dialDevice = (TileEntityDialDevice) world.getBlockTileEntity(packetPortal.xCoord, packetPortal.yCoord, packetPortal.zCoord);
			
			dialDevice.PortalDataList = null;
			dialDevice.PortalDataList = packetPortal.portalDataList;
		}
	}
}
