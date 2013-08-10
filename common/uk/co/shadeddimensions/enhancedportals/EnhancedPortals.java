package uk.co.shadeddimensions.enhancedportals;

import uk.co.shadeddimensions.enhancedportals.network.CommonProxy;
import uk.co.shadeddimensions.enhancedportals.network.GuiHandler;
import uk.co.shadeddimensions.enhancedportals.network.PacketHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import enhancedportals.lib.Reference;

@Mod(name = Reference.MOD_NAME, modid = Reference.MOD_ID_NEW, version = "1.1.0", dependencies = "required-after:EnhancedCore@[1.1.1,)", acceptedMinecraftVersions = "[1.6.2,)")
@NetworkMod(clientSideRequired = true, serverSideRequired = true, packetHandler = PacketHandler.class, channels = { Reference.MOD_ID_NEW })
public class EnhancedPortals
{
    @Instance(Reference.MOD_ID_NEW)
    public static EnhancedPortals instance;

    @SidedProxy(clientSide = "uk.co.shadeddimensions.enhancedportals.network.ClientProxy", serverSide = "uk.co.shadeddimensions.enhancedportals.network.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.registerBlocks();
        proxy.registerTileEntities();
        proxy.registerItems();
        proxy.registerRenderers();

        NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());
    }

    @EventHandler
    public void postInit(FMLInitializationEvent event)
    {

    }

    @EventHandler
    public void preInit(FMLInitializationEvent event)
    {

    }
}
