package uk.co.shadeddimensions.enhancedportals.block;

import net.minecraft.world.World;
import uk.co.shadeddimensions.enhancedportals.lib.Identifiers;

public class BlockPortal extends net.minecraft.block.BlockPortal
{
    public BlockPortal()
    {
        super(Identifiers.Block.PORTAL_BLOCK);
    }

    @Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        
    }
}
