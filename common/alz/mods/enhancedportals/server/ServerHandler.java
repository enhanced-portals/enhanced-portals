package alz.mods.enhancedportals.server;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import alz.mods.enhancedportals.networking.ITileEntityNetworking;
import alz.mods.enhancedportals.networking.PacketAllPortalData;
import alz.mods.enhancedportals.tileentity.TileEntityDialDevice;
import alz.mods.enhancedportals.tileentity.TileEntityNetherPortal;
import alz.mods.enhancedportals.tileentity.TileEntityPortalModifier;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class ServerHandler
{
	public ModifierNetwork ModifierNetwork;
	public DialDeviceNetwork DialDeviceNetwork;
	private MinecraftServer serverInstance;

	public ServerHandler()
	{
		serverInstance = MinecraftServer.getServer();

		ModifierNetwork = new ModifierNetwork(serverInstance);
		DialDeviceNetwork = new DialDeviceNetwork(serverInstance);
	}

	public void saveAllData()
	{
		ModifierNetwork.saveData();
		DialDeviceNetwork.saveData();
	}

	public WorldServer getWorldServerForDimension(int dim)
	{
		return serverInstance.worldServerForDimension(dim);
	}

	public void sendUpdatePacketToNearbyClients(TileEntityPortalModifier modifier)
	{
		PacketDispatcher.sendPacketToAllAround(modifier.xCoord + 0.5, modifier.yCoord + 0.5, modifier.zCoord + 0.5, 128, modifier.worldObj.provider.dimensionId, modifier.getUpdatePacket().getServerPacket());
	}

	public void sendUpdatePacketToNearbyClients(TileEntityNetherPortal modifier)
	{
		PacketDispatcher.sendPacketToAllAround(modifier.xCoord + 0.5, modifier.yCoord + 0.5, modifier.zCoord + 0.5, 128, modifier.worldObj.provider.dimensionId, modifier.getUpdatePacket().getServerPacket());
	}

	public void sendUpdatePacketToPlayer(ITileEntityNetworking tileEntity, Player player)
	{
		PacketDispatcher.sendPacketToPlayer(tileEntity.getUpdatePacket().getServerPacket(), player);
	}

	public void sendDialDevicePacketToNearbyClients(TileEntityDialDevice dialDevice)
	{
		PacketAllPortalData packetData = new PacketAllPortalData();
		packetData.xCoord = dialDevice.xCoord;
		packetData.yCoord = dialDevice.yCoord;
		packetData.zCoord = dialDevice.zCoord;
		packetData.Dimension = dialDevice.worldObj.provider.dimensionId;
		packetData.portalDataList = dialDevice.PortalDataList;

		PacketDispatcher.sendPacketToAllAround(dialDevice.xCoord + 0.5, dialDevice.yCoord + 0.5, dialDevice.zCoord + 0.5, 128, dialDevice.worldObj.provider.dimensionId, packetData.getServerPacket());
	}
}