package enhancedportals.portal.network;

public class DialDeviceNetworkObject
{
    public String network, displayName;
    public String texture;
    public byte   thickness;

    public DialDeviceNetworkObject(String displayname, String net, String tex, byte thick)
    {
        displayName = displayname;
        network = net;
        texture = tex;
        thickness = thick;

        if (texture == null || texture.equals(""))
        {
            texture = "C:5";
        }
    }
}
