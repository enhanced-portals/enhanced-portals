package uk.co.shadeddimensions.ep3.container;

import net.minecraft.entity.player.EntityPlayer;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;

public class ContainerPortalFrameController extends ContainerEnhancedPortals
{
    public ContainerPortalFrameController(TilePortalController tile)
    {
        super(tile.getSizeInventory(), tile);
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return ((TilePortalController) tile).isUseableByPlayer(entityplayer);
    }
}
