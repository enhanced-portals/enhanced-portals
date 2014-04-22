package uk.co.shadeddimensions.ep3.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import uk.co.shadeddimensions.ep3.client.gui.GuiDefault;
import uk.co.shadeddimensions.ep3.tileentity.TileEP;

public class ContainerDefault extends Container
{
    TileEP tile;
    EntityPlayer player;
    
    public ContainerDefault(TileEP t, EntityPlayer p)
    {
        this(t, p, 0);
    }
    
    public ContainerDefault(TileEP t, EntityPlayer p, int leftNudge)
    {
        this(t, p, GuiDefault.defaultGuiSize, leftNudge);
    }
    
    public ContainerDefault(TileEP t, EntityPlayer p, int containerHeight, int leftNudge)
    {
        tile = t;
        player = p;

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, leftNudge + 8 + j * 18, (containerHeight - 82) + i * 18));
            }
        }

        for (int i = 0; i < 9; i++)
        {
            addSlotToContainer(new Slot(player.inventory, i, leftNudge + 8 + i * 18, containerHeight - 24));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return tile == null ? true : tile instanceof IInventory ? ((IInventory) tile).isUseableByPlayer(entityplayer) : true;
    }
    
    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        return null;
    }
}
