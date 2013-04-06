package alz.mods.enhancedportals.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import alz.mods.enhancedportals.networking.ITileEntityNetworking;
import alz.mods.enhancedportals.networking.PacketAllPortalData;
import alz.mods.enhancedportals.reference.Reference;
import alz.mods.enhancedportals.teleportation.TeleportData;
import alz.mods.enhancedportals.tileentity.TileEntityDialDevice;
import alz.mods.enhancedportals.tileentity.TileEntityNetherPortal;
import alz.mods.enhancedportals.tileentity.TileEntityPortalModifier;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

public class LinkData
{
	Map<Integer, List<TeleportData>> LinkData;
	public MinecraftServer serverInstance;

	public LinkData(MinecraftServer server)
	{
		serverInstance = server;

		loadWorldData();
	}

	public void loadWorldData()
	{
		World world = serverInstance.worldServerForDimension(0);
		LinkData = new HashMap<Integer, List<TeleportData>>();
		BufferedReader reader;
		int frequency = -1;
		List<TeleportData> items = new ArrayList<TeleportData>();

		if (FMLCommonHandler.instance().getSide() == Side.SERVER)
		{
			Reference.File.DataFile = serverInstance.getFile(world.getSaveHandler().getSaveDirectoryName() + File.separator + Reference.MOD_ID + ".dat").getAbsolutePath();
		}
		else
		{
			Reference.File.DataFile = serverInstance.getFile("saves" + File.separator + world.getSaveHandler().getSaveDirectoryName() + File.separator + Reference.MOD_ID + ".dat").getAbsolutePath();
		}

		if (!new File(Reference.File.DataFile).exists())
			return;

		try
		{
			reader = new BufferedReader(new FileReader(Reference.File.DataFile));
			String line = null;

			while ((line = reader.readLine()) != null)
			{
				if (line.startsWith(">"))
				{
					if (frequency != -1)
					{
						AddFrequency(frequency);

						for (TeleportData item : items)
						{
							AddToFrequency(frequency, item);
						}
					}

					frequency = Integer.parseInt(line.replace(">", ""));
					items = new ArrayList<TeleportData>();
				}
				else if (line.startsWith("#"))
				{
					String theLine[] = line.replace("#", "").split(",");

					if (theLine.length == 4)
					{
						items.add(new TeleportData(Integer.parseInt(theLine[0]), Integer.parseInt(theLine[1]), Integer.parseInt(theLine[2]), Integer.parseInt(theLine[3])));
					}
				}
			}

			if (frequency != -1)
			{
				AddFrequency(frequency);

				for (TeleportData item : items)
				{
					AddToFrequency(frequency, item);
				}
			}

			reader.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void saveWorldData()
	{
		BufferedWriter writer;

		try
		{
			writer = new BufferedWriter(new FileWriter(Reference.File.DataFile));

			for (int list : LinkData.keySet())
			{
				List<TeleportData> items = GetFrequency(list);

				writer.write(">" + list);
				writer.newLine();

				for (TeleportData item : items)
				{
					writer.write("#" + item.getX() + "," + item.getY() + "," + item.getZ() + "," + item.getDimension());
					writer.newLine();
				}
			}

			writer.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public List<TeleportData> getFrequencyExcluding(int frequency, TeleportData exclude)
	{
		List<TeleportData> theItems = GetFrequency(frequency);
		List<TeleportData> returnItems = new ArrayList<TeleportData>();

		if (theItems == null)
			return returnItems;

		for (int i = 0; i < theItems.size(); i++)
		{
			TeleportData item = theItems.get(i);

			if (item.getX() == exclude.getX() && item.getY() == exclude.getY() && item.getZ() == exclude.getZ() && item.getDimension() == exclude.getDimension())
			{
				;
			}
			else
			{
				returnItems.add(item);
			}
		}

		return returnItems;
	}

	public boolean DoesFrequencyExist(int frequency)
	{
		return LinkData.containsKey(frequency);
	}

	public boolean IsInFrequency(int frequency, TeleportData data)
	{
		if (!DoesFrequencyExist(frequency))
			return false;

		List<TeleportData> list = GetFrequency(frequency);

		for (int i = 0; i < list.size(); i++)
		{
			if (list.get(i).getX() == data.getX() && list.get(i).getY() == data.getY() && list.get(i).getZ() == data.getZ() && list.get(i).getDimension() == data.getDimension())
				return true;
		}

		return false;
	}

	public List<TeleportData> GetFrequency(int frequency)
	{
		if (!DoesFrequencyExist(frequency))
			return null;

		return LinkData.get(frequency);
	}

	public void AddFrequency(int frequency)
	{
		if (LinkData.containsKey(frequency))
			return;

		LinkData.put(frequency, new ArrayList<TeleportData>());
	}

	public void AddToFrequency(int frequency, int x, int y, int z, int dimension)
	{
		AddToFrequency(frequency, new TeleportData(x, y, z, dimension));
	}

	public void AddToFrequency(int frequency, TeleportData data)
	{
		long freq = IsInAFrequency(data);

		if (freq != Integer.MAX_VALUE + 1)
		{
			RemoveFromFrequency((int) freq, data);
		}

		if (!DoesFrequencyExist(frequency))
		{
			AddFrequency(frequency);
		}

		GetFrequency(frequency).add(data);
	}

	public void RemoveFromFrequency(int frequency, int x, int y, int z, int dimension)
	{
		RemoveFromFrequency(frequency, new TeleportData(x, y, z, dimension));
	}

	public void RemoveFromFrequency(int frequency, TeleportData data)
	{
		if (!DoesFrequencyExist(frequency))
		{
			AddFrequency(frequency);
		}

		if (IsInFrequency(frequency, data))
		{
			List<TeleportData> list = GetFrequency(frequency);

			for (int i = 0; i < list.size(); i++)
			{
				if (list.get(i).getX() == data.getX() && list.get(i).getY() == data.getY() && list.get(i).getZ() == data.getZ() && list.get(i).getDimension() == data.getDimension())
				{
					list.remove(i);
				}
			}
		}
	}

	public void RemoveFrequency(int frequency)
	{
		if (!DoesFrequencyExist(frequency))
			return;

		LinkData.remove(frequency);
	}

	public long IsInAFrequency(TeleportData data)
	{
		for (int key : LinkData.keySet())
		{
			if (IsInFrequency(key, data))
				return key;
		}

		return Integer.MAX_VALUE + 1;
	}

	public void sendUpdatePacketToPlayer(int x, int y, int z, int dim, Player player)
	{
		WorldServer world = serverInstance.worldServerForDimension(dim);

		if (world.getBlockId(x, y, z) == Reference.BlockIDs.PortalModifier && world.blockHasTileEntity(x, y, z))
		{
			TileEntityPortalModifier modifier = (TileEntityPortalModifier) world.getBlockTileEntity(x, y, z);

			sendUpdatePacketToPlayer(modifier, player);
		}
	}

	public void sendUpdatePacketToPlayer(ITileEntityNetworking tileEntity, Player player)
	{
		PacketDispatcher.sendPacketToPlayer(tileEntity.getUpdatePacket().getServerPacket(), player);
	}

	public void sendUpdatePacketToNearbyClients(TileEntityPortalModifier modifier)
	{
		PacketDispatcher.sendPacketToAllAround(modifier.xCoord + 0.5, modifier.yCoord + 0.5, modifier.zCoord + 0.5, 128, modifier.worldObj.provider.dimensionId, modifier.getUpdatePacket().getServerPacket());
	}
	
	public void sendUpdatePacketToNearbyClients(TileEntityNetherPortal modifier)
	{
		PacketDispatcher.sendPacketToAllAround(modifier.xCoord + 0.5, modifier.yCoord + 0.5, modifier.zCoord + 0.5, 128, modifier.worldObj.provider.dimensionId, modifier.getUpdatePacket().getServerPacket());
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
