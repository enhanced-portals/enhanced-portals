package com.alz.enhancedportals.core.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import com.alz.enhancedportals.core.tileentity.TileEntityDialHomeDevice;
import com.alz.enhancedportals.creativetab.CreativeTabEnhancedPortals;
import com.alz.enhancedportals.portals.PortalTexture;
import com.alz.enhancedportals.reference.BlockIds;
import com.alz.enhancedportals.reference.Reference;
import com.alz.enhancedportals.reference.Strings;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockPortalModifier extends BlockEnhancedPortalsContainer
{
    Icon sideTexture;

    public BlockPortalModifier()
    {
        super(BlockIds.PORTAL_MODIFIER, Material.rock);
        setHardness(50.0F);
        setResistance(2000.0F);
        setStepSound(soundStoneFootstep);
        setUnlocalizedName(Strings.Block.PORTAL_MODIFIER_NAME);
        setCreativeTab(CreativeTabEnhancedPortals.tabEnhancedPortals);
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileEntityDialHomeDevice();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        int metaData = blockAccess.getBlockMetadata(x, y, z);

        if (side == metaData)
        {
            return PortalTexture.PURPLE.getModifierIcon(); // TODO: Use TileEntity data
        }

        return sideTexture;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getBlockTextureFromSideAndMetadata(int side, int meta)
    {
        return side == 1 ? PortalTexture.PURPLE.getModifierIcon() : sideTexture;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        if (player.isSneaking())
        {
            if (player.inventory.mainInventory[player.inventory.currentItem] == null)
            {
                ForgeDirection nextRotation = ForgeDirection.getOrientation(world.getBlockMetadata(x, y, z));

                if (nextRotation.ordinal() + 1 < ForgeDirection.VALID_DIRECTIONS.length)
                {
                    nextRotation = ForgeDirection.getOrientation(nextRotation.ordinal() + 1);
                }
                else
                {
                    nextRotation = ForgeDirection.getOrientation(0);
                }

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

        if (entityLiving.rotationPitch > 65 && entityLiving.rotationPitch <= 90)
        {
            direction = ForgeDirection.UP.ordinal();
        }
        else if (entityLiving.rotationPitch < -65 && entityLiving.rotationPitch >= -90)
        {
            direction = ForgeDirection.DOWN.ordinal();
        }

        world.setBlockMetadataWithNotify(x, y, z, direction, 0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        sideTexture = iconRegister.registerIcon(Reference.MOD_ID + ":" + Strings.Block.PORTAL_MODIFIER_NAME + "_side");

        // TODO: Move to a better place...
        PortalTexture.registerTextures(iconRegister);
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
