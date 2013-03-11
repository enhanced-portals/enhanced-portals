package alz.mods.enhancedportals.item;

import alz.mods.enhancedportals.reference.Settings;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Items
{
	public static void Init()
	{
		if (!Settings.AllowModifiers)
			return; // All items are upgrades for the portal modifier
		
		SetupItems();
		SetupLanguage();
		SetupRecipes();
	}
	
	private static Item itemUpgrade;
	
	private static void SetupItems()
	{
		itemUpgrade = new ItemUpgrade();
	}
	
	private static void SetupLanguage()
	{
		LanguageRegistry.addName(itemUpgrade, "Portal Modifier Upgrade");
	}
	
	private static void SetupRecipes()
	{		
		GameRegistry.addRecipe(new ItemStack(itemUpgrade, 1), "XYX", "IKI", "XRX", Character.valueOf('X'), Item.goldNugget, Character.valueOf('Y'), new ItemStack(Item.dyePowder, 1, 5), Character.valueOf('K'), Item.paper, Character.valueOf('I'), Item.ingotIron, Character.valueOf('R'), Item.redstone);
		GameRegistry.addRecipe(new ItemStack(itemUpgrade, 1, 1), "XYX", "IKI", "XRX", Character.valueOf('X'), Item.goldNugget, Character.valueOf('Y'), new ItemStack(Item.dyePowder, 1, 5), Character.valueOf('K'), Item.paper, Character.valueOf('I'), Item.ingotGold, Character.valueOf('R'), Item.redstone);
		GameRegistry.addRecipe(new ItemStack(itemUpgrade, 1, 2), "GIG", "IUI", "GDG", Character.valueOf('G'), Item.ingotGold, Character.valueOf('I'), Item.ingotIron, Character.valueOf('D'), Item.diamond, Character.valueOf('U'), new ItemStack(itemUpgrade, 1, 1));
	}
}
