package enhancedportals.network.packet;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import enhancedportals.lib.Reference;

public abstract class PacketEP
{
    protected boolean isChunkPacket = false;

    public Packet getPacket()
    {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(byteStream);

        try
        {
            dataStream.writeByte(getPacketID());
            writeData(dataStream);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = Reference.MOD_ID;
        packet.data = byteStream.toByteArray();
        packet.length = packet.data.length;
        packet.isChunkDataPacket = isChunkPacket;

        return packet;
    }

    public abstract int getPacketID();

    public abstract void readData(DataInputStream stream) throws IOException;

    public abstract void writeData(DataOutputStream stream) throws IOException;
}
