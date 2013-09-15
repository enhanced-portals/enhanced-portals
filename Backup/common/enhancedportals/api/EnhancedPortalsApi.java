package enhancedportals.api;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import enhancedportals.lib.Settings;
import enhancedportals.lib.Textures;
import enhancedportals.portal.PortalTexture;

public class EnhancedPortalsApi
{
    /***
     * Registers a new block to the excluded texture list.
     */
    public static void excludeBlockFromTexture(Block block)
    {
        excludeBlockFromTexture(block.blockID, 0);
    }

    /***
     * Registers a new block to the excluded texture list.
     */
    public static void excludeBlockFromTexture(int blockID)
    {
        excludeBlockFromTexture(blockID, 0);
    }

    /***
     * Registers a new block to the excluded texture list.
     */
    public static void excludeBlockFromTexture(int blockID, int meta)
    {
        Settings.ExcludedBlockList.add(blockID); // TODO METADATA
    }

    /***
     * Registers a new block to the border list.
     */
    public static void registerBorderBlock(Block block)
    {
        registerBorderBlock(block.blockID, 0);
    }

    /***
     * Registers a new block to the border list.
     */
    public static void registerBorderBlock(int blockID)
    {
        registerBorderBlock(blockID, 0);
    }

    /***
     * Registers a new block to the border list.
     */
    public static void registerBorderBlock(int blockID, int meta)
    {
        Settings.BorderBlocks.add(blockID); // TODO METADATA
    }

    /***
     * Registers a new block to the list of blocks that will be destroyed when a
     * portal is created.
     */
    public static void registerDestroyBlock(Block block)
    {
        registerDestroyBlock(block.blockID, 0);
    }

    /***
     * Registers a new block to the list of blocks that will be destroyed when a
     * portal is created.
     */
    public static void registerDestroyBlock(int blockID)
    {
        registerDestroyBlock(blockID, 0);
    }

    /***
     * Registers a new block to the list of blocks that will be destroyed when a
     * portal is created.
     */
    public static void registerDestroyBlock(int blockID, int meta)
    {
        Settings.DestroyBlocks.add(blockID); // TODO METADATA
    }

    /**
     * Registers a new portal texture. Should only be used for Items.
     * 
     * @param stack
     *            The item you place into the modifier.
     * @param portalTexture
     *            The texture of the portal.
     */
    public static boolean registerPortalTexture(ItemStack stack, Icon portalTexture)
    {
        String key = (stack.getItem().getSpriteNumber() == 0 ? "B:" : "I:") + stack.itemID + ":" + stack.getItemDamage();

        if (!Textures.portalTextureMap.containsKey(key))
        {
            Textures.portalTextureMap.put(key, new PortalTexture(key, portalTexture));
            return true;
        }

        return false;
    }

    /***
     * Registers a new upgrade.
     */
    public static boolean registerUpgrade(IUpgrade upgrade)
    {
        return false; // TODO
    }
}
