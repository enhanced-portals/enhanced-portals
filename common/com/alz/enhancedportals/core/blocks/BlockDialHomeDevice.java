package com.alz.enhancedportals.core.blocks;

import com.alz.enhancedportals.reference.BlockIds;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockDialHomeDevice extends BlockEnhancedPortalsContainer
{
    protected BlockDialHomeDevice()
    {
        super(BlockIds.DIAL_HOME_DEVICE, Material.rock);
    }
    
    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return null;
    }
}
