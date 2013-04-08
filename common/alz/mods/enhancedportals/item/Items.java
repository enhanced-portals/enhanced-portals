package alz.mods.enhancedportals.item;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import alz.mods.enhancedportals.reference.Reference;
import alz.mods.enhancedportals.reference.Settings;
import cpw.mods.fml.common.registry.GameRegistry;

public class Items
{
	public static void Init()
	{
		SetupItems();
		SetupRecipes();
	}

	@SuppressWarnings("unused")
	private static Item itemScroll;
	private static Item itemUpgrade;

	private static void SetupItems()
	{
		itemUpgrade = new ItemUpgrade();
		itemScroll = new ItemScroll();
	}

	private static void SetupRecipes()
	{
		if (Settings.AllowModifiers)
		{
			GameRegistry.addRecipe(new ItemStack(itemUpgrade, 1), "XYX", "IKI", "XRX", Character.valueOf('X'), Item.goldNugget, Character.valueOf('Y'), new ItemStack(Item.dyePowder, 1, 5), Character.valueOf('K'), Item.paper, Character.valueOf('I'), Item.ingotIron, Character.valueOf('R'), Item.redstone);
			GameRegistry.addRecipe(new ItemStack(itemUpgrade, 1, 1), "XYX", "IKI", "XRX", Character.valueOf('X'), Item.goldNugget, Character.valueOf('Y'), new ItemStack(Item.dyePowder, 1, 5), Character.valueOf('K'), Item.paper, Character.valueOf('I'), Item.ingotGold, Character.valueOf('R'), Item.redstone);
			GameRegistry.addRecipe(new ItemStack(itemUpgrade, 1, 2), "GIG", "IUI", "GDG", Character.valueOf('G'), Item.ingotGold, Character.valueOf('I'), Item.ingotIron, Character.valueOf('D'), Item.diamond, Character.valueOf('U'), new ItemStack(itemUpgrade, 1, 1));
			GameRegistry.addRecipe(new ItemStack(itemUpgrade, 1, 3), "XYX", "IKI", "XOX", Character.valueOf('X'), Item.goldNugget, Character.valueOf('Y'), new ItemStack(Item.dyePowder, 1, 5), Character.valueOf('K'), Item.paper, Character.valueOf('I'), Item.ingotIron, Character.valueOf('O'), Block.obsidian);

			if (Reference.ComputercraftComputer != null)
			{
				GameRegistry.addRecipe(new ItemStack(itemUpgrade, 1, 4), "XYX", "IKI", "XOX", Character.valueOf('X'), Item.goldNugget, Character.valueOf('Y'), new ItemStack(Item.dyePowder, 1, 5), Character.valueOf('K'), Item.paper, Character.valueOf('I'), Item.ingotIron, Character.valueOf('O'), Reference.ComputercraftComputer);
			}
		}
	}
}
