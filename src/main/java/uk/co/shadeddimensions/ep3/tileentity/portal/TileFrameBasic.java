package uk.co.shadeddimensions.ep3.tileentity.portal;

import uk.co.shadeddimensions.ep3.item.ItemPaintbrush;
import uk.co.shadeddimensions.ep3.network.GuiHandler;
import uk.co.shadeddimensions.library.util.ItemHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileFrameBasic extends TileFrame
{
	@Override
	public boolean activate(EntityPlayer player, ItemStack stack)
	{
		if (player.isSneaking())
		{
			return false;
		}
		
		TileController controller = getPortalController();

		if (stack != null && controller != null && controller.isFinalized())
		{
			if (ItemHelper.isWrench(stack))
			{
				GuiHandler.openGui(player, controller, GuiHandler.PORTAL_CONTROLLER);
				return true;
			}
			else if (stack.itemID == ItemPaintbrush.ID)
			{
				GuiHandler.openGui(player, controller, GuiHandler.TEXTURE_FRAME);
				return true;
			}
		}

		return false;
	}

    @Override
    public void addDataToPacket(NBTTagCompound tag)
    {
        
    }

    @Override
    public void onDataPacket(NBTTagCompound tag)
    {
        
    }
}
