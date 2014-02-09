package uk.co.shadeddimensions.ep3.tileentity.portal;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.Icon;
import uk.co.shadeddimensions.ep3.block.BlockFrame;
import uk.co.shadeddimensions.ep3.item.ItemPaintbrush;
import uk.co.shadeddimensions.ep3.lib.Localization;
import uk.co.shadeddimensions.ep3.network.GuiHandler;
import uk.co.shadeddimensions.library.util.ItemHelper;

public class TileNetworkInterface extends TileFrame //implements IPeripheral
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
			if (ItemHelper.isWrench(stack) && !player.isSneaking())
			{
				if (controller.getIdentifierUnique() == null)
				{
					if (!worldObj.isRemote)
					{
						player.sendChatToPlayer(ChatMessageComponent.createFromText(Localization.getChatString("noUidSet")));
					}
				}
				else
				{
					GuiHandler.openGui(player, controller, GuiHandler.NETWORK_INTERFACE);
				}
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
	public boolean canUpdate()
	{
		return true;
	}

	/* IPeripheral */
	/*@Override
    public String getType()
    {
        return "Network Interface";
    }

    @Override
    public String[] getMethodNames()
    {
        return new String[] { "dial", "terminate" };
    }

    @Override
    public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws Exception
    {
        if (method == 0) // dial
        {
            dial();
        }
        else if (method == 1) // terminate
        {
            terminate();
        }

        return null;
    }

    @Override
    public boolean canAttachToSide(int side)
    {
        return true;
    }

    @Override
    public void attach(IComputerAccess computer)
    {

    }

    @Override
    public void detach(IComputerAccess computer)
    {

    }*/
}
