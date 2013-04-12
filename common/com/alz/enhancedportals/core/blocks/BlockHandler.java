package com.alz.enhancedportals.core.blocks;

import net.minecraft.block.Block;

import com.alz.enhancedportals.reference.BlockIds;
import com.alz.enhancedportals.reference.Strings;

import cpw.mods.fml.common.registry.GameRegistry;

public class BlockHandler
{
    public static void init()
    {
        Block.blocksList[BlockIds.DIAL_HOME_DEVICE] = new BlockDialHomeDevice();
        Block.blocksList[BlockIds.PORTAL_MODIFIER] = new BlockPortalModifier();
        Block.blocksList[BlockIds.OBSIDIAN_STAIRS] = new BlockObsidianStairs();
        //Block.blocksList[BlockIds.NETHER_PORTAL] = new BlockNetherPortal();

        GameRegistry.registerBlock(Block.blocksList[BlockIds.DIAL_HOME_DEVICE], Strings.Block.DIAL_HOME_DEVICE_NAME);
        GameRegistry.registerBlock(Block.blocksList[BlockIds.PORTAL_MODIFIER], Strings.Block.PORTAL_MODIFIER_NAME);
        GameRegistry.registerBlock(Block.blocksList[BlockIds.OBSIDIAN_STAIRS], Strings.Block.OBSIDIAN_STAIRS_NAME);
        //GameRegistry.registerBlock(Block.blocksList[BlockIds.NETHER_PORTAL], Strings.Block.NETHER_PORTAL_NAME);
    }
}
