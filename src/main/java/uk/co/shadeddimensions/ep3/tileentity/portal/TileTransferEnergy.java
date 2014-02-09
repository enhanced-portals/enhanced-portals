package uk.co.shadeddimensions.ep3.tileentity.portal;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import uk.co.shadeddimensions.ep3.network.GuiHandler;
import uk.co.shadeddimensions.library.util.ItemHelper;

public class TileTransferEnergy extends TileFrameTransfer
{
    @Override
    public boolean activate(EntityPlayer player, ItemStack stack)
    {
    	if (player.isSneaking())
		{
			return false;
		}
    	
        TileController controller = getPortalController();

        if (ItemHelper.isWrench(stack) && controller != null && controller.isFinalized())
        {
            GuiHandler.openGui(player, this, GuiHandler.TRANSFER_ENERGY);
            return true;
        }
        
        return false;
    }
    
    @Override
    public void packetGui(NBTTagCompound tag, EntityPlayer player)
    {
        
    }
}
