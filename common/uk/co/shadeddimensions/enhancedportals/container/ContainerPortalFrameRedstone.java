package uk.co.shadeddimensions.enhancedportals.container;

import net.minecraft.entity.player.EntityPlayer;
import uk.co.shadeddimensions.enhancedportals.tileentity.frame.TileRedstoneInterface;

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
