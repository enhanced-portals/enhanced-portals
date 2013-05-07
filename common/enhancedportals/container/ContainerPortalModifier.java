package enhancedportals.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import enhancedportals.EnhancedPortals;
import enhancedportals.lib.PortalTexture;
import enhancedportals.tileentity.TileEntityPortalModifier;

public class ContainerPortalModifier extends Container
{
    public TileEntityPortalModifier portalModifier;

    public ContainerPortalModifier(InventoryPlayer player, TileEntityPortalModifier modifier)
    {
        portalModifier = modifier;

        addSlotToContainer(new Slot(portalModifier, 0, 152, 54));

        // Add player inventory
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
        return true; // TODO
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot)
    {
        ItemStack stack = null;
        Slot slotObject = (Slot) inventorySlots.get(slot);

        if (slotObject != null && slotObject.getHasStack())
        {
            ItemStack stackInSlot = slotObject.getStack();
            stack = stackInSlot.copy();
            
            if (!EnhancedPortals.instance.isValidItemForPortalTexture(stackInSlot) || PortalTexture.getTextureFromItemStack(stackInSlot).isEqualTo(portalModifier.texture))
            {
                return null;
            }
            
            ItemStack newStack = stackInSlot.copy();
            newStack.stackSize = 1;
            stack.stackSize--;
            stackInSlot.stackSize--;
                        
            if (slot < 1)
            {
                if (!mergeItemStack(newStack, 1, 37, true))
                {
                    return null;
                }
            }

            else if (!mergeItemStack(newStack, 0, 1, false))
            {
                return null;
            }

            if (stackInSlot.stackSize == 0)
            {
                slotObject.putStack(null);
            }
            else
            {
                slotObject.onSlotChanged();
            }

            if (stackInSlot.stackSize == stack.stackSize)
            {
                return null;
            }
            
            slotObject.onPickupFromSlot(player, stackInSlot);
        }

        return stack;
    }
    
    @Override
    public ItemStack slotClick(int par1, int par2, int par3, EntityPlayer par4EntityPlayer)
    {
        if (portalModifier.isActive())
        {
            return null;
        }
        
        return super.slotClick(par1, par2, par3, par4EntityPlayer);
    }
}
