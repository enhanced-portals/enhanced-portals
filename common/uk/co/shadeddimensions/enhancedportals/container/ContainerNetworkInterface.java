package uk.co.shadeddimensions.enhancedportals.container;

import net.minecraft.entity.player.EntityPlayer;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameNetworkInterface;

public class ContainerNetworkInterface extends ContainerEnhancedPortals
{
    public ContainerNetworkInterface(TilePortalFrameNetworkInterface t)
    {
        super(t.getSizeInventory(), t);
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return ((TilePortalFrameNetworkInterface) tile).isUseableByPlayer(entityplayer);
    }
}
