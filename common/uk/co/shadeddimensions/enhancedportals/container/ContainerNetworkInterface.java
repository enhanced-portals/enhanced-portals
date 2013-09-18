package uk.co.shadeddimensions.enhancedportals.container;

import net.minecraft.entity.player.EntityPlayer;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameController;

public class ContainerNetworkInterface extends ContainerEnhancedPortals
{
    public ContainerNetworkInterface(TilePortalFrameController t)
    {
        super(t.getSizeInventory(), t);
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return ((TilePortalFrameController) tile).isUseableByPlayer(entityplayer);
    }
}
