package uk.co.shadeddimensions.enhancedportals.gui;

import net.minecraft.entity.player.EntityPlayer;

public class ContainerPortalFrameController extends ContainerEnhancedPortals
{
    public ContainerPortalFrameController()
    {
        super(0);
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return true;
    }
}
