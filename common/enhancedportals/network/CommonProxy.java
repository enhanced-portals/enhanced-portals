package enhancedportals.network;

import net.minecraft.block.Block;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.common.registry.GameRegistry;
import enhancedportals.block.BlockNetherPortal;
import enhancedportals.block.BlockObsidian;
import enhancedportals.block.BlockPortalModifier;
import enhancedportals.lib.BlockIds;
import enhancedportals.lib.Localization;
import enhancedportals.network.packet.PacketRequestSync;
import enhancedportals.network.packet.PacketTEUpdate;
import enhancedportals.tileentity.TileEntityEnhancedPortals;
import enhancedportals.tileentity.TileEntityNetherPortal;
import enhancedportals.tileentity.TileEntityPortalModifier;

public class CommonProxy
{
    public BlockNetherPortal blockNetherPortal;
    public BlockObsidian blockObsidian;
    public BlockPortalModifier blockPortalModifier;

    public World getClientWorld()
    {
        return null;
    }

    public MinecraftServer getMinecraftServer()
    {
        return FMLCommonHandler.instance().getMinecraftServerInstance();
    }

    public WorldServer getWorldForDimension(int dimension)
    {
        return getMinecraftServer().worldServerForDimension(dimension);
    }

    public void loadBlocks()
    {
        Block.blocksList[BlockIds.Obsidian] = null;

        blockNetherPortal = new BlockNetherPortal();
        blockPortalModifier = new BlockPortalModifier();
        blockObsidian = new BlockObsidian();

        GameRegistry.registerBlock(blockNetherPortal, Localization.NetherPortal_Name);
        GameRegistry.registerBlock(blockPortalModifier, Localization.PortalModifier_Name);
        GameRegistry.registerBlock(blockObsidian, Localization.Obsidian_Name);
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

    public void parseRequestSync(PacketRequestSync sync, Player player)
    {
        WorldServer world = getMinecraftServer().worldServerForDimension(sync.dimension);

        if (world.blockHasTileEntity(sync.xCoord, sync.yCoord, sync.zCoord))
        {
            TileEntity tileEntity = world.getBlockTileEntity(sync.xCoord, sync.yCoord, sync.zCoord);

            if (tileEntity instanceof TileEntityEnhancedPortals)
            {
                PacketDispatcher.sendPacketToPlayer(new PacketTEUpdate((TileEntityEnhancedPortals) tileEntity).getPacket(), player);
            }
        }
    }

    public void parseTileEntityUpdate(PacketTEUpdate update)
    {
        // Nothing to do serverside.
    }
}
