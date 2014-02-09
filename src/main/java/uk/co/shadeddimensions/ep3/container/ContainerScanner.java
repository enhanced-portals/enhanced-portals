package uk.co.shadeddimensions.ep3.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import uk.co.shadeddimensions.ep3.item.ItemEntityCard;
import uk.co.shadeddimensions.library.gui.slot.SlotOutput;
import uk.co.shadeddimensions.library.gui.slot.SlotSpecificItem;

public class ContainerScanner extends Container
{
    EntityPlayer thePlayer;
    ItemStack stack;
    public InventoryScanner scannerInventory;
    public boolean hasChanged;

    public ContainerScanner(InventoryScanner scanner, EntityPlayer player, ItemStack s)
    {
        thePlayer = player;
        scannerInventory = scanner;
        hasChanged = false;
        stack = s;

        addSlotToContainer(new SlotSpecificItem(scanner, 0, 56, 35, new ItemStack(ItemEntityCard.instance)));
        addSlotToContainer(new SlotOutput(scanner, 1, 116, 35));

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++)
        {
            addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return true;
    }

    public void saveToNBT(ItemStack stack)
    {
        if (!stack.hasTagCompound())
        {
            stack.setTagCompound(new NBTTagCompound());
        }

        scannerInventory.saveContentsToNBT(stack.getTagCompound());
    }

    @Override
    public ItemStack slotClick(int par1, int par2, int par3, EntityPlayer par4EntityPlayer)
    {
        hasChanged = true;
        return super.slotClick(par1, par2, par3, par4EntityPlayer);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex)
    {
        hasChanged = true;
        return null;
    }
}
