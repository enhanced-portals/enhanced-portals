package com.alz.enhancedportals.core.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.alz.enhancedportals.reference.BlockIds;

public class BlockNetherPortal extends BlockEnhancedPortalsContainer
{
    public BlockNetherPortal()
    {
        super(BlockIds.NETHER_PORTAL, Material.portal);
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return null;
    }
}
