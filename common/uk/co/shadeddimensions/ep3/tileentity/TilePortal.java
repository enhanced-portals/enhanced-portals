package uk.co.shadeddimensions.ep3.tileentity;

import net.minecraft.entity.player.EntityPlayer;

public class TilePortal extends TilePortalPart
{
    @Override
    public boolean activate(EntityPlayer player)
    {
        if (super.activate(player))
        {
            return true;
        }
                
        return false;
    }
}
