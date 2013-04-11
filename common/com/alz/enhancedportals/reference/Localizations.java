package com.alz.enhancedportals.reference;

import cpw.mods.fml.common.registry.LanguageRegistry;

public class Localizations
{
    private static String[] localeFiles = new String[] { "en_GB", "en_US", "it_IT" };

    public static String getLocalizedString(String key)
    {
        String local = LanguageRegistry.instance().getStringLocalization(key);

        if (local.length() == 0)
        {
            local = LanguageRegistry.instance().getStringLocalization(key, "en_GB");
        }

        return local;
    }

    public static void loadLocales()
    {
        for (String str : localeFiles)
        {
            LanguageRegistry.instance().loadLocalization(Strings.LANGUAGE_DIRECTORY + str + ".xml", str, true);
        }
    }
}
