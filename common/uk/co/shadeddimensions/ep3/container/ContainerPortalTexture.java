package uk.co.shadeddimensions.ep3.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import uk.co.shadeddimensions.ep3.gui.slots.SlotPortalTexture;
import uk.co.shadeddimensions.ep3.portal.StackHelper;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;

public class ContainerPortalTexture extends ContainerEnhancedPortals
{
    public ContainerPortalTexture(EntityPlayer player, TilePortalController frame)
    {
        super(frame.getSizeInventory(), frame);

        addSlotToContainer(new SlotPortalTexture(frame, 1, 129, 23));

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
        return ((TilePortalController) tile).isUseableByPlayer(entityplayer);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex)
    {
        ItemStack s = getSlot(slotIndex).getStack();

        if (StackHelper.isItemStackValidForPortalTexture(s))
        {
            ItemStack s2 = s.copy();
            s2.stackSize = 1;

            ((TilePortalController) tile).setInventorySlotContents(1, s2);
            return null;
        }

        return super.transferStackInSlot(player, slotIndex);
    }
}
