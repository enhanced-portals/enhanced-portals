package alz.mods.enhancedportals.reference;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import alz.mods.enhancedportals.server.ServerHandler;

public class Reference
{
    /*** Block IDs ***/
    public static class BlockIDs
    {
        public static final int ObsidianStairs_Default = 1345;
        public static final int PortalModifier_Default = 1346;
        public static final int DialDevice_Default = 1347;
        public static final int NetherPortal_Default = 1349;
        public static final int Obsidian_Default = 49;

        public static int ObsidianStairs;
        public static int PortalModifier;
        public static int Obsidian = Obsidian_Default;
        public static int NetherPortal = NetherPortal_Default;
        public static int DialDevice;
    }

    /*** Contains everything related to I/O ***/
    public static class File
    {
        public static final String GuiTextureDirectory = "/mods/" + MOD_ID + "/textures/gui/";
        public static final String LanguageDirectory = "/mods/" + MOD_ID + "/lang/";
    }

    public static class GuiIDs
    {
        public static final int PortalModifier = 0;
        public static final int DialDevice = 1;
        public static final int DialDeviceAdd = 2;
    }

    /*** Item IDs ***/
    public static class ItemIDs
    {
        public static final int PortalModifierUpgrade_Default = 6000;
        public static final int MiscItems_Default = 6001;
        public static final int ItemScroll_Default = 6002;

        public static int PortalModifierUpgrade;
        public static int MiscItems;
        public static int ItemScroll;
    }

    public static class Networking
    {
        public static final int TileEntityUpdate = 0;
        public static final int DataRequest = 1;
        public static final int GuiRequest = 2;
        public static final int DialDevice_NewPortalData = 5;
        public static final int DialDevice_AllPortalData = 6;
        public static final int DialDevice_GenericData = 7;
        public static final int DD_Select = 1;
        public static final int DD_Remove = 2;
        public static final int DD_RemoveSelect = 3;
    }

    public static final String MOD_NAME = "Enhanced Portals";
    public static final String MOD_ID = "enhancedportals";
    public static final String MOD_VERSION = "2.1.2";

    public static final String PROXY_CLIENT = "alz.mods.enhancedportals.client.ClientProxy";

    public static final String PROXY_COMMON = "alz.mods.enhancedportals.common.CommonProxy";

    public static ServerHandler ServerHandler;

    public static Block ComputercraftComputer;

    public static Logger Logger;

    public static void LoadLanguage()
    {
        Localizations.loadLocales();
    }

    /*** Logs data ***/
    public static void LogData(String data)
    {
        Logger.log(Level.INFO, data);
    }

    /*** Logs on server only, send world.isRemote ***/
    public static void LogData(String data, boolean test)
    {
        if (!test)
        {
            Logger.log(Level.INFO, data);
        }
    }
}
