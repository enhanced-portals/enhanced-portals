package alz.mods.enhancedportals.common;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import alz.mods.enhancedportals.client.ClientProxy;
import alz.mods.enhancedportals.reference.IO;
import alz.mods.enhancedportals.reference.ModData;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler
{
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
	{
		if (!packet.channel.equals(ModData.ID))
			return;
		
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
		
		if (type == 1)
		{
			ClientProxy.RecieveBlockUpdate(frequency, colour, x, y, z, dim);
		}
		else if (type == 2 && IO.LinkData != null)
		{
			IO.LinkData.HandlePacket(packet);
		}
	}
}
