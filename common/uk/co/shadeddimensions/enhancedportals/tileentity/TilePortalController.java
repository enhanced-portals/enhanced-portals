package uk.co.shadeddimensions.enhancedportals.tileentity;

import net.minecraft.block.Block;
import net.minecraft.util.Icon;
import uk.co.shadeddimensions.enhancedportals.network.ClientProxy;

public class TilePortalController extends TileEP
{
    @Override
    public Icon getTexture(int side, int renderpass)
    {
        if (renderpass == 1 && ClientProxy.isWearingGoggles)
        {
            return Block.glass.getBlockTextureFromSide(side);
        }
        
        return Block.obsidian.getBlockTextureFromSide(side);
    }
}
