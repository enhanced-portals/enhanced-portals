package uk.co.shadeddimensions.enhancedportals.container;

import net.minecraft.entity.player.EntityPlayer;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameRedstone;

public class ContainerPortalFrameRedstone extends ContainerEnhancedPortals
{
    public ContainerPortalFrameRedstone(TilePortalFrameRedstone tile)
    {
        super(tile.getSizeInventory(), tile);
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return ((TilePortalFrameRedstone) tile).isUseableByPlayer(entityplayer);
    }
}
