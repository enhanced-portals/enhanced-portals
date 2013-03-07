package alz.mods.enhancedportals.reference;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.Configuration;

public class Settings
{
	public static boolean AllowTeleporting;
	public static boolean CanDyePortals;
	public static boolean DoesDyingCost;
	public static boolean AllowModifiers;
	public static boolean AllowObsidianStairs;
	public static boolean PrintPortalMessages;
	
	public static int PigmenSpawnChance;
	public static int SoundLevel;
	public static int ParticleLevel;
	
	public static List<Integer> RemovableBlocks = new ArrayList<Integer>();
	public static List<Integer> BorderBlocks = new ArrayList<Integer>();
	
	public static Configuration ConfigFile;
	
	public static void AddToBorderBlocks(String str)
	{
		if (str == null || str == " " || str.length() == 0)
			return;
		
		try
		{
			if (str.contains(","))
			{
				String[] strs = str.split(",");
				
				for (String str2 : strs)
					BorderBlocks.add(Integer.parseInt(str2));
			}
			else
			{
				BorderBlocks.add(Integer.parseInt(str));
			}
		}
		catch (Exception e)
		{
			Logger.LogData("Custom Border Blocks string is not correctly formatted.");
		}
	}
	
	public static void AddToDestroyBlocks(String str)
	{
		if (str == null || str == " " || str.length() == 0)
			return;
		
		try
		{
			if (str.contains(","))
			{
				String[] strs = str.split(",");
				
				for (String str2 : strs)
					RemovableBlocks.add(Integer.parseInt(str2));
			}
			else
			{
				RemovableBlocks.add(Integer.parseInt(str));
			}
		}
		catch (Exception e)
		{
			Logger.LogData("Custom Destroy Blocks string is not correctly formatted.");
		}
	}
	
	public static boolean GetFromConfig(String key, boolean defaultValue)
	{
		return ConfigFile.get(Configuration.CATEGORY_GENERAL, key, defaultValue).getBoolean(defaultValue);
	}
	
	public static int GetFromConfig(String key, int defaultValue, int minValue, int maxValue)
	{
		int val = ConfigFile.get(Configuration.CATEGORY_GENERAL, key, defaultValue).getInt(defaultValue);
		
		if (val < minValue)
			return minValue;
		
		if (val > maxValue)
			return maxValue;
		
		return val;
	}
	
	public static int GetFromConfig(String key, int defaultValue, boolean block)
	{
		if (block)
			return ConfigFile.get(Configuration.CATEGORY_BLOCK, key, defaultValue).getInt(defaultValue);
		else
			return ConfigFile.get(Configuration.CATEGORY_ITEM, key, defaultValue).getInt(defaultValue) - 256;
	}
	
	public static String GetFromConfig(String key, String defaultValue)
	{
		return ConfigFile.get(Configuration.CATEGORY_GENERAL, key, defaultValue).value;
	}
}
