package uk.co.shadeddimensions.library.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * This class allows for OreDictionary-compatible ItemStack comparisons.
 * 
 * The intended purpose of this is for things such as Recipe Handlers or HashMaps of ItemStacks.
 * 
 * @author King Lemming
 * 
 */
public class ComparableItemStack
{

    public int itemID = -1;
    public int metadata = -1;
    public int stackSize = -1;
    public int oreID = -1;

    public ComparableItemStack(ComparableItemStack stack)
    {

        itemID = stack.itemID;
        metadata = stack.metadata;
        stackSize = stack.stackSize;
        oreID = stack.oreID;
    }

    public ComparableItemStack(int itemID, int damage, int stackSize)
    {

        this.itemID = itemID;
        metadata = damage;
        this.stackSize = stackSize;
        oreID = OreDictionary.getOreID(toItemStack());
    }

    public ComparableItemStack(ItemStack stack)
    {

        if (stack != null)
        {
            itemID = stack.itemID;
            metadata = stack.getItemDamage();
            stackSize = stack.stackSize;
            oreID = OreDictionary.getOreID(stack);
        }
    }

    public ComparableItemStack(String oreName)
    {

        if (!OreDictionary.getOres(oreName).isEmpty())
        {
            ItemStack ore = OreDictionary.getOres(oreName).get(0);
            itemID = ore.itemID;
            metadata = ore.getItemDamage();
            stackSize = 1;
            oreID = OreDictionary.getOreID(oreName);
        }
    }

    @Override
    public ComparableItemStack clone()
    {

        return new ComparableItemStack(this);
    }

    @Override
    public boolean equals(Object o)
    {

        if (!(o instanceof ComparableItemStack))
        {
            return false;
        }
        return isItemEqual((ComparableItemStack) o);
    }

    public Item getItem()
    {

        return itemID < 0 || itemID >= 32000 ? null : Item.itemsList[itemID];
    }

    @Override
    public int hashCode()
    {

        return oreID != -1 ? oreID : metadata | itemID << 16;
    }

    public boolean isItemEqual(ComparableItemStack other)
    {

        return other != null && (oreID != -1 && oreID == other.oreID || itemID == other.itemID && metadata == other.metadata);
    }

    public boolean isStackEqual(ComparableItemStack other)
    {

        return isItemEqual(other) && stackSize == other.stackSize;
    }

    public boolean isStackValid()
    {

        return getItem() != null;
    }

    public ComparableItemStack set(ComparableItemStack stack)
    {

        if (stack != null)
        {
            itemID = stack.itemID;
            metadata = stack.metadata;
            stackSize = stack.stackSize;
            oreID = stack.oreID;
        }
        else
        {
            itemID = -1;
            metadata = -1;
            stackSize = -1;
            oreID = -1;
        }
        return this;
    }

    public ComparableItemStack set(ItemStack stack)
    {

        if (stack != null)
        {
            itemID = stack.itemID;
            metadata = stack.getItemDamage();
            stackSize = stack.stackSize;
            oreID = OreDictionary.getOreID(stack);
        }
        else
        {
            itemID = -1;
            metadata = -1;
            stackSize = -1;
            oreID = -1;
        }
        return this;
    }

    public ItemStack toItemStack()
    {

        return itemID < 0 || itemID >= 32000 ? null : new ItemStack(itemID, stackSize, metadata);
    }

}
