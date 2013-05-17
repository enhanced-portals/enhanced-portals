package enhancedportals.network.packet;

import java.io.DataInputStream;

import enhancedportals.lib.PacketIds;

public class PacketDialRequest extends PacketUpdate
{
    public PacketDialRequest()
    {
        super();
    }

    public PacketDialRequest(DataInputStream stream)
    {
        super(stream);
    }

    public PacketDialRequest(int x, int y, int z, int d, String network)
    {
        super();

        xCoord = x;
        yCoord = y;
        zCoord = z;
        dimension = d;
        packetData = new PacketData(new int[0], new byte[0], new String[] { network });
    }

    @Override
    public int getPacketID()
    {
        return PacketIds.DialDeviceRequest;
    }
}
