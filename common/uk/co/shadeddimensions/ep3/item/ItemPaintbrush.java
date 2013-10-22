package uk.co.shadeddimensions.ep3.item;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import uk.co.shadeddimensions.ep3.EnhancedPortals;
import uk.co.shadeddimensions.ep3.lib.GuiIds;
import uk.co.shadeddimensions.ep3.tileentity.TilePortal;
import uk.co.shadeddimensions.ep3.tileentity.TilePortalPart;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;

public class ItemPaintbrush extends ItemPortalTool
{
    public static Icon texture;

    public ItemPaintbrush(int id, String name)
    {
        super(id, true, name);
    }

    @Override
    public Icon getIconFromDamage(int par1)
    {
        return texture;
    }

    @Override
    public void registerIcons(IconRegister register)
    {
        texture = register.registerIcon("enhancedportals:paintbrush");
    }

    @Override
    public boolean openGui(TilePortalPart portalPart, ItemStack stack, boolean isSneaking, EntityPlayer player)
    {
        if (super.openGui(portalPart, stack, isSneaking, player))
        {
            if (portalPart instanceof TilePortal)
            {
                TilePortalController control = ((TilePortalPart) portalPart).getPortalController();

                if (control != null)
                {
                    player.openGui(EnhancedPortals.instance, isSneaking ? GuiIds.PARTICLE_TEXTURE : GuiIds.PORTAL_TEXTURE, portalPart.worldObj, control.xCoord, control.yCoord, control.zCoord);
                }
            }
            else
            {
                TilePortalController control = ((TilePortalPart) portalPart).getPortalController();

                if (control != null)
                {
                    player.openGui(EnhancedPortals.instance, GuiIds.PORTAL_FRAME_TEXTURE, portalPart.worldObj, control.xCoord, control.yCoord, control.zCoord);
                }
            }
            
            return true;
        }

        return false;
    }
}
