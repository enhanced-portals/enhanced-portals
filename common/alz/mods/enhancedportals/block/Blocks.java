package alz.mods.enhancedportals.block;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import alz.mods.enhancedportals.reference.Reference;
import alz.mods.enhancedportals.reference.Settings;
import alz.mods.enhancedportals.reference.Strings;
import cpw.mods.fml.common.registry.GameRegistry;

public class Blocks
{
    public static void Init()
    {
        SetupBlocks();
        SetupRecipes();

        if (Settings.AllowModifiers)
        {
            Settings.BorderBlocks.add(Reference.BlockIDs.PortalModifier);
            Settings.BorderBlocks.add(Reference.BlockIDs.DialDevice + 1);
        }

        if (Settings.AllowObsidianStairs)
        {
            Settings.BorderBlocks.add(Reference.BlockIDs.ObsidianStairs);
        }

        Settings.RemovableBlocks.add(0);
        Settings.RemovableBlocks.add(Block.fire.blockID);
        Settings.BorderBlocks.add(Reference.BlockIDs.Obsidian);
    }

    private static void SetupBlocks()
    {
        Block.blocksList[Reference.BlockIDs.Obsidian] = null;
        Block.blocksList[Reference.BlockIDs.Obsidian] = new BlockObsidian();

        if (Settings.AllowModifiers)
        {
            Block.blocksList[Reference.BlockIDs.PortalModifier] = new BlockPortalModifier();
            GameRegistry.registerBlock(Block.blocksList[Reference.BlockIDs.PortalModifier], Strings.PortalModifier_Name);
        }

        if (Settings.AllowObsidianStairs)
        {
            Block.blocksList[Reference.BlockIDs.ObsidianStairs] = new BlockStairsObsidian();
            GameRegistry.registerBlock(Block.blocksList[Reference.BlockIDs.ObsidianStairs], Strings.ObsidianStairs_Name);
        }

        if (Settings.AllowDialDevice)
        {
            Block.blocksList[Reference.BlockIDs.DialDevice] = new BlockDialDevice();
            GameRegistry.registerBlock(Block.blocksList[Reference.BlockIDs.DialDevice], Strings.DialDevice_Name);
        }

        Block.blocksList[Reference.BlockIDs.NetherPortal] = new BlockNetherPortal();
        GameRegistry.registerBlock(Block.blocksList[Reference.BlockIDs.NetherPortal], "portal");
    }

    private static void SetupRecipes()
    {
        if (Settings.AllowModifiers)
        {
            GameRegistry.addRecipe(new ItemStack(Block.blocksList[Reference.BlockIDs.PortalModifier]), "OFO", "IDI", "ORO", Character.valueOf('O'), Block.obsidian, Character.valueOf('F'), Item.flintAndSteel, Character.valueOf('I'), Item.ingotIron, Character.valueOf('D'), Item.diamond, Character.valueOf('R'), Item.redstone);
        }

        if (Settings.AllowObsidianStairs)
        {
            GameRegistry.addRecipe(new ItemStack(Block.blocksList[Reference.BlockIDs.ObsidianStairs], 4), "X  ", "XX ", "XXX", Character.valueOf('X'), Block.obsidian);
        }
    }
}
