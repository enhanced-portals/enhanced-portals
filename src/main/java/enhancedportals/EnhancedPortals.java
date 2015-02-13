package enhancedportals;

import java.io.File;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;

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
import cpw.mods.fml.common.network.NetworkRegistry;
import enhancedportals.network.PacketPipeline;
import enhancedportals.network.ProxyCommon;
import enhancedportals.util.CreativeTabEP;

@Mod(name = EnhancedPortals.MOD_NAME, modid = EnhancedPortals.MOD_ID, version = EnhancedPortals.MOD_VERS, dependencies = EnhancedPortals.MOD_DEPENDENCIES)
public class EnhancedPortals
{
    public static final String MOD_NAME = "EnhancedPortals", MOD_ID = "enhancedportals", MOD_VERS = "3.1.0", MOD_DEPENDENCIES = "", MOD_UPDATE_URL = "";

    final PacketPipeline packetPipeline = new PacketPipeline();
    final Logger logger = LogManager.getLogger("EnhancedPortals");

    @Instance(MOD_ID)
    public static EnhancedPortals instance;

    @SidedProxy(clientSide = "enhancedportals.network.ProxyClient", serverSide = "enhancedportals.network.ProxyCommon")
    public static ProxyCommon proxy;

    public EnhancedPortals()
    {
        LoggerConfig fml = new LoggerConfig(FMLCommonHandler.instance().getFMLLogger().getName(), Level.ALL, true);
        LoggerConfig modConf = new LoggerConfig(logger.getName(), Level.ALL, true);
        modConf.setParent(fml);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @EventHandler
    public void pre(FMLPreInitializationEvent event)
    {
        proxy.pre(new File(event.getSuggestedConfigurationFile().getParentFile(), MOD_NAME + File.separator + "config.cfg"));
        packetPipeline.initalise();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init();
    }

    @EventHandler
    public void post(FMLPostInitializationEvent event)
    {
        packetPipeline.postInitialise();
        proxy.post();
    }

    public void log(Level l, String s)
    {
        logger.log(l, s);
    }
}
