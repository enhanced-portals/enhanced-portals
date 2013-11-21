package uk.co.shadeddimensions.ep3.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import uk.co.shadeddimensions.ep3.network.CommonProxy;

public class InventoryScanner implements IInventory
{
    ItemStack parent;
    ItemStack[] inventory;

    public InventoryScanner(ItemStack stack)
    {
        inventory = new ItemStack[2];
        loadContentsFromNBT(stack.getTagCompound());
    }

    public void loadContentsFromNBT(NBTTagCompound tag)
    {
        if (tag == null)
        {
            return;
        }

        if (tag.hasKey("scannerInventory"))
        {
            NBTTagList tagList = tag.getTagList("scannerInventory");

            for (int i = 0; i < tagList.tagCount(); i++)
            {
                NBTTagCompound t = (NBTTagCompound) tagList.tagAt(i);
                byte slot = t.getByte("Slot");

                if (slot >= 0 && slot < inventory.length)
                {
                    inventory[slot] = ItemStack.loadItemStackFromNBT(t);
                }
            }
        }
    }

    public void saveContentsToNBT(NBTTagCompound tag)
    {
        NBTTagList itemList = new NBTTagList();

        for (int i = 0; i < inventory.length; i++)
        {
            ItemStack stack = inventory[i];

            if (stack != null)
            {
                NBTTagCompound t = new NBTTagCompound();
                t.setByte("Slot", (byte) i);
                stack.writeToNBT(t);
                itemList.appendTag(t);
            }
        }

        tag.setTag("scannerInventory", itemList);
    }

    @Override
    public int getSizeInventory()
    {
        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int i)
    {
        return inventory[i];
    }

    @Override
    public ItemStack decrStackSize(int i, int j)
    {
        ItemStack stack = getStackInSlot(i);

        if (stack != null)
        {
            if (stack.stackSize <= j)
            {
                setInventorySlotContents(i, null);
            }
            else
            {
                stack = stack.splitStack(j);

                if (stack.stackSize == 0)
                {
                    setInventorySlotContents(i, null);
                }
            }
        }

        return stack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i)
    {
        return inventory[i];
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        inventory[i] = itemstack;
    }

    @Override
    public String getInvName()
    {
        return "item.ep3.scanner.name";
    }

    @Override
    public boolean isInvNameLocalized()
    {
        return false;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return true;
    }

    @Override
    public void openChest()
    {

    }

    @Override
    public void closeChest()
    {

    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        return itemstack == null || i == 0 && itemstack.itemID == CommonProxy.itemEntityCard.itemID;
    }

    @Override
    public void onInventoryChanged()
    {

    }
}
