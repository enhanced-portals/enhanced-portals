package uk.co.shadeddimensions.ep3.tileentity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class TilePortalFrame extends TilePortalPart
{
    @Override
    public int getPowerDrainPerTick()
    {
        return 1;
    }
    
    @Override
    public void breakBlock(int oldBlockID, int oldMetadata)
    {
        // reset controller
    }
    
    @Override
    public void onBlockPlacedBy(EntityLivingBase entity, ItemStack stack)
    {
        super.onBlockPlacedBy(entity, stack);
        
        // reset all surrounding
    }
}
