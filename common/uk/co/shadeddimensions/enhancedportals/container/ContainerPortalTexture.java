package uk.co.shadeddimensions.enhancedportals.container;

import net.minecraft.entity.player.EntityPlayer;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortal;

public class ContainerPortalTexture extends ContainerEnhancedPortals
{
    public ContainerPortalTexture(TilePortal t)
    {
        super(t.getSizeInventory(), t);
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return ((TilePortal) tile).isUseableByPlayer(entityplayer);
    }
}
