package com.alz.enhancedportals.core.blocks;

import com.alz.enhancedportals.reference.BlockIds;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockPortalModifier extends BlockEnhancedPortalsContainer
{
    protected BlockPortalModifier()
    {
        super(BlockIds.PORTAL_MODIFIER, Material.rock);
    }
    
    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return null;
    }
}
