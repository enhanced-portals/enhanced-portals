package alz.mods.enhancedportals.networking;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.packet.Packet250CustomPayload;
import alz.mods.enhancedportals.reference.Reference;

public class PacketTileUpdate extends PacketUpdate
{
	public int xCoord, yCoord, zCoord, dimension;
	public int[] data;
	
	public PacketTileUpdate() { }
	
	public PacketTileUpdate(int x, int y, int z, int dim, int[] theData)
	{
		xCoord = x;
		yCoord = y;
		zCoord = z;
		dimension = dim;
		data = theData;
	}
	
	@Override
	public int getPacketID()
	{
		return Reference.Networking.TileEntityUpdate;
	}	
	
	@Override
	public Packet250CustomPayload getClientPacket()
	{
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		DataOutputStream dataStream = new DataOutputStream(byteStream);
		Packet250CustomPayload packet = new Packet250CustomPayload();
		
		try
		{
			dataStream.writeByte(getPacketID());
			dataStream.writeByte(0);
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
	
	@Override
	public Packet250CustomPayload getServerPacket()
	{
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		DataOutputStream dataStream = new DataOutputStream(byteStream);
		Packet250CustomPayload packet = new Packet250CustomPayload();
		
		try
		{
			dataStream.writeByte(getPacketID());
			dataStream.writeByte(1);
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

	@Override
	public void getPacketData(DataInputStream stream) throws IOException
	{
		xCoord = stream.readInt();
		yCoord = stream.readInt();
		zCoord = stream.readInt();
		dimension = stream.readInt();
		
		int size = stream.readInt();
				
		if (size > 0)
		{
			data = new int[size];
			
			for (int i = 0; i < size; i++)
			{
				data[i] = stream.readInt();
			}
		}
	}

	@Override
	public void addPacketData(DataOutputStream stream) throws IOException
	{
		stream.writeInt(xCoord);
		stream.writeInt(yCoord);
		stream.writeInt(zCoord);
		stream.writeInt(dimension);
		
		stream.writeInt(data.length);
		
		for (int i = 0; i < data.length; i++)
		{
			stream.writeInt(data[i]);
		}
	}
}
