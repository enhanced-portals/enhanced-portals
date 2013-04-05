package alz.mods.enhancedportals.reference;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraftforge.common.Configuration;
import alz.mods.enhancedportals.common.LinkData;

public class Reference
{
	public static Configuration ConfigFile;
	public static LinkData LinkData;
	public static Logger Logger;

	public static final String MOD_NAME = "Enhanced Portals";
	public static final String MOD_ID = "enhancedportals";
	public static final String MOD_VERSION = "2.1.2";

	public static final String PROXY_CLIENT = "alz.mods.enhancedportals.client.ClientProxy";
	public static final String PROXY_COMMON = "alz.mods.enhancedportals.common.CommonProxy";

	public static Block ComputercraftComputer;

	/*** Block IDs ***/
	public static class BlockIDs
	{
		public static final int ObsidianStairs_Default = 1345;
		public static final int PortalModifier_Default = 1346;
		public static final int DialDevice_Default = 1347;
		public static final int NetherPortal_Default = 1349;
		public static final int Obsidian_Default = 49;
		
		public static int ObsidianStairs;
		public static int PortalModifier;
		public static int Obsidian = Obsidian_Default;
		public static int NetherPortal = NetherPortal_Default;
		public static int DialDevice;
	}

	/*** Item IDs ***/
	public static class ItemIDs
	{
		public static final int PortalModifierUpgrade_Default = 6000;
		public static final int MiscItems_Default = 6001;
		public static final int ItemScroll_Default = 6002;

		public static int PortalModifierUpgrade;
		public static int MiscItems;
		public static int ItemScroll;
	}

	public static class GuiIDs
	{
		public static final int PortalModifier = 0;
		public static final int DialDevice = 1;
		public static final int DialDeviceAdd = 2;
	}

	public static class Networking
	{
		public static final int TileEntityUpdate = 0;
		public static final int DataRequest = 1;
		public static final int GuiRequest = 2;
		public static final int DialDevice_NewPortalData = 5;
		public static final int DialDevice_AllPortalData = 6;
		public static final int DialDevice_GenericData = 7;
		public static final int DD_Select = 1;
		public static final int DD_Remove = 2;
		public static final int DD_RemoveSelect = 3;
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
		public static final boolean AllowDialDevice_Default = true;
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
		public static boolean AllowDialDevice;
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
					{
						BorderBlocks.add(Integer.parseInt(str2));
					}
				}
				else
				{
					BorderBlocks.add(Integer.parseInt(str));
				}
			}
			catch (Exception e)
			{
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
					{
						RemovableBlocks.add(Integer.parseInt(str2));
					}
				}
				else
				{
					RemovableBlocks.add(Integer.parseInt(str));
				}
			}
			catch (Exception e)
			{
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
		{
			Logger.log(Level.INFO, data);
		}
	}
}
