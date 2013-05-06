package enhancedportals.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import enhancedportals.tileentity.TileEntityPortalModifier;

public class ContainerPortalModifier extends Container
{
    public TileEntityPortalModifier portalModifier;
    
    public ContainerPortalModifier(InventoryPlayer player, TileEntityPortalModifier modifier)
    {
        portalModifier = modifier;
        
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
        return null;
    }
}
