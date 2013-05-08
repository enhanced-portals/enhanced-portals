package enhancedportals.lib;

import cpw.mods.fml.common.registry.LanguageRegistry;

public class Localization
{
    public static final String NetherPortal_Name = "portal";
    public static final String Obsidian_Name = "obsidian";
    public static final String PortalModifier_Name = "portalModifier";
    public static final String ObsidianStairs_Name = "stairsObsidian";

    public static String[] Locales = new String[] { "en_GB", "en_US", "it_IT" };

    public static String getLocalizedString(String string)
    {
        return LanguageRegistry.instance().getStringLocalization(string);
    }

    public static void loadLocales()
    {
        for (String str : Locales)
        {
            LanguageRegistry.instance().loadLocalization(Reference.LOCALE_LOCATION + str + ".xml", str, true);
        }
    }
}
