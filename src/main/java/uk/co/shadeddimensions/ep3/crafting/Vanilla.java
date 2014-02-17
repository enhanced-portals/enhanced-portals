package uk.co.shadeddimensions.ep3.crafting;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import uk.co.shadeddimensions.ep3.block.BlockDecoration;
import uk.co.shadeddimensions.ep3.block.BlockFrame;
import uk.co.shadeddimensions.ep3.block.BlockStabilizer;
import uk.co.shadeddimensions.ep3.item.ItemEntityCard;
import uk.co.shadeddimensions.ep3.item.ItemGoggles;
import uk.co.shadeddimensions.ep3.item.ItemGuide;
import uk.co.shadeddimensions.ep3.item.ItemHandheldScanner;
import uk.co.shadeddimensions.ep3.item.ItemLocationCard;
import uk.co.shadeddimensions.ep3.item.ItemMisc;
import uk.co.shadeddimensions.ep3.item.ItemPaintbrush;
import uk.co.shadeddimensions.ep3.item.ItemPortalModule;
import uk.co.shadeddimensions.ep3.item.ItemUpgrade;
import uk.co.shadeddimensions.ep3.item.ItemWrench;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import cpw.mods.fml.common.registry.GameRegistry;

public class Vanilla
{
    public static void registerRecipes()
    {
        if (!CommonProxy.disableVanillaRecipes)
        {
            // Frames
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockFrame.instance, 4, 0), new Object[] { "SIS", "IQI", "SIS", 'S', Block.stone, 'Q', Block.blockNetherQuartz, 'I', Item.ingotIron }));

            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockFrame.instance, 1, BlockFrame.REDSTONE_INTERFACE), new Object[] { " R ", "RFR", " R ", 'F', new ItemStack(BlockFrame.instance, 1, 0), 'R', Item.redstone }));
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockFrame.instance, 1, BlockFrame.NETWORK_INTERFACE), new ItemStack(BlockFrame.instance, 1, 0), Item.enderPearl));

            // In-Place Upgrades
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemUpgrade.instance, 1, 0), new Object[] { " R ", "RFR", " R ", 'F', new ItemStack(ItemMisc.instance, 1, 1), 'R', Item.redstone }));
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemUpgrade.instance, 1, 1), new ItemStack(ItemMisc.instance, 1, 1), Item.enderPearl));

            // Stabilizer
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockStabilizer.instance, 6), new Object[] { "QPQ", "PDP", "QPQ", 'D', Item.diamond, 'Q', Block.blockIron, 'P', Item.enderPearl }));
        }

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockFrame.instance, 1, BlockFrame.PORTAL_CONTROLLER), new ItemStack(BlockFrame.instance, 1, 0), Item.diamond));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockFrame.instance, 1, BlockFrame.DIALLING_DEVICE), new ItemStack(BlockFrame.instance, 1, BlockFrame.NETWORK_INTERFACE), Item.diamond));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockFrame.instance, 1, BlockFrame.BIOMETRIC_IDENTIFIER), new Object[] { "PBC", "ZFZ", 'F', new ItemStack(BlockFrame.instance, 1, 0), 'Z', Item.blazePowder, 'P', Item.porkRaw, 'B', Item.beefRaw, 'C', Item.chickenRaw }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockFrame.instance, 1, BlockFrame.BIOMETRIC_IDENTIFIER), new Object[] { "PBC", "ZFZ", 'F', new ItemStack(BlockFrame.instance, 1, 0), 'Z', Item.blazePowder, 'P', Item.porkCooked, 'B', Item.beefCooked, 'C', Item.chickenCooked }));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockFrame.instance, 1, BlockFrame.MODULE_MANIPULATOR), new ItemStack(BlockFrame.instance, 1, 0), Item.diamond, Item.emerald, new ItemStack(ItemMisc.instance, 1, 0)));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockFrame.instance, 1, BlockFrame.TRANSFER_ENERGY), new ItemStack(BlockFrame.instance, 1, 0), Item.enderPearl, Item.diamond, Block.blockRedstone));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockFrame.instance, 1, BlockFrame.TRANSFER_FLUID), new ItemStack(BlockFrame.instance, 1, 0), Item.enderPearl, Item.diamond, Item.bucketEmpty));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockFrame.instance, 1, BlockFrame.TRANSFER_ITEM), new ItemStack(BlockFrame.instance, 1, 0), Item.enderPearl, Item.diamond, Block.chest));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemUpgrade.instance, 1, 2), new ItemStack(ItemUpgrade.instance, 1, 1), Item.diamond));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemUpgrade.instance, 1, 3), new Object[] { "PBC", "ZFZ", 'F', new ItemStack(ItemMisc.instance, 1, 1), 'Z', Item.blazePowder, 'P', Item.porkRaw, 'B', Item.beefRaw, 'C', Item.chickenRaw }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemUpgrade.instance, 1, 3), new Object[] { "PBC", "ZFZ", 'F', new ItemStack(ItemMisc.instance, 1, 1), 'Z', Item.blazePowder, 'P', Item.porkCooked, 'B', Item.beefCooked, 'C', Item.chickenCooked }));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemUpgrade.instance, 1, 4), new ItemStack(ItemMisc.instance, 1, 1), Item.diamond, Item.emerald, new ItemStack(ItemMisc.instance, 1, 0)));
        
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemUpgrade.instance, 1, 7), new ItemStack(ItemMisc.instance, 1, 1), Item.enderPearl, Item.diamond, Block.blockRedstone));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemUpgrade.instance, 1, 5), new ItemStack(ItemMisc.instance, 1, 1), Item.enderPearl, Item.diamond, Item.bucketEmpty));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemUpgrade.instance, 1, 6), new ItemStack(ItemMisc.instance, 1, 1), Item.enderPearl, Item.diamond, Block.chest));

        // Handheld Scanner
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemHandheldScanner.instance), new Object[] { "GRG", "IQI", "IEI", 'G', Item.ingotGold, 'I', Item.ingotIron, 'R', Item.redstone, 'Q', Item.netherQuartz, 'E', ItemEntityCard.instance }));

        // Decoration
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockDecoration.instance, 8, 0), new Object[] { "SQS", "QQQ", "SQS", 'S', Block.stone, 'Q', Block.blockNetherQuartz }));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BlockDecoration.instance, 10, 1), Block.blockIron, Item.ingotIron, Item.ingotIron, Item.ingotIron, Item.ingotIron));

        // Blank stuff
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemMisc.instance, 1, 0), true, new Object[] { "NNN", "NIN", "NNN", 'I', Item.ingotIron, 'N', Item.goldNugget })); // Blank Portal Module
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemMisc.instance, 8, 1), new Object[] { "D", "P", "R", 'P', Item.paper, 'D', Item.diamond, 'R', "dyeRed" })); // Blank Upgrade

        // Portal Modules
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemPortalModule.instance, 1, 0), new Object[] { "RXG", 'X', new ItemStack(ItemMisc.instance, 1, 0), 'R', Item.redstone, 'G', Item.gunpowder })); // Particle Destroyer
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemPortalModule.instance, 1, 1), new Object[] { "RGB", " X ", "BGR", 'X', new ItemStack(ItemMisc.instance, 1, 0), 'R', "dyeRed", 'B', "dyeBlue", 'G', "dyeGreen" })); // Rainbow particles
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemPortalModule.instance, 1, 2), new Object[] { "RXN", 'X', new ItemStack(ItemMisc.instance, 1, 0), 'R', Item.redstone, 'N', Block.music })); // Portal Silencer
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemPortalModule.instance, 1, 3), new Object[] { "AXF", 'X', new ItemStack(ItemMisc.instance, 1, 0), 'A', Block.anvil, 'F', Item.feather })); // Momentum
        // 4 - Portal Cloaking - does not have a recipe
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemPortalModule.instance, 1, 5), new Object[] { "BXI", 'X', new ItemStack(ItemMisc.instance, 1, 0), 'B', "dyeWhite", 'I', "dyeBlack" })); // Particle Shader
        // 6 - Ethereal Frame - removed
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemPortalModule.instance, 1, 7), new Object[] { "FFF", "FXF", "FFF", 'X', new ItemStack(ItemMisc.instance, 1, 0), 'F', Item.feather })); // Featherfall

        // Guide Book
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemGuide.instance), new ItemStack(Item.book), new ItemStack(ItemLocationCard.instance)));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ItemGuide.instance), new ItemStack(Item.book), new ItemStack(ItemEntityCard.instance)));

        // Nanobrush
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemPaintbrush.instance), new Object[] { "WT ", "TS ", "  S", 'W', Block.cloth, 'T', Item.silk, 'S', "stickWood" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemPaintbrush.instance), new Object[] { " TW", " ST", "S  ", 'W', Block.cloth, 'T', Item.silk, 'S', "stickWood" }));

        // Location Card
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemLocationCard.instance, 16), new Object[] { "IPI", "PPP", "IDI", 'I', Item.ingotIron, 'P', Item.paper, 'D', "dyeBlue" }));

        // Entity Card
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemEntityCard.instance, 8), new Object[] { "GPG", "PPP", "GDG", 'G', Item.ingotGold, 'P', Item.paper, 'D', "dyeLime" }));

        // Wrench
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemWrench.instance), new Object[] { "I I", " Q ", " I ", 'I', Item.ingotIron, 'Q', Item.netherQuartz }));

        // Glasses
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemGoggles.instance), true, new Object[] { "R B", "GLG", "L L", 'R', "dyeRed", 'B', "dyeCyan", 'G', Block.thinGlass, 'L', Item.leather }));
    }
}
