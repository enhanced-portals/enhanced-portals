package alz.mods.enhancedportals.networking;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.packet.Packet250CustomPayload;
import alz.mods.enhancedportals.reference.Reference;
import alz.mods.enhancedportals.tileentity.TileEntityPortalModifier;

public class PacketDataRequest extends PacketUpdate
{
	public PacketDataRequest()
	{
	}

	public PacketDataRequest(TileEntityPortalModifier modifier)
	{
		xCoord = modifier.xCoord;
		yCoord = modifier.yCoord;
		zCoord = modifier.zCoord;
		Dimension = modifier.worldObj.provider.dimensionId;
	}

	public PacketDataRequest(int x, int y, int z, int dim)
	{
		xCoord = x;
		yCoord = y;
		zCoord = z;
		Dimension = dim;
	}

	public int getPacketID()
	{
		return Reference.Networking.DataRequest;
	}

	public void getPacketData(DataInputStream stream) throws IOException
	{
		xCoord = stream.readInt();
		yCoord = stream.readInt();
		zCoord = stream.readInt();
		Dimension = stream.readInt();
	}

	public void addPacketData(DataOutputStream stream) throws IOException
	{
		stream.writeInt(xCoord);
		stream.writeInt(yCoord);
		stream.writeInt(zCoord);
		stream.writeInt(Dimension);
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
		return null;
	}
}
