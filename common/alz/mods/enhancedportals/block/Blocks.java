package alz.mods.enhancedportals.block;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import alz.mods.enhancedportals.reference.BlockID;
import alz.mods.enhancedportals.reference.Settings;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Blocks
{
	public static void Init()
	{
		SetupBlocks();
		SetupLanguage();
		SetupRecipes();
		
		if (Settings.AllowModifiers)
			Settings.BorderBlocks.add(BlockID.PortalModifier);
		
		if (Settings.AllowObsidianStairs)
			Settings.BorderBlocks.add(BlockID.ObsidianStairs);
		
		Settings.RemovableBlocks.add(0);
		Settings.RemovableBlocks.add(Block.fire.blockID);
		Settings.BorderBlocks.add(BlockID.Obsidian);
	}

	private static void SetupBlocks()
	{
		Block.blocksList[BlockID.Obsidian] = null;		
		Block.blocksList[BlockID.Obsidian] = new BlockObsidian();
		Block.blocksList[BlockID.NetherPortal] = null;
		Block.blocksList[BlockID.NetherPortal] = new BlockNetherPortal();
		
		if (Settings.AllowModifiers)		
		{
			Block.blocksList[BlockID.PortalModifier] = new BlockPortalModifier();
			GameRegistry.registerBlock(Block.blocksList[BlockID.PortalModifier], "portalModifier");
		}
		
		if (Settings.AllowObsidianStairs)
		{
			Block.blocksList[BlockID.ObsidianStairs] = new BlockStairsObsidian();
			GameRegistry.registerBlock(Block.blocksList[BlockID.ObsidianStairs], "stairsObsidian");
		}
	}
		
	private static void SetupLanguage()
	{
		if (Settings.AllowModifiers)
			LanguageRegistry.addName(Block.blocksList[BlockID.PortalModifier], "Portal Modifier");
		
		if (Settings.AllowObsidianStairs)
			LanguageRegistry.addName(Block.blocksList[BlockID.ObsidianStairs], "Obsidian Stairs");
	}
	
	private static void SetupRecipes()
	{
		if (Settings.AllowModifiers)
			GameRegistry.addRecipe(new ItemStack(Block.blocksList[BlockID.PortalModifier]), "OFO", "IDI", "ORO", Character.valueOf('O'), Block.obsidian, Character.valueOf('F'), Item.flintAndSteel, Character.valueOf('I'), Item.ingotIron, Character.valueOf('D'), Item.diamond, Character.valueOf('R'), Item.redstone);
		
		if (Settings.AllowObsidianStairs)
			GameRegistry.addRecipe(new ItemStack(Block.blocksList[BlockID.ObsidianStairs], 4), "X  ", "XX ", "XXX", Character.valueOf('X'), Block.obsidian);		
	}
}
