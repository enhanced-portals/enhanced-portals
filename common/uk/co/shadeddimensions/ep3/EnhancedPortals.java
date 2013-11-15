package uk.co.shadeddimensions.ep3;

import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;
import uk.co.shadeddimensions.ep3.lib.Reference;
import uk.co.shadeddimensions.ep3.network.ClientProxy;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.network.GuiHandler;
import uk.co.shadeddimensions.ep3.network.PacketHandlerClient;
import uk.co.shadeddimensions.ep3.network.PacketHandlerServer;
import uk.co.shadeddimensions.ep3.portal.NetworkManager;
import uk.co.shadeddimensions.ep3.world.EPWorldProvider;
import uk.co.shadeddimensions.ep3.world.biome.Biomes;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(name = Reference.NAME, modid = Reference.ID, version = Reference.VERSION, dependencies = Reference.DEPENDENCIES, acceptedMinecraftVersions = Reference.MC_VERSION)
@NetworkMod(clientSideRequired = true, serverSideRequired = true, serverPacketHandlerSpec = @SidedPacketHandler(channels = Reference.SHORT_ID, packetHandler = PacketHandlerServer.class), clientPacketHandlerSpec = @SidedPacketHandler(channels = Reference.SHORT_ID, packetHandler = PacketHandlerClient.class))
public class EnhancedPortals
{
    @Instance(Reference.ID)
    public static EnhancedPortals instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY, serverSide = Reference.COMMON_PROXY)
    public static CommonProxy proxy;

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        CommonProxy.logger.setParent(FMLLog.getLogger());
        proxy.registerBlocks();
        proxy.registerTileEntities();
        proxy.registerItems();
        proxy.registerEntities();
        proxy.registerRenderers();
        proxy.miscSetup();
        Biomes.initBiomes();

        MinecraftForge.EVENT_BUS.register(this);
        NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());
        DimensionManager.registerProviderType(CommonProxy.Dimension, EPWorldProvider.class, false);
        DimensionManager.registerDimension(CommonProxy.Dimension, CommonProxy.Dimension);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        proxy.setupConfiguration(new Configuration(event.getSuggestedConfigurationFile()));
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        CommonProxy.networkManager = new NetworkManager(event);
    }

    @ForgeSubscribe
    public void worldSave(WorldEvent.Save event)
    {
        if (!event.world.isRemote)
        {
            CommonProxy.networkManager.saveAllData();
        }
    }
    
    @ForgeSubscribe
    @SideOnly(Side.CLIENT)
    public void textureHook(TextureStitchEvent.Pre event)
    {
        if (event.map.textureType == 0)
        {
            ClientProxy.customPortalTextures.clear();
            ClientProxy.customFrameTextures.clear();            
            int counter = 0;
            
            while (ClientProxy.resourceExists("textures/blocks/customPortal/" + String.format("%02d", counter) + ".png"))
            {
                CommonProxy.logger.info("Registered custom portal Icon: " + String.format("%02d", counter) + ".png");
                ClientProxy.customPortalTextures.add(event.map.registerIcon("enhancedportals:customPortal/" + String.format("%02d", counter)));                
                counter++;
            }
            
            counter = 0;
            
            while (ClientProxy.resourceExists("textures/blocks/customFrame/" + String.format("%02d", counter) + ".png"))
            {
                CommonProxy.logger.info("Registered custom frame Icon: " + String.format("%02d", counter) + ".png");
                ClientProxy.customFrameTextures.add(event.map.registerIcon("enhancedportals:customFrame/" + String.format("%02d", counter)));                
                counter++;
            }
        }
    }
}
