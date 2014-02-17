package uk.co.shadeddimensions.ep3.lib;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

public class Localization
{
    public static String getBlockString(String s)
    {
        return StatCollector.translateToLocal(Reference.SHORT_ID + ".block." + s);
    }

    public static String getChatString(String s)
    {
        return StatCollector.translateToLocal(Reference.SHORT_ID + ".chat." + s);
    }

    public static String getGuiString(String s)
    {
        return StatCollector.translateToLocal(Reference.SHORT_ID + ".gui." + s).replace("<N>", "\n").replace("<MODVERSION>", Reference.VERSION);
    }

    public static String getItemString(String s)
    {
        return StatCollector.translateToLocal(Reference.SHORT_ID + ".item." + s);
    }

    public static String getErrorString(String s)
    {
        return EnumChatFormatting.RED + StatCollector.translateToLocal(Reference.SHORT_ID + ".error.prefix") + EnumChatFormatting.WHITE + StatCollector.translateToLocal(Reference.SHORT_ID + ".error." + s);
    }

    public static String getSuccessString(String s)
    {
        return EnumChatFormatting.GREEN + StatCollector.translateToLocal(Reference.SHORT_ID + ".success.prefix") + EnumChatFormatting.WHITE + StatCollector.translateToLocal(Reference.SHORT_ID + ".success." + s);
    }

    public static String getConfigString(String s)
    {
        return StatCollector.translateToLocal(Reference.SHORT_ID + ".config." + s);
    }
}
