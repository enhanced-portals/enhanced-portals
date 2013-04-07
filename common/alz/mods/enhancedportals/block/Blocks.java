package alz.mods.enhancedportals.block;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import alz.mods.enhancedportals.reference.Reference;
import alz.mods.enhancedportals.reference.Strings;
import cpw.mods.fml.common.registry.GameRegistry;

public class Blocks
{
	public static void Init()
	{
		SetupBlocks();
		SetupRecipes();

		if (Reference.Settings.AllowModifiers)
		{
			Reference.Settings.BorderBlocks.add(Reference.BlockIDs.PortalModifier);
			Reference.Settings.BorderBlocks.add(Reference.BlockIDs.DialDevice + 1);
		}

		if (Reference.Settings.AllowObsidianStairs)
		{
			Reference.Settings.BorderBlocks.add(Reference.BlockIDs.ObsidianStairs);
		}

		Reference.Settings.RemovableBlocks.add(0);
		Reference.Settings.RemovableBlocks.add(Block.fire.blockID);
		Reference.Settings.BorderBlocks.add(Reference.BlockIDs.Obsidian);
	}

	private static void SetupBlocks()
	{
		Block.blocksList[Reference.BlockIDs.Obsidian] = null;
		Block.blocksList[Reference.BlockIDs.Obsidian] = new BlockObsidian();

		if (Reference.Settings.AllowModifiers)
		{
			Block.blocksList[Reference.BlockIDs.PortalModifier] = new BlockPortalModifier();
			GameRegistry.registerBlock(Block.blocksList[Reference.BlockIDs.PortalModifier], Strings.PortalModifier_Name);

			Block.blocksList[Reference.BlockIDs.DialDevice + 1] = new BlockPortalModifier2();
			GameRegistry.registerBlock(Block.blocksList[Reference.BlockIDs.DialDevice + 1], Strings.PortalModifier_Name + "a");
		}

		if (Reference.Settings.AllowObsidianStairs)
		{
			Block.blocksList[Reference.BlockIDs.ObsidianStairs] = new BlockStairsObsidian();
			GameRegistry.registerBlock(Block.blocksList[Reference.BlockIDs.ObsidianStairs], Strings.ObsidianStairs_Name);
		}

		if (Reference.Settings.AllowDialDevice)
		{
			Block.blocksList[Reference.BlockIDs.DialDevice] = new BlockDialDevice();
			GameRegistry.registerBlock(Block.blocksList[Reference.BlockIDs.DialDevice], Strings.DialDevice_Name);
		}

		Block.blocksList[Reference.BlockIDs.NetherPortal] = new BlockNetherPortal();
		GameRegistry.registerBlock(Block.blocksList[Reference.BlockIDs.NetherPortal], "portal");
	}

	private static void SetupRecipes()
	{
		if (Reference.Settings.AllowModifiers)
		{
			GameRegistry.addRecipe(new ItemStack(Block.blocksList[Reference.BlockIDs.PortalModifier]), "OFO", "IDI", "ORO", Character.valueOf('O'), Block.obsidian, Character.valueOf('F'), Item.flintAndSteel, Character.valueOf('I'), Item.ingotIron, Character.valueOf('D'), Item.diamond, Character.valueOf('R'), Item.redstone);
		}

		if (Reference.Settings.AllowObsidianStairs)
		{
			GameRegistry.addRecipe(new ItemStack(Block.blocksList[Reference.BlockIDs.ObsidianStairs], 4), "X  ", "XX ", "XXX", Character.valueOf('X'), Block.obsidian);
		}
	}
}
