package uk.co.shadeddimensions.ep3.container;

import net.minecraft.entity.player.EntityPlayer;
import uk.co.shadeddimensions.ep3.tileentity.TileStabilizerMain;

public class ContainerDimensionalBridgeStabilizer extends ContainerEnhancedPortals
{
    public ContainerDimensionalBridgeStabilizer(TileStabilizerMain t, EntityPlayer player)
    {
        super(t.getSizeInventory(), t);
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return ((TileStabilizerMain) tile).isUseableByPlayer(entityplayer);
    }
}
