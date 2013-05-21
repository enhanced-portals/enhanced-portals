package enhancedportals.lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;
import enhancedportals.portal.Texture;

public class Textures
{
    public static List<IIconRegister> IconsToRegister = new ArrayList<IIconRegister>();
    //public static final int[] PARTICLE_COLOURS = { 2437522, 11743532, 3887386, 5320730, 1973019, 8073150, 2651799, 11250603, 4408131, 14188952, 4312372, 14602026, 6719955, 12801229, 15435844, 15790320, 15435844, 2437522, 15435844, 2437522, 0 };
    // public static Icon[] TEXTURE_ICONS = new Icon[16];
    //public static Icon[] PORTAL_MODIFIER_ICONS = new Icon[16];

    public static Map<String, Texture> textureMap = new HashMap<String, Texture>();

    public static ItemStack getItemStackFromTexture(String texture)
    {
        return getItemStackFromTexture(getTexture(texture));
    }

    public static ItemStack getItemStackFromTexture(Texture texture)
    {
        ItemStack stack = null;

        if (texture.getID().startsWith("B:") || texture.getID().startsWith("I:"))
        {
            return new ItemStack(Integer.parseInt(texture.getID().substring(2).split(":")[0]), 1, Integer.parseInt(texture.getID().substring(2).split(":")[1]));
        }
        else if (texture.getID().startsWith("C:"))
        {
            return new ItemStack(BlockIds.DummyPortal, 1, Integer.parseInt(texture.getID().substring(2)));
        }
        else if (texture.getID().startsWith("L:"))
        {
            return LiquidDictionary.getLiquid(texture.getID().substring(2), 1).asItemStack();
        }

        return stack;
    }

    public static Texture getTexture(String string)
    {
        if (textureMap.containsKey(string))
        {
            return textureMap.get(string);
        }
        else if (string.startsWith("B:"))
        {
            return new Texture(string);
        }
        else
        {
            return textureMap.get("C:5");
        }
    }

    public static Texture getTextureFromItemStack(ItemStack stack)
    {
        if (stack.getItemName().startsWith("tile."))
        {
            return getTexture("B:" + stack.itemID + ":" + stack.getItemDamage());
        }
        else
        {
            if (stack.itemID == Item.dyePowder.itemID)
            {
                return getTexture("C:" + stack.getItemDamage());
            }
            else if (textureMap.containsKey("I:" + stack.itemID + ":" + stack.getItemDamage()))
            {
                return getTexture("I:" + stack.itemID + ":" + stack.getItemDamage());
            }
            else
            {
                if (LiquidDictionary.findLiquidName(new LiquidStack(stack.itemID, 1, stack.getItemDamage())) != null)
                {
                    return getTexture("L:" + LiquidDictionary.findLiquidName(new LiquidStack(stack.itemID, 1, stack.getItemDamage())));
                }
            }
        }

        return getTexture("");
    }

    public static Texture getTextureFromItemStack(ItemStack stack, String oldTexture)
    {
        return getTextureFromItemStack(stack);
    }

    public static void registerTextures(IconRegister iconRegister)
    {
        for (int i = 0; i < 16; i++)
        {
            textureMap.put("C:" + i, new Texture("C:" + i, iconRegister.registerIcon(Reference.MOD_ID + ":netherPortal_" + i), iconRegister.registerIcon(Reference.MOD_ID + ":portalModifier_active_" + i), ItemDye.dyeColors[i]));
        }

        for (IIconRegister icon : IconsToRegister)
        {
            icon.registerIcons(iconRegister);
        }
    }
}
