package uk.co.shadeddimensions.ep3.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.ItemStack;
import uk.co.shadeddimensions.ep3.block.BlockPortal;
import uk.co.shadeddimensions.ep3.network.PacketHandlerServer;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileTransferItem;
import uk.co.shadeddimensions.library.container.ContainerBase;

public class ContainerTransferItem extends ContainerBase
{
    ItemStack lastItem = new ItemStack(BlockPortal.instance, 0);
    byte lastState = -1;
    
    public ContainerTransferItem(TileTransferItem item)
    {
        object = item;
    }
    
    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        TileTransferItem item = (TileTransferItem) object;
        byte state = (byte) (item.isSending ? 1 : 0);
        ItemStack itemStack = item.getStackInSlot(0);
        
        for (int i = 0; i < crafters.size(); i++)
        {
            ICrafting icrafting = (ICrafting) crafters.get(i);

            if (lastState != state)
            {
                icrafting.sendProgressBarUpdate(this, 1, state);
            }
            if (itemStack != lastItem)
            {
                 PacketHandlerServer.sendGuiPacketToPlayer(item, (EntityPlayer) icrafting);
            }
        }

        lastState = state;
        lastItem = itemStack;
    }

    @Override
    public void updateProgressBar(int par1, int par2)
    {
        TileTransferItem item = (TileTransferItem) object;
        
        if (par1 == 1)
        {
            item.isSending = par2 == 1;
        }
    }
}
