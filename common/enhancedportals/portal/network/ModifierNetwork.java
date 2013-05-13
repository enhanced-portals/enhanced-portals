package enhancedportals.portal.network;

import net.minecraft.server.MinecraftServer;
import enhancedportals.lib.Reference;

public class ModifierNetwork extends NetworkManager
{
    public ModifierNetwork(MinecraftServer server)
    {
        super(server);
    }

    @Override
    public String getSaveFileName()
    {
        return Reference.MOD_ID + "_PortalModifiers.dat";
    }
}
