package uk.co.shadeddimensions.enhancedportals.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameController;

public class ContainerPortalController extends Container
{
    protected TilePortalFrameController tile;

    public ContainerPortalController(InventoryPlayer player, TilePortalFrameController frame)
    {
        tile = frame;
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return true;
    }
}
