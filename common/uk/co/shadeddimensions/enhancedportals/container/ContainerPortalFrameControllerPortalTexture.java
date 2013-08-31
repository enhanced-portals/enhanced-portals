package uk.co.shadeddimensions.enhancedportals.container;

import net.minecraft.entity.player.EntityPlayer;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameController;

public class ContainerPortalFrameControllerPortalTexture extends ContainerEnhancedPortalsPlayer
{
    TilePortalFrameController controller;

    public ContainerPortalFrameControllerPortalTexture(TilePortalFrameController tile, EntityPlayer play)
    {
        super(0, play, 10);

        controller = tile;
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return true;
    }
}
