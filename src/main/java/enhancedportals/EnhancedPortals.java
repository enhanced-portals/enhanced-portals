package enhancedportals;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.network.GuiHandler;
import uk.co.shadeddimensions.ep3.network.PacketHandlerClient;
import uk.co.shadeddimensions.ep3.network.PacketHandlerServer;
import uk.co.shadeddimensions.ep3.portal.NetworkManager;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import enhancedportals.common.CreativeTabEP3;

@Mod(name = EnhancedPortals.NAME, modid = EnhancedPortals.ID, dependencies = EnhancedPortals.DEPENDENCIES, acceptedMinecraftVersions = EnhancedPortals.MC_VERSION)
@NetworkMod(clientSideRequired = true, serverSideRequired = true, serverPacketHandlerSpec = @SidedPacketHandler(channels = EnhancedPortals.SHORT_ID, packetHandler = PacketHandlerServer.class), clientPacketHandlerSpec = @SidedPacketHandler(channels = EnhancedPortals.SHORT_ID, packetHandler = PacketHandlerClient.class))
public class EnhancedPortals
{
    public static final String NAME = "EnhancedPortals", ID = "EnhancedPortals3", SHORT_ID = "ep3", DEPENDENCIES = "after:ThermalExpansion", MC_VERSION = "[1.6.4,)", VERSION = "1.6.4-3.0.0b5f", CLIENT_PROXY = "uk.co.shadeddimensions.ep3.network.ClientProxy", COMMON_PROXY = "uk.co.shadeddimensions.ep3.network.CommonProxy";
    public static CreativeTabs creativeTab = new CreativeTabEP3();

    @Instance(ID)
    public static EnhancedPortals instance;

    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = COMMON_PROXY)
    public static CommonProxy proxy;

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.setupCrafting();
        proxy.miscSetup();

        MinecraftForge.EVENT_BUS.register(this);
        NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        CommonProxy.logger.setParent(FMLLog.getLogger());
        event.getModMetadata().version = VERSION;
        proxy.setupConfiguration(new Configuration(event.getSuggestedConfigurationFile()));

        proxy.registerBlocks();
        proxy.registerTileEntities();
        proxy.registerItems();
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        proxy.networkManager = new NetworkManager(event);
    }

    @ForgeSubscribe
    public void worldSave(WorldEvent.Save event)
    {
        if (!event.world.isRemote)
        {
            proxy.networkManager.saveAllData();
        }
    }

    /**
     * Localizes an EP3 string. (Prepends the mod ID, then localizes)
     */
    public static String localize(String s)
    {
        return StatCollector.translateToLocal(SHORT_ID + "." + s);
    }

    /*
     * @ForgeSubscribe
     * 
     * @SideOnly(Side.CLIENT) public void textureHook(TextureStitchEvent.Pre event) { if (event.map.textureType == 0) { ClientProxy.customPortalTextures.clear(); ClientProxy.customFrameTextures.clear(); int counter = 0;
     * 
     * while (ClientProxy.resourceExists("textures/blocks/customPortal/" + String.format("%02d", counter) + ".png")) { CommonProxy.logger.info("Registered custom portal Icon: " + String.format("%02d", counter) + ".png"); ClientProxy.customPortalTextures.add(event.map.registerIcon("enhancedportals:customPortal/" + String.format("%02d", counter))); counter++; }
     * 
     * counter = 0;
     * 
     * while (ClientProxy.resourceExists("textures/blocks/customFrame/" + String.format("%02d", counter) + ".png")) { CommonProxy.logger.info("Registered custom frame Icon: " + String.format("%02d", counter) + ".png"); ClientProxy.customFrameTextures.add(event.map.registerIcon("enhancedportals:customFrame/" + String.format("%02d", counter))); counter++; } } }
     */
}
