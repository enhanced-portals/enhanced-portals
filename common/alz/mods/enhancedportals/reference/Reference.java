package alz.mods.enhancedportals.reference;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraftforge.common.Configuration;

import alz.mods.enhancedportals.common.LinkData;

public class Reference
{
	public static Configuration ConfigFile;
	public static LinkData LinkData;
	public static Logger Logger;
	
	public static final String MOD_NAME = "Enhanced Portals";
	public static final String MOD_ID = "enhancedportals";
	public static final String MOD_VERSION = "2.0.4-dev";
	
	public static final int Update_Range = 128;
	
	/*** Block IDs ***/
	public static class BlockIDs
	{
		public static final int ObsidianStairs_Default = 1345;
		public static final int PortalModifier_Default = 1346;
		public static final int Obsidian_Default = 49;
		public static final int NetherPortal_Default = 90;
		
		public static int ObsidianStairs;
		public static int PortalModifier;
		public static int Obsidian = Obsidian_Default;
		public static int NetherPortal = NetherPortal_Default;
	}
	
	/*** Item IDs ***/
	public static class ItemIDs
	{
		public static final int PortalModifierUpgrade_Default = 6000;
		
		public static int PortalModifierUpgrade;
	}
	
	public static class GuiIDs
	{
		public static final int PortalModifier = 0;
	}
	
	public static class Networking
	{
		public static final int TileEntityUpdate = 0;
	}
	
	/*** Contains everything related to I/O ***/
	public static class File
	{
		public static final String GuiTextureDirectory = "/mods/" + MOD_ID + "/textures/gui/";
		public static final String LanguageDirectory = "/mods/" + MOD_ID + "/lang/";
		
		public static String DataFile;
	}
	
	/*** All the user-configurable settings ***/
	public static class Settings
	{
		public static final boolean AllowTeleporting_Default = true;
		public static final boolean CanDyePortals_Default = true;
		public static final boolean DoesDyingCost_Default = true;
		public static final boolean AllowModifiers_Default = true;
		public static final boolean AllowObsidianStairs_Default = true;
		public static final boolean PrintPortalMessages_Default = true;
		public static final boolean CanDyeByThrowing_Default = true;
		
		public static final int MaximumPortalSize_Default = 0;	
		public static final int PigmenSpawnChance_Default = 100;
		public static final int SoundLevel_Default = 100;
		public static final int ParticleLevel_Default = 100;
		
		public static final List<Integer> RemovableBlocks_Default = new ArrayList<Integer>();
		public static final List<Integer> BorderBlocks_Default = new ArrayList<Integer>();
		
		public static boolean AllowTeleporting;
		public static boolean CanDyePortals;
		public static boolean DoesDyingCost;
		public static boolean AllowModifiers;
		public static boolean AllowObsidianStairs;
		public static boolean PrintPortalMessages;
		public static boolean CanDyeByThrowing;
		
		public static int MaximumPortalSize;	
		public static int PigmenSpawnChance;
		public static int SoundLevel;
		public static int ParticleLevel;
		
		public static List<Integer> RemovableBlocks = new ArrayList<Integer>();
		public static List<Integer> BorderBlocks = new ArrayList<Integer>();
		
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
			catch (Exception e) { }
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
			catch (Exception e) { }
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
		
		public static int GetFromConfig(String key, int defaultValue)
		{
			return ConfigFile.get(Configuration.CATEGORY_GENERAL, key, defaultValue).getInt(defaultValue);
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
			return ConfigFile.get(Configuration.CATEGORY_GENERAL, key, defaultValue).getString();
		}
	}
	
	/*** Language strings ***/
	public static class Strings
	{
		// Block Names
		public static final String PortalModifier_Name = "portalModifier";
		public static final String ObsidianStairs_Name = "stairsObsidian";
		
		// Block Icons
		public static final String PortalModifier_Icon_Side = MOD_ID + ":" + PortalModifier_Name + "_side";
		public static final String PortalModifier_Icon_Active = MOD_ID + ":" + PortalModifier_Name + "_active_%s";
		public static final String NetherPortal_Icon = MOD_ID + ":netherPortal_%s";
		
		// Item Names
		public static final String PortalModifierUpgrade_Name = "portalModifierUpgrade";
		
		public static final String PortalModifierUpgrade_MultiplePortals_Name = "item." + PortalModifierUpgrade_Name + ".multiplePortals";
		public static final String PortalModifierUpgrade_Dimensional_Name = "item." + PortalModifierUpgrade_Name + ".dimensional";
		public static final String PortalModifierUpgrade_AdvancedDimensional_Name = "item." + PortalModifierUpgrade_Name + ".advancedDimensional";
		public static final String PortalModifierUpgrade_ModifierCamo_Name = "item." + PortalModifierUpgrade_Name + ".modifierCamo";
		
		// Item Icons
		public static final String PortalModifierUpgrade_Icon = MOD_ID + ":" + PortalModifier_Name + "Upgrade_%s";
		
		// Tile Entities
		public static final String PortalModifier_TE_Name = "epmodif";
		
		// Portal
		public static final String Portal_NoExitFound = "portal.noExitFound";
		public static final String Portal_MissingUpgrade = "portal.missingUpgrade";
		public static final String Portal_ExitBlocked = "portal.exitBlocked";
		
		// Console
		public static final String Console_NoExitFound = "console.portal.noExitFound";
		public static final String Console_MissingUpgrade = "console.portal.missingUpgrade";
		public static final String Console_ExitBlocked = "console.portal.exitBlocked";
		public static final String Console_sizeFail = "console.portal.sizeFail";
		public static final String Console_success = "console.portal.success";
		public static final String Console_fail = "console.portal.fail";
		
		// GUI
		public static final String GUI_ClearCharacter = "gui.portalModifier.clearCharacter";
		public static final String GUI_SaveCharacter = "gui.portalModifier.saveCharacter";
		public static final String GUI_ClearFrequency = "gui.portalModifier.clearFrequency";
		public static final String GUI_SaveFrequency = "gui.portalModifier.saveFrequency";
		public static final String GUI_Frequency = "gui.portalModifier.frequency";
	}
		
	public static void LoadLanguage()
	{
		Localizations.loadLocales();
	}
	
	/*** Logs data ***/
	public static void LogData(String data)
	{
		Logger.log(Level.INFO, data);
	}
	
	/*** Logs on server only, send world.isRemote ***/
	public static void LogData(String data, boolean test)
	{
		if (!test)
			Logger.log(Level.INFO, data);
	}
}
