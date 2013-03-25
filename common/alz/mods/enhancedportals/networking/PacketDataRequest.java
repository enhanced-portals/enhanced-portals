package alz.mods.enhancedportals.networking;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.packet.Packet250CustomPayload;
import alz.mods.enhancedportals.reference.Reference;
import alz.mods.enhancedportals.tileentity.TileEntityPortalModifier;

public class PacketDataRequest
{
	public int xCoord, yCoord, zCoord, dimension;
	
	public PacketDataRequest() { }
	
	public PacketDataRequest(TileEntityPortalModifier modifier)
	{
		xCoord = modifier.xCoord;
		yCoord = modifier.yCoord;
		zCoord = modifier.zCoord;
		dimension = modifier.worldObj.provider.dimensionId;
	}
	
	public PacketDataRequest(int x, int y, int z, int dim)
	{
		xCoord = x;
		yCoord = y;
		zCoord = z;
		dimension = dim;
	}
	
	public int getPacketID()
	{
		return Reference.Networking.DataRequest;
	}
	
	public Packet250CustomPayload getPacket()
	{
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		DataOutputStream dataStream = new DataOutputStream(byteStream);
		Packet250CustomPayload packet = new Packet250CustomPayload();
		
		try
		{
			dataStream.writeByte(getPacketID());
			addPacketData(dataStream);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		packet.channel = Reference.MOD_ID;
		packet.data = byteStream.toByteArray();
		packet.length = packet.data.length;
		packet.isChunkDataPacket = true;
		
		return packet;
	}
	
	public void getPacketData(DataInputStream stream) throws IOException
	{
		xCoord = stream.readInt();
		yCoord = stream.readInt();
		zCoord = stream.readInt();
		dimension = stream.readInt();
	}

	public void addPacketData(DataOutputStream stream) throws IOException
	{
		stream.writeInt(xCoord);
		stream.writeInt(yCoord);
		stream.writeInt(zCoord);
		stream.writeInt(dimension);
	}
}
