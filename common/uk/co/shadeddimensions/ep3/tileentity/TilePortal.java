package uk.co.shadeddimensions.ep3.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TilePortal extends TilePortalPart
{
    @SideOnly(Side.CLIENT)
    public byte previousRenderState = -1;
    
    @Override
    public boolean activate(EntityPlayer player)
    {
        if (super.activate(player))
        {
            return true;
        }
                
        return false;
    }
    
    @Override
    public void breakBlock(int oldBlockID, int oldMetadata)
    {
        
    }
}
