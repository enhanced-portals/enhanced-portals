package uk.co.shadeddimensions.ep3.container;

import cofh.gui.slot.SlotEnergy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import uk.co.shadeddimensions.ep3.tileentity.TileStabilizerMain;

public class ContainerDimensionalBridgeStabilizer extends ContainerEnhancedPortals
{
    private int lastPower, lastPortals, lastInstability, lastPowerState;
    
    public ContainerDimensionalBridgeStabilizer(TileStabilizerMain t, EntityPlayer player)
    {
        super(t);
        
        addSlotToContainer(new SlotEnergy(t, 0, 152, 58));
        
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 94 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++)
        {
            addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 152));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return ((TileStabilizerMain) tile).isUseableByPlayer(entityplayer);
    }
    
    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        int currentPower = ((TileStabilizerMain) tile).getEnergyStored(null), currentPortals = ((TileStabilizerMain) tile).getActiveConnections(), currentInstability = ((TileStabilizerMain) tile).instability, currentPowerState = ((TileStabilizerMain) tile).powerState;
        
        for (int i = 0; i < crafters.size(); i++)
        {
            ICrafting icrafting = (ICrafting) crafters.get(i);
            
            if (lastPower != currentPower)
            {
                icrafting.sendProgressBarUpdate(this, 0, currentPower);
            }
            if (lastPortals != currentPortals)
            {
                icrafting.sendProgressBarUpdate(this, 1, currentPortals);
            }
            if (lastInstability != currentInstability)
            {
                icrafting.sendProgressBarUpdate(this, 2, currentInstability);
            }
            if (lastPowerState != currentPowerState)
            {
                icrafting.sendProgressBarUpdate(this, 3, currentPowerState);
            }
        }
        
        lastPower = currentPower;
        lastPortals = currentPortals;
        lastInstability = currentInstability;
        lastPowerState = currentPowerState;
    }
    
    @Override
    public void updateProgressBar(int par1, int par2)
    {
        if (par1 == 0)
        {
            ((TileStabilizerMain) tile).getEnergyStorage().setEnergyStored(par2);
        }
        else if (par1 == 1)
        {
            ((TileStabilizerMain) tile).intActiveConnections = par2;
        }
        else if (par1 == 2)
        {
            ((TileStabilizerMain) tile).instability = par2;
        }
        else if (par1 == 3)
        {
            ((TileStabilizerMain) tile).powerState = par2;
        }
    }
}
