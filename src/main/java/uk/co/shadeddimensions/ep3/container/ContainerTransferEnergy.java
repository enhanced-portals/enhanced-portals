package uk.co.shadeddimensions.ep3.container;

import net.minecraft.inventory.ICrafting;
import net.minecraftforge.fluids.FluidStack;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileTransferEnergy;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileTransferFluid;
import uk.co.shadeddimensions.library.container.ContainerBase;

public class ContainerTransferEnergy extends ContainerBase
{
    private int lastAmt = -1;
    byte lastState = -1;
    
    public ContainerTransferEnergy(TileTransferEnergy energy)
    {
        object = energy;
    }
    
    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        TileTransferEnergy energy = (TileTransferEnergy) object;
        int amt = energy.storage.getEnergyStored();
        byte state = (byte) (energy.isSending ? 1 : 0);
        
        for (int i = 0; i < crafters.size(); i++)
        {
            ICrafting icrafting = (ICrafting) crafters.get(i);

            if (lastAmt != amt)
            {
                icrafting.sendProgressBarUpdate(this, 1, amt);
            }
            if (lastState != state)
            {
                icrafting.sendProgressBarUpdate(this, 2, state);
            }
        }

        lastAmt = amt;
        lastState = state;
    }

    @Override
    public void updateProgressBar(int par1, int par2)
    {
        TileTransferEnergy energy = (TileTransferEnergy) object;
        
        if (par1 == 1)
        {
            energy.storage.setEnergyStored(par2);
        }
        else if (par1 == 2)
        {
            energy.isSending = par2 == 1;
        }
    }
}
