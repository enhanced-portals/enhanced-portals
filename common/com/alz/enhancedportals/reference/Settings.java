package com.alz.enhancedportals.reference;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Settings
{
    public class Common
    {
        public static final boolean CAN_TELEPORT_DEFAULT = true;
        public static final boolean CAN_DYE_PORTALS_DEFAULT = true;
        public static final boolean CAN_DYE_PORTALS_BY_THROWING_DYE_DEFAULT = true;
        public static final boolean PORTAL_MODIFIERS_DEFAULT = true;
        public static final boolean OBSIDIAN_STAIRS_DEFAULT = true;
        public static final boolean DIAL_HOME_DEVICE_DEFAULT = true;
        
        // common settings
    }
    
    @SideOnly(Side.SERVER)
    public class Server
    {
        // server-only settings
    }
    
    @SideOnly(Side.CLIENT)
    public class Client
    {
        // client settings
    }
}
