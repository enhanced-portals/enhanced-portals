package enhancedportals.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import enhancedportals.client.gui.BaseGui;
import enhancedportals.client.gui.GuiTransferFluid;
import enhancedportals.tileentity.TileTransferFluid;

public class ContainerTransferFluid extends BaseContainer
{
    TileTransferFluid fluid;
    byte wasSending = -1;
    int fluidID = -1, fluidAmt = -1;

    public ContainerTransferFluid(TileTransferFluid f, InventoryPlayer p)
    {
        super(null, p, GuiTransferFluid.CONTAINER_SIZE + BaseGui.bufferSpace + BaseGui.playerInventorySize);
        fluid = f;
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        byte isSending = (byte) (fluid.isSending ? 1 : 0);
        int fID = fluid.tank.getFluid() != null ? fluid.tank.getFluid().fluidID : -1, fAmt = fluid.tank.getFluidAmount();

        for (int i = 0; i < crafters.size(); i++)
        {
            ICrafting icrafting = (ICrafting) crafters.get(i);

            if (wasSending != isSending)
            {
                icrafting.sendProgressBarUpdate(this, 0, isSending);
            }

            if (fluidID != fID)
            {
                icrafting.sendProgressBarUpdate(this, 1, fID);
            }

            if (fluidAmt != fAmt)
            {
                icrafting.sendProgressBarUpdate(this, 2, fAmt);
            }

            wasSending = isSending;
            fluidID = fID;
            fluidAmt = fAmt;
        }
    }

    @Override
    public void handleGuiPacket(NBTTagCompound tag, EntityPlayer player)
    {
        fluid.isSending = !fluid.isSending;
    }

    @Override
    public void updateProgressBar(int id, int val)
    {
        if (id == 0)
        {
            fluid.isSending = val == 1;
        }
        else if (id == 1)
        {
            if (val == -1)
            {
                fluid.tank.setFluid(null);
            }
            else
            {
                fluid.tank.setFluid(new FluidStack(val, 0));
            }
        }
        else if (id == 2)
        {
            FluidStack f = fluid.tank.getFluid();

            if (f != null)
            {
                f.amount = val;
                fluid.tank.setFluid(f);
            }
        }
    }
}
