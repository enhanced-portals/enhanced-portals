package alz.mods.enhancedportals.block;

import cpw.mods.fml.common.registry.GameRegistry;
import alz.mods.enhancedportals.reference.Reference;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Blocks
{
	public static void Init()
	{
		SetupBlocks();
		SetupRecipes();
		
		if (Reference.Settings.AllowModifiers)
			Reference.Settings.BorderBlocks.add(Reference.BlockIDs.PortalModifier);
		
		if (Reference.Settings.AllowObsidianStairs)
			Reference.Settings.BorderBlocks.add(Reference.BlockIDs.ObsidianStairs);
		
		Reference.Settings.RemovableBlocks.add(0);
		Reference.Settings.RemovableBlocks.add(Block.fire.blockID);
		Reference.Settings.BorderBlocks.add(Reference.BlockIDs.Obsidian);
	}

	private static void SetupBlocks()
	{
		Block.blocksList[Reference.BlockIDs.Obsidian] = null;		
		Block.blocksList[Reference.BlockIDs.Obsidian] = new BlockObsidian();
		Block.blocksList[Reference.BlockIDs.NetherPortal] = null;
		Block.blocksList[Reference.BlockIDs.NetherPortal] = new BlockNetherPortal();
		
		if (Reference.Settings.AllowModifiers)		
		{
			Block.blocksList[Reference.BlockIDs.PortalModifier] = new BlockPortalModifier();
			GameRegistry.registerBlock(Block.blocksList[Reference.BlockIDs.PortalModifier], Reference.Strings.PortalModifier_Name);
		}
		
		if (Reference.Settings.AllowObsidianStairs)
		{
			Block.blocksList[Reference.BlockIDs.ObsidianStairs] = new BlockStairsObsidian();
			GameRegistry.registerBlock(Block.blocksList[Reference.BlockIDs.ObsidianStairs], Reference.Strings.ObsidianStairs_Name);
		}
	}
	
	private static void SetupRecipes()
	{
		if (Reference.Settings.AllowModifiers)
			GameRegistry.addRecipe(new ItemStack(Block.blocksList[Reference.BlockIDs.PortalModifier]), "OFO", "IDI", "ORO", Character.valueOf('O'), Block.obsidian, Character.valueOf('F'), Item.flintAndSteel, Character.valueOf('I'), Item.ingotIron, Character.valueOf('D'), Item.diamond, Character.valueOf('R'), Item.redstone);
		
		if (Reference.Settings.AllowObsidianStairs)
			GameRegistry.addRecipe(new ItemStack(Block.blocksList[Reference.BlockIDs.ObsidianStairs], 4), "X  ", "XX ", "XXX", Character.valueOf('X'), Block.obsidian);		
	}
}
