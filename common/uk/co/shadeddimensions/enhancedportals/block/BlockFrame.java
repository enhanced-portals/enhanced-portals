package uk.co.shadeddimensions.enhancedportals.block;

import uk.co.shadeddimensions.enhancedportals.lib.Identifiers;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockFrame extends Block
{
    public BlockFrame()
    {
        super(Identifiers.Block.PORTAL_FRAME, Material.rock);
    }
}
