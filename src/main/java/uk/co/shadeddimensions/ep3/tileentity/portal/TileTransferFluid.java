package uk.co.shadeddimensions.ep3.tileentity.portal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import cofh.api.energy.IEnergyHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import uk.co.shadeddimensions.ep3.item.ItemPaintbrush;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.network.GuiHandler;
import uk.co.shadeddimensions.ep3.util.WorldUtils;
import uk.co.shadeddimensions.library.util.ItemHelper;

public class TileTransferFluid extends TileFrameTransfer implements IFluidHandler
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
            if (ItemHelper.isWrench(stack))
            {
                GuiHandler.openGui(player, this, GuiHandler.TRANSFER_FLUID);
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
    public void packetGui(NBTTagCompound tag, EntityPlayer player)
    {
        if (tag.hasKey("state"))
        {
            isSending = !isSending;
        }
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
                        TileController exitController =  (TileController) controller.getDestinationLocation().getBlockTileEntity();

                        if (exitController != null)
                        {
                            for (ChunkCoordinates c : exitController.getTransferFluids())
                            {
                                TileEntity tile = WorldUtils.getTileEntity(exitController.worldObj, c);

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
}
