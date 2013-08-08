package uk.co.shadeddimensions.enhancedportals.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrame;

public class ContainerFrameController extends Container
{
    protected TilePortalFrame tile;
    
    public ContainerFrameController(InventoryPlayer player, TilePortalFrame frame)
    {
        tile = frame;
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return true;
    }
}
