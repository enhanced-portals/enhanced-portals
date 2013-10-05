package uk.co.shadeddimensions.ep3.container;

import net.minecraft.entity.player.EntityPlayer;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileRedstoneInterface;

public class ContainerPortalFrameRedstone extends ContainerEnhancedPortals
{
    public ContainerPortalFrameRedstone(TileRedstoneInterface tile)
    {
        super(tile.getSizeInventory(), tile);
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return ((TileRedstoneInterface) tile).isUseableByPlayer(entityplayer);
    }
}
