package uk.co.shadeddimensions.enhancedportals.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import uk.co.shadeddimensions.enhancedportals.lib.Identifiers;

public class BlockFrame extends Block
{
    public BlockFrame()
    {
        super(Identifiers.Block.PORTAL_FRAME, Material.rock);
    }
}
