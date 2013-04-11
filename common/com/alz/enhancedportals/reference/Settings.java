package com.alz.enhancedportals.reference;

import java.io.File;

import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Settings
{
    public static class Common
    {
        public static final boolean CAN_TELEPORT_DEFAULT = true;
        public static final boolean CAN_DYE_PORTALS_DEFAULT = true;
        public static final boolean CAN_DYE_PORTALS_BY_THROWING_DYE_DEFAULT = true;
        public static final boolean PORTAL_MODIFIERS_DEFAULT = true;
        public static final boolean OBSIDIAN_STAIRS_DEFAULT = true;
        public static final boolean DIAL_HOME_DEVICE_DEFAULT = true;
        
        // common settings
        
        private static void loadSettings()
        {
            
        }
    }
    
    @SideOnly(Side.SERVER)
    public static class Server
    {
        // server-only settings
        
        private static void loadSettings()
        {
            
        }
    }
    
    @SideOnly(Side.CLIENT)
    public static class Client
    {
        // client settings
        
        private static void loadSettings()
        {
            
        }
    }
    
    private static Configuration config;
    public static String CATEGORY_BLOCK = "Blocks";
    public static String CATEGORY_ITEM = "Items";
    public static String CATEGORY_GENERAL = "General";
    
    public static void loadSettings(File configFile)
    {
        config = new Configuration(configFile);
        
        config.load();
        Common.loadSettings();
        
        switch (FMLCommonHandler.instance().getSide())
        {
            case CLIENT:
                Client.loadSettings();
                break;
            case SERVER:
                Server.loadSettings();
                break;
            case BUKKIT:
                break;
            default:
                break;
        }
        
        config.save();
    }
}
