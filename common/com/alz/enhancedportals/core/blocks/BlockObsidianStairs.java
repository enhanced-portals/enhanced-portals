package com.alz.enhancedportals.core.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

import com.alz.enhancedportals.creativetab.CreativeTabEnhancedPortals;
import com.alz.enhancedportals.portals.PortalHandler;
import com.alz.enhancedportals.reference.BlockIds;
import com.alz.enhancedportals.reference.Strings;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockObsidianStairs extends BlockStairs
{
    Icon texture;
    
    protected BlockObsidianStairs()
    {
        super(BlockIds.OBSIDIAN_STAIRS, Block.obsidian, 0);
        setHardness(50.0F);
        setResistance(2000.0F);
        setStepSound(soundStoneFootstep);
        setUnlocalizedName(Strings.Block.OBSIDIAN_STAIRS_NAME);
        setCreativeTab(CreativeTabEnhancedPortals.tabEnhancedPortals);
    }
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        PortalHandler.createPortalAround(world, x, y, z, player);

        return false;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        texture = iconRegister.registerIcon("obsidian");
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getBlockTextureFromSideAndMetadata(int side, int meta)
    {
        return texture;
    }
}
