package uk.co.shadeddimensions.enhancedportals.lib;

import net.minecraft.creativetab.CreativeTabs;
import uk.co.shadeddimensions.enhancedportals.creativetab.CreativeTabEP3;

public class Reference
{
    public static final String ID = "EnhancedPortals3";
    public static final String SHORT_ID = "ep3";
    public static final String NAME = "EnhancedPortals 3";
    public static final String VERSION = "0.5.5 (Alpha)";
    public static final String DEPENDENCIES = "";
    public static final String MC_VERSION = "[1.6.4,)";

    public static final String CLIENT_PROXY = "uk.co.shadeddimensions.enhancedportals.network.ClientProxy";
    public static final String COMMON_PROXY = "uk.co.shadeddimensions.enhancedportals.network.CommonProxy";

    public static CreativeTabs creativeTab = new CreativeTabEP3();
}
