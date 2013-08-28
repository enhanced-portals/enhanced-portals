package uk.co.shadeddimensions.enhancedportals;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.Configuration;
import uk.co.shadeddimensions.enhancedportals.creativetab.CreativeTabEP2;
import uk.co.shadeddimensions.enhancedportals.multipart.RegisterParts;
import uk.co.shadeddimensions.enhancedportals.network.CommonProxy;
import uk.co.shadeddimensions.enhancedportals.network.GoggleTickHandler;
import uk.co.shadeddimensions.enhancedportals.network.GuiHandler;
import uk.co.shadeddimensions.enhancedportals.network.PacketHandler;
import uk.co.shadeddimensions.enhancedportals.util.ConfigurationManager;
import codechicken.multipart.handler.MultipartProxy;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import enhancedportals.lib.Reference;

@Mod(name = Reference.MOD_NAME, modid = Reference.MOD_ID_NEW, version = "1.1.0", dependencies = "required-after:EnhancedCore@[1.1.1,)", acceptedMinecraftVersions = "[1.6.2,)")
@NetworkMod(clientSideRequired = true, serverSideRequired = true, packetHandler = PacketHandler.class, channels = { Reference.MOD_ID_NEW })
public class EnhancedPortals
{
    public static ConfigurationManager config;
    public static CreativeTabs creativeTab = new CreativeTabEP2();

    @Instance(Reference.MOD_ID_NEW)
    public static EnhancedPortals instance;

    @SidedProxy(clientSide = "uk.co.shadeddimensions.enhancedportals.network.ClientProxy", serverSide = "uk.co.shadeddimensions.enhancedportals.network.CommonProxy")
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
        if (Loader.isModLoaded("ForgeMultipart"))
        {
            new RegisterParts().init();
            CommonProxy.multiPartID = MultipartProxy.config().getTag("block.id").getIntValue();
        }
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        config = new ConfigurationManager(new Configuration(event.getSuggestedConfigurationFile()));
    }
}
