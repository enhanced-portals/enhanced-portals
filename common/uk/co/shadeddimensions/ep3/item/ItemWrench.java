package uk.co.shadeddimensions.ep3.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.world.World;
import uk.co.shadeddimensions.ep3.EnhancedPortals;
import uk.co.shadeddimensions.ep3.lib.GuiIds;
import uk.co.shadeddimensions.ep3.lib.Reference;
import uk.co.shadeddimensions.ep3.portal.NetworkManager;
import uk.co.shadeddimensions.ep3.tileentity.TilePortal;
import uk.co.shadeddimensions.ep3.tileentity.TilePortalFrame;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileBiometricIdentifier;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileDiallingDevice;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileModuleManipulator;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileNetworkInterface;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileRedstoneInterface;

public class ItemWrench extends ItemEP
{
    public ItemWrench(int id, String name) // TODO: Needs a better name, "wrench" doesn't really describe what it does.
    {
        super(id, true);
        setUnlocalizedName(name);
        maxStackSize = 1;
        setMaxDamage(0);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int par7, float par8, float par9, float par10)
    {
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (tile != null)
        {
            if (tile instanceof TilePortalController)
            {
                TilePortalController controller = (TilePortalController) tile;

                if (!controller.hasConfigured)
                {
                    return false;
                }

                player.openGui(EnhancedPortals.instance, player.isSneaking() ? GuiIds.PORTAL_FRAME_TEXTURE : GuiIds.PORTAL_CONTROLLER, world, x, y, z);
                return true;
            }
            else if (tile instanceof TileRedstoneInterface)
            {
                player.openGui(EnhancedPortals.instance, GuiIds.PORTAL_REDSTONE, world, x, y, z);
                return true;
            }
            else if (tile instanceof TileDiallingDevice)
            {
                TileDiallingDevice dialDevice = (TileDiallingDevice) tile;

                if (dialDevice.getPortalController() == null)
                {
                    return false;
                }
                else if (dialDevice.getPortalController().uniqueIdentifier.equals(NetworkManager.BLANK_IDENTIFIER))
                {
                    if (!world.isRemote)
                    {
                        player.sendChatToPlayer(ChatMessageComponent.createFromTranslationKey("chat." + Reference.SHORT_ID + ".dialDevice.noUidSet"));
                    }

                    return false;
                }

                // TODO

                return true;
            }
            else if (tile instanceof TileNetworkInterface)
            {
                TileNetworkInterface networkInterface = (TileNetworkInterface) tile;

                if (networkInterface.getPortalController() == null)
                {
                    return false;
                }
                else if (networkInterface.getPortalController().uniqueIdentifier.equals(NetworkManager.BLANK_IDENTIFIER))
                {
                    if (!world.isRemote)
                    {
                        player.sendChatToPlayer(ChatMessageComponent.createFromTranslationKey("chat." + Reference.SHORT_ID + ".networkInterface.noUidSet"));
                    }

                    return false;
                }

                player.openGui(EnhancedPortals.instance, GuiIds.NETWORK_INTERFACE, world, x, y, z);
                return true;
            }
            else if (tile instanceof TileBiometricIdentifier)
            {
                // TODO
                return true;
            }
            else if (tile instanceof TileModuleManipulator)
            {
                TileModuleManipulator module = (TileModuleManipulator) tile;

                if (module.getPortalController() == null)
                {
                    return false;
                }

                player.openGui(EnhancedPortals.instance, GuiIds.MODULE_MANIPULATOR, world, x, y, z);
                return true;
            }
            else if (tile instanceof TilePortalFrame)
            {
                TilePortalFrame frame = (TilePortalFrame) tile;
                TilePortalController control = frame.getPortalController();

                if (control != null)
                {
                    player.openGui(EnhancedPortals.instance, player.isSneaking() ? GuiIds.PORTAL_FRAME_TEXTURE : GuiIds.PORTAL_CONTROLLER, world, control.xCoord, control.yCoord, control.zCoord);
                    return true;
                }
            }
            else if (tile instanceof TilePortal)
            {
                TilePortal portal = (TilePortal) tile;
                TilePortalController control = portal.getPortalController();

                if (control != null)
                {
                    player.openGui(EnhancedPortals.instance, player.isSneaking() ? GuiIds.PORTAL_TEXTURE : GuiIds.PORTAL_CONTROLLER, world, control.xCoord, control.yCoord, control.zCoord);
                    return true;
                }
            }
        }

        return false;
    }
}
