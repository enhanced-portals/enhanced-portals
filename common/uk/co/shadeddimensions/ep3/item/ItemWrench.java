package uk.co.shadeddimensions.ep3.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatMessageComponent;
import uk.co.shadeddimensions.ep3.EnhancedPortals;
import uk.co.shadeddimensions.ep3.lib.GuiIds;
import uk.co.shadeddimensions.ep3.lib.Reference;
import uk.co.shadeddimensions.ep3.portal.NetworkManager;
import uk.co.shadeddimensions.ep3.tileentity.TilePortal;
import uk.co.shadeddimensions.ep3.tileentity.TilePortalFrame;
import uk.co.shadeddimensions.ep3.tileentity.TilePortalPart;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileBiometricIdentifier;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileDiallingDevice;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileModuleManipulator;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileNetworkInterface;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileRedstoneInterface;

public class ItemWrench extends ItemPortalTool
{
    public ItemWrench(int id, String name)
    {
        super(id, true, name);
    }

    @Override
    public void openGui(TilePortalPart portalPart, ItemStack stack, boolean isSneaking, EntityPlayer player)
    {
        if (portalPart instanceof TilePortalController)
        {
            player.openGui(EnhancedPortals.instance, GuiIds.PORTAL_CONTROLLER, portalPart.worldObj, portalPart.xCoord, portalPart.yCoord, portalPart.zCoord);
        }
        else if (portalPart instanceof TileRedstoneInterface)
        {
            player.openGui(EnhancedPortals.instance, GuiIds.PORTAL_REDSTONE, portalPart.worldObj, portalPart.xCoord, portalPart.yCoord, portalPart.zCoord);
        }
        else if (portalPart instanceof TileDiallingDevice)
        {
            TileDiallingDevice dialDevice = (TileDiallingDevice) portalPart;

            if (dialDevice.getPortalController() != null && dialDevice.getPortalController().uniqueIdentifier.equals(NetworkManager.BLANK_IDENTIFIER))
            {
                if (!portalPart.worldObj.isRemote)
                {
                    player.sendChatToPlayer(ChatMessageComponent.createFromTranslationKey("chat." + Reference.SHORT_ID + ".dialDevice.noUidSet"));
                }

                return;
            }

            // TODO
        }
        else if (portalPart instanceof TileNetworkInterface)
        {
            TileNetworkInterface networkInterface = (TileNetworkInterface) portalPart;

            if (networkInterface.getPortalController() != null && networkInterface.getPortalController().uniqueIdentifier.equals(NetworkManager.BLANK_IDENTIFIER))
            {
                if (!portalPart.worldObj.isRemote)
                {
                    player.sendChatToPlayer(ChatMessageComponent.createFromTranslationKey("chat." + Reference.SHORT_ID + ".networkInterface.noUidSet"));
                }

                return;
            }

            player.openGui(EnhancedPortals.instance, GuiIds.NETWORK_INTERFACE, portalPart.worldObj, portalPart.xCoord, portalPart.yCoord, portalPart.zCoord);
        }
        else if (portalPart instanceof TileBiometricIdentifier)
        {
            // TODO
        }
        else if (portalPart instanceof TileModuleManipulator)
        {
            if (((TileModuleManipulator) portalPart).getPortalController() != null)
            {
                player.openGui(EnhancedPortals.instance, GuiIds.MODULE_MANIPULATOR, portalPart.worldObj, portalPart.xCoord, portalPart.yCoord, portalPart.zCoord);
            }
        }
        else if (portalPart instanceof TilePortalFrame)
        {
            TilePortalController control = ((TilePortalPart) portalPart).getPortalController();

            if (control != null)
            {
                player.openGui(EnhancedPortals.instance, GuiIds.PORTAL_CONTROLLER, portalPart.worldObj, control.xCoord, control.yCoord, control.zCoord);
            }
        }
        else if (portalPart instanceof TilePortal)
        {
            TilePortalController control = ((TilePortalPart) portalPart).getPortalController();

            if (control != null)
            {
                player.openGui(EnhancedPortals.instance, GuiIds.PORTAL_CONTROLLER, portalPart.worldObj, control.xCoord, control.yCoord, control.zCoord);
            }
        }
    }
}
