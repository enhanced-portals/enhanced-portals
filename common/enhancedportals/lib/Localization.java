package enhancedportals.lib;

import net.minecraft.util.StatCollector;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class Localization
{
    public static final String NetherPortal_Name = "portal";
    public static final String Obsidian_Name = "obsidian";
    public static final String PortalModifier_Name = "portalModifier";
    public static final String ObsidianStairs_Name = "stairsObsidian";
    public static final String PortalModifierUpgrade_Name = PortalModifier_Name + "Upgrade";
    public static final String NetworkCard_Name = "networkCard";
    public static final String DialDevice_Name = "dialDevice";
    public static final String DialDeviceBasic_Name = "dialDeviceBasic";
    public static final String AutomaticDialler_Name = "automaticDialler";
    public static final String ObsidianSlab_Name = "slabObsidian";
    public static final String EnhancedFlintSteel_Name = "flintAndSteel";
    public static final String MiscellaneousItems_Name = "miscItems";

    public static String[] Locales = new String[] { "en_US" };

    public static void loadLocales()
    {
        for (String str : Locales)
        {
            LanguageRegistry.instance().loadLocalization(Reference.LOCALE_LOCATION + str + ".xml", str, true);
        }
    }

    public static String localizeString(String str)
    {
        String str2 = LanguageRegistry.instance().getStringLocalization(str);

        if (str2.length() == 0)
        {
            str2 = StatCollector.translateToLocal(str);
        }

        return str2;
    }
}
