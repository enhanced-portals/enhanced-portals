package alz.mods.enhancedportals.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alz.mods.enhancedportals.helpers.WorldHelper;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class LinkData
{
	Map<Integer, List<int[]>> LinkData;
	MinecraftServer serverInstance;
	
	public LinkData(MinecraftServer server)
	{
		serverInstance = server;
		
		loadWorldData();
	}
	
	public void loadWorldData()
	{
		World world = serverInstance.worldServerForDimension(0);
		LinkData = new HashMap<Integer, List<int[]>>();
		BufferedReader reader;
		int frequency = -1;
		List<int[]> items = new ArrayList<int[]>();
		
		if (FMLCommonHandler.instance().getSide() == Side.SERVER)		
			Reference.DataFile = serverInstance.getFile(world.getSaveHandler().getSaveDirectoryName() + File.separator + "EnhancedPortals.dat").getAbsolutePath();
		else
			Reference.DataFile = serverInstance.getFile("saves" + File.separator + world.getSaveHandler().getSaveDirectoryName() + File.separator + "EnhancedPortals.dat").getAbsolutePath();
		
		if (!new File(Reference.DataFile).exists())
			return;
		
		try
		{
			reader = new BufferedReader(new FileReader(Reference.DataFile));
			String line = null;			
			
			while ((line = reader.readLine()) != null)
			{
				if (line.startsWith(">"))
				{
					if (frequency != -1)
					{
						AddFrequency(frequency);
						
						for (int[] item : items)
						{
							AddToFrequency(frequency, item);
						}
					}
					
					frequency = Integer.parseInt(line.replace(">", ""));
					items = new ArrayList<int[]>();
				}
				else if (line.startsWith("#"))
				{
					String theLine[] = line.replace("#", "").split(",");

					if (theLine.length == 4)
					{
						items.add(new int[] { Integer.parseInt(theLine[0]), Integer.parseInt(theLine[1]), Integer.parseInt(theLine[2]), Integer.parseInt(theLine[3]) });
					}
				}
			}
			
			if (frequency != -1)
			{
				AddFrequency(frequency);
				
				for (int[] item : items)
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
			writer = new BufferedWriter(new FileWriter(Reference.DataFile));
			
			for (int list : LinkData.keySet())
			{
				List<int[]> items = GetFrequency(list);
				
				writer.write(">" + list);
				writer.newLine();
				
				for (int[] item : items)
				{
					writer.write("#" + item[0] + "," + item[1] + "," + item[2] + "," + item[3]);	
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
	
	public List<int[]> getFrequencyExcluding(int frequency, int[] exclude)
	{
		List<int[]> theItems = GetFrequency(frequency);
		List<int[]> returnItems = new ArrayList<int[]>();
		
		if (theItems == null)
			return returnItems;
		
		for (int i = 0; i < theItems.size(); i++)
		{
			int[] item = theItems.get(i);
			
			if (item[0] == exclude[0] && item[1] == exclude[1] &&
				item[2] == exclude[2] && item[3] == exclude[3])
			{
				;
			}
			else
				returnItems.add(item);
		}
		
		return returnItems;
	}
	
	public boolean DoesFrequencyExist(int frequency)
	{
		return LinkData.containsKey(frequency);
	}
	
	public boolean IsInFrequency(int frequency, int[] data)
	{
		if (!DoesFrequencyExist(frequency))
			return false;
		
		List<int[]> list = GetFrequency(frequency);
		
		for (int i = 0; i < list.size(); i++)
		{
			if (list.get(i)[0] == data[0] && list.get(i)[1] == data[1] && list.get(i)[2] == data[2] && list.get(i)[3] == data[3])
				return true;
		}
		
		return false;		
	}
	
	public List<int[]> GetFrequency(int frequency)
	{
		if (!DoesFrequencyExist(frequency))
			return null;
		
		return LinkData.get(frequency);
	}
	
	public void AddFrequency(int frequency)
	{
		if (LinkData.containsKey(frequency))
			return;
		
		LinkData.put(frequency, new ArrayList<int[]>());
	}
	
	public void AddToFrequency(int frequency, int x, int y, int z, int dimension)
	{
		AddToFrequency(frequency, new int[] { x, y, z, dimension });
	}
	
	public void AddToFrequency(int frequency, int[] data)
	{
		if (!DoesFrequencyExist(frequency))
			AddFrequency(frequency);
		
		if (data.length != 4)
			return;
		
		GetFrequency(frequency).add(data);
	}
		
	public void RemoveFromFrequency(int frequency, int x, int y, int z, int dimension)
	{
		RemoveFromFrequency(frequency, new int[] { x, y, z, dimension });
	}
	
	public void RemoveFromFrequency(int frequency, int[] data)
	{
		if (!DoesFrequencyExist(frequency))
			AddFrequency(frequency);
		
		if (data.length != 4)
			return;
		
		if (IsInFrequency(frequency, data))
		{
			List<int[]> list = GetFrequency(frequency);
			
			for (int i = 0; i < list.size(); i++)
			{
				if (list.get(i)[0] == data[0] && list.get(i)[1] == data[1] && list.get(i)[2] == data[2] && list.get(i)[3] == data[3])
					list.remove(i);
			}
		}
	}
	
	public void RemoveFrequency(int frequency)
	{
		if (!DoesFrequencyExist(frequency))
			return;
		
		LinkData.remove(frequency);
	}
	
	public long IsInAFrequency(int[] data)
	{
		for (int key : LinkData.keySet())
		{
			if (IsInFrequency(key, data))
				return key;
		}
		
		return Integer.MAX_VALUE + 1;
	}
	
	public void HandlePacket(Packet250CustomPayload packet)
	{
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
	    
        byte type;
        int frequency, colour, x, y, z, dim;
       
        try
        {
        	type = inputStream.readByte();
        	frequency = inputStream.readInt();
        	colour = inputStream.readInt();
        	x = inputStream.readInt();
        	y = inputStream.readInt();
        	z = inputStream.readInt();
        	dim = inputStream.readInt();
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        	return;
        }
        
        if (type != 1)
        	return;
        
        World world = Reference.LinkData.serverInstance.worldServerForDimension(dim);
        
        if (world.getBlockId(x, y, z) != Reference.IDPortalModifier)
        	return;
        	
        TileEntityPortalModifier modifier = (TileEntityPortalModifier)world.getBlockTileEntity(x, y, z);
        
        if (colour != -1)
        {
        	modifier.Colour = colour;
        	
        	sendPacketToNearbyClients(-1, colour, new int[] { x, y, z, dim });
        	
        	if (world.getBlockId(x, y + 1, z) == EnhancedPortals.instance.blockNetherPortal.blockID)
        		WorldHelper.floodUpdateMetadata(world, x, y + 1, z, EnhancedPortals.instance.blockNetherPortal.blockID, colour);
        }
                
        if (frequency != -1)
        {
        	modifier.Frequency = frequency;
        	
	        long oldFreq = Reference.LinkData.IsInAFrequency(new int[] { x, y, z, dim });
	        	
	        if (oldFreq != Integer.MAX_VALUE + 1)
	        	Reference.LinkData.RemoveFromFrequency((int)oldFreq, new int[] { x, y, z, dim });
	        	
	        Reference.LinkData.AddToFrequency(frequency, x, y, z, dim);
        }
	}
	
	public void sendPacketToClient(Player player, int freq, int colour, int x, int y, int z, int dim)
	{
		sendPacketToClient(player, freq, colour, new int[] { x, y, z, dim });
	}
	
	public void sendPacketToClient(Player player, int freq, int colour, int[] data)
	{
		if (data.length != 4 || colour < 0 || colour > 15)
			return;
		
		// construct & send packet
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream outputStream = new DataOutputStream(bos);
		
		try
		{
			outputStream.writeByte((byte)1);  // Identifier for packet
			outputStream.writeInt(freq);      // Frequency
			outputStream.writeInt(colour);    // Colour
			outputStream.writeInt(data[0]);   // X
			outputStream.writeInt(data[1]);   // Y
			outputStream.writeInt(data[2]);   // Z
			outputStream.writeInt(data[3]);   // D
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = Reference.modID;
		packet.data = bos.toByteArray();
		packet.length = bos.size();
		
		PacketDispatcher.sendPacketToPlayer(packet, player);
	}
	
	public void sendPacketToNearbyClients(int freq, int colour, int[] data)
	{
		if (data.length != 4 || colour < 0 || colour > 15)
			return;
		
		// construct & send packet
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream outputStream = new DataOutputStream(bos);
		
		try
		{
			outputStream.writeByte((byte)1);  // Identifier for packet
			outputStream.writeInt(freq);      // Frequency
			outputStream.writeInt(colour);    // Colour
			outputStream.writeInt(data[0]);   // X
			outputStream.writeInt(data[1]);   // Y
			outputStream.writeInt(data[2]);   // Z
			outputStream.writeInt(data[3]);   // D
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = Reference.modID;
		packet.data = bos.toByteArray();
		packet.length = bos.size();
		
		PacketDispatcher.sendPacketToAllAround(data[0], data[1], data[2], 32, data[3], packet);
	}
}
