package uk.co.shadeddimensions.enhancedportals.container;

import net.minecraft.entity.player.EntityPlayer;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameRedstone;

public class ContainerPortalFrameRedstone extends ContainerEnhancedPortals
{
    public TilePortalFrameRedstone redstone;

    public ContainerPortalFrameRedstone(TilePortalFrameRedstone tile)
    {
        super(0);

        redstone = tile;
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return true;
    }
}
