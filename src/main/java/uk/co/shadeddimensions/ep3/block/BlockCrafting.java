package uk.co.shadeddimensions.ep3.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import uk.co.shadeddimensions.ep3.lib.Reference;

public class BlockCrafting extends Block
{
    public static final int UNFILLED_STABILIZER = 0;

    public BlockCrafting(int id, String name)
    {
        super(id, Material.rock);
        setCreativeTab(Reference.creativeTab);
        setHardness(5);
        setResistance(2000);
        setUnlocalizedName(name);
        setStepSound(soundStoneFootstep);
    }

    @Override
    public Icon getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        int meta = blockAccess.getBlockMetadata(x, y, z);

        if (meta == 0)
        {
            return BlockStabilizer.connectedTextures.getBaseIcon();
        }

        return super.getBlockTexture(blockAccess, x, y, z, side);
    }

    @Override
    public Icon getIcon(int side, int meta)
    {
        if (meta == 0)
        {
            return BlockStabilizer.connectedTextures.getBaseIcon();
        }

        return super.getIcon(side, meta);
    }

    @Override
    public void registerIcons(IconRegister register)
    {

    }
}
