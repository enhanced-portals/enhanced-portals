package alz.mods.enhancedportals.networking;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import alz.mods.enhancedportals.reference.Reference;

public class PacketDataRequest extends PacketUpdate
{
    public PacketDataRequest()
    {
    }

    public PacketDataRequest(int x, int y, int z, int dim)
    {
        xCoord = x;
        yCoord = y;
        zCoord = z;
        Dimension = dim;
    }

    public PacketDataRequest(TileEntity tileEntity)
    {
        xCoord = tileEntity.xCoord;
        yCoord = tileEntity.yCoord;
        zCoord = tileEntity.zCoord;
        Dimension = tileEntity.worldObj.provider.dimensionId;
    }

    @Override
    public void addPacketData(DataOutputStream stream) throws IOException
    {
        super.addPacketData(stream);
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
    public void getPacketData(DataInputStream stream) throws IOException
    {
        super.getPacketData(stream);
    }

    @Override
    public int getPacketID()
    {
        return Reference.Networking.DataRequest;
    }

    @Override
    public Packet250CustomPayload getServerPacket()
    {
        return null;
    }
}
