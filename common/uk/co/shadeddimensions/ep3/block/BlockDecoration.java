package uk.co.shadeddimensions.ep3.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import uk.co.shadeddimensions.ep3.lib.Reference;
import uk.co.shadeddimensions.ep3.util.ConnectedTextures;

public class BlockDecoration extends Block
{
    public static final int BLOCK_TYPES = 2;
    ConnectedTextures[] connectedTextures;

    public BlockDecoration(int par1, String name)
    {
        super(par1, Material.rock);
        setUnlocalizedName(name);
        setHardness(3);
        setStepSound(soundStoneFootstep);
        setCreativeTab(Reference.creativeTab);
    }

    @Override
    public Icon getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int face)
    {
        int meta = blockAccess.getBlockMetadata(x, y, z);
        return meta == 0 ? connectedTextures[0].getIconForFace(blockAccess, x, y, z, face) : meta == 1 ? connectedTextures[1].getIconForFace(blockAccess, x, y, z, face) : null;
    }

    @Override
    public Icon getIcon(int side, int meta)
    {
        return meta == 0 ? connectedTextures[0].getNormalIcon() : meta == 1 ? connectedTextures[1].getNormalIcon() : null;
    }

    @Override
    public void registerIcons(IconRegister register)
    {
        connectedTextures = new ConnectedTextures[2];
        connectedTextures[0] = new ConnectedTextures(BlockFrame.connectedTextures, blockID, 0);
        connectedTextures[1] = new ConnectedTextures(BlockStabilizer.connectedTextures, blockID, 1);
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
        return new ItemStack(blockID, 1, world.getBlockMetadata(x, y, z));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubBlocks(int par1, CreativeTabs creativeTab, List list)
    {
        for (int i = 0; i < BLOCK_TYPES; i++)
        {
            list.add(new ItemStack(blockID, 1, i));
        }
    }

    @Override
    public int damageDropped(int par1)
    {
        return par1;
    }
}
