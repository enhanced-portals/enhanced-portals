package enhancedportals.tileentity.portal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

import li.cil.oc.api.network.Arguments;
import li.cil.oc.api.network.Callback;
import li.cil.oc.api.network.Context;
import li.cil.oc.api.network.SimpleComponent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import enhancedportals.item.ItemPaintbrush;
import enhancedportals.network.GuiHandler;
import enhancedportals.utility.GeneralUtils;
import enhancedportals.utility.WorldUtils;

public class TileTransferFluid extends TileFrameTransfer implements IFluidHandler, IPeripheral, SimpleComponent
{
    public FluidTank tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 16);

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
            if (GeneralUtils.isWrench(stack))
            {
                GuiHandler.openGui(player, this, GuiHandler.TRANSFER_FLUID);
                return true;
            }
            else if (stack.getItem() == ItemPaintbrush.instance)
            {
                GuiHandler.openGui(player, controller, GuiHandler.TEXTURE_A);
                return true;
            }
        }

        return false;
    }

    @Override
    public void packetGuiFill(DataOutputStream stream) throws IOException
    {
        if (tank.getFluid() != null)
        {
            stream.writeBoolean(false);
            stream.writeInt(tank.getFluid().fluidID);
            stream.writeInt(tank.getFluidAmount());
        }
        else
        {
            stream.writeBoolean(false);
        }
    }

    @Override
    public void packetGuiUse(DataInputStream stream) throws IOException
    {
        if (stream.readBoolean())
        {
            tank.setFluid(new FluidStack(FluidRegistry.getFluid(stream.readInt()), stream.readInt()));
        }
        else
        {
            tank.setFluid(null);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        tank.writeToNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        tank.readFromNBT(tag);
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
        return tank.fill(resource, doFill);
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
    {
        if (resource == null || !resource.isFluidEqual(tank.getFluid()))
        {
            return null;
        }

        return tank.drain(resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
    {
        return tank.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid)
    {
        return true;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid)
    {
        return true;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from)
    {
        return new FluidTankInfo[] { tank.getInfo() };
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

                    if (controller != null && controller.isPortalActive() && tank.getFluidAmount() > 0)
                    {
                        TileController exitController =  (TileController) controller.getDestinationLocation().getTileEntity();

                        if (exitController != null)
                        {
                            for (ChunkCoordinates c : exitController.getTransferFluids())
                            {
                                TileEntity tile = WorldUtils.getTileEntity(exitController.getWorldObj(), c);

                                if (tile != null && tile instanceof TileTransferFluid)
                                {
                                    TileTransferFluid fluid = (TileTransferFluid) tile;

                                    if (!fluid.isSending)
                                    {
                                        if (fluid.fill(null, tank.getFluid(), false) > 0)
                                        {
                                            tank.drain(fluid.fill(null, tank.getFluid(), true), true);
                                        }
                                    }
                                }

                                if (tank.getFluidAmount() == 0)
                                {
                                    break;
                                }
                            }
                        }
                    }
                }

                time++;
            }
            else
            {
                if (!cached)
                {
                    updateFluidHandlers();
                }
                
                for (int i = outputTracker; (i < 6) && (tank.getFluidAmount() > 0); i++)
                {
                    transferFluid(i);
                }
                
                outputTracker++;
                outputTracker = (byte) (outputTracker % 6);
            }
        }
    }

    IFluidHandler[] handlers = new IFluidHandler[6];
    boolean cached = false;
    byte outputTracker = 0;

    @Override
    public void onNeighborChanged()
    {
        updateFluidHandlers();
    }

    void transferFluid(int side)
    {
        if (handlers[side] == null)
        {
            return;
        }

        tank.drain(handlers[side].fill(ForgeDirection.getOrientation(side).getOpposite(), tank.getFluid(), true), true);
    }

    void updateFluidHandlers()
    {
        for (int i = 0; i < 6; i++)
        {
            TileEntity tile = WorldUtils.getTileEntity(this, ForgeDirection.getOrientation(i));

            if (tile != null && tile instanceof IFluidHandler)
            {
                IFluidHandler fluid = (IFluidHandler) tile;

                if (fluid.getTankInfo(ForgeDirection.getOrientation(i).getOpposite()) != null)
                {
                    handlers[i] = fluid;
                }
                else
                {
                    handlers[i] = null;
                }
            }
            else
            {
                handlers[i] = null;
            }
        }

        cached = true;
    }
    
    @Override
    public String getType()
    {
        return "FTM";
    }

    @Override
    public String[] getMethodNames()
    {
        return new String[] { "getFluidStored", "getAmountStored", "isFull", "isEmpty", "isSending" };
    }

    @Override
    public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws Exception
    {
        if (method == 0)
        {
            return new Object[] { tank.getFluid() != null ? tank.getFluid().getFluid().getName() : "" };
        }
        else if (method == 1)
        {
            return new Object[] { tank.getFluidAmount() };
        }
        else if (method == 2)
        {
            return new Object[] { tank.getFluidAmount() == tank.getCapacity() };
        }
        else if (method == 3)
        {
            return new Object[] { tank.getFluidAmount() == 0 };
        }
        else if (method == 4)
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
		return "ep_transfer_fluid";
	}
	
	@Callback(direct = true, limit = 1)
	public Object[] getFluid(Context context, Arguments args) {
		final HashMap<String, Object> map = new HashMap<String, Object>();
		FluidTankInfo value = tank.getInfo();
		
		// Code taken from OpenComponents by Sangar
		// https://github.com/MightyPirates/OpenComponents
        map.put("capacity", value.capacity);
        if (value.fluid != null) {
            map.put("amount", value.fluid.amount);
            map.put("id", value.fluid.fluidID);
            final Fluid fluid = value.fluid.getFluid();
            if (fluid != null) {
                map.put("name", fluid.getName());
                map.put("label", fluid.getLocalizedName());
            }
        } else {
            map.put("amount", 0);
        }
		return new Object[]{ map };
	}
	
	@Callback(direct = true)
	public Object[] isSending(Context context, Arguments args) {
		return new Object[]{isSending};
	}
}
