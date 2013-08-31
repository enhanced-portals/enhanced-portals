package uk.co.shadeddimensions.enhancedportals.container;

import net.minecraft.entity.player.EntityPlayer;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameController;

public class ContainerPortalFrameController extends ContainerEnhancedPortals
{
    public TilePortalFrameController controller;

    public ContainerPortalFrameController(TilePortalFrameController tile)
    {
        super(0);
        controller = tile;
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return true;
    }
}
