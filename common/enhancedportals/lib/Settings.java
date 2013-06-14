package enhancedportals.lib;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.Configuration;

public class Settings
{
    public static Configuration config;

    public static boolean AllowPortalModifiers = true;
    public static boolean AllowDialHomeDevices = true;
    public static boolean AllowObsidianStairs = true;
    public static boolean AllowTeleporting = true;
    public static boolean AllowFlintSteel = true;
    public static boolean DisableModifierRecipe = false;
    public static boolean DisableDDRecipe = false;
    public static boolean RenderPortalEffect = true;
    public static boolean AllowPortalColours = true;
    public static boolean AdventureModeLimitation = true;

    public static int PigmenLevel = 100;
    public static int SoundLevel = 100;
    public static int ParticleLevel = 100;
    public static int MinimumPortalSize = 0;
    public static int MaximumPortalSize = 0;

    public static List<Integer> ExcludedBlockList = new ArrayList<Integer>();
    public static List<Integer> BorderBlocks = new ArrayList<Integer>();
    public static List<Integer> DestroyBlocks = new ArrayList<Integer>();

    public static void addBorderBlocks(String str)
    {
        if (str.contains(" "))
        {
            String[] strs = str.split(" ");

            for (String s : strs)
            {
                try
                {
                    int i = Integer.parseInt(s);

                    if (!BorderBlocks.contains(i))
                    {
                        BorderBlocks.add(i);
                    }
                }
                catch (Exception e)
                {
                    Reference.log.log(Level.WARNING, String.format("'%s' isn't a valid block ID", s));
                    continue;
                }
            }
        }
        else if (str.length() > 0)
        {
            try
            {
                int i = Integer.parseInt(str);

                if (!BorderBlocks.contains(i))
                {
                    BorderBlocks.add(i);
                }
            }
            catch (Exception e)
            {
                Reference.log.log(Level.WARNING, String.format("'%s' isn't a valid block ID", str));
            }
        }
    }

    public static void addDestroyBlocks(String str)
    {
        if (str.contains(" "))
        {
            String[] strs = str.split(" ");

            for (String s : strs)
            {
                try
                {
                    int i = Integer.parseInt(s);

                    if (!DestroyBlocks.contains(i))
                    {
                        DestroyBlocks.add(i);
                    }
                }
                catch (Exception e)
                {
                    Reference.log.log(Level.WARNING, String.format("'%s' isn't a valid block ID", s));
                    continue;
                }
            }
        }
        else if (str.length() > 0)
        {
            try
            {
                int i = Integer.parseInt(str);

                if (!DestroyBlocks.contains(i))
                {
                    DestroyBlocks.add(i);
                }
            }
            catch (Exception e)
            {
                Reference.log.log(Level.WARNING, String.format("'%s' isn't a valid block ID", str));
            }
        }
    }

    public static void addExcludedBlocks(String str)
    {
        if (str.contains(" "))
        {
            String[] strs = str.split(" ");

            for (String s : strs)
            {
                try
                {
                    int i = Integer.parseInt(s);

                    if (!ExcludedBlockList.contains(i))
                    {
                        ExcludedBlockList.add(i);
                    }
                }
                catch (Exception e)
                {
                    Reference.log.log(Level.WARNING, String.format("'%s' isn't a valid block ID", s));
                    continue;
                }
            }
        }
        else if (str.length() > 0)
        {
            try
            {
                int i = Integer.parseInt(str);

                if (!ExcludedBlockList.contains(i))
                {
                    ExcludedBlockList.add(i);
                }
            }
            catch (Exception e)
            {
                Reference.log.log(Level.WARNING, String.format("'%s' isn't a valid block ID", str));
            }
        }
    }

    public static boolean canUse(EntityPlayer player)
    {
        return player.capabilities.allowEdit && AdventureModeLimitation;
    }

    public static boolean isBlockExcluded(int id)
    {
        for (int i : ExcludedBlockList)
        {
            if (i == id)
            {
                return true;
            }
        }

        return false;
    }

    public static void setConfigOption(String category, String property, boolean val)
    {
        config.load();

        if (config.getCategoryNames().contains(category))
        {
            if (config.getCategory(category).containsKey(property))
            {
                config.getCategory(category).get(property).set(val);
            }
        }

        config.save();
    }

    public static void setConfigOption(String category, String property, int val)
    {
        config.load();

        if (config.getCategoryNames().contains(category))
        {
            if (config.getCategory(category).containsKey(property))
            {
                config.getCategory(category).get(property).set(val);
            }
        }

        config.save();
    }
}
