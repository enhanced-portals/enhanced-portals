package uk.co.shadeddimensions.ep3.lib;

import java.io.InputStream;
import java.util.Properties;

import net.minecraft.creativetab.CreativeTabs;
import uk.co.shadeddimensions.ep3.creativetab.CreativeTabEP3;

import com.google.common.base.Throwables;

public class Reference
{
    public static final String ID = "EnhancedPortals3";
    public static final String SHORT_ID = "ep3";
    public static final String NAME = "EnhancedPortals 3";
    public static final String VERSION = "1.6.4-3.0.0b5e";
    public static final String DEPENDENCIES = "after:ThermalExpansion";
    public static final String MC_VERSION = "[1.6.4,)";

    public static final String CLIENT_PROXY = "uk.co.shadeddimensions.ep3.network.ClientProxy";
    public static final String COMMON_PROXY = "uk.co.shadeddimensions.ep3.network.CommonProxy";

    public static CreativeTabs creativeTab = new CreativeTabEP3();
}
