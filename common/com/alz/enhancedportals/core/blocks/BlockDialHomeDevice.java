package com.alz.enhancedportals.core.blocks;

import com.alz.enhancedportals.core.tileentity.TileEntityDialHomeDevice;
import com.alz.enhancedportals.creativetab.CreativeTabEnhancedPortals;
import com.alz.enhancedportals.reference.BlockIds;
import com.alz.enhancedportals.reference.Reference;
import com.alz.enhancedportals.reference.Strings;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockDialHomeDevice extends BlockEnhancedPortalsContainer
{
    Icon texture;
    
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
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        texture = iconRegister.registerIcon(Reference.MOD_ID + ":" + Strings.Block.DIAL_HOME_DEVICE_NAME);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getBlockTextureFromSideAndMetadata(int side, int meta)
    {
        return texture;
    }
}
