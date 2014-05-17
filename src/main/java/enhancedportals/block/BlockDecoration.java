package enhancedportals.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import enhancedportals.EnhancedPortals;

public class BlockDecoration extends Block
{
    protected BlockDecoration(String n)
    {
        super(Material.rock);
        setBlockName(n);
        setHardness(3);
        setStepSound(soundTypeStone);
        setCreativeTab(EnhancedPortals.creativeTab);
    }
}
