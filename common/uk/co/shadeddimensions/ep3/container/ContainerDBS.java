package uk.co.shadeddimensions.ep3.container;

import net.minecraft.entity.player.EntityPlayer;
import uk.co.shadeddimensions.ep3.tileentity.TileStabilizer;

public class ContainerDBS extends ContainerEnhancedPortals
{
    public ContainerDBS(TileStabilizer t)
    {
        super(t.getSizeInventory(), t);
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return ((TileStabilizer) tile).isUseableByPlayer(entityplayer);
    }
}
