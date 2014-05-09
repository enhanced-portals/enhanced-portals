package enhancedportals.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import enhancedportals.client.gui.BaseGui;

public abstract class BaseContainer extends Container
{
    IInventory inventory;
    InventoryPlayer inventoryPlayer;

    public BaseContainer(IInventory i, InventoryPlayer p)
    {
        this(i, p, BaseGui.defaultGuiSize, 0);
    }

    public BaseContainer(IInventory i, InventoryPlayer p, int containerSize)
    {
        this(i, p, containerSize, 0);
    }

    public BaseContainer(IInventory i, InventoryPlayer p, int containerSize, int xOffset)
    {
        inventory = i;
        inventoryPlayer = p;
        addPlayerInventory(containerSize, xOffset);
    }

    protected void addPlayerInventory(int containerSize, int xOffset)
    {
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, xOffset + 8 + j * 18, containerSize - 82 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++)
        {
            addSlotToContainer(new Slot(inventoryPlayer, i, xOffset + 8 + i * 18, containerSize - 24));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return inventory == null ? true : inventory.isUseableByPlayer(entityplayer);
    }

    public abstract void handleGuiPacket(NBTTagCompound tag, EntityPlayer player);

    /** It's stupid that I'm forced to do this, even though it's not my issue. **/
    protected void hideInventorySlots()
    {
        for (Object o : inventorySlots)
        {
            if (o instanceof Slot)
            {
                Slot slot = (Slot) o;
                slot.xDisplayPosition = -1000000;
                slot.yDisplayPosition = -1000000;
            }
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int s)
    {
        if (inventory == null)
        {
            return null;
        }
        
        ItemStack itemstack = null;
        Slot slot = (Slot) inventorySlots.get(s);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            int playerInventory = 35, inventorySize = playerInventory + inventory.getSizeInventory() + 1;
            
            if (s > playerInventory && s < inventorySize)
            {
                if (!this.mergeItemStack(itemstack1, 0, 36, true))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (!mergeItemStack(itemstack1, 36, inventorySize, false))
            {
                return null;
            }
            
            if (itemstack1.stackSize == 0)
            {
                slot.putStack(null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(player, itemstack1);
        }

        return itemstack;
    }
    
    @Override
    protected boolean mergeItemStack(ItemStack par1ItemStack, int par2, int par3, boolean par4)
    {
        boolean flag1 = false;
        int k = par2;

        if (par4)
        {
            k = par3 - 1;
        }

        Slot slot;
        ItemStack itemstack1;

        if (par1ItemStack.isStackable())
        {
            while (par1ItemStack.stackSize > 0 && (!par4 && k < par3 || par4 && k >= par2))
            {
                slot = (Slot)this.inventorySlots.get(k);
                
                if (!slot.isItemValid(par1ItemStack))
                {
                    if (par4)
                    {
                        --k;
                    }
                    else
                    {
                        ++k;
                    }
                    
                    continue;
                }
                
                itemstack1 = slot.getStack();

                if (itemstack1 != null && itemstack1.getItem() == par1ItemStack.getItem() && (!par1ItemStack.getHasSubtypes() || par1ItemStack.getItemDamage() == itemstack1.getItemDamage()) && ItemStack.areItemStackTagsEqual(par1ItemStack, itemstack1))
                {
                    int l = itemstack1.stackSize + par1ItemStack.stackSize;

                    if (l <= par1ItemStack.getMaxStackSize())
                    {
                        par1ItemStack.stackSize = 0;
                        itemstack1.stackSize = l;
                        slot.onSlotChanged();
                        flag1 = true;
                    }
                    else if (itemstack1.stackSize < par1ItemStack.getMaxStackSize())
                    {
                        par1ItemStack.stackSize -= par1ItemStack.getMaxStackSize() - itemstack1.stackSize;
                        itemstack1.stackSize = par1ItemStack.getMaxStackSize();
                        slot.onSlotChanged();
                        flag1 = true;
                    }
                }

                if (par4)
                {
                    --k;
                }
                else
                {
                    ++k;
                }
            }
        }

        if (par1ItemStack.stackSize > 0)
        {
            if (par4)
            {
                k = par3 - 1;
            }
            else
            {
                k = par2;
            }

            while (!par4 && k < par3 || par4 && k >= par2)
            {
                slot = (Slot)this.inventorySlots.get(k);
                
                if (!slot.isItemValid(par1ItemStack))
                {
                    if (par4)
                    {
                        --k;
                    }
                    else
                    {
                        ++k;
                    }
                    
                    continue;
                }
                
                itemstack1 = slot.getStack();

                if (itemstack1 == null)
                {
                    slot.putStack(par1ItemStack.copy());
                    slot.onSlotChanged();
                    par1ItemStack.stackSize = 0;
                    flag1 = true;
                    break;
                }

                if (par4)
                {
                    --k;
                }
                else
                {
                    ++k;
                }
            }
        }

        return flag1;
    }
}
