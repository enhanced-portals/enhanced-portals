package enhancedportals.portal.network;

import enhancedportals.lib.Reference;
import net.minecraft.server.MinecraftServer;

public class DialDeviceNetwork extends NetworkManager
{
    public DialDeviceNetwork(MinecraftServer server)
    {
        super(server);
    }

    public String getSaveFileName()
    {
        return Reference.MOD_ID + "_DialDevices.dat";
    }
}
