package enhancedportals.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import enhancedportals.EnhancedPortals;
import enhancedportals.item.ItemLocationCard;
import enhancedportals.item.ItemWrench;
import enhancedportals.portal.PortalConstructor;

public class TileFrameController extends TileFrame
{
    TilePortal[] savedPortal;
    TileFrame[] savedFrame;
    TileFrameDialDevice[] savedDialDevice;
    TileFrameNetworkInterface[] savedNetwork;
    TileFramePortalManipulator savedPortalManip;
    TileFrameRedstoneInterface[] savedRedstone;
    TileFrameTransferEnergy[] savedTranEnergy;
    TileFrameTransferFluid[] savedTranFluid;
    TileFrameTransferItem[] savedTranItem;

    /***
     * 0 = Brand new portal. Needs to be hit with a wrench to form.<br>
     * 1 = Formed but missing DBS. Needs a location card.<br>
     * 5 = Finalised
     */
    byte portalState = 0;

    @Override
    public boolean onBlockActivated(EntityPlayer player)
    {
        ItemStack stack = player.getCurrentEquippedItem();
        if (stack == null || player.isSneaking() || worldObj.isRemote) return true;

        if (portalState == 0) // brand new portal
        {
            if (stack.getItem() instanceof ItemWrench)
            {
                form(player);
            }
            else if (stack.getItem() instanceof ItemLocationCard)
            {
                player.addChatMessage(new ChatComponentText("Use a wrench first."));
            }
        }
        else if (portalState == 1) // missing dbs
        {
            if (stack.getItem() instanceof ItemWrench)
            {
                openContainer(player);
            }
            else if (stack.getItem() instanceof ItemLocationCard)
            {
                if (EnhancedPortals.proxy.portalMap.getPortalGlyphs(getDimensionCoordinates()) == null)
                {
                    player.addChatMessage(new ChatComponentText("Set a UID first."));
                    return true;
                }
                else if (!ItemLocationCard.isDataSet(stack))
                {
                    player.addChatMessage(new ChatComponentText("Location card not set."));
                    return true;
                }
                
                EnhancedPortals.proxy.portalMap.setPortalDBS(getDimensionCoordinates(), ItemLocationCard.getData(stack));
                player.addChatMessage(new ChatComponentText("Set."));
            }
        }
        else
        {
            if (stack.getItem() instanceof ItemWrench)
            {
                openContainer(player);
            }
        }

        return true;
    }

    public boolean isFinalized()
    {
        return portalState == 5;
    }

    private void form(EntityPlayer player)
    {
        PortalConstructor constructor = new PortalConstructor();
        
        try
        {
            constructor.perform(this);
            
            //savedPortal = constructor.getPortal();
            //savedFrame = constructor.getFrame();
        }
        catch (Exception e)
        {
            player.addChatMessage(new ChatComponentText("something went wrong: " + e.getMessage()));
            return;
        }

        portalState = 1;
        openContainer(player);
    }

    private void openContainer(EntityPlayer player)
    {
        // TODO
        player.addChatMessage(new ChatComponentText("opening menu"));
        //player.addChatMessage(new ChatComponentText(savedPortal.length + " portals, " + savedFrame.length + " frames"));
    }

    @Override
    public void onPreDestroy()
    {
        EnhancedPortals.proxy.portalMap.removePortalGlyphs(getDimensionCoordinates());
    }
}
