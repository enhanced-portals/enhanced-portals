package uk.co.shadeddimensions.enhancedportals.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameController;

public class ContainerPortalFrameControllerPortalTexture extends ContainerEnhancedPortals
{
    public TilePortalFrameController controller;

    public ContainerPortalFrameControllerPortalTexture(TilePortalFrameController tile, InventoryPlayer inventory)
    {
        super(tile.getSizeInventory());

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                addSlotToContainer(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18 + 10));
            }
        }

        for (int i = 0; i < 9; i++)
        {
            addSlotToContainer(new Slot(inventory, i, 8 + i * 18, 142 + 10));
        }

        controller = tile;
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return controller.isUseableByPlayer(entityplayer);
    }
}
