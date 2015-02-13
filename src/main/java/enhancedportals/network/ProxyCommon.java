package enhancedportals.network;

import java.io.File;

import net.minecraft.creativetab.CreativeTabs;
import cpw.mods.fml.common.registry.GameRegistry;
import enhancedportals.block.BlockDimensionalBridgeStabilizerController;
import enhancedportals.block.BlockFrameController;
import enhancedportals.item.ItemLocationCard;
import enhancedportals.item.ItemWrench;
import enhancedportals.portal.PortalMap;
import enhancedportals.tile.TileDimensionalBridgeStabilizerController;
import enhancedportals.tile.TileFrameController;
import enhancedportals.util.CreativeTabEP;

public class ProxyCommon {
    public static final CreativeTabs creativeTab = new CreativeTabEP();
    
    public static final BlockFrameController portalController = new BlockFrameController("frame.controller");
    public static final BlockDimensionalBridgeStabilizerController dbsController = new BlockDimensionalBridgeStabilizerController("dbs");
    
    public static final ItemWrench wrench = new ItemWrench("wrench");
    public static final ItemLocationCard locationCard = new ItemLocationCard("location_card");
    
    public PortalMap portalMap = new PortalMap();
    
    public void pre(File configFile) {
        //GameRegistry.registerBlock(portalFrame, "frame");
        GameRegistry.registerBlock(portalController, "portal_controller");
        GameRegistry.registerBlock(dbsController, "dbs_controller");
        
        GameRegistry.registerTileEntity(TileFrameController.class, "portal_controller");
        GameRegistry.registerTileEntity(TileDimensionalBridgeStabilizerController.class, "dbs_controller");
        
        GameRegistry.registerItem(wrench, "wrench");
        GameRegistry.registerItem(locationCard, "location_card");
    }
    
    public void init() {
        //NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
    }
    
    public void post() {
        
    }
}
