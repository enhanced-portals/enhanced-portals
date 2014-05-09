package enhancedportals.tileentity.portal;

import io.netty.buffer.ByteBuf;

import java.util.HashMap;

import li.cil.oc.api.network.Arguments;
import li.cil.oc.api.network.Callback;
import li.cil.oc.api.network.Context;
import li.cil.oc.api.network.SimpleComponent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import cpw.mods.fml.common.network.ByteBufUtils;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import enhancedportals.network.GuiHandler;
import enhancedportals.utility.GeneralUtils;
import enhancedportals.utility.WorldUtils;

public class TileTransferItem extends TileFrameTransfer implements IInventory, IPeripheral, SimpleComponent
{
    ItemStack stack;

    @Override
    public boolean activate(EntityPlayer player, ItemStack stack)
    {
        if (player.isSneaking())
        {
            return false;
        }

        TileController controller = getPortalController();

        if (GeneralUtils.isWrench(stack) && controller != null && controller.isFinalized())
        {
            GuiHandler.openGui(player, this, GuiHandler.TRANSFER_ITEM);
            return true;
        }

        return false;
    }

    @Override
    public void packetGuiFill(ByteBuf buffer)
    {
        if (stack != null)
        {
            buffer.writeBoolean(true);
            ByteBufUtils.writeItemStack(buffer, stack);
        }
        else
        {
            buffer.writeBoolean(false);
        }
    }

    @Override
    public void packetGuiUse(ByteBuf buffer)
    {
        if (buffer.readBoolean())
        {
            stack = ByteBufUtils.readItemStack(buffer);
        }
        else
        {
            stack = null;
        }
    }

    @Override
    public int getSizeInventory()
    {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int i)
    {
        return stack;
    }

    @Override
    public ItemStack decrStackSize(int i, int j)
    {
        ItemStack stack = getStackInSlot(i);

        if (stack != null)
        {
            if (stack.stackSize <= j)
            {
                setInventorySlotContents(i, null);
            }
            else
            {
                stack = stack.splitStack(j);

                if (stack.stackSize == 0)
                {
                    setInventorySlotContents(i, null);
                }
            }
        }

        return stack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i)
    {
        return stack;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        stack = itemstack;
    }

    @Override
    public String getInventoryName()
    {
        return "tile.frame.item.name";
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return true;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        return true;
    }

    int tickTimer = 20, time = 0;

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if (!worldObj.isRemote)
        {
            if (isSending)
            {
                if (time >= tickTimer)
                {
                    time = 0;

                    TileController controller = getPortalController();

                    if (controller != null && controller.isPortalActive() && stack != null)
                    {
                        TileController exitController =  (TileController) controller.getDestinationLocation().getTileEntity();

                        if (exitController != null)
                        {
                            for (ChunkCoordinates c : exitController.getTransferItems())
                            {
                                TileEntity tile = WorldUtils.getTileEntity(exitController.getWorldObj(), c);

                                if (tile != null && tile instanceof TileTransferItem)
                                {
                                    TileTransferItem item = (TileTransferItem) tile;

                                    if (!item.isSending)
                                    {
                                        if (item.getStackInSlot(0) == null)
                                        {
                                            item.setInventorySlotContents(0, stack);
                                            item.markDirty();
                                            stack = null;
                                            markDirty();
                                        }
                                        else if (item.getStackInSlot(0).getItem() == stack.getItem())
                                        {
                                            int amount = 0;

                                            if (item.getStackInSlot(0).stackSize + stack.stackSize <= stack.getMaxStackSize())
                                            {
                                                amount = stack.stackSize;
                                            }
                                            else
                                            {
                                                amount = stack.stackSize - ((item.getStackInSlot(0).stackSize + stack.stackSize) - 64);
                                            }

                                            if (amount <= 0)
                                            {
                                                continue;
                                            }

                                            item.getStackInSlot(0).stackSize += amount;
                                            item.markDirty();

                                            if (amount == stack.stackSize)
                                            {
                                                stack = null;
                                            }
                                            else
                                            {
                                                stack.stackSize -= amount;
                                            }
                                            
                                            markDirty();
                                        }
                                    }
                                }

                                if (stack == null)
                                {
                                    break;
                                }
                            }
                        }
                    }
                }

                time++;
            }
        }
    }
    
    // ComputerCraft

    @Override
    public String getType()
    {
        return "item_transfer_module";
    }

    @Override
    public String[] getMethodNames()
    {
        return new String[] { "getItemStored", "getAmountStored", "hasStack", "isSending" };
    }

    @Override
    public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws Exception
    {
        if (method == 0)
        {
            return new Object[] { stack != null ? Item.getIdFromItem(stack.getItem()) : 0 };
        }
        else if (method == 1)
        {
            return new Object[] { stack != null ? stack.stackSize : 0 };
        }
        else if (method == 2)
        {
            return new Object[] { stack != null };
        }
        else if (method == 3)
        {
            return new Object[] { isSending };
        }

        return null;
    }

    @Override
	public boolean equals(IPeripheral other)
	{
		return other == this;
	}

    @Override
    public void attach(IComputerAccess computer)
    {

    }

    @Override
    public void detach(IComputerAccess computer)
    {

    }
    
    // OpenComputers

	@Override
	public String getComponentName() {
		return "ep_transfer_item";
	}
	
	@Callback(direct = true)
	public Object[] getStack(Context context, Arguments args) {
		HashMap<String, Object> hstack = new HashMap<String, Object>();
		hstack.put("name", stack.getItem().getUnlocalizedNameInefficiently(stack));
		hstack.put("meta", stack.getItemDamage());
		hstack.put("amount", stack.stackSize);
		return new Object[]{hstack};
	}
	
	@Callback(direct = true)
	public Object[] hasStack(Context context, Arguments args) {
		return new Object[]{stack != null};
	}
	
	@Callback(direct = true)
	public Object[] isSending(Context context, Arguments args) {
		return new Object[]{isSending};
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return false;
	}

	@Override
	public void openInventory()
	{
		
	}

	@Override
	public void closeInventory()
	{
		
	}
}
