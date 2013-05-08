package enhancedportals.network;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import enhancedportals.block.BlockDummyPortal;
import enhancedportals.block.BlockNetherPortal;
import enhancedportals.block.BlockObsidian;
import enhancedportals.block.BlockPortalModifier;
import enhancedportals.lib.BlockIds;
import enhancedportals.lib.Localization;
import enhancedportals.tileentity.TileEntityNetherPortal;
import enhancedportals.tileentity.TileEntityPortalModifier;

public class CommonProxy
{
    public BlockNetherPortal blockNetherPortal;
    public BlockObsidian blockObsidian;
    public BlockPortalModifier blockPortalModifier;
    public BlockDummyPortal blockDummyPortal;

    public void loadBlocks()
    {
        Block.blocksList[BlockIds.Obsidian] = null;

        blockNetherPortal = new BlockNetherPortal();
        blockPortalModifier = new BlockPortalModifier();
        blockObsidian = new BlockObsidian();
        blockDummyPortal = new BlockDummyPortal();

        GameRegistry.registerBlock(blockNetherPortal, Localization.NetherPortal_Name);
        GameRegistry.registerBlock(blockPortalModifier, Localization.PortalModifier_Name);
        GameRegistry.registerBlock(blockObsidian, Localization.Obsidian_Name);
        GameRegistry.registerBlock(blockDummyPortal, "Dummy Portal");
    }

    public void loadItems()
    {

    }

    public void loadRecipes()
    {

    }

    public void loadSettings()
    {

    }

    public void loadTileEntities()
    {
        GameRegistry.registerTileEntity(TileEntityNetherPortal.class, "EPNPortal");
        GameRegistry.registerTileEntity(TileEntityPortalModifier.class, "EPPModifier");
    }

    public World getWorld(int dimension)
    {
        return FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(dimension);
    }
}
