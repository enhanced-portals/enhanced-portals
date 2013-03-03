package alz.mods.enhancedportals.client;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import alz.mods.enhancedportals.common.EnhancedPortals;
import alz.mods.enhancedportals.common.Reference;
import alz.mods.enhancedportals.common.TileEntityPortalModifier;
import alz.mods.enhancedportals.helpers.WorldHelper;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;

public class ClientNetworking
{	
	public static void SendBlockUpdate(int frequency, int colour, int x, int y, int z, int dim)
	{
		SendBlockUpdate(frequency, colour, new int[] { x, y, z, dim });
	}
	
	public static void SendBlockUpdate(int frequency, int colour, int[] data)
	{
		if (data.length != 4  || colour < -1 || colour > 15)
			return;
		
		// construct & send packet
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream outputStream = new DataOutputStream(bos);
		
		try
		{
			outputStream.writeByte((byte)2);  // Identifier for packet
			outputStream.writeInt(frequency); // Frequency
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
		
		PacketDispatcher.sendPacketToServer(packet);
	}
	
	public static void RecieveBlockUpdate(int frequency, int colour, int x, int y, int z, int dim)
	{		
		World world = FMLClientHandler.instance().getClient().theWorld;
		
		if (world.provider.dimensionId != dim || world.getBlockId(x, y, z) != Reference.IDPortalModifier)
		{
			System.out.println("Invalid dimension or not a valid block");
			return;
		}
		
		TileEntityPortalModifier modifier = (TileEntityPortalModifier)world.getBlockTileEntity(x, y, z);
		
		if (colour != -1)
		{
			int oldColour = modifier.Colour;
			modifier.Colour = colour;
			world.markBlockForRenderUpdate(x, y, z);
			
			if (world.getBlockId(x, y + 1, z) == EnhancedPortals.instance.blockNetherPortal.blockID && world.getBlockMetadata(x, y + 1, z) == oldColour)
				WorldHelper.floodUpdateMetadata(world, x, y + 1, z, EnhancedPortals.instance.blockNetherPortal.blockID, colour);
		}
		
		if (frequency != -1)
			modifier.Frequency = frequency;
	}
}
