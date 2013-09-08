package uk.co.shadeddimensions.enhancedportals.util;

import java.util.ArrayList;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GlyphManager
{
    public static ArrayList<ItemStack> Glyphs = new ArrayList<ItemStack>();
    
    static
    {
        Glyphs.add(new ItemStack(Item.diamond, 0));
        Glyphs.add(new ItemStack(Item.emerald, 0));
        Glyphs.add(new ItemStack(Item.goldNugget, 0));
        Glyphs.add(new ItemStack(Item.redstone, 0));
        Glyphs.add(new ItemStack(Item.ingotIron, 0));
        Glyphs.add(new ItemStack(Item.glowstone, 0));
        Glyphs.add(new ItemStack(Item.netherQuartz, 0));
        Glyphs.add(new ItemStack(Item.bucketLava, 0));
        Glyphs.add(new ItemStack(Item.dyePowder, 0, 4));

        Glyphs.add(new ItemStack(Item.appleGold, 0));
        Glyphs.add(new ItemStack(Item.blazeRod, 0));
        Glyphs.add(new ItemStack(Item.slimeBall, 0));
        Glyphs.add(new ItemStack(Item.goldenCarrot, 0));
        Glyphs.add(new ItemStack(Item.enderPearl, 0));
        Glyphs.add(new ItemStack(Item.fireballCharge, 0));
        Glyphs.add(new ItemStack(Item.netherStar, 0));
        Glyphs.add(new ItemStack(Item.ghastTear, 0));
        Glyphs.add(new ItemStack(Item.magmaCream, 0));

        Glyphs.add(new ItemStack(Item.eyeOfEnder, 0));
        Glyphs.add(new ItemStack(Item.firework, 0));
        Glyphs.add(new ItemStack(Item.ingotGold, 0));
        Glyphs.add(new ItemStack(Item.pickaxeDiamond, 0));
        Glyphs.add(new ItemStack(Item.gunpowder, 0));
        Glyphs.add(new ItemStack(Item.pocketSundial, 0));
        Glyphs.add(new ItemStack(Item.writableBook, 0));
        Glyphs.add(new ItemStack(Item.potion, 0, 5));
        Glyphs.add(new ItemStack(Item.cake, 0));
    }
}
