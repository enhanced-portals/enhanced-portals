package uk.co.shadeddimensions.ep3.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockUnstable extends Block
{

    protected BlockUnstable(int id, String name)
    {
        super(id, Material.rock);
        setUnlocalizedName(name);
    }

}
