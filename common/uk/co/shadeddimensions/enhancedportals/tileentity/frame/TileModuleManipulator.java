package uk.co.shadeddimensions.enhancedportals.tileentity.frame;

import net.minecraft.item.ItemStack;
import uk.co.shadeddimensions.enhancedportals.portal.StackHelper;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrame;

public class TileModuleManipulator extends TilePortalFrame
{
    ItemStack[] inventory;
    
    public TileModuleManipulator()
    {
        inventory = new ItemStack[9];
    }
    
    @Override
    public ItemStack decrStackSize(int i, int j)
    {
        ItemStack s = getStackInSlot(i);
        s.stackSize -= j;

        return s;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }

    @Override
    public String getInvName()
    {
        return "tile.ep3.portalFrame.upgrade.name";
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
    public boolean isItemValidForSlot(int i, ItemStack stack)
    {
        return StackHelper.isUpgrade(stack);
    }
}
