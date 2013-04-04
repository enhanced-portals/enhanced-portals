package alz.mods.enhancedportals.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.packet.Packet250CustomPayload;

public class PacketDialDevice extends PacketUpdate
{

	@Override
	public int getPacketID()
	{
		return 0;
	}

	@Override
	public Packet250CustomPayload getClientPacket()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Packet250CustomPayload getServerPacket()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void getPacketData(DataInputStream stream) throws IOException
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addPacketData(DataOutputStream stream) throws IOException
	{
		// TODO Auto-generated method stub
		
	}

}
