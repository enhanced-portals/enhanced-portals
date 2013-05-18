package enhancedportals.portal.network;

import enhancedportals.portal.PortalTexture;

public class DialDeviceNetworkObject
{
    public String network, displayName;
    public PortalTexture texture;
    public byte thickness;
    public boolean sounds, particles;

    public DialDeviceNetworkObject(String displayname, String net, PortalTexture tex, byte thick, boolean sound, boolean particle)
    {
        displayName = displayname;
        network = net;
        texture = tex;
        thickness = thick;
        sounds = sound;
        particles = particle;
    }
}
