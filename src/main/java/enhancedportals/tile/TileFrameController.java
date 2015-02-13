package enhancedportals.tile;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import enhancedportals.EnhancedPortals;
import enhancedportals.item.ItemLocationCard;
import enhancedportals.item.ItemWrench;
import enhancedportals.util.PortalUtils;

public class TileFrameController extends TileFrame {
    TilePortal[] sPortal;
    TileFrame[] sFrame;
    TileFrameDialDevice[] sDialDevice;
    TileFrameNetworkInterface[] sNetwork;
    TileFramePortalManipulator sPortalManip;
    TileFrameRedstoneInterface[] sRedstone;
    TileFrameTransferEnergy[] sTranEnergy;
    TileFrameTransferFluid[] sTranFluid;
    TileFrameTransferItem[] sTranItem;
    
    /***
     * 0 = Brand new portal. Needs to be hit with a wrench to form.<br>
     * 1 = Formed but missing DBS. Needs a location card.<br>
     * 5 = Finalised
     */
    byte portalState = 0;
    public byte portalType = 0;
    
    @Override
    public boolean onBlockActivated(EntityPlayer player) {
        ItemStack stack = player.getCurrentEquippedItem();
        if (stack == null || player.isSneaking() || worldObj.isRemote) return true;

        if (portalState == 0) { // brand new portal
            if (stack.getItem() instanceof ItemWrench) {
                form(player);
            } else if (stack.getItem() instanceof ItemLocationCard) {
                player.addChatMessage(new ChatComponentText("Use a wrench first."));
            }
        } else if (portalState == 1) { // missing dbs 
            if (stack.getItem() instanceof ItemWrench) {
                openContainer(player);
            } else if (stack.getItem() instanceof ItemLocationCard) {
                if (EnhancedPortals.proxy.portalMap.getPortalGlyphs(getDimensionCoordinates()) == null) { player.addChatMessage(new ChatComponentText("Set a UID first.")); return true; }
                EnhancedPortals.proxy.portalMap.setPortalDBS(getDimensionCoordinates(), ItemLocationCard.getData(stack));
                player.addChatMessage(new ChatComponentText("Set."));
            }
        } else {
            if (stack.getItem() instanceof ItemWrench) {
                openContainer(player);
            }
        }

        return true;
    }
    
    public boolean isFinalized() {
        return portalState == 5;
    }
    
    private void form(EntityPlayer player) {
        boolean wasSuccess = false, hasController = false, hasModule = false;
        
        try {
            ArrayList<TileEntity> portalStructure = PortalUtils.getAllPortalComponents(this);
            
            ArrayList<TilePortal> p = new ArrayList<TilePortal>();
            ArrayList<TileFrame> f = new ArrayList<TileFrame>();
            
            for (TileEntity t : portalStructure) {
                if (t instanceof TileFrameController) {
                    if (hasController) {
                        throw new Exception("two controllers found");
                    } else {
                        hasController = true;
                    }
                } else if (t instanceof TilePortal) {
                    p.add((TilePortal) t);
                } else if (t instanceof TileFrame) {
                    f.add((TileFrame) t);
                }
            }
            
            sPortal = p.toArray(new TilePortal[p.size()]);
            sFrame = f.toArray(new TileFrame[p.size()]);
            wasSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
            wasSuccess = false;
        }
        
        if (wasSuccess) {
            portalState = 1;
            openContainer(player);
        } else {
            player.addChatMessage(new ChatComponentText("Something went wrong"));
        }
    }
    
    private void openContainer(EntityPlayer player) {
        // TODO
        player.addChatMessage(new ChatComponentText("opening menu"));
        player.addChatMessage(new ChatComponentText(sPortal.length + " portals, " + sFrame.length + " frames"));
    }
    
    @Override
    public void onPreDestroy() {
        EnhancedPortals.proxy.portalMap.removePortalGlyphs(getDimensionCoordinates());
    }
}
