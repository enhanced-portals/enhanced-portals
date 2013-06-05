package enhancedportals.portal.network;

import enhancedportals.lib.Reference;
import net.minecraft.server.MinecraftServer;

public class ModifierNetwork extends NetworkManager
{
    public ModifierNetwork(MinecraftServer server)
    {
        super(server);
    }
    
    @Override
    public String getSaveFileName()
    {
        return Reference.MOD_ID + "_Modifiers.dat";
    }
}
