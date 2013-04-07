package alz.mods.enhancedportals.networking;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.packet.Packet250CustomPayload;
import alz.mods.enhancedportals.reference.Reference;

public class PacketGuiRequest extends PacketUpdate
{
	public int guiID;

	public PacketGuiRequest()
	{
	}

	public PacketGuiRequest(int GUIID, int x, int y, int z)
	{
		guiID = GUIID;
		xCoord = x;
		yCoord = y;
		zCoord = z;
	}

	@Override
	public int getPacketID()
	{
		return Reference.Networking.GuiRequest;
	}

	@Override
	public void getPacketData(DataInputStream stream) throws IOException
	{
		super.getPacketData(stream);

		guiID = stream.readInt();
	}

	@Override
	public void addPacketData(DataOutputStream stream) throws IOException
	{
		super.addPacketData(stream);

		stream.writeInt(guiID);
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
