package uk.co.shadeddimensions.enhancedportals.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;

public class ContainerEnhancedPortalsPlayer extends ContainerEnhancedPortals
{
    EntityPlayer player;
    int inventoryOffset = 0;

    public ContainerEnhancedPortalsPlayer(int inventorySize, EntityPlayer play)
    {
        super(inventorySize);

        player = play;
        bindPlayerInventory(player.inventory);
    }
    
    public ContainerEnhancedPortalsPlayer(int inventorySize, EntityPlayer play, int iOffset)
    {
        super(inventorySize);

        player = play;
        inventoryOffset = iOffset;
        bindPlayerInventory(player.inventory);
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return false;
    }

    protected void bindPlayerInventory(InventoryPlayer inventoryPlayer)
    {
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18 + inventoryOffset));
            }
        }

        for (int i = 0; i < 9; i++)
        {
            addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142 + inventoryOffset));
        }
    }
}
