package com.alz.enhancedportals.computercraft.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.alz.enhancedportals.computercraft.tileentity.TileEntityDialHomeDeviceCC;
import com.alz.enhancedportals.core.blocks.BlockDialHomeDevice;

public class BlockDialHomeDeviceCC extends BlockDialHomeDevice
{
    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileEntityDialHomeDeviceCC();
    }
}
