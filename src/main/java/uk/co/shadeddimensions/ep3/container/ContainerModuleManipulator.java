package uk.co.shadeddimensions.ep3.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import uk.co.shadeddimensions.ep3.client.gui.slot.SlotPortalModule;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileModuleManipulator;
import uk.co.shadeddimensions.library.container.ContainerBase;

public class ContainerModuleManipulator extends ContainerBase
{
    public ContainerModuleManipulator(TileModuleManipulator t, EntityPlayer player)
    {
        super(t);

        for (int i = 0; i < 9; i++)
        {
            addSlotToContainer(new SlotPortalModule(t, i, 8 + i * 18, 22));
        }
        
        //addSlotToContainer(new Slot(t, 9, 152, 52));

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
}
