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
    public TileEntity createNewTileEntity(World world)
    {
        return new TileEnhancedPortals();
    }
    
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack)
    {
        ((TileEnhancedPortals) world.getBlockTileEntity(x, y, z)).onBlockPlacedBy(entity, stack);
    }
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        return ((TileEnhancedPortals) world.getBlockTileEntity(x, y, z)).activate(player);
    }
    
    @Override
    public void breakBlock(World world, int x, int y, int z, int oldBlockID, int oldMetadata)
    {
        ((TileEnhancedPortals) world.getBlockTileEntity(x, y, z)).breakBlock(oldBlockID, oldMetadata);    
        super.breakBlock(world, x, y, z, oldBlockID, oldMetadata);
    }
    
    @Override
    public int isProvidingStrongPower(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        return ((TileEnhancedPortals) blockAccess.getBlockTileEntity(x, y, z)).isProvidingStrongPower(side);
    }
    
    @Override
    public int isProvidingWeakPower(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        return ((TileEnhancedPortals) blockAccess.getBlockTileEntity(x, y, z)).isProvidingWeakPower(side);
    }
    
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int blockID)
    {
        ((TileEnhancedPortals) world.getBlockTileEntity(x, y, z)).onNeighborBlockChange(blockID);
    }
    
    @Override
    public void updateTick(World world, int x, int y, int z, Random random)
    {
        ((TileEnhancedPortals) world.getBlockTileEntity(x, y, z)).updateTick(random);
    }
    
    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {
        ((TileEnhancedPortals) world.getBlockTileEntity(x, y, z)).onEntityCollidedWithBlock(entity);
    }
}
