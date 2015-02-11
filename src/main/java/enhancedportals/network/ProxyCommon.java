package enhancedportals.network;

import java.io.File;

import net.minecraft.creativetab.CreativeTabs;
import cpw.mods.fml.common.registry.GameRegistry;
import enhancedportals.block.BlockDimensionalBridgeStabilizerController;
import enhancedportals.block.BlockFrameController;
import enhancedportals.portal.PortalMap;
import enhancedportals.util.CreativeTabEP;

public class ProxyCommon {
    public static final CreativeTabs creativeTab = new CreativeTabEP();
    
    public static final BlockFrameController portalController = new BlockFrameController("portal_controller");
    public static final BlockDimensionalBridgeStabilizerController dbsController = new BlockDimensionalBridgeStabilizerController("dbs_controller");
    
    public PortalMap portalMap = new PortalMap();
    
    public void pre(File configFile) {
        //GameRegistry.registerBlock(portalFrame, "frame");
        GameRegistry.registerBlock(portalController, "portal_controller");
        GameRegistry.registerBlock(dbsController, "dbs_controller");
    }
    
    public void init() {
        //NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
    }
    
    public void post() {
        
    }
}
