package enhancedportals.portal.network;

public class DialDeviceNetworkObject
{
    public String network, displayName;
    public String texture;
    public byte   thickness;
    public boolean sounds, particles;

    public DialDeviceNetworkObject(String displayname, String net, String tex, byte thick, boolean sound, boolean particle)
    {
        displayName = displayname;
        network = net;
        texture = tex;
        thickness = thick;
        sounds = sound;
        particles = particle;

        if (texture == null || texture.equals(""))
        {
            texture = "C:5";
        }
    }
}
