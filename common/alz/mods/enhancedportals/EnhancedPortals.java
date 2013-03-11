package alz.mods.enhancedportals;

import alz.mods.enhancedportals.block.Blocks;
import alz.mods.enhancedportals.common.CommonProxy;
import alz.mods.enhancedportals.common.EventHooks;
import alz.mods.enhancedportals.common.GuiHandler;
import alz.mods.enhancedportals.common.PacketHandler;
import alz.mods.enhancedportals.item.Items;
import alz.mods.enhancedportals.reference.BlockID;
import alz.mods.enhancedportals.reference.ItemID;
import alz.mods.enhancedportals.reference.Language;
import alz.mods.enhancedportals.reference.Logger;
import alz.mods.enhancedportals.reference.ModData;
import alz.mods.enhancedportals.reference.Settings;
import alz.mods.enhancedportals.tileentity.TileEntities;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(modid=ModData.ID, name=ModData.Name, version=ModData.Version)
@NetworkMod(clientSideRequired=true, serverSideRequired=false, channels={ModData.ID}, packetHandler=PacketHandler.class)
public class EnhancedPortals
{
	@SidedProxy(clientSide="alz.mods.enhancedportals.client.ClientProxy", serverSide="alz.mods.enhancedportals.common.CommonProxy")
	public static CommonProxy proxy;
	
	@Instance(ModData.ID)
	public static EnhancedPortals instance;
		
	@PreInit
	public void preLoad(FMLPreInitializationEvent event)
	{
		Settings.ConfigFile = new Configuration(event.getSuggestedConfigurationFile());
		Logger.Logger = event.getModLog();
		
		Settings.AllowTeleporting = Settings.GetFromConfig("AllowTeleporting", true);
		Settings.CanDyePortals = Settings.GetFromConfig("CanDyePortals", true);
		Settings.DoesDyingCost = Settings.GetFromConfig("DoesDyingCost", true);
		Settings.AllowModifiers = Settings.GetFromConfig("AllowModifiers", true);
		Settings.AllowObsidianStairs = Settings.GetFromConfig("AllowObsidianStairs", true);
		Settings.PrintPortalMessages = Settings.GetFromConfig("PrintPortalCreationMessages", true);
		
		Settings.PigmenSpawnChance = Settings.GetFromConfig("PigmenSpawnChance", 100, 0, 100);
		Settings.SoundLevel = Settings.GetFromConfig("SoundLevel", 100, 0, 100);
		Settings.ParticleLevel = Settings.GetFromConfig("ParticleLevel", 100, 0, 100);
		
		BlockID.ObsidianStairs = Settings.GetFromConfig("ObsidianStairsID", 512, true);
		BlockID.PortalModifier = Settings.GetFromConfig("PortalModifierID", 513, true);
		ItemID.ModifierUpgrade = Settings.GetFromConfig("PortalModifierUpgradeID", 6000, false);
		
		Language.NoUpgrade = Settings.GetFromConfig("TEXT_NoUpgrade", "You do not have the required upgrade.");
		Language.ExitBlocked = Settings.GetFromConfig("TEXT_ExitBlocked", "The exit portal is blocked or invalid.");
		Language.NoExit = Settings.GetFromConfig("TEXT_NoExit", "Couldn't find a valid exit.");
		
		Language.Frequency = Settings.GetFromConfig("TEXT_Frequency", "Frequency");
		Language.Upgrades = Settings.GetFromConfig("TEXT_Upgrades", "Upgrades");
		Language.PortalModifierTitle = Settings.GetFromConfig("TEXT_PortalModifier", "Portal Modifier");
	
		Settings.AddToBorderBlocks(Settings.GetFromConfig("CustomBorderBlocks", ""));
		Settings.AddToDestroyBlocks(Settings.GetFromConfig("CustomDestroyBlocks", "8,9,10,11,6,18,30,31,32,37,38,39,40,78,104,105,106,111,115"));
		
		Settings.ConfigFile.save();
	}
	
	@Init
	public void load(FMLInitializationEvent event)
	{
		Blocks.Init();
		TileEntities.Init();
		Items.Init();
		
		MinecraftForge.EVENT_BUS.register(new EventHooks());
		NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());		
	}
}
