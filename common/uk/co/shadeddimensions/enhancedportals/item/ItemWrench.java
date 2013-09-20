package uk.co.shadeddimensions.enhancedportals.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import uk.co.shadeddimensions.enhancedportals.EnhancedPortals;
import uk.co.shadeddimensions.enhancedportals.lib.GuiIds;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortal;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrame;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameBiometric;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameController;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameDialDevice;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameNetworkInterface;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameRedstone;

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
            if (tile instanceof TilePortalFrameController)
            {
                TilePortalFrameController controller = (TilePortalFrameController) tile;
                
                if (!controller.hasInitialized)
                {
                    return false;
                }
                
                player.openGui(EnhancedPortals.instance, player.isSneaking() ? GuiIds.PORTAL_FRAME_TEXTURE : GuiIds.PORTAL_CONTROLLER, world, x, y, z);
                return true;
            }
            else if (tile instanceof TilePortalFrameRedstone)
            {
                player.openGui(EnhancedPortals.instance, GuiIds.PORTAL_REDSTONE, world, x, y, z);
                return true;
            }
            else if (tile instanceof TilePortalFrameDialDevice)
            {
                // TODO
                return true;
            }
            else if (tile instanceof TilePortalFrameNetworkInterface)
            {
                TilePortalFrameNetworkInterface ni = (TilePortalFrameNetworkInterface) tile;
                
                if (ni.getControllerValidated() == null)
                {
                    return false;
                }
                
                player.openGui(EnhancedPortals.instance, GuiIds.NETWORK_INTERFACE, world, x, y, z);
                return true;
            }
            else if (tile instanceof TilePortalFrameBiometric)
            {
                // TODO
                return true;
            }
            else if (tile instanceof TilePortalFrame)
            {
                TilePortalFrame frame = (TilePortalFrame) tile;
                TilePortalFrameController control = frame.getControllerValidated();
                
                if (control != null)
                {
                    player.openGui(EnhancedPortals.instance, player.isSneaking() ? GuiIds.PORTAL_FRAME_TEXTURE : GuiIds.PORTAL_CONTROLLER, world, control.xCoord, control.yCoord, control.zCoord);
                    return true;
                }
            }
            else if (tile instanceof TilePortal)
            {
                TilePortal portal = (TilePortal) tile;
                TilePortalFrameController control = portal.getControllerValidated();
                
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
