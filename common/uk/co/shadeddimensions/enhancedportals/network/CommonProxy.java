package uk.co.shadeddimensions.enhancedportals.network;

import uk.co.shadeddimensions.enhancedportals.block.BlockFrame;
import uk.co.shadeddimensions.enhancedportals.block.BlockPortal;
import uk.co.shadeddimensions.enhancedportals.item.ItemNetherQuartzIgniter;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy
{
    public static BlockFrame blockFrame;
    public static BlockPortal blockPortal;

    public static ItemNetherQuartzIgniter itemNetherQuartzIgniter;

    public void registerBlocks()
    {
        blockFrame = new BlockFrame();
        blockPortal = new BlockPortal();

        GameRegistry.registerBlock(blockFrame, "blockFrame");
        GameRegistry.registerBlock(blockPortal, "blockPortal");
    }

    public void registerItems()
    {
        itemNetherQuartzIgniter = new ItemNetherQuartzIgniter();

        GameRegistry.registerItem(itemNetherQuartzIgniter, "itemNetherQuartzIgniter");
    }
}
