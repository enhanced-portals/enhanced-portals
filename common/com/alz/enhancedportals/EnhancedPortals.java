package com.alz.enhancedportals;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;

import com.alz.enhancedportals.core.blocks.BlockHandler;
import com.alz.enhancedportals.core.items.ItemHandler;
import com.alz.enhancedportals.core.networking.PacketHandler;
import com.alz.enhancedportals.core.proxy.CommonProxy;
import com.alz.enhancedportals.core.tileentity.TileEntityHandler;
import com.alz.enhancedportals.reference.Localizations;
import com.alz.enhancedportals.reference.Log;
import com.alz.enhancedportals.reference.Reference;
import com.alz.enhancedportals.reference.Settings;

@Mod(modid=Reference.MOD_ID, name=Reference.MOD_NAME, version=Reference.MOD_VERSION)
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels={Reference.MOD_ID}, packetHandler=PacketHandler.class)
public class EnhancedPortals
{
    @Instance(Reference.MOD_ID)
    public static EnhancedPortals instance;
    
    @SidedProxy(clientSide=Reference.CLIENT_PROXY, serverSide=Reference.COMMON_PROXY)
    public static CommonProxy proxy;
    
    @Init
    private void init(FMLInitializationEvent event)
    {
        Localizations.loadLocales();
        
        BlockHandler.init();
        ItemHandler.init();
        TileEntityHandler.init();
    }
    
    @PreInit
    private void preInit(FMLPreInitializationEvent event)
    {
        Log.setLogger(event.getModLog());
        Settings.loadSettings(event.getSuggestedConfigurationFile());
    }
    
    @ServerStarting
    private void serverStarting(FMLServerStartingEvent event)
    {
        // load networks
    }
}
