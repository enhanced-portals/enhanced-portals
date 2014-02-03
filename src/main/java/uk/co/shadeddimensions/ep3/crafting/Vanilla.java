package uk.co.shadeddimensions.ep3.crafting;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import uk.co.shadeddimensions.ep3.block.BlockFrame;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import cpw.mods.fml.common.registry.GameRegistry;

public class Vanilla
{
    public static void registerRecipes()
    {
        if (!CommonProxy.disableVanillaRecipes)
        {
            // Frames
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CommonProxy.blockFrame, 4, 0), new Object[] { "SIS", "IQI", "SIS", 'S', Block.stone, 'Q', Block.blockNetherQuartz, 'I', Item.ingotIron }));

            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CommonProxy.blockFrame, 1, BlockFrame.REDSTONE_INTERFACE), new Object[] { " R ", "RFR", " R ", 'F', new ItemStack(CommonProxy.blockFrame, 1, 0), 'R', Item.redstone }));
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(CommonProxy.blockFrame, 1, BlockFrame.NETWORK_INTERFACE), new ItemStack(CommonProxy.blockFrame, 1, 0), Item.enderPearl));

            // In-Place Upgrades
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CommonProxy.itemInPlaceUpgrade, 1, 0), new Object[] { " R ", "RFR", " R ", 'F', new ItemStack(CommonProxy.itemMisc, 1, 1), 'R', Item.redstone }));
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(CommonProxy.itemInPlaceUpgrade, 1, 1), new ItemStack(CommonProxy.itemMisc, 1, 1), Item.enderPearl));

            // Stabilizer
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CommonProxy.blockStabilizer, 6), new Object[] { "QPQ", "PDP", "QPQ", 'D', Item.diamond, 'Q', Block.blockIron, 'P', Item.enderPearl }));
        }

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(CommonProxy.blockFrame, 1, BlockFrame.PORTAL_CONTROLLER), new ItemStack(CommonProxy.blockFrame, 1, 0), Item.diamond));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(CommonProxy.blockFrame, 1, BlockFrame.DIALLING_DEVICE), new ItemStack(CommonProxy.blockFrame, 1, BlockFrame.NETWORK_INTERFACE), Item.diamond));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CommonProxy.blockFrame, 1, BlockFrame.BIOMETRIC_IDENTIFIER), new Object[] { "PBC", "ZFZ", 'F', new ItemStack(CommonProxy.blockFrame, 1, 0), 'Z', Item.blazePowder, 'P', Item.porkRaw, 'B', Item.beefRaw, 'C', Item.chickenRaw }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CommonProxy.blockFrame, 1, BlockFrame.BIOMETRIC_IDENTIFIER), new Object[] { "PBC", "ZFZ", 'F', new ItemStack(CommonProxy.blockFrame, 1, 0), 'Z', Item.blazePowder, 'P', Item.porkCooked, 'B', Item.beefCooked, 'C', Item.chickenCooked }));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(CommonProxy.blockFrame, 1, BlockFrame.MODULE_MANIPULATOR), new ItemStack(CommonProxy.blockFrame, 1, 0), Item.diamond, Item.emerald, new ItemStack(CommonProxy.itemMisc, 1, 0)));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(CommonProxy.itemInPlaceUpgrade, 1, 2), new ItemStack(CommonProxy.itemInPlaceUpgrade, 1, 1), Item.diamond));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CommonProxy.itemInPlaceUpgrade, 1, 3), new Object[] { "PBC", "ZFZ", 'F', new ItemStack(CommonProxy.itemMisc, 1, 1), 'Z', Item.blazePowder, 'P', Item.porkRaw, 'B', Item.beefRaw, 'C', Item.chickenRaw }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CommonProxy.itemInPlaceUpgrade, 1, 3), new Object[] { "PBC", "ZFZ", 'F', new ItemStack(CommonProxy.itemMisc, 1, 1), 'Z', Item.blazePowder, 'P', Item.porkCooked, 'B', Item.beefCooked, 'C', Item.chickenCooked }));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(CommonProxy.itemInPlaceUpgrade, 1, 4), new ItemStack(CommonProxy.itemMisc, 1, 1), Item.diamond, Item.emerald, new ItemStack(CommonProxy.itemMisc, 1, 0)));

        // Synchronizer
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CommonProxy.itemSynchronizer), new Object[] { "GGG", "IQI", "III", 'G', Item.ingotGold, 'I', Item.ingotIron, 'Q', Item.netherQuartz }));

        // Handheld Scanner
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CommonProxy.itemScanner), new Object[] { "GRG", "IQI", "IEI", 'G', Item.ingotGold, 'I', Item.ingotIron, 'R', Item.redstone, 'Q', Item.netherQuartz, 'E', CommonProxy.itemEntityCard }));

        // Decoration
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CommonProxy.blockDecoration, 8, 0), new Object[] { "SQS", "QQQ", "SQS", 'S', Block.stone, 'Q', Block.blockNetherQuartz }));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(CommonProxy.blockDecoration, 10, 1), Block.blockIron, Item.ingotIron, Item.ingotIron, Item.ingotIron, Item.ingotIron));

        // Blank stuff
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CommonProxy.itemMisc, 1, 0), true, new Object[] { "NNN", "NIN", "NNN", 'I', Item.ingotIron, 'N', Item.goldNugget })); // Blank Portal Module
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CommonProxy.itemMisc, 8, 1), new Object[] { "D", "P", "R", 'P', Item.paper, 'D', Item.diamond, 'R', "dyeRed" })); // Blank Upgrade

        // Portal Modules
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CommonProxy.itemPortalModule, 1, 0), new Object[] { "RXG", 'X', new ItemStack(CommonProxy.itemMisc, 1, 0), 'R', Item.redstone, 'G', Item.gunpowder })); // Particle Destroyer
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CommonProxy.itemPortalModule, 1, 1), new Object[] { "RGB", " X ", "BGR", 'X', new ItemStack(CommonProxy.itemMisc, 1, 0), 'R', "dyeRed", 'B', "dyeBlue", 'G', "dyeGreen" })); // Rainbow particles
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CommonProxy.itemPortalModule, 1, 2), new Object[] { "RXN", 'X', new ItemStack(CommonProxy.itemMisc, 1, 0), 'R', Item.redstone, 'N', Block.music })); // Portal Silencer
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CommonProxy.itemPortalModule, 1, 3), new Object[] { "AXF", 'X', new ItemStack(CommonProxy.itemMisc, 1, 0), 'A', Block.anvil, 'F', Item.feather })); // Momentum
        // 4 - Portal Cloaking - does not have a recipe
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CommonProxy.itemPortalModule, 1, 5), new Object[] { "BXI", 'X', new ItemStack(CommonProxy.itemMisc, 1, 0), 'B', "dyeWhite", 'I', "dyeBlack" })); // Particle Shader
        // 6 - Ethereal Frame - removed
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CommonProxy.itemPortalModule, 1, 7), new Object[] { "FFF", "FXF", "FFF", 'X', new ItemStack(CommonProxy.itemMisc, 1, 0), 'F', Item.feather })); // Featherfall

        // Guide Book
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(CommonProxy.itemGuide), new ItemStack(Item.book), new ItemStack(CommonProxy.itemLocationCard)));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(CommonProxy.itemGuide), new ItemStack(Item.book), new ItemStack(CommonProxy.itemEntityCard)));

        // Nanobrush
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CommonProxy.itemPaintbrush), new Object[] { "WT ", "TS ", "  S", 'W', Block.cloth, 'T', Item.silk, 'S', "stickWood" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CommonProxy.itemPaintbrush), new Object[] { " TW", " ST", "S  ", 'W', Block.cloth, 'T', Item.silk, 'S', "stickWood" }));

        // Location Card
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CommonProxy.itemLocationCard, 16), new Object[] { "IPI", "PPP", "IDI", 'I', Item.ingotIron, 'P', Item.paper, 'D', "dyeBlue" }));

        // Entity Card
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CommonProxy.itemEntityCard, 8), new Object[] { "GPG", "PPP", "GDG", 'G', Item.ingotGold, 'P', Item.paper, 'D', "dyeLime" }));

        // Wrench
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CommonProxy.itemWrench), new Object[] { "I I", " Q ", " I ", 'I', Item.ingotIron, 'Q', Item.netherQuartz }));

        // Glasses
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(CommonProxy.itemGoggles), true, new Object[] { "R B", "GLG", "L L", 'R', "dyeRed", 'B', "dyeCyan", 'G', Block.thinGlass, 'L', Item.leather }));
    }
}
