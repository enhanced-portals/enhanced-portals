package uk.co.shadeddimensions.enhancedportals.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import uk.co.shadeddimensions.enhancedportals.gui.slots.SlotPortalModule;
import uk.co.shadeddimensions.enhancedportals.tileentity.frame.TileModuleManipulator;

public class ContainerModuleManipulator extends ContainerEnhancedPortals
{
    public ContainerModuleManipulator(EntityPlayer player, TileModuleManipulator t)
    {
        super(t.getSizeInventory(), t);
        
        for (int i = 0; i < 9; i++)
        {
            addSlotToContainer(new SlotPortalModule(t, i, 8 + i * 18, 22));
        }
        
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
        return ((TileModuleManipulator) tile).isUseableByPlayer(entityplayer);
    }
}
