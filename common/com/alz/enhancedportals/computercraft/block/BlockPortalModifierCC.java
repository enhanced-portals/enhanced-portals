package com.alz.enhancedportals.computercraft.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.alz.enhancedportals.computercraft.tileentity.TileEntityPortalModifierCC;
import com.alz.enhancedportals.core.blocks.BlockPortalModifier;

public class BlockPortalModifierCC extends BlockPortalModifier
{
    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileEntityPortalModifierCC();
    }
}
