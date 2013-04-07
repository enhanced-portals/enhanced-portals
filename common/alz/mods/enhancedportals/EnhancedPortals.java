package alz.mods.enhancedportals;

import java.lang.reflect.Field;

import net.minecraft.block.Block;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import alz.mods.enhancedportals.block.Blocks;
import alz.mods.enhancedportals.common.CommonProxy;
import alz.mods.enhancedportals.common.EventHooks;
import alz.mods.enhancedportals.common.GuiHandler;
import alz.mods.enhancedportals.item.Items;
import alz.mods.enhancedportals.networking.PacketHandler;
import alz.mods.enhancedportals.reference.Reference;
import alz.mods.enhancedportals.server.CommandEP;
import alz.mods.enhancedportals.server.CommandEPClient;
import alz.mods.enhancedportals.tileentity.TileEntities;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION)
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = { Reference.MOD_ID }, packetHandler = PacketHandler.class)
public class EnhancedPortals
{
	@SidedProxy(clientSide = Reference.PROXY_CLIENT, serverSide = Reference.PROXY_COMMON)
	public static CommonProxy proxy;

	@Instance(Reference.MOD_ID)
	public static EnhancedPortals instance;

	@PreInit
	public void preLoad(FMLPreInitializationEvent event)
	{
		Reference.ConfigFile = new Configuration(event.getSuggestedConfigurationFile());
		Reference.Logger = event.getModLog();

		Reference.Settings.AllowTeleporting = Reference.Settings.GetFromConfig("AllowTeleporting", Reference.Settings.AllowTeleporting_Default);
		Reference.Settings.CanDyePortals = Reference.Settings.GetFromConfig("CanDyePortals", Reference.Settings.CanDyePortals_Default);
		Reference.Settings.DoesDyingCost = Reference.Settings.GetFromConfig("DoesDyingCost", Reference.Settings.DoesDyingCost_Default);
		Reference.Settings.CanDyeByThrowing = Reference.Settings.GetFromConfig("CanDyeByThrowing", Reference.Settings.CanDyeByThrowing_Default);
		Reference.Settings.AllowModifiers = Reference.Settings.GetFromConfig("AllowModifiers", Reference.Settings.AllowModifiers_Default);
		Reference.Settings.AllowObsidianStairs = Reference.Settings.GetFromConfig("AllowObsidianStairs", Reference.Settings.AllowObsidianStairs_Default);
		Reference.Settings.AllowDialDevice = Reference.Settings.GetFromConfig("AllowDialDevice", Reference.Settings.AllowDialDevice_Default);
		Reference.Settings.PrintPortalMessages = Reference.Settings.GetFromConfig("PrintPortalCreationMessages", Reference.Settings.PrintPortalMessages_Default);

		Reference.Settings.MaximumPortalSize = Reference.Settings.GetFromConfig("MaximumPortalSize", Reference.Settings.MaximumPortalSize_Default);

		Reference.Settings.PigmenSpawnChance = Reference.Settings.GetFromConfig("PigmenSpawnChance", 100, 0, 100);
		Reference.Settings.SoundLevel = Reference.Settings.GetFromConfig("SoundLevel", 100, 0, 100);
		Reference.Settings.ParticleLevel = Reference.Settings.GetFromConfig("ParticleLevel", 100, 0, 100);

		Reference.BlockIDs.ObsidianStairs = Reference.Settings.GetFromConfig("ObsidianStairsID", Reference.BlockIDs.ObsidianStairs_Default, true);
		Reference.BlockIDs.PortalModifier = Reference.Settings.GetFromConfig("PortalModifierID", Reference.BlockIDs.PortalModifier_Default, true);
		Reference.BlockIDs.DialDevice = Reference.Settings.GetFromConfig("DialDeviceID", Reference.BlockIDs.DialDevice_Default, true);

		Reference.ItemIDs.PortalModifierUpgrade = Reference.Settings.GetFromConfig("PortalModifierUpgradeID", Reference.ItemIDs.PortalModifierUpgrade_Default, false);
		Reference.ItemIDs.MiscItems = Reference.Settings.GetFromConfig("MiscItemsID", Reference.ItemIDs.MiscItems_Default, false);
		Reference.ItemIDs.ItemScroll = Reference.Settings.GetFromConfig("ItemScrollID", Reference.ItemIDs.ItemScroll_Default, false);

		Reference.Settings.AddToBorderBlocks(Reference.Settings.GetFromConfig("CustomBorderBlocks", ""));
		Reference.Settings.AddToDestroyBlocks(Reference.Settings.GetFromConfig("CustomDestroyBlocks", "8,9,10,11,6,18,30,31,32,37,38,39,40,78,104,105,106,111,115"));

		Reference.ConfigFile.save();
	}

	@Init
	public void load(FMLInitializationEvent event)
	{
		Reference.LoadLanguage();

		MinecraftForge.EVENT_BUS.register(new EventHooks());
		NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());

		try
		{
			@SuppressWarnings("rawtypes")
			Class ccClass = Class.forName("dan200.ComputerCraft");
			Field blockComputer = ccClass.getField("computerBlockID");

			Reference.ComputercraftComputer = Block.blocksList[blockComputer.getInt(blockComputer)];
			Reference.LogData("Found and loaded ComputerCraft.");
		}
		catch (Exception e)
		{
			Reference.LogData("ComputerCraft was not found.");
		}

		Blocks.Init();
		TileEntities.Init();
		Items.Init();
	}

	@ServerStarting
	public void serverStarting(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new CommandEP());
		event.registerServerCommand(new CommandEPClient());
	}
}
