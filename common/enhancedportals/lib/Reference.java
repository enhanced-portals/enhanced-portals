package enhancedportals.lib;

import net.minecraft.creativetab.CreativeTabs;
import enhancedportals.creativetab.CreativeTabEP;

public class Reference
{
    public static final String MOD_ID = "enhancedportals";
    public static final String MOD_NAME = "EnhancedPortals";
    public static final String MOD_VERSION = "@VERSION@";

    public static final String MOD_ID_CC = "enhancedportals_cc";
    public static final String MOD_NAME_CC = "EP ComputerCraft";

    public static final String MOD_ID_WORLD = "enhancedportals_world";
    public static final String MOD_NAME_WORLD = "EP World";

    public static final String PROXY_CLIENT = "enhancedportals.network.ClientProxy";
    public static final String PROXY_COMMON = "enhancedportals.network.CommonProxy";

    public static CreativeTabs CREATIVE_TAB = new CreativeTabEP();
}
