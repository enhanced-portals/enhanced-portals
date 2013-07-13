package enhancedportals.lib;

import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;
import enhancedportals.portal.PortalTexture;

public class Textures
{
    public enum Colour
    {
        BLACK,
        RED,
        GREEN,
        BROWN,
        BLUE,
        PURPLE,
        CYAN,
        LIGHT_GRAY,
        GRAY,
        PINK,
        LIME,
        YELLOW,
        LIGHT_BLUE,
        MAGENTA,
        ORANGE,
        WHITE;

        public PortalTexture getPortalTexture()
        {
            return getTexture("C:" + ordinal());
        }
    }

    public static Map<String, PortalTexture> portalTextureMap = new HashMap<String, PortalTexture>();
    
    public static ItemStack getItemStackFromTexture(PortalTexture texture)
    {
        ItemStack stack = null;

        if (texture.getID().startsWith("B:") || texture.getID().startsWith("I:") || texture.getID().startsWith("F:"))
        {
            return new ItemStack(Integer.parseInt(texture.getID().substring(2).split(":")[0]), 1, Integer.parseInt(texture.getID().substring(2).split(":")[1]));
        }
        else if (texture.getID().startsWith("C:"))
        {
            return new ItemStack(BlockIds.DummyPortal, 1, Integer.parseInt(texture.getID().substring(2)));
        }

        return stack;
    }

    public static ItemStack getItemStackFromTexture(String texture)
    {
        return getItemStackFromTexture(getTexture(texture));
    }

    public static PortalTexture getTexture(String string)
    {
        if (portalTextureMap.containsKey(string))
        {
            return portalTextureMap.get(string);
        }
        else if (string.startsWith("B:") || string.startsWith("F:"))
        {
            return new PortalTexture(string);
        }
        else
        {
            return portalTextureMap.get("C:5");
        }
    }

    public static PortalTexture getTextureFromItemStack(ItemStack stack)
    {
        return getTextureFromItemStack(stack, "NOTSET");
    }

    public static PortalTexture getTextureFromItemStack(ItemStack stack, String oldTexture)
    {
        if (stack == null)
        {
            return null;
        }

        if (stack.getItemName().startsWith("tile."))
        {
            if (Settings.isBlockExcluded(stack.itemID))
            {
                return null;
            }

            if (getTexture("B:" + stack.itemID + ":" + stack.getItemDamage()).getID().equals(oldTexture))
            {
                if (getTexture("B:" + stack.itemID + ":" + stack.getItemDamage() + "_") != null)
                {
                    return getTexture("B:" + stack.itemID + ":" + stack.getItemDamage() + "_");
                }
                else
                {
                    return null;
                }
            }
            else
            {
                return getTexture("B:" + stack.itemID + ":" + stack.getItemDamage());
            }
        }
        else
        {
            if (getDyeColour(stack) >= 0)
            {
                return getTexture("C:" + getDyeColour(stack));
            }
            else if (portalTextureMap.containsKey("I:" + stack.itemID + ":" + stack.getItemDamage()))
            {
                if (getTexture("I:" + stack.itemID + ":" + stack.getItemDamage()).getID().equals(oldTexture))
                {
                    if (getTexture("I:" + stack.itemID + ":" + stack.getItemDamage() + "_") != null)
                    {
                        return getTexture("I:" + stack.itemID + ":" + stack.getItemDamage() + "_");
                    }
                    else
                    {
                        return null;
                    }
                }
                else
                {
                    return getTexture("I:" + stack.itemID + ":" + stack.getItemDamage());
                }
            }
            else if (FluidContainerRegistry.isFilledContainer(stack))
            {
            	return getTexture("F:" + stack.itemID + ":" + stack.getItemDamage());
            }
        }

        return null;
    }
        
    public static int getDyeColour(ItemStack stack)
    {
        if (stack != null)
        {
            String name = OreDictionary.getOreName(OreDictionary.getOreID(stack));
            
            String[] dyes =
                {
                    "dyeBlack",
                    "dyeRed",
                    "dyeGreen",
                    "dyeBrown",
                    "dyeBlue",
                    "dyePurple",
                    "dyeCyan",
                    "dyeLightGray",
                    "dyeGray",
                    "dyePink",
                    "dyeLime",
                    "dyeYellow",
                    "dyeLightBlue",
                    "dyeMagenta",
                    "dyeOrange",
                    "dyeWhite"
                };
            
            if (name.startsWith("dye"))
            {
                for (int i = 0; i < dyes.length; i++)
                {
                    if (name.equals(dyes[i]))
                    {
                        return i;
                    }
                }
            }
        }
        
        return -1;
    }
}
