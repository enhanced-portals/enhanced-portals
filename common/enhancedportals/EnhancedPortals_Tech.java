package enhancedportals;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import enhancedportals.lib.Reference;

@Mod(modid = Reference.MOD_ID + "_Tech", name = "EP2 Tech", version = "@TECH_VERSION@", dependencies = "required-after:" + Reference.MOD_ID)
public class EnhancedPortals_Tech
{
    @Instance(Reference.MOD_ID + "_Tech")
    public static EnhancedPortals_Tech instance;
}
