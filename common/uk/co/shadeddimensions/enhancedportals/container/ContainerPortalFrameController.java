package uk.co.shadeddimensions.enhancedportals.container;

import net.minecraft.entity.player.EntityPlayer;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameController;

public class ContainerPortalFrameController extends ContainerEnhancedPortals
{
    public ContainerPortalFrameController(TilePortalFrameController tile)
    {
        super(tile.getSizeInventory(), tile);
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return ((TilePortalFrameController) tile).isUseableByPlayer(entityplayer);
    }
}
