package uk.co.shadeddimensions.ep3.container;

import net.minecraft.inventory.ICrafting;
import net.minecraftforge.fluids.FluidStack;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileTransferFluid;
import uk.co.shadeddimensions.library.container.ContainerBase;

public class ContainerTransferFluid extends ContainerBase
{
    private int lastFluidID = -1, lastFluidAmt = -1;
    byte lastState = -1;
    
    public ContainerTransferFluid(TileTransferFluid fluid)
    {
        object = fluid;
    }
    
    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        TileTransferFluid fluid = (TileTransferFluid) object;
        int fluidID = fluid.tank.getFluid() == null ? -1 : fluid.tank.getFluid().fluidID, fluidAmt = fluid.tank.getFluidAmount();
        byte state = (byte) (fluid.isSending ? 1 : 0);
        
        for (int i = 0; i < crafters.size(); i++)
        {
            ICrafting icrafting = (ICrafting) crafters.get(i);

            if (lastFluidID != fluidID)
            {
                icrafting.sendProgressBarUpdate(this, 1, fluidID);
            }
            if (lastFluidAmt != fluidAmt)
            {
                icrafting.sendProgressBarUpdate(this, 2, fluidAmt);
            }
            if (lastState != state)
            {
                icrafting.sendProgressBarUpdate(this, 3, state);
            }
        }

        lastFluidID = fluidID;
        lastFluidAmt = fluidAmt;
        lastState = state;
    }

    @Override
    public void updateProgressBar(int par1, int par2)
    {
        TileTransferFluid fluid = (TileTransferFluid) object;
        
        if (par1 == 1)
        {
            if (par2 == -1)
            {
                fluid.tank.setFluid(null);
            }
            else
            {
                fluid.tank.setFluid(new FluidStack(par2, 0));
            }
        }
        else if (par1 == 2)
        {
            if (par2 == 0)
            {
                fluid.tank.setFluid(null);
            }
            else
            {
                fluid.tank.setFluid(new FluidStack(fluid.tank.getFluid().fluidID, par2));
            }
        }
        else if (par1 == 3)
        {
            fluid.isSending = par2 == 1;
        }
    }
}
