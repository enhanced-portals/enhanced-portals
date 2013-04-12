package com.alz.enhancedportals.reference;

public class Strings
{
    public static class Block
    {
        public static final String DIAL_HOME_DEVICE_NAME = "dialHomeDevice";
        public static final String PORTAL_MODIFIER_NAME = "portalModifier";
        public static final String OBSIDIAN_STAIRS_NAME = "stairsObsidian";

        public static final String NETHER_PORTAL_ICON = Reference.MOD_ID + ":netherPortal_%s";
        public static final String PORTAL_MODIFIER_ICON = Reference.MOD_ID + ":" + PORTAL_MODIFIER_NAME + "_active_%s";

        public static String getFullTileName(String string)
        {
            return "tile." + string + ".name";
        }
    }

    public static class Gui
    {

    }

    public static class Item
    {

    }

    public static final String LANGUAGE_DIRECTORY = "/mods/" + Reference.MOD_ID + "/lang/";
    public static final String GUI_DIRECTORY = "/mods/" + Reference.MOD_ID + "/textures/gui/";

}
