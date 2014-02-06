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
        String str = StatCollector.translateToLocal(Reference.SHORT_ID + ".gui." + s);
        
        if (str != null)
        {
            if (str.contains("<N>"))
            {
                str = str.replace("<N>", "\n");
            }
            
            if (str.contains("<MODVERSION>") && Reference.VERSION != null)
            {
                str = str.replace("<MODVERSION>", Reference.VERSION);
            }
        }
        
        return str;
    }

    public static String getItemString(String s)
    {
        return StatCollector.translateToLocal(Reference.SHORT_ID + ".item." + s);
    }

    public static String getErrorString(String s)
    {
        return EnumChatFormatting.RED + StatCollector.translateToLocal(Reference.SHORT_ID + ".error.prefix") + EnumChatFormatting.WHITE + StatCollector.translateToLocal(Reference.SHORT_ID + ".error." + s);
    }
}
