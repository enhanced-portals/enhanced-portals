package enhancedportals.lib;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Settings
{
    public static boolean AllowPortalModifiers;
    public static boolean AllowDialHomeDevices;

    public static boolean DisableModifierRecipe;
    public static boolean DisableDHDRecipe;

    public static int PigmenLevel;

    public static int SoundLevel = 10;
    public static int ParticleLevel = 75;
    public static boolean RenderPortalEffect = false;
    public static boolean AllowPortalColours = true;
    
    public static int[] ExcludedBlockList = new int[] { Block.torchWood.blockID };
    public static int[] ValidItemsList = new int[] { Item.bucketLava.itemID, Item.bucketWater.itemID };
    public static Map<String, PortalTexture> ItemPortalTextureMap = new HashMap<String, PortalTexture>();
    
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
}
