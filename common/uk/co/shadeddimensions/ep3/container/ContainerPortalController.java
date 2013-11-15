package uk.co.shadeddimensions.ep3.container;

import net.minecraft.entity.player.EntityPlayer;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;

public class ContainerPortalController extends ContainerEnhancedPortals
{
    public ContainerPortalController(TilePortalController tile, EntityPlayer player)
    {
        super(tile);
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return ((TilePortalController) tile).isUseableByPlayer(entityplayer);
    }
}
