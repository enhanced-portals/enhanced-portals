package uk.co.shadeddimensions.ep3.tileentity.frame;

import net.minecraft.entity.player.EntityPlayer;
import uk.co.shadeddimensions.ep3.tileentity.TilePortalPart;

public class TileBiometricIdentifier extends TilePortalPart
{
    @Override
    public boolean activate(EntityPlayer player)
    {
        if (super.activate(player))
        {
            return true;
        }
        
        // Do stuff
        
        return false;
    }
}
