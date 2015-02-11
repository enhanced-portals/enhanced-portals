package enhancedportals.tile;

import net.minecraft.entity.player.EntityPlayer;
import enhancedportals.EnhancedPortals;
import enhancedportals.portal.GlyphSequence;

public class TileDimensionalBridgeStabilizerController extends TileDimensionalBridgeStabilizer {
    @Override
    public boolean onBlockActivated(EntityPlayer player) {
        if (!worldObj.isRemote) {
            if (player.isSneaking()) {
                TileFrameController.test = getDimensionCoordinates();
                System.out.println("Set DBS to " + getDimensionCoordinates());
            } else {
                GlyphSequence[] glyphs = EnhancedPortals.proxy.portalMap.getDBSPortals(getDimensionCoordinates());
                if (glyphs == null ||glyphs.length == 0) return true;
                for (GlyphSequence g : glyphs) {
                    System.out.println(g);
                }
            }
        } 
        
        return true;
    }
    
    @Override
    public void onPreDestroy() {
        System.out.println("Contains " + EnhancedPortals.proxy.portalMap.getDBSPortals(getDimensionCoordinates()).length + " portals.");
        EnhancedPortals.proxy.portalMap.removeDBS(getDimensionCoordinates());
        System.out.println("Contains " + EnhancedPortals.proxy.portalMap.getDBSPortals(getDimensionCoordinates()).length + " portals.");
    }
}
