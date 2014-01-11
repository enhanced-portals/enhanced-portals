package uk.co.shadeddimensions.ep3.block;

import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import uk.co.shadeddimensions.ep3.lib.Reference;
import uk.co.shadeddimensions.ep3.tileentity.TileEnhancedPortals;

public class BlockEnhancedPortals extends BlockContainer
{
    protected BlockEnhancedPortals(int id, Material material, boolean showOnTab)
    {
        super(id, material);

        if (showOnTab)
        {
            setCreativeTab(Reference.creativeTab);
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int oldBlockID, int oldMetadata)
    {
        TileEnhancedPortals tile = (TileEnhancedPortals) world.getBlockTileEntity(x, y, z);

        if (tile != null)
        {
            tile.breakBlock(oldBlockID, oldMetadata);
        }

        super.breakBlock(world, x, y, z, oldBlockID, oldMetadata);
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return null;
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        TileEnhancedPortals tile = (TileEnhancedPortals) blockAccess.getBlockTileEntity(x, y, z);

        if (tile == null)
        {
            return super.isProvidingStrongPower(blockAccess, x, y, z, side);
        }

        return tile.isProvidingStrongPower(side);
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        TileEnhancedPortals tile = (TileEnhancedPortals) blockAccess.getBlockTileEntity(x, y, z);

        if (tile == null)
        {
            return super.isProvidingWeakPower(blockAccess, x, y, z, side);
        }

        return tile.isProvidingWeakPower(side);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        TileEnhancedPortals tile = (TileEnhancedPortals) world.getBlockTileEntity(x, y, z);

        if (tile == null)
        {
            return false;
        }

        return tile.activate(player);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack)
    {
        TileEnhancedPortals tile = (TileEnhancedPortals) world.getBlockTileEntity(x, y, z);

        if (tile == null)
        {
            return;
        }

        tile.onBlockPlacedBy(entity, stack);
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {
        TileEnhancedPortals tile = (TileEnhancedPortals) world.getBlockTileEntity(x, y, z);

        if (tile == null)
        {
            return;
        }

        tile.onEntityCollidedWithBlock(entity);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int blockID)
    {
        TileEnhancedPortals tile = (TileEnhancedPortals) world.getBlockTileEntity(x, y, z);

        if (tile == null)
        {
            return;
        }

        tile.onNeighborBlockChange(blockID);
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random)
    {
        TileEnhancedPortals tile = (TileEnhancedPortals) world.getBlockTileEntity(x, y, z);

        if (tile == null)
        {
            return;
        }

        tile.updateTick(random);
    }
}
