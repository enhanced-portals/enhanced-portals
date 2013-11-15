package uk.co.shadeddimensions.ep3.container;

import net.minecraft.entity.player.EntityPlayer;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileDiallingDevice;

public class ContainerDiallingDevice extends ContainerEnhancedPortals
{
    public ContainerDiallingDevice(TileDiallingDevice t, EntityPlayer player)
    {
        super(t);
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return ((TileDiallingDevice) tile).isUseableByPlayer(entityplayer);
    }
}
