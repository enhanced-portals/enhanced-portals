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

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                addSlotToContainer(new Slot(player, j + i * 9 + 9, 8 + j * 18, 67 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++)
        {
            addSlotToContainer(new Slot(player, i, 8 + i * 18, 125));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return true;
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

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot)
    {
        return null;
    }
}
