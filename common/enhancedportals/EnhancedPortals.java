package enhancedportals;

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
import enhancedportals.lib.Reference;
import enhancedportals.network.CommonProxy;
import enhancedportals.network.PacketHandler;

@Mod(name=Reference.MOD_NAME, modid=Reference.MOD_ID, version=Reference.MOD_VERSION)
@NetworkMod(channels={Reference.MOD_ID}, clientSideRequired=true, serverSideRequired=false, packetHandler=PacketHandler.class)
public class EnhancedPortals
{
    @Instance(Reference.MOD_NAME)
    public static EnhancedPortals instance;
    
    @SidedProxy(clientSide=Reference.PROXY_CLIENT, serverSide=Reference.PROXY_COMMON)
    public static CommonProxy proxy;
    
    @PreInit
    private void preInit(FMLPreInitializationEvent event)
    {
        proxy.loadSettings();
        proxy.loadBlocks();
        proxy.loadItems();
        proxy.loadTileEntities();
        proxy.loadRecipes();
    }
    
    @Init
    private void init(FMLInitializationEvent event)
    {
        
    }
    
    @PostInit
    private void postInit(FMLPostInitializationEvent event)
    {
        
    }
    
    @ServerStarting
    private void serverStarting(FMLServerStartingEvent event)
    {
        
    }
    
    @ServerStopping
    private void serverStopping(FMLServerStoppingEvent event)
    {
        
    }
}
