package com.alz.enhancedportals.reference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Settings
{
    @SideOnly(Side.CLIENT)
    // The server will never need these.
    public static class Client
    {
        public static final boolean RENDER_PORTAL_EFFECT_DEFAULT = true;
        public static final int SOUND_LEVEL_DEFAULT = 100;
        public static final int PARTICLE_LEVEL_DEFAULT = 100;

        public static boolean RENDER_PORTAL_EFFECT;
        public static int SOUND_LEVEL;
        public static int PARTICLE_LEVEL;

        private static void loadSettings()
        {
            RENDER_PORTAL_EFFECT = config.get(CATEGORY_CLIENT, "RenderPortalEffect", RENDER_PORTAL_EFFECT_DEFAULT).getBoolean(RENDER_PORTAL_EFFECT_DEFAULT);
            SOUND_LEVEL = config.get(CATEGORY_CLIENT, "SoundLevel", SOUND_LEVEL_DEFAULT).getInt();
            PARTICLE_LEVEL = config.get(CATEGORY_CLIENT, "ParticleLevel", PARTICLE_LEVEL_DEFAULT).getInt();
        }
    }

    public static class Common
    {
        public static final boolean PORTAL_MODIFIERS_DEFAULT = true;
        public static final boolean OBSIDIAN_STAIRS_DEFAULT = true;
        public static final boolean DIAL_HOME_DEVICE_DEFAULT = true;

        public static boolean PORTAL_MODIFIERS;
        public static boolean OBSIDIAN_STAIRS;
        public static boolean DIAL_HOME_DEVICE;

        private static void loadSettings()
        {
            PORTAL_MODIFIERS = config.get(CATEGORY_GENERAL, "PortalModifiersEnabled", PORTAL_MODIFIERS_DEFAULT).getBoolean(PORTAL_MODIFIERS_DEFAULT);
            OBSIDIAN_STAIRS = config.get(CATEGORY_GENERAL, "ObsidianStairsEnabled", OBSIDIAN_STAIRS_DEFAULT).getBoolean(OBSIDIAN_STAIRS_DEFAULT);
            DIAL_HOME_DEVICE = config.get(CATEGORY_GENERAL, "DialHomeDeviceEnabled", DIAL_HOME_DEVICE_DEFAULT).getBoolean(DIAL_HOME_DEVICE_DEFAULT);

            BlockIds.DIAL_HOME_DEVICE = config.get(CATEGORY_BLOCK, "DialHomeDeviceBlockID", BlockIds.DIAL_HOME_DEVICE_DEFAULT).getInt();
            BlockIds.NETHER_PORTAL = config.get(CATEGORY_BLOCK, "NetherPortalBlockID", BlockIds.NETHER_PORTAL_DEFAULT).getInt();
            BlockIds.OBSIDIAN_STAIRS = config.get(CATEGORY_BLOCK, "ObsidianStairsBlockID", BlockIds.OBSIDIAN_STAIRS_DEFAULT).getInt();
            BlockIds.PORTAL_MODIFIER = config.get(CATEGORY_BLOCK, "PortalModifierBlockID", BlockIds.PORTAL_MODIFIER_DEFAULT).getInt();

            ItemIds.PORTAL_MODIFIER_UPGRADE = config.get(CATEGORY_ITEM, "PortalModifierUpgradeItemID", ItemIds.PORTAL_MODIFIER_UPGRADE_DEFAULT).getInt();
            ItemIds.TELEPORTATION_SCROLL = config.get(CATEGORY_ITEM, "TeleportationScrollItemID", ItemIds.TELEPORTATION_SCROLL).getInt();
        }
    }

    public static class Server // The client will need these for SP
    {
        public static final boolean CAN_TELEPORT_DEFAULT = true;
        public static final boolean CAN_DYE_PORTALS_DEFAULT = true;
        public static final boolean CAN_DYE_PORTALS_BY_THROWING_DYE_DEFAULT = true;
        public static final int PIGMEN_SPAWN_CHANCE_DEFAULT = 100;
        public static final boolean PRINT_PORTAL_MESSAGES_DEFAULT = true;
        public static final String REMOVABLE_BLOCKS_DEFAULT = "";
        public static final String BORDER_BLOCKS_DEFAULT = "8,9,10,11,6,18,30,31,32,37,38,39,40,78,104,105,106,111,115";
        public static final boolean DOES_DYING_COST_DEFAULT = true;

        public static boolean CAN_TELEPORT;
        public static boolean CAN_DYE_PORTALS;
        public static boolean CAN_DYE_PORTALS_BY_THROWING_DYE;
        public static int PIGMEN_SPAWN_CHANCE;
        public static boolean PRINT_PORTAL_MESSAGES;
        public static List<Integer> REMOVABLE_BLOCKS = new ArrayList<Integer>();
        public static List<Integer> BORDER_BLOCKS = new ArrayList<Integer>();
        public static boolean DOES_DYING_COST;

        private static void loadSettings()
        {
            CAN_TELEPORT = config.get(CATEGORY_SERVER, "TeleportingEnabled", CAN_TELEPORT_DEFAULT).getBoolean(CAN_TELEPORT_DEFAULT);
            CAN_DYE_PORTALS = config.get(CATEGORY_SERVER, "CanDyePortals", CAN_DYE_PORTALS_DEFAULT).getBoolean(CAN_DYE_PORTALS_DEFAULT);
            CAN_DYE_PORTALS_BY_THROWING_DYE = config.get(CATEGORY_SERVER, "CanDyePortalsByThrowingDye", CAN_DYE_PORTALS_BY_THROWING_DYE_DEFAULT).getBoolean(CAN_DYE_PORTALS_BY_THROWING_DYE_DEFAULT);
            PIGMEN_SPAWN_CHANCE = config.get(CATEGORY_SERVER, "PigmenSpawnChance", PIGMEN_SPAWN_CHANCE_DEFAULT).getInt();
            PRINT_PORTAL_MESSAGES = config.get(CATEGORY_SERVER, "PrintPortalMessages", PRINT_PORTAL_MESSAGES_DEFAULT).getBoolean(PRINT_PORTAL_MESSAGES_DEFAULT);
            DOES_DYING_COST = config.get(CATEGORY_SERVER, "DoesDyingCost", DOES_DYING_COST_DEFAULT).getBoolean(DOES_DYING_COST_DEFAULT);

            String BorderBlocks = config.get(CATEGORY_SERVER, "CustomBorderBlocks", REMOVABLE_BLOCKS_DEFAULT).getString(), RemovableBlocks = config.get(CATEGORY_SERVER, "CustomDestroyBlocks", BORDER_BLOCKS_DEFAULT).getString();

            // Add border blocks to list
            if (BorderBlocks.length() >= 1)
            {
                try
                {
                    if (BorderBlocks.contains(","))
                    {
                        String[] strs = BorderBlocks.split(",");

                        for (String str2 : strs)
                        {
                            BORDER_BLOCKS.add(Integer.parseInt(str2));
                        }
                    }
                    else
                    {
                        BORDER_BLOCKS.add(Integer.parseInt(BorderBlocks));
                    }
                }
                catch (Exception e)
                {
                }
            }

            // Add removable blocks to list
            if (RemovableBlocks.length() >= 1)
            {
                try
                {
                    if (RemovableBlocks.contains(","))
                    {
                        String[] strs = RemovableBlocks.split(",");

                        for (String str2 : strs)
                        {
                            REMOVABLE_BLOCKS.add(Integer.parseInt(str2));
                        }
                    }
                    else
                    {
                        REMOVABLE_BLOCKS.add(Integer.parseInt(RemovableBlocks));
                    }
                }
                catch (Exception e)
                {
                }
            }
        }
    }

    private static Configuration config;
    public static String CATEGORY_BLOCK = "Blocks";
    public static String CATEGORY_ITEM = "Items";
    public static String CATEGORY_GENERAL = "General";
    public static String CATEGORY_CLIENT = "Client";
    public static String CATEGORY_SERVER = "Server";

    public static void loadSettings(File configFile)
    {
        config = new Configuration(configFile);

        config.load();
        Common.loadSettings();
        Server.loadSettings();

        if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
        {
            Client.loadSettings();
        }

        config.save();
    }
}
