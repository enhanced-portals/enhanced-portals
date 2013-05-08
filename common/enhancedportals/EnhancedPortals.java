package enhancedportals;

import net.minecraft.item.Item;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.Mod.ServerStopping;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import enhancedportals.client.gui.GuiHandler;
import enhancedportals.lib.Localization;
import enhancedportals.lib.PortalTexture;
import enhancedportals.lib.Reference;
import enhancedportals.lib.Settings;
import enhancedportals.network.ClientPacketHandler;
import enhancedportals.network.CommonProxy;
import enhancedportals.network.ServerPacketHandler;

@Mod(name = Reference.MOD_NAME, modid = Reference.MOD_ID, version = Reference.MOD_VERSION)
@NetworkMod(clientSideRequired = true, serverSideRequired = false, clientPacketHandlerSpec = @SidedPacketHandler(channels = { Reference.MOD_ID }, packetHandler = ClientPacketHandler.class), serverPacketHandlerSpec = @SidedPacketHandler(channels = { Reference.MOD_ID }, packetHandler = ServerPacketHandler.class))
public class EnhancedPortals
{
    @Instance(Reference.MOD_ID)
    public static EnhancedPortals instance;

    @SidedProxy(clientSide = Reference.PROXY_CLIENT, serverSide = Reference.PROXY_COMMON)
    public static CommonProxy proxy;

    @Init
    private void init(FMLInitializationEvent event)
    {
        NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());

        Settings.ItemPortalTextureMap.put(Item.bucketLava.itemID + ":0", new PortalTexture(10, 0));
        Settings.ItemPortalTextureMap.put(Item.bucketLava.itemID + ":0_", new PortalTexture(11, 0));
        Settings.ItemPortalTextureMap.put(Item.bucketWater.itemID + ":0", new PortalTexture(8, 0));
        Settings.ItemPortalTextureMap.put(Item.bucketWater.itemID + ":0_", new PortalTexture(9, 0));
    }

    @PostInit
    private void postInit(FMLPostInitializationEvent event)
    {

    }

    @PreInit
    private void preInit(FMLPreInitializationEvent event)
    {
        proxy.loadSettings(new Configuration(event.getSuggestedConfigurationFile()));
        proxy.loadBlocks();
        proxy.loadItems();
        proxy.loadTileEntities();
        proxy.loadRecipes();

        Localization.loadLocales();
    }

    @ServerStarting
    private void serverStarting(FMLServerStartingEvent event)
    {
        // Load up networks
    }

    @ServerStopping
    private void serverStopping(FMLServerStoppingEvent event)
    {
        // Save networks
    }
}
