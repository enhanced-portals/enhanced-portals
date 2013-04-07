package alz.mods.enhancedportals.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.packet.Packet250CustomPayload;
import alz.mods.enhancedportals.reference.Reference;

public class PacketDialDevice extends PacketUpdate
{
	public int SubID;

	@Override
	public int getPacketID()
	{
		return Reference.Networking.DialDevice_GenericData;
	}

	public int getSubPacketID()
	{
		return SubID;
	}

	@Override
	public Packet250CustomPayload getClientPacket()
	{
		return null;
	}

	@Override
	public Packet250CustomPayload getServerPacket()
	{
		return null;
	}

	@Override
	public void getPacketData(DataInputStream stream) throws IOException
	{
		super.getPacketData(stream);
	}

	@Override
	public void addPacketData(DataOutputStream stream) throws IOException
	{
		super.addPacketData(stream);

		stream.writeInt(SubID);
	}
}
