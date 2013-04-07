package alz.mods.enhancedportals.networking;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import alz.mods.enhancedportals.EnhancedPortals;
import alz.mods.enhancedportals.client.ClientProxy;
import alz.mods.enhancedportals.item.ItemScroll;
import alz.mods.enhancedportals.portals.PortalData;
import alz.mods.enhancedportals.portals.PortalTexture;
import alz.mods.enhancedportals.reference.Reference;
import alz.mods.enhancedportals.tileentity.TileEntityDialDevice;
import alz.mods.enhancedportals.tileentity.TileEntityNetherPortal;
import alz.mods.enhancedportals.tileentity.TileEntityPortalModifier;
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

			System.out.println("Recieved packet ID of " + packetID);

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
			else if (packetID == Reference.Networking.GuiRequest)
			{
				PacketGuiRequest packetGui = new PacketGuiRequest();
				packetGui.getPacketData(dataStream);
				onGuiRequest(packetGui, player);
			}
			else if (packetID == Reference.Networking.DialDevice_NewPortalData)
			{
				PacketAddPortalData packetPortal = new PacketAddPortalData();
				packetPortal.getPacketData(dataStream);
				onAddPortalData(packetPortal, player);
			}
			else if (packetID == Reference.Networking.DialDevice_AllPortalData)
			{
				PacketAllPortalData packetPortal = new PacketAllPortalData();
				packetPortal.getPacketData(dataStream);
				ClientProxy.onAllPacketData(packetPortal);
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

		World world = Reference.LinkData.serverInstance.worldServerForDimension(packet.Dimension);

		if (world.blockHasTileEntity(packet.xCoord, packet.yCoord, packet.zCoord))
		{
			TileEntity tileEntity = world.getBlockTileEntity(packet.xCoord, packet.yCoord, packet.zCoord);

			if (tileEntity instanceof TileEntityPortalModifier)
			{
				TileEntityPortalModifier modifier = (TileEntityPortalModifier) tileEntity;

				modifier.parseUpdatePacket(packet);
				Reference.LinkData.sendUpdatePacketToNearbyClients(modifier);
				Reference.LinkData.AddToFrequency(packet.data[0], packet.xCoord, packet.yCoord, packet.zCoord, packet.Dimension);
			}
		}
	}

	private void onDataRequest(PacketDataRequest packet, Player player)
	{
		if (Reference.LinkData == null)
			return;

		World world = Reference.LinkData.serverInstance.worldServerForDimension(packet.Dimension);

		if (world.blockHasTileEntity(packet.xCoord, packet.yCoord, packet.zCoord))
		{
			TileEntity tileEntity = world.getBlockTileEntity(packet.xCoord, packet.yCoord, packet.zCoord);

			if (tileEntity instanceof TileEntityPortalModifier)
			{
				TileEntityPortalModifier modifier = (TileEntityPortalModifier) tileEntity;
				Reference.LinkData.sendUpdatePacketToPlayer(modifier, player);
			}
			else if (tileEntity instanceof TileEntityNetherPortal)
			{
				TileEntityNetherPortal netherPortal = (TileEntityNetherPortal) tileEntity;
				Reference.LinkData.sendUpdatePacketToPlayer(netherPortal, player);
			}
		}
	}

	private void onGuiRequest(PacketGuiRequest packet, Player player)
	{
		if (!(player instanceof EntityPlayer))
			return;

		EntityPlayer Player = (EntityPlayer) player;

		if (Reference.LinkData == null || Player.worldObj.isRemote)
			return;

		Player.openGui(EnhancedPortals.instance, packet.guiID, Player.worldObj, packet.xCoord, packet.yCoord, packet.zCoord);
	}

	private void onAddPortalData(PacketAddPortalData packet, Player player)
	{
		if (Reference.LinkData == null || ((EntityPlayer) player).worldObj.isRemote)
			return;

		World world = Reference.LinkData.serverInstance.worldServerForDimension(packet.Dimension);

		if (world.getBlockId(packet.xCoord, packet.yCoord, packet.zCoord) == Reference.BlockIDs.DialDevice && world.blockHasTileEntity(packet.xCoord, packet.yCoord, packet.zCoord))
		{
			TileEntityDialDevice dialDevice = (TileEntityDialDevice) world.getBlockTileEntity(packet.xCoord, packet.yCoord, packet.zCoord);

			ItemStack itemScroll = dialDevice.inventory[0], itemModifier = dialDevice.inventory[1];
			PortalData portalData = packet.portalData;

			dialDevice.inventory = new ItemStack[dialDevice.getSizeInventory()];

			if (itemScroll.itemID == Reference.ItemIDs.ItemScroll + 256 && itemScroll.getItemDamage() == 1)
			{
				portalData.TeleportData = ((ItemScroll) itemScroll.getItem()).getLocationData(itemScroll);
			}

			if (itemModifier != null)
			{
				if (itemModifier.itemID == Item.bucketLava.itemID)
				{
					portalData.Texture = PortalTexture.LAVA;
				}
				else if (itemModifier.itemID == Item.bucketWater.itemID)
				{
					portalData.Texture = PortalTexture.WATER;
				}
				else if (itemModifier.itemID == Item.dyePowder.itemID)
				{
					portalData.Texture = PortalTexture.getPortalTexture(PortalTexture.SwapColours(itemModifier.getItemDamage()));
				}
			}
			else
			{
				portalData.Texture = PortalTexture.PURPLE;
			}

			dialDevice.PortalDataList.add(portalData);
			Reference.LinkData.sendDialDevicePacketToNearbyClients(dialDevice);
		}
	}
}
