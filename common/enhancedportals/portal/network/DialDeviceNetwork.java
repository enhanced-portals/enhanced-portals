package enhancedportals.portal.network;

import enhancedcore.world.WorldLocation;
import enhancedportals.lib.Reference;
import net.minecraft.server.MinecraftServer;

public class DialDeviceNetwork extends NetworkManager
{
    public DialDeviceNetwork(MinecraftServer server)
    {
        super(server);
    }

    @Override
    public void addToNetwork(String key, WorldLocation data)
    {
        if (!hasNetwork(key) || getNetwork(key).isEmpty())
        {
            super.addToNetwork(key, data);
        }
    }
    
    @Override
    public String getSaveFileName()
    {
        return Reference.MOD_ID + "_DialDevices.dat";
    }
}
