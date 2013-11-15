package uk.co.shadeddimensions.ep3.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.tileentity.TileScanner;
import cofh.gui.slot.SlotOutput;
import cofh.gui.slot.SlotSpecificItem;

public class ContainerScanner extends ContainerEnhancedPortals
{
    public ContainerScanner(TileScanner scanner, EntityPlayer player)
    {
        super(scanner);
        
        addSlotToContainer(new SlotSpecificItem(scanner, 0, 56, 35, new ItemStack(CommonProxy.itemEntityCard)));
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
        return ((TileScanner) tile).isUseableByPlayer(entityplayer);
    }
}
