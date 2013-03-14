package alz.mods.enhancedportals.reference;

import cpw.mods.fml.common.registry.LanguageRegistry;

public class Localizations
{
	private static String[] localeFiles = new String[] { "en_GB" };
	
	public static void loadLocales()
	{
		for (String str : localeFiles)
		{
			LanguageRegistry.instance().loadLocalization(Reference.File.LanguageDirectory + str + ".xml", str, true);
		}
	}
	
	public static String getLocalizedString(String key)
	{
		String local = LanguageRegistry.instance().getStringLocalization(key);
		
		if (local.length() == 0)
		{
			local = LanguageRegistry.instance().getStringLocalization(key, "en_GB");
		}
				
		return local;
	}
}
