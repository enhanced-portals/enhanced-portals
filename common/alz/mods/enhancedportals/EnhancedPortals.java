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
import alz.mods.enhancedportals.reference.Settings;
import alz.mods.enhancedportals.server.CommandEP;
import alz.mods.enhancedportals.server.CommandEPClient;
import alz.mods.enhancedportals.server.ServerHandler;
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

    @PreInit
    public void preLoad(FMLPreInitializationEvent event)
    {
        Settings.ConfigFile = new Configuration(event.getSuggestedConfigurationFile());
        Reference.Logger = event.getModLog();

        Settings.AllowTeleporting = Settings.GetFromConfig("AllowTeleporting", Settings.AllowTeleporting_Default);
        Settings.CanDyePortals = Settings.GetFromConfig("CanDyePortals", Settings.CanDyePortals_Default);
        Settings.DoesDyingCost = Settings.GetFromConfig("DoesDyingCost", Settings.DoesDyingCost_Default);
        Settings.CanDyeByThrowing = Settings.GetFromConfig("CanDyeByThrowing", Settings.CanDyeByThrowing_Default);
        Settings.AllowModifiers = Settings.GetFromConfig("AllowModifiers", Settings.AllowModifiers_Default);
        Settings.AllowObsidianStairs = Settings.GetFromConfig("AllowObsidianStairs", Settings.AllowObsidianStairs_Default);
        Settings.AllowDialDevice = Settings.GetFromConfig("AllowDialDevice", Settings.AllowDialDevice_Default);
        Settings.PrintPortalMessages = Settings.GetFromConfig("PrintPortalCreationMessages", Settings.PrintPortalMessages_Default);

        Settings.MaximumPortalSize = Settings.GetFromConfig("MaximumPortalSize", Settings.MaximumPortalSize_Default);

        Settings.PigmenSpawnChance = Settings.GetFromConfig("PigmenSpawnChance", 100, 0, 100);
        Settings.SoundLevel = Settings.GetFromConfig("SoundLevel", 100, 0, 100);
        Settings.ParticleLevel = Settings.GetFromConfig("ParticleLevel", 100, 0, 100);

        Reference.BlockIDs.ObsidianStairs = Settings.GetFromConfig("ObsidianStairsID", Reference.BlockIDs.ObsidianStairs_Default, true);
        Reference.BlockIDs.PortalModifier = Settings.GetFromConfig("PortalModifierID", Reference.BlockIDs.PortalModifier_Default, true);
        Reference.BlockIDs.DialDevice = Settings.GetFromConfig("DialDeviceID", Reference.BlockIDs.DialDevice_Default, true);

        Reference.ItemIDs.PortalModifierUpgrade = Settings.GetFromConfig("PortalModifierUpgradeID", Reference.ItemIDs.PortalModifierUpgrade_Default, false);
        Reference.ItemIDs.MiscItems = Settings.GetFromConfig("MiscItemsID", Reference.ItemIDs.MiscItems_Default, false);
        Reference.ItemIDs.ItemScroll = Settings.GetFromConfig("ItemScrollID", Reference.ItemIDs.ItemScroll_Default, false);

        Settings.AddToBorderBlocks(Settings.GetFromConfig("CustomBorderBlocks", ""));
        Settings.AddToDestroyBlocks(Settings.GetFromConfig("CustomDestroyBlocks", "8,9,10,11,6,18,30,31,32,37,38,39,40,78,104,105,106,111,115"));

        Settings.ConfigFile.save();
    }

    @ServerStarting
    public void serverStarting(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandEP());
        event.registerServerCommand(new CommandEPClient());
        Reference.ServerHandler = new ServerHandler();
    }
}
