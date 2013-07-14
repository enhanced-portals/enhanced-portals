package enhancedportals.client.gui.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import enhancedcore.gui.EnhancedCoreContainer;
import enhancedportals.client.gui.slots.SlotPhantomTexture;
import enhancedportals.lib.Textures;
import enhancedportals.tileentity.TileEntityPortalModifier;

public class ContainerModifier extends EnhancedCoreContainer
{
    private TileEntityPortalModifier modifier;

    public ContainerModifier(InventoryPlayer inventory, TileEntityPortalModifier theModifier)
    {
        super(theModifier.getSizeInventory());
        modifier = theModifier;

        addSlotToContainer(new SlotPhantomTexture(modifier, 0, 152, 18));

        for (int i = 0; i < 3; i++)
        {
            for (int k = 0; k < 9; k++)
            {
                addSlotToContainer(new Slot(inventory, k + i * 9 + 9, 8 + k * 18, 82 + i * 18));
            }

        }

        for (int j = 0; j < 9; j++)
        {
            addSlotToContainer(new Slot(inventory, j, 8 + j * 18, 140));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex)
    {
        Slot slot = (Slot) inventorySlots.get(slotIndex);

        if (slot != null && slot.getStack() != null)
        {
            if (Textures.getTextureFromItemStack(slot.getStack()) != null)
            {
                ItemStack newStack = slot.getStack().copy();
                newStack.stackSize = 1;

                ((Slot) inventorySlots.get(0)).putStack(newStack);
                return null;
            }
        }

        return super.transferStackInSlot(player, slotIndex);
    }
}
