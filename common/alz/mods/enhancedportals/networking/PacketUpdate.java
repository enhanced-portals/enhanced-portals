package alz.mods.enhancedportals.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.packet.Packet250CustomPayload;

public abstract class PacketUpdate
{
    public int xCoord, yCoord, zCoord, Dimension;

    public void addPacketData(DataOutputStream stream) throws IOException
    {
        stream.writeInt(xCoord);
        stream.writeInt(yCoord);
        stream.writeInt(zCoord);
        stream.writeInt(Dimension);
    }

    public abstract Packet250CustomPayload getClientPacket();

    public void getPacketData(DataInputStream stream) throws IOException
    {
        xCoord = stream.readInt();
        yCoord = stream.readInt();
        zCoord = stream.readInt();
        Dimension = stream.readInt();
    }

    public abstract int getPacketID();

    public abstract Packet250CustomPayload getServerPacket();
}
