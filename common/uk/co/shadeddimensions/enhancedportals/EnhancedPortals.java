package uk.co.shadeddimensions.enhancedportals;

import net.minecraftforge.common.Configuration;
import uk.co.shadeddimensions.enhancedportals.lib.Reference;
import uk.co.shadeddimensions.enhancedportals.network.CommonProxy;
import uk.co.shadeddimensions.enhancedportals.network.GoggleTickHandler;
import uk.co.shadeddimensions.enhancedportals.network.GuiHandler;
import uk.co.shadeddimensions.enhancedportals.network.PacketHandler;
import uk.co.shadeddimensions.enhancedportals.portal.networking.NetworkManager;
import uk.co.shadeddimensions.enhancedportals.util.ConfigurationManager;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(name = Reference.NAME, modid = Reference.ID, version = Reference.VERSION, dependencies = Reference.DEPENDENCIES, acceptedMinecraftVersions = Reference.MC_VERSION)
@NetworkMod(clientSideRequired = true, serverSideRequired = true, packetHandler = PacketHandler.class, channels = { Reference.ID })
public class EnhancedPortals
{
    public static ConfigurationManager config;    

    @Instance(Reference.ID)
    public static EnhancedPortals instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY, serverSide = Reference.COMMON_PROXY)
    public static CommonProxy proxy;

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.setupConfiguration();
        proxy.registerBlocks();
        proxy.registerTileEntities();
        proxy.registerItems();
        proxy.registerRenderers();

        NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());
        TickRegistry.registerTickHandler(new GoggleTickHandler(), Side.CLIENT);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        /*if (Loader.isModLoaded("ForgeMultipart"))
        {
            new RegisterParts().init();
            CommonProxy.multiPartID = MultipartProxy.config().getTag("block.id").getIntValue();
        }*/
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        config = new ConfigurationManager(new Configuration(event.getSuggestedConfigurationFile()));
    }
    
    @EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {        
        CommonProxy.networkManager = new NetworkManager(event);
    }
}
