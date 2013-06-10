package enhancedportals.network;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.TextureStitchEvent;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import enhancedportals.client.renderer.ItemDialDeviceRenderer;
import enhancedportals.client.renderer.TileEntityDialDeviceRenderer;
import enhancedportals.lib.BlockIds;
import enhancedportals.lib.Reference;
import enhancedportals.lib.Textures;
import enhancedportals.portal.PortalTexture;
import enhancedportals.tileentity.TileEntityDialDevice;
import enhancedportals.tileentity.TileEntityDialDeviceBasic;

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

        MinecraftForgeClient.registerItemRenderer(BlockIds.DialHomeDevice, new ItemDialDeviceRenderer());
        MinecraftForgeClient.registerItemRenderer(BlockIds.DialHomeDeviceBasic, new ItemDialDeviceRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDialDeviceBasic.class, new TileEntityDialDeviceRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDialDevice.class, new TileEntityDialDeviceRenderer());
    }

    @Override
    public void registerIcons(TextureStitchEvent.Pre event)
    {
        if (event.map == FMLClientHandler.instance().getClient().renderEngine.textureMapBlocks)
        {
            for (int i = 0; i < 16; i++)
            {
                Textures.portalTextureMap.put("C:" + i, new PortalTexture("C:" + i, event.map.registerIcon(Reference.MOD_ID + ":netherPortal_" + i), event.map.registerIcon(Reference.MOD_ID + ":portalModifier_active_" + i), ItemDye.dyeColors[i]));
            }
            
            Textures.portalTextureMap.put("I:" + Item.netherStar.itemID + ":0", new PortalTexture("I:" + Item.netherStar.itemID + ":0", event.map.registerIcon(Reference.MOD_ID + ":netherPortal_invisible")));
        }
        
        PortalTexture orangeTexture = Textures.getTexture("C:14"), blueTexture = Textures.getTexture("C:4"), whiteTexture = Textures.getTexture("C:15");
        Textures.portalTextureMap.put("I:" + Item.bucketWater.itemID + ":0", new PortalTexture("I:" + Item.bucketWater.itemID + ":0", Block.waterStill.getBlockTextureFromSide(0), blueTexture.getModifierTexture(), blueTexture.getParticleColour()));
        Textures.portalTextureMap.put("I:" + Item.bucketLava.itemID + ":0", new PortalTexture("I:" + Item.bucketLava.itemID + ":0", Block.lavaStill.getBlockTextureFromSide(0), orangeTexture.getModifierTexture(), orangeTexture.getParticleColour()));
        Textures.portalTextureMap.put("I:" + Item.snowball.itemID + ":0", new PortalTexture("I:" + Item.snowball.itemID + ":0", Block.snow.getBlockTextureFromSide(0), whiteTexture.getModifierTexture(), whiteTexture.getParticleColour()));
        
        Textures.portalTextureMap.put("I:" + Item.helmetChain.itemID + ":0", new PortalTexture("I:" + Item.helmetChain.itemID + ":0", Block.fire.getBlockTextureFromSide(2), orangeTexture.getModifierTexture(), orangeTexture.getParticleColour()));
        Textures.portalTextureMap.put("I:" + Item.plateChain.itemID + ":0", new PortalTexture("I:" + Item.plateChain.itemID + ":0", Block.fire.getBlockTextureFromSide(2), orangeTexture.getModifierTexture(), orangeTexture.getParticleColour()));
        Textures.portalTextureMap.put("I:" + Item.legsChain.itemID + ":0", new PortalTexture("I:" + Item.legsChain.itemID + ":0", Block.fire.getBlockTextureFromSide(2), orangeTexture.getModifierTexture(), orangeTexture.getParticleColour()));
        Textures.portalTextureMap.put("I:" + Item.bootsChain.itemID + ":0", new PortalTexture("I:" + Item.bootsChain.itemID + ":0", Block.fire.getBlockTextureFromSide(2), orangeTexture.getModifierTexture(), orangeTexture.getParticleColour()));
    }
}
