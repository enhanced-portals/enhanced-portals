package enhancedportals.network;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.TextureStitchEvent;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import enhancedportals.client.renderer.ItemDialDeviceRenderer;
import enhancedportals.client.renderer.TileEntityDialDeviceRenderer;
import enhancedportals.client.renderer.TileEntityNetherPortalRenderer;
import enhancedportals.lib.BlockIds;
import enhancedportals.lib.Reference;
import enhancedportals.lib.Textures;
import enhancedportals.portal.PortalTexture;
import enhancedportals.tileentity.TileEntityDialDevice;
import enhancedportals.tileentity.TileEntityDialDeviceBasic;
import enhancedportals.tileentity.TileEntityNetherPortal;

public class ClientProxy extends CommonProxy
{
    @Override
    public World getWorld(int dimension)
    {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
        {
            return FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(dimension);
        }
        else
        {
            return FMLClientHandler.instance().getClient().theWorld;
        }
    }

    @Override
    public void loadTileEntities()
    {
        super.loadTileEntities();

        MinecraftForgeClient.registerItemRenderer(BlockIds.DialDevice, new ItemDialDeviceRenderer());
        MinecraftForgeClient.registerItemRenderer(BlockIds.DialDeviceBasic, new ItemDialDeviceRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDialDeviceBasic.class, new TileEntityDialDeviceRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDialDevice.class, new TileEntityDialDeviceRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityNetherPortal.class, new TileEntityNetherPortalRenderer());
    }

    @Override
    public void registerIcons(TextureStitchEvent.Pre event)
    {
        if (event.map == FMLClientHandler.instance().getClient().renderEngine.textureMapBlocks)
        {
            for (int i = 0; i < 16; i++)
            {
                // Colours
                Textures.portalTextureMap.put("C:" + i, new PortalTexture("C:" + i, event.map.registerIcon(Reference.MOD_ID + ":netherPortal_" + i)));
            }
        }

        // Nether Star
        Textures.portalTextureMap.put("I:" + Item.netherStar.itemID + ":0", new PortalTexture("I:" + Item.netherStar.itemID + ":0"));
        
        // Fire
        PortalTexture fireTexture = new PortalTexture("B:" + Block.fire.blockID + ":0");
        Textures.portalTextureMap.put("I:" + Item.helmetChain.itemID + ":0", fireTexture);
        Textures.portalTextureMap.put("I:" + Item.plateChain.itemID + ":0", fireTexture);
        Textures.portalTextureMap.put("I:" + Item.legsChain.itemID + ":0", fireTexture);
        Textures.portalTextureMap.put("I:" + Item.bootsChain.itemID + ":0", fireTexture);
    }
}
