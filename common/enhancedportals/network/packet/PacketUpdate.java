package enhancedportals.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.tileentity.TileEntity;
import enhancedportals.lib.PacketIds;

public class PacketUpdate extends PacketEP
{
    public PacketData packetData;
    public int xCoord, yCoord, zCoord, dimension;

    public PacketUpdate()
    {
        isChunkPacket = true;
    }

    public PacketUpdate(DataInputStream stream)
    {
        try
        {
            xCoord = stream.readInt();
            yCoord = stream.readInt();
            zCoord = stream.readInt();
            dimension = stream.readInt();

            packetData = new PacketData(stream.readInt(), stream.readInt());

            for (int i = 0; i < packetData.integerData.length; i++)
            {
                packetData.integerData[i] = stream.readInt();
            }

            for (int i = 0; i < packetData.stringData.length; i++)
            {
                packetData.stringData[i] = stream.readUTF();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public PacketUpdate(int x, int y, int z, int dim, PacketData data)
    {
        this();

        xCoord = x;
        yCoord = y;
        zCoord = z;
        dimension = dim;
        packetData = data;
    }

    public PacketUpdate(TileEntity tileEntity, PacketData data)
    {
        this(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, tileEntity.worldObj.provider.dimensionId, data);
    }

    @Override
    public int getPacketID()
    {
        return PacketIds.TileEntityUpdate;
    }

    @Override
    public void readData(DataInputStream stream) throws IOException
    {
        xCoord = stream.readInt();
        yCoord = stream.readInt();
        zCoord = stream.readInt();
        dimension = stream.readInt();

        packetData = new PacketData(stream.readInt(), stream.readInt());

        for (int i = 0; i < packetData.integerData.length; i++)
        {
            packetData.integerData[i] = stream.readInt();
        }

        for (int i = 0; i < packetData.stringData.length; i++)
        {
            packetData.stringData[i] = stream.readUTF();
        }
    }

    @Override
    public void writeData(DataOutputStream stream) throws IOException
    {
        stream.writeInt(xCoord);
        stream.writeInt(yCoord);
        stream.writeInt(zCoord);
        stream.writeInt(dimension);

        if (packetData == null)
        {
            stream.writeInt(0);
            stream.writeInt(0);
        }
        else
        {
            stream.writeInt(packetData.integerData.length);
            stream.writeInt(packetData.stringData.length);

            for (int i : packetData.integerData)
            {
                stream.writeInt(i);
            }

            for (String s : packetData.stringData)
            {
                stream.writeUTF(s);
            }
        }
    }
}
