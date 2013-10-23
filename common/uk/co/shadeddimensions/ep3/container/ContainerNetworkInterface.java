package uk.co.shadeddimensions.ep3.container;

import net.minecraft.entity.player.EntityPlayer;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;

public class ContainerNetworkInterface extends ContainerEnhancedPortals
{
    public ContainerNetworkInterface(TilePortalController t)
    {
        super(t.getSizeInventory(), t);
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return ((TilePortalController) tile).isUseableByPlayer(entityplayer);
    }
}
