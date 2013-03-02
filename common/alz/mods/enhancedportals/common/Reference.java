package alz.mods.enhancedportals.common;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;

public class Reference
{
	public static final String modName = "Enhanced Portals";
	public static final String modID = "enhancedportals";
	public static final String modVersion = "2.1";
	
	public static final String textureLocation = "/alz/mods/enhancedportals/client/textures/terrain.png";
	public static final String textureItemLocation = "/alz/mods/enhancedportals/client/textures/items.png";
	public static final String textureDirectory = "/alz/mods/enhancedportals/client/textures/";
	public static final String textureGuiDirectory = "/alz/mods/enhancedportals/client/textures/";
	
	public static String DataFile = "EnhancedPortals.dat";
	public static LinkData LinkData;
	
	public static boolean allowTeleporting = true;
	public static boolean canDyePortals = true;
	public static boolean doesDyingCost = true;
	public static boolean allowModifiers = true;
	public static boolean allowObsidianStairs = true;
	public static boolean printPortalMessages = true;
	
	public static int pigmenSpawnChance = 100;
	public static int soundLevel = 100;
	public static int particleLevel = 100;
	
	public static int IDObsidianStairs = 512;
	public static int IDPortalModifier = 513;
	public static int IDPortalModifierUpgrade = 6000;
	
	public static final int IDPortalModifierGui = 0;
	
	// TODO Language files
	public static String STR_NoUpgrade = "You do not have the required upgrade.";
	public static String STR_ExitBlocked = "The exit portal is blocked or not valid.";
	public static String STR_NoExit = "Couldn't find a valid exit.";	
	public static String STR_Frequency = "Frequency";
	public static String STR_Upgrades = "Upgrades";
	public static String STR_PortalModifierTitle = "Portal Modifier";
	
	public static List<Integer> removableBlocks = new ArrayList<Integer>();
	public static List<Integer> borderBlocks = new ArrayList<Integer>();
	
	public static Configuration ConfigFile;
	public static java.util.logging.Logger Logger;
		
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
					borderBlocks.add(Integer.parseInt(str2));
			}
			else
			{
				borderBlocks.add(Integer.parseInt(str));
			}
		}
		catch (Exception e)
		{
			Logger.log(Level.WARNING, "Custom Border Blocks string is not correctly formatted.");
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
					removableBlocks.add(Integer.parseInt(str2));
			}
			else
			{
				removableBlocks.add(Integer.parseInt(str));
			}
		}
		catch (Exception e)
		{
			Logger.log(Level.WARNING, "Custom Destroy Blocks string is not correctly formatted.");
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
	
	public static void LogData(String data)
	{
		if (printPortalMessages)
		Logger.log(Level.INFO, data);
	}
	
	// Server-only
	public static void LogData(String data, boolean isRemote)
	{
		if (printPortalMessages && !isRemote)
		Logger.log(Level.INFO, data);
	}
}
