package alz.mods.enhancedportals.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import alz.mods.enhancedportals.client.gui.GuiDialDeviceItemSlot;
import alz.mods.enhancedportals.tileentity.TileEntityDialDevice;

public class ContainerDialDeviceAdd extends Container
{
    TileEntityDialDevice DialDevice;

    public ContainerDialDeviceAdd(InventoryPlayer inventoryPlayer, TileEntityDialDevice dialDevice)
    {
        DialDevice = dialDevice;

        addSlotToContainer(new GuiDialDeviceItemSlot(dialDevice, 0, 0, 20));
        addSlotToContainer(new GuiDialDeviceItemSlot(dialDevice, 1, 161, 20));

        bindPlayerInventory(inventoryPlayer);
    }

    protected void bindPlayerInventory(InventoryPlayer player)
    {
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                addSlotToContainer(new Slot(player, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++)
        {
            addSlotToContainer(new Slot(player, i, 8 + i * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        return null;
    }
}
