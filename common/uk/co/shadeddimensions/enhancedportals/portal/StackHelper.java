package uk.co.shadeddimensions.enhancedportals.portal;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class StackHelper
{
    public static int getDyeColour(ItemStack stack)
    {
        String ore = OreDictionary.getOreName(OreDictionary.getOreID(stack));
        String[] dyes = { "dyeBlack", "dyeRed", "dyeGreen", "dyeBrown", "dyeBlue", "dyePurple", "dyeCyan", "dyeLightGray", "dyeGray", "dyePink", "dyeLime", "dyeYellow", "dyeLightBlue", "dyeMagenta", "dyeOrange", "dyeWhite" };

        for (int i = 0; i < dyes.length; i++)
        {
            if (ore.equals(dyes[i]))
            {
                return i;
            }
        }

        return 5;
    }

    public static boolean isItemStackValidForPortalFrameTexture(ItemStack stack)
    {
        if (stack != null)
        {
            if (stack.getItemSpriteNumber() == 0)
            {
                return Block.blocksList[stack.itemID].isOpaqueCube();
            }
        }

        return false;
    }

    public static boolean isItemStackValidForPortalTexture(ItemStack stack)
    {
        if (stack != null)
        {
            if (stack.getItemSpriteNumber() == 0)
            {
                return true;
            }
            else
            {
                if (FluidContainerRegistry.isFilledContainer(stack))
                {
                    return true;
                }
                else if (isStackDye(stack))
                {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isStackDye(ItemStack stack)
    {
        return OreDictionary.getOreName(OreDictionary.getOreID(stack)).startsWith("dye");
    }

    public static boolean isUpgrade(ItemStack stack)
    {
        // TODO
        return true;
    }
}
