package enhancedportals.network.packet;

import java.io.DataInputStream;

import enhancedportals.lib.PacketIds;
import enhancedportals.portal.PortalTexture;

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
        packetData = new PacketData(new int[] { 0 }, new byte[0], new String[] { network });
    }
    
    public PacketDialRequest(int x, int y, int z, int d, String network, PortalTexture text, byte thick, boolean sounds, boolean particles)
    {
        super();

        xCoord = x;
        yCoord = y;
        zCoord = z;
        dimension = d;
        packetData = new PacketData(new int[] { 1, text.colour, text.blockID, text.metaData }, new byte[] { thick, (byte) (sounds ? 1 : 0), (byte) (particles ? 1 : 0) }, new String[] { network, text.liquidID });
    }

    @Override
    public int getPacketID()
    {
        return PacketIds.DialDeviceRequest;
    }
}
