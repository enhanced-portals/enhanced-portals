package enhancedportals;

import java.io.File;
import java.lang.reflect.Method;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.LoggerConfig;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import enhancedportals.block.BlockFrame;
import enhancedportals.common.CreativeTabEP3;
import enhancedportals.network.CommonProxy;
import enhancedportals.network.GuiHandler;
import enhancedportals.network.PacketPipeline;
import enhancedportals.portal.NetworkManager;

@Mod(name = EnhancedPortals.NAME, modid = EnhancedPortals.ID, version = "3.0.0", dependencies = EnhancedPortals.DEPENDENCIES)
public class EnhancedPortals
{
    public static final String NAME = "EnhancedPortals", ID = "enhancedportals", SHORT_ID = "ep3", DEPENDENCIES = "after:ThermalExpansion", CLIENT_PROXY = "enhancedportals.network.ClientProxy", COMMON_PROXY = "enhancedportals.network.CommonProxy";
    public static final PacketPipeline packetPipeline = new PacketPipeline();
    public static final Logger logger = LogManager.getLogger("EnhancedPortals");
    public static final CreativeTabs creativeTab = new CreativeTabEP3();
    public static final String MODID_OPENCOMPUTERS = "OpenComputers", MODID_COMPUTERCRAFT = "ComputerCraft";

    @Instance(ID)
    public static EnhancedPortals instance;

    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = COMMON_PROXY)
    public static CommonProxy proxy;

    public static String localize(String s)
    {
        String s2 = StatCollector.translateToLocal(SHORT_ID + "." + s).replace("<N>", "\n").replace("<P>", "\n\n");
        return s2.contains(SHORT_ID + ".") || s2.contains("item.") || s2.contains("tile.") ? StatCollector.translateToLocal(s2) : s2;
    }

    public static String localizeError(String s)
    {
        return EnumChatFormatting.RED + localize("error") + EnumChatFormatting.WHITE + localize("error." + s);
    }

    public static String localizeSuccess(String s)
    {
        return EnumChatFormatting.GREEN + localize("success") + EnumChatFormatting.WHITE + localize("success." + s);
    }

    public EnhancedPortals()
    {
        LoggerConfig fml = new LoggerConfig(FMLCommonHandler.instance().getFMLLogger().getName(), Level.ALL, true);
        LoggerConfig modConf = new LoggerConfig(logger.getName(), Level.ALL, true);
        modConf.setParent(fml);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.miscSetup();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
    }

    /** Taken from the CC-API, allowing for use it if it's available, instead of shipping it/requiring it **/
    void initializeComputerCraft()
    {
        if (!Loader.isModLoaded(MODID_COMPUTERCRAFT))
        {
            return;
        }

        try
        {
            Class computerCraft = Class.forName("dan200.computercraft.ComputerCraft");
            Method computerCraft_registerPeripheralProvider = computerCraft.getMethod("registerPeripheralProvider", new Class[] { Class.forName("dan200.computercraft.api.peripheral.IPeripheralProvider") });
            computerCraft_registerPeripheralProvider.invoke(null, BlockFrame.instance);
        }
        catch (Exception e)
        {
            logger.error("Could not load the CC-API");
        }
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        packetPipeline.postInitialise();
        initializeComputerCraft();
        proxy.setupCrafting();
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        proxy.setupConfiguration(new File(event.getSuggestedConfigurationFile().getParentFile(), NAME + File.separator + "config.cfg"));
        packetPipeline.initalise();
        proxy.registerBlocks();
        proxy.registerTileEntities();
        proxy.registerItems();
        proxy.registerPackets();
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        proxy.networkManager = new NetworkManager(event);
    }

    @SubscribeEvent
    public void worldSave(WorldEvent.Save event)
    {
        if (!event.world.isRemote)
        {
            proxy.networkManager.saveAllData();
        }
    }
}
