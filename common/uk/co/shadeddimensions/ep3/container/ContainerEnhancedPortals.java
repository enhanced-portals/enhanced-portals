package uk.co.shadeddimensions.ep3.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import uk.co.shadeddimensions.ep3.tileentity.TileEnhancedPortals;

public abstract class ContainerEnhancedPortals extends Container
{
    public TileEnhancedPortals tile;

    public ContainerEnhancedPortals(TileEnhancedPortals t)
    {
        tile = t;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex)
    {
        return null;
    }
}
