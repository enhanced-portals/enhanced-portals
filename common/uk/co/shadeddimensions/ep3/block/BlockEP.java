package uk.co.shadeddimensions.ep3.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import uk.co.shadeddimensions.ep3.lib.Reference;

public class BlockEP extends BlockContainer
{
    public BlockEP(int par1, Material par2Material, boolean tab)
    {
        super(par1, par2Material);

        if (tab)
        {
            setCreativeTab(Reference.creativeTab);
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return null;
    }

}
