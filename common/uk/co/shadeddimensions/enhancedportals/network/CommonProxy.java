package uk.co.shadeddimensions.enhancedportals.network;

import net.minecraft.network.packet.Packet250CustomPayload;
import uk.co.shadeddimensions.enhancedportals.EnhancedPortals;
import uk.co.shadeddimensions.enhancedportals.block.BlockFrame;
import uk.co.shadeddimensions.enhancedportals.block.BlockPortal;
import uk.co.shadeddimensions.enhancedportals.item.ItemNetherQuartzIgniter;
import uk.co.shadeddimensions.enhancedportals.item.ItemTextureDuplicator;
import uk.co.shadeddimensions.enhancedportals.item.ItemWrench;
import uk.co.shadeddimensions.enhancedportals.network.packet.MainPacket;
import uk.co.shadeddimensions.enhancedportals.network.packet.PacketPortalFrameData;
import uk.co.shadeddimensions.enhancedportals.tileentity.TileEP;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortal;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalController;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrame;
import cpw.mods.fml.common.network.PacketDispatcher;
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
        blockFrame = (BlockFrame) EnhancedPortals.config.registerBlock(BlockFrame.class, "ep2.portalFrame");
        blockPortal = (BlockPortal) EnhancedPortals.config.registerBlock(BlockPortal.class, "ep2.portal");
    }

    public void registerTileEntities()
    {
        GameRegistry.registerTileEntity(TilePortal.class, "epPortal");
        GameRegistry.registerTileEntity(TilePortalFrame.class, "epPortalFrame");
        GameRegistry.registerTileEntity(TilePortalController.class, "epPortalController");
    }

    public void registerItems()
    {        
        itemNetherQuartzIgniter = (ItemNetherQuartzIgniter) EnhancedPortals.config.registerItem(ItemNetherQuartzIgniter.class, "ep2.netherQuartzIgniter");
        itemTextureDuplicator = (ItemTextureDuplicator) EnhancedPortals.config.registerItem(ItemTextureDuplicator.class, "ep2.textureDuplicator");
        itemWrench = (ItemWrench) EnhancedPortals.config.registerItem(ItemWrench.class, "ep2.wrench");
    }

    public void registerRenderers()
    {

    }
    
    public static void sendUpdatePacketToAllAround(TileEP tile)
    {
        Packet250CustomPayload packet = null;
        
        if (tile instanceof TilePortalFrame)
        {
            packet = MainPacket.makePacket(new PacketPortalFrameData((TilePortalFrame) tile));
        }
        
        if (packet != null)
        {
            PacketDispatcher.sendPacketToAllAround(tile.xCoord + 0.5, tile.yCoord + 0.5, tile.zCoord + 0.5, 128, tile.worldObj.provider.dimensionId, packet);
        }
    }

    public void setupConfiguration()
    {
        EnhancedPortals.config.addBlock("ep2.portal");
        EnhancedPortals.config.addBlock("ep2.portalFrame");
        
        EnhancedPortals.config.addItem("ep2.netherQuartzIgniter");
        EnhancedPortals.config.addItem("ep2.textureDuplicator");
        EnhancedPortals.config.addItem("ep2.wrench");
        
        EnhancedPortals.config.registerIds();
    }
}
