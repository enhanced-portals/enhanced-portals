package enhancedportals.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
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

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return inventory == null ? true : inventory.isUseableByPlayer(entityplayer);
    }
    
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

    protected void addPlayerInventory(int containerSize, int xOffset)
    {
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, xOffset + 8 + j * 18, (containerSize - 82) + i * 18));
            }
        }

        for (int i = 0; i < 9; i++)
        {
            addSlotToContainer(new Slot(inventoryPlayer, i, xOffset + 8 + i * 18, containerSize - 24));
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int par2)
    {
        return null;
    }

    public abstract void handleGuiPacket(NBTTagCompound tag, EntityPlayer player);
}
