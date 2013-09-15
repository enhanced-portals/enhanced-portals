package uk.co.shadeddimensions.enhancedportals.lib;

import net.minecraft.creativetab.CreativeTabs;
import uk.co.shadeddimensions.enhancedportals.creativetab.CreativeTabEP2;

public class Reference
{
    public static final String ID = "EnhancedPortals2";
    public static final String NAME = "EnhancedPortals 2";
    public static final String VERSION = "1.1.0";
    public static final String DEPENDENCIES = "";
    public static final String MC_VERSION = "[1.6.2,)";
    
    public static final String CLIENT_PROXY = "uk.co.shadeddimensions.enhancedportals.network.ClientProxy";
    public static final String COMMON_PROXY = "uk.co.shadeddimensions.enhancedportals.network.CommonProxy";
    
    public static CreativeTabs creativeTab = new CreativeTabEP2();
    
    public static final boolean DEBUG = true;
}
