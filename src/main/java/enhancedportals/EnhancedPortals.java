package enhancedportals;

import java.io.File;

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
    public static CreativeTabs creativeTab = new CreativeTabEP3();

    @Instance(ID)
    public static EnhancedPortals instance;

    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = COMMON_PROXY)
    public static CommonProxy proxy;

    public static String localize(String s)
    {
        return StatCollector.translateToLocal(SHORT_ID + "." + s);
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
        proxy.setupCrafting();
        proxy.miscSetup();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        packetPipeline.postInitialise();
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
