package uk.co.shadeddimensions.enhancedportals.network;

import uk.co.shadeddimensions.enhancedportals.block.BlockFrame;
import uk.co.shadeddimensions.enhancedportals.block.BlockPortal;
import uk.co.shadeddimensions.enhancedportals.item.ItemNetherQuartzIgniter;
import uk.co.shadeddimensions.enhancedportals.item.ItemTextureDuplicator;
import uk.co.shadeddimensions.enhancedportals.item.ItemWrench;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortal;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrame;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy
{
    public static BlockFrame blockFrame;
    public static BlockPortal blockPortal;

    public static ItemNetherQuartzIgniter itemNetherQuartzIgniter;
    public static ItemTextureDuplicator itemTextureDuplicator;
    public static ItemWrench itemWrench;

    public void registerBlocks()
    {
        blockFrame = new BlockFrame();
        blockPortal = new BlockPortal();
        
        GameRegistry.registerBlock(blockFrame, "blockFrame");
        GameRegistry.registerBlock(blockPortal, "blockPortal");
    }

    public void registerTileEntities()
    {
        GameRegistry.registerTileEntity(TilePortal.class, "epPortal");
        GameRegistry.registerTileEntity(TilePortalFrame.class, "epPortalFrame");
    }
    
    public void registerItems()
    {
        itemNetherQuartzIgniter = new ItemNetherQuartzIgniter();
        itemTextureDuplicator = new ItemTextureDuplicator();
        itemWrench = new ItemWrench();

        GameRegistry.registerItem(itemNetherQuartzIgniter, "itemNetherQuartzIgniter");
        GameRegistry.registerItem(itemTextureDuplicator, "itemTextureDuplicator");
        GameRegistry.registerItem(itemWrench, "itemWrench");
    }
    
    public void registerRenderers()
    {
        
    }
}
