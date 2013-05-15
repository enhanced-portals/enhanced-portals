package enhancedportals.network.packet;

public class PacketData
{
    public int[] integerData = new int[0];
    public byte[] byteData = new byte[0];
    public String[] stringData = new String[0];

    public PacketData()
    {

    }

    public PacketData(int integerSize, int byteSize, int stringSize)
    {
        integerData = new int[integerSize];
        byteData = new byte[byteSize];
        stringData = new String[stringSize];
    }

    public PacketData(int[] integerdata, byte[] bytedata, String[] stringdata)
    {
        integerData = integerdata;
        byteData = bytedata;
        stringData = stringdata;
    }
}
