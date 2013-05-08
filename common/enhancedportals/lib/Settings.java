package enhancedportals.lib;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;

public class Settings
{
    public static Configuration config;
    
    public static boolean AllowPortalModifiers = true;
    public static boolean AllowDialHomeDevices = true;

    public static boolean DisableModifierRecipe = false;
    public static boolean DisableDHDRecipe = false;

    public static boolean RenderPortalEffect = true;
    public static boolean AllowPortalColours = true;
    
    public static int PigmenLevel = 100;
    public static int SoundLevel = 100;
    public static int ParticleLevel = 100;
    
    public static int[] ExcludedBlockList = new int[] { Block.torchWood.blockID };
    public static int[] ValidItemsList = new int[] { Item.bucketLava.itemID, Item.bucketWater.itemID, Item.dyePowder.itemID };
    public static Map<String, PortalTexture> ItemPortalTextureMap = new HashMap<String, PortalTexture>();

    public static PortalTexture getPortalTextureFromItem(ItemStack stack, PortalTexture texture)
    {
        PortalTexture text = null;

        text = ItemPortalTextureMap.get(stack.itemID + ":" + stack.getItemDamage());

        if (texture.isEqualTo(text))
        {
            return ItemPortalTextureMap.get(stack.itemID + ":" + stack.getItemDamage() + "_");
        }

        return text;
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

    public static boolean isValidItem(int itemID)
    {
        for (int i : ValidItemsList)
        {
            if (i == itemID)
            {
                return true;
            }
        }

        return false;
    }
}
