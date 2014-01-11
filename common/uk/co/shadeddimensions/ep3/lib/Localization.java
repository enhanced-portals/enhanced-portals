package uk.co.shadeddimensions.ep3.lib;

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
        if (s.equals("inventory"))
        {
            return StatCollector.translateToLocal("container.inventory");
        }

        return StatCollector.translateToLocal(Reference.SHORT_ID + ".gui." + s);
    }

    public static String getItemString(String s)
    {
        return StatCollector.translateToLocal(Reference.SHORT_ID + ".item." + s);
    }
}
