package alz.mods.enhancedportals.common;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import alz.mods.enhancedportals.client.ClientNetworking;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler
{
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
	{
		if (!packet.channel.equals(Reference.modID))
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
			ClientNetworking.RecieveBlockUpdate(frequency, colour, x, y, z, dim);
		}
		else if (type == 2 && Reference.LinkData != null)
		{
			Reference.LinkData.HandlePacket(packet);
		}
	}
}
