package cofh.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Contains various helper functions to assist with {@link Item} and {@link ItemStack} manipulation and interaction.
 * 
 * @author King Lemming
 * 
 */
public final class ItemHelper {

	private ItemHelper() {

	}

	public static ItemStack consumeItem(ItemStack stack) {

		if (stack.stackSize == 1) {
			if (stack.getItem().hasContainerItem()) {
				return stack.getItem().getContainerItemStack(stack);
			} else {
				return null;
			}
		}
		stack.splitStack(1);
		return stack;
	}

	/**
	 * Gets a vanilla CraftingManager result.
	 */
	public static ItemStack findMatchingRecipe(InventoryCrafting inv, World world) {

		ItemStack[] dmgItems = new ItemStack[2];
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			if (inv.getStackInSlot(i) != null) {
				if (dmgItems[0] == null) {
					dmgItems[0] = inv.getStackInSlot(i);
				} else {
					dmgItems[1] = inv.getStackInSlot(i);
					break;
				}
			}
		}
		if (dmgItems[1] != null && dmgItems[0].itemID == dmgItems[1].itemID && dmgItems[0].stackSize == 1 && dmgItems[1].stackSize == 1
				&& Item.itemsList[dmgItems[0].itemID].isRepairable()) {
			Item theItem = Item.itemsList[dmgItems[0].itemID];
			int var13 = theItem.getMaxDamage() - dmgItems[0].getItemDamageForDisplay();
			int var8 = theItem.getMaxDamage() - dmgItems[1].getItemDamageForDisplay();
			int var9 = var13 + var8 + theItem.getMaxDamage() * 5 / 100;
			int var10 = MathHelper.maxI(0, theItem.getMaxDamage() - var9);
			return new ItemStack(dmgItems[0].itemID, 1, var10);
		} else {
			IRecipe recipe;
			for (int i = 0; i < CraftingManager.getInstance().getRecipeList().size(); ++i) {
				recipe = (IRecipe) CraftingManager.getInstance().getRecipeList().get(i);

				if (recipe.matches(inv, world)) {
					return recipe.getCraftingResult(inv);
				}
			}
			return null;
		}
	}

	/**
	 * Get a hashcode based on the ItemStack's ID and Metadata. As both of these are shorts, this should be collision-free for non-NBT sensitive ItemStacks.
	 * 
	 * @param stack
	 *            The ItemStack to get a hashcode for.
	 * @return The hashcode.
	 */
	public static int getHashCode(ItemStack stack) {

		return stack.getItemDamage() | stack.itemID << 16;
	}

	/**
	 * Get a hashcode based on an ID and Metadata pair. As both of these are shorts, this should be collision-free if NBT is not involved.
	 * 
	 * @param id
	 *            ID value to use.
	 * @param metadata
	 *            Metadata value to use.
	 * @return The hashcode.
	 */
	public static int getHashCode(int id, int metadata) {

		return metadata | id << 16;
	}

	/**
	 * Extract the ID from a hashcode created from one of the getHashCode() methods in this class.
	 */
	public static int getIDFromHashCode(int hashCode) {

		return hashCode >> 16;
	}

	/**
	 * Extract the Metadata from a hashcode created from one of the getHashCode() methods in this class.
	 */
	public static int getMetaFromHashCode(int hashCode) {

		return hashCode & 0xFF;
	}

	public static String getOreName(ItemStack stack) {

		return OreDictionary.getOreName(OreDictionary.getOreID(stack));
	}

	public static boolean isOreID(ItemStack stack, int oreID) {

		return OreDictionary.getOreID(stack) == oreID;
	}

	public static boolean isOreName(ItemStack stack, String oreName) {

		return OreDictionary.getOreName(OreDictionary.getOreID(stack)).equals(oreName);
	}

	/**
	 * Determine if a player is holding a registered Fluid Container.
	 */
	public static boolean isPlayerHoldingFluidContainer(EntityPlayer player) {

		return FluidContainerRegistry.isContainer(player.getCurrentEquippedItem());
	}

	/**
	 * Determine if a player is holding an ItemStack of a specific Item type.
	 */
	public static boolean isPlayerHoldingItem(Item item, EntityPlayer player) {

		Item equipped = player.getCurrentEquippedItem() != null ? player.getCurrentEquippedItem().getItem() : null;
		return item == null ? equipped == null : item.equals(equipped);
	}

	/**
	 * Determine if a player is holding an ItemStack with a specific Item ID, Metadata, and NBT.
	 */
	public static boolean isPlayerHoldingItemStack(ItemStack stack, EntityPlayer player) {

		ItemStack equipped = player.getCurrentEquippedItem() != null ? player.getCurrentEquippedItem() : null;
		return stack == null ? equipped == null : equipped != null && stack.isItemEqual(equipped) && ItemStack.areItemStackTagsEqual(stack, equipped);
	}

	/* Inventory Utilities */
	/**
	 * Copy an entire inventory. Best to avoid doing this often.
	 */
	public static ItemStack[] cloneInventory(ItemStack[] stacks) {

		ItemStack[] inventoryCopy = new ItemStack[stacks.length];
		for (int i = 0; i < stacks.length; i++) {
			inventoryCopy[i] = stacks[i] == null ? null : stacks[i].copy();
		}
		return inventoryCopy;
	}

	/**
	 * Add an ItemStack to an inventory. Return true if the entire stack was added.
	 * 
	 * @param inventory
	 *            The inventory.
	 * @param stack
	 *            ItemStack to add.
	 * @param startIndex
	 *            First slot to attempt to add into. Does not loop around fully.
	 */
	public static boolean addItemStackToInventory(ItemStack[] inventory, ItemStack stack, int startIndex) {

		if (stack == null) {
			return true;
		}
		int openSlot = -1;
		for (int i = startIndex; i < inventory.length; i++) {
			if (areItemStacksEqualNoNBT(stack, inventory[i]) && inventory[i].getMaxStackSize() > inventory[i].stackSize) {
				int hold = inventory[i].getMaxStackSize() - inventory[i].stackSize;
				if (hold >= stack.stackSize) {
					inventory[i].stackSize += stack.stackSize;
					stack = null;
					return true;
				} else {
					stack.stackSize -= hold;
					inventory[i].stackSize += hold;
				}
			} else if (inventory[i] == null && openSlot == -1) {
				openSlot = i;
			}
		}
		if (stack != null) {
			if (openSlot > -1) {
				inventory[openSlot] = stack;
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * Shortcut method for above, assumes starting slot is 0.
	 */
	public static boolean addItemStackToInventory(ItemStack[] inventory, ItemStack stack) {

		return addItemStackToInventory(inventory, stack, 0);
	}

	public static boolean areItemStacksEqualNoNBT(ItemStack stackA, ItemStack stackB) {

		if (stackB == null) {
			return false;
		}
		return stackA.itemID == stackB.itemID
				&& (stackA.getItemDamage() == OreDictionary.WILDCARD_VALUE ? true : stackB.getItemDamage() == OreDictionary.WILDCARD_VALUE ? true : stackA
						.getHasSubtypes() == false ? true : stackB.getItemDamage() == stackA.getItemDamage());
	}

	public static boolean craftingEquivalence(ItemStack checked, ItemStack source, String oreDict) {

		return areItemStacksEqualNoNBT(checked, source) ? true : oreDict == null ? false : getOreName(checked).equalsIgnoreCase(oreDict);
	}

	public static String getItemNBTString(ItemStack theItem, String nbtKey, String invalidReturn) {

		return theItem.stackTagCompound != null ? theItem.stackTagCompound.hasKey(nbtKey) ? theItem.stackTagCompound.getString(nbtKey) : invalidReturn
				: invalidReturn;
	}

	public static Item getItemFromStack(ItemStack theStack) {

		return theStack == null ? null : theStack.getItem();
	}

	public static boolean itemsEqualWithMetadata(ItemStack Item1, ItemStack Item2) {

		return Item1.itemID == Item2.itemID && (Item1.getItemDamage() == Item2.getItemDamage() || Item1.getHasSubtypes() == false);
	}

	public static boolean itemsEqualWithoutMetadata(ItemStack Item1, ItemStack Item2) {

		return Item1.itemID == Item2.itemID;
	}

	public static boolean itemsEqualWithMetadata(ItemStack Item1, ItemStack Item2, boolean checkNBT) {

		return Item1.itemID == Item2.itemID && Item1.getItemDamage() == Item2.getItemDamage()
				&& (!checkNBT || NBTsMatch(Item1.stackTagCompound, Item2.stackTagCompound));
	}

	public static boolean itemsEqualWithoutMetadata(ItemStack Item1, ItemStack Item2, boolean checkNBT) {

		return Item1.itemID == Item2.itemID && (!checkNBT || NBTsMatch(Item1.stackTagCompound, Item2.stackTagCompound));
	}

	public static boolean OreIDMatches(ItemStack Item1, ItemStack Item2) {

		return OreDictionary.getOreID(Item1) >= 0 && OreDictionary.getOreID(Item1) == OreDictionary.getOreID(Item2);
	}

	public static boolean NBTsMatch(NBTTagCompound Item1, NBTTagCompound Item2) {

		return Item1 == null ? Item2 == null ? true : false : Item2 == null ? false : Item1.equals(Item2);
	}

}
