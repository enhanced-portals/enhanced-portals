package enhancedportals.network.packet;

public class PacketData
{
    public int[] integerData = new int[0];
    public String[] stringData = new String[0];

    public PacketData()
    {

    }

    public PacketData(int integerSize, int stringSize)
    {
        integerData = new int[integerSize];
        stringData = new String[stringSize];
    }

    public PacketData(int[] integerdata, String[] stringdata)
    {
        integerData = integerdata;
        stringData = stringdata;
    }
}
