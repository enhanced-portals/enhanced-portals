package alz.mods.enhancedportals.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.packet.Packet250CustomPayload;

public abstract class PacketUpdate
{
	public abstract int getPacketID();
	
	public abstract Packet250CustomPayload getClientPacket();
	public abstract Packet250CustomPayload getServerPacket();
	
	public abstract void getPacketData(DataInputStream stream) throws IOException;
	public abstract void addPacketData(DataOutputStream stream) throws IOException;
}
