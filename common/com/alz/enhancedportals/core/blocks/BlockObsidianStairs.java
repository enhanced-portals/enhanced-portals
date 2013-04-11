package com.alz.enhancedportals.core.blocks;

import com.alz.enhancedportals.creativetab.CreativeTabEnhancedPortals;
import com.alz.enhancedportals.reference.BlockIds;
import com.alz.enhancedportals.reference.Strings;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;

public class BlockObsidianStairs extends BlockStairs
{
    protected BlockObsidianStairs()
    {
        super(BlockIds.OBSIDIAN_STAIRS, Block.obsidian, 0);
        setHardness(50.0F);
        setResistance(2000.0F);
        setStepSound(soundStoneFootstep);
        setUnlocalizedName(Strings.Block.OBSIDIAN_STAIRS_NAME);
        setCreativeTab(CreativeTabEnhancedPortals.tabEnhancedPortals);
    }
}
