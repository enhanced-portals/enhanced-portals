package enhancedportals.block;

import enhancedportals.network.ProxyCommon;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockMachine extends BlockContainer
{
    protected BlockMachine(String n)
    {
        super(Material.rock);
        setCreativeTab(ProxyCommon.creativeTab);
        setHardness(5);
        setResistance(2000);
        setBlockName(n);
        setStepSound(soundTypeStone);
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return null;
    }
}
