package uk.co.shadeddimensions.ep3.container;

import net.minecraft.entity.player.EntityPlayer;
import uk.co.shadeddimensions.ep3.tileentity.TileStabilizer;

public class ContainerDimensionalBridgeStabilizer extends ContainerEnhancedPortals
{
    public ContainerDimensionalBridgeStabilizer(TileStabilizer t, EntityPlayer player)
    {
        super(t.getSizeInventory(), t);
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return ((TileStabilizer) tile).isUseableByPlayer(entityplayer);
    }
}
