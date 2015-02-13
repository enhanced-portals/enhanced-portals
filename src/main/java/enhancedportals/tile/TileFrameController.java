package enhancedportals.tile;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import enhancedportals.EnhancedPortals;
import enhancedportals.portal.GlyphSequence;
import enhancedportals.util.DimensionCoordinates;

public class TileFrameController extends TileFrame {
    public static DimensionCoordinates test;
    
    @Override
    public boolean onBlockActivated(EntityPlayer player) {
        Random r = new Random();
        if (!worldObj.isRemote) {
            int[] uid = new int[] { r.nextInt(50), 12, r.nextInt(50), 52, r.nextInt(50), 31, r.nextInt(50) };
            GlyphSequence g = new GlyphSequence();
            g.setGlyphs(uid);
            
            if (player.isSneaking()) {
                if (player.inventory.getStackInSlot(player.inventory.currentItem) == null) {
                    boolean b = EnhancedPortals.proxy.portalMap.setPortalGlyphs(getDimensionCoordinates(), g);
                    player.addChatMessage(new ChatComponentText(b ? "Set UID to " + g.toString():"Couldn't set UID."));
                } else {
                    boolean b = EnhancedPortals.proxy.portalMap.removePortalGlyphs(getDimensionCoordinates());
                    player.addChatMessage(new ChatComponentText(b ? "Removed UID":"Couldn't remove UID."));
                }
            } else {
                if (player.inventory.getStackInSlot(player.inventory.currentItem) == null) {
                    boolean b = EnhancedPortals.proxy.portalMap.setPortalDBS(getDimensionCoordinates(), test);
                    player.addChatMessage(new ChatComponentText(b ? "Added DBS.":"Couldn't add DBS."));
                } else {
                    boolean b = EnhancedPortals.proxy.portalMap.removePortalDBS(getDimensionCoordinates());
                    player.addChatMessage(new ChatComponentText(b ? "Removed DBS.":"Couldn't remove DBS."));
                }
            }
        }
        
        return true;
    }
    
    @Override
    public void onPreDestroy() {
        System.out.println("Has UID of " + EnhancedPortals.proxy.portalMap.getPortalGlyphs(getDimensionCoordinates()));
        EnhancedPortals.proxy.portalMap.removePortalGlyphs(getDimensionCoordinates());
        System.out.println("Has UID of " + EnhancedPortals.proxy.portalMap.getPortalGlyphs(getDimensionCoordinates()));
    }
}
