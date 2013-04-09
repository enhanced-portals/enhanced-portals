package alz.mods.enhancedportals.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import alz.mods.enhancedportals.reference.Reference;

public class GuiDialDeviceItemSlot extends Slot
{
    private int ID;

    public GuiDialDeviceItemSlot(IInventory par1iInventory, int par2, int par3, int par4)
    {
        super(par1iInventory, par2, par3, par4);
        ID = par2;
    }

    @Override
    public boolean canTakeStack(EntityPlayer par1EntityPlayer)
    {
        return true;
    }

    @Override
    public int getSlotStackLimit()
    {
        return 1;
    }

    @Override
    public boolean isItemValid(ItemStack itemStack)
    {
        if (ID == 0)
        {
            if (itemStack.itemID == Reference.ItemIDs.ItemScroll + 256)
            {
                if (itemStack.getItemDamage() == 1)
                {
                    return true;
                }
            }
        }
        else if (ID == 1)
        {
            if (itemStack.itemID == Item.bucketLava.itemID || itemStack.itemID == Item.bucketWater.itemID || itemStack.itemID == Item.dyePowder.itemID)
            {
                return true;
            }
        }

        return false;
    }
}