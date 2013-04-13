package com.alz.enhancedportals.core.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import com.alz.enhancedportals.core.tileentity.TileEntityDialHomeDevice;
import com.alz.enhancedportals.creativetab.CreativeTabEnhancedPortals;
import com.alz.enhancedportals.reference.BlockIds;
import com.alz.enhancedportals.reference.Strings;

public class BlockDialHomeDevice extends BlockEnhancedPortalsContainer
{
    public BlockDialHomeDevice()
    {
        super(BlockIds.DIAL_HOME_DEVICE, Material.rock);
        setHardness(50.0F);
        setResistance(2000.0F);
        setStepSound(soundStoneFootstep);
        setUnlocalizedName(Strings.Block.DIAL_HOME_DEVICE_NAME);
        setCreativeTab(CreativeTabEnhancedPortals.tabEnhancedPortals);
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileEntityDialHomeDevice();
    }

    @Override
    public int getRenderType()
    {
        return -1;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        if (player.isSneaking())
        {
            if (player.inventory.mainInventory[player.inventory.currentItem] == null)
            {
                ForgeDirection nextRotation = ForgeDirection.getOrientation(world.getBlockMetadata(x, y, z)).getRotation(ForgeDirection.UP);

                rotateBlock(world, x, y, z, nextRotation);
                return true;
            }

            return false;
        }

        return false;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityLiving, ItemStack itemStack)
    {
        int direction = 0;
        int facing = MathHelper.floor_double(entityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

        if (facing == 0)
        {
            direction = ForgeDirection.NORTH.ordinal();
        }
        else if (facing == 1)
        {
            direction = ForgeDirection.EAST.ordinal();
        }
        else if (facing == 2)
        {
            direction = ForgeDirection.SOUTH.ordinal();
        }
        else if (facing == 3)
        {
            direction = ForgeDirection.WEST.ordinal();
        }

        world.setBlockMetadataWithNotify(x, y, z, direction, 2);
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public boolean rotateBlock(World world, int x, int y, int z, ForgeDirection axis)
    {
        boolean success = world.setBlockMetadataWithNotify(x, y, z, axis.ordinal(), 0);

        if (world.isRemote && success)
        {
            world.markBlockForRenderUpdate(x, y, z);
        }

        return success;
    }
}
