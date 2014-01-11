package uk.co.shadeddimensions.ep3.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import uk.co.shadeddimensions.ep3.client.gui.slot.SlotDBS;
import uk.co.shadeddimensions.ep3.network.packet.PacketGuiData;
import uk.co.shadeddimensions.ep3.tileentity.TileStabilizerMain;
import uk.co.shadeddimensions.ep3.util.GuiPayload;
import uk.co.shadeddimensions.library.container.ContainerBase;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class ContainerDimensionalBridgeStabilizer extends ContainerBase
{
    private int lastPower, lastPortals, lastInstability, lastPowerState;

    public ContainerDimensionalBridgeStabilizer(TileStabilizerMain t, EntityPlayer player)
    {
        super(t);

        addSlotToContainer(new SlotDBS(t, 0, 152, 58));

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
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        TileStabilizerMain stabilizer = (TileStabilizerMain) object;
        int currentPower = stabilizer.getEnergyStorage().getEnergyStored(), currentPortals = stabilizer.getActiveConnections(), currentInstability = stabilizer.instability, currentPowerState = stabilizer.powerState;

        for (int i = 0; i < crafters.size(); i++)
        {
            ICrafting icrafting = (ICrafting) crafters.get(i);

            if (lastPower != currentPower)
            {
                GuiPayload payload = new GuiPayload();
                payload.data.setInteger("energy", currentPower);
                PacketDispatcher.sendPacketToPlayer(new PacketGuiData(payload).getPacket(), (Player) icrafting);
                //icrafting.sendProgressBarUpdate(this, 1, currentPower);
            }
            if (lastPortals != currentPortals)
            {
                icrafting.sendProgressBarUpdate(this, 2, currentPortals);
            }
            if (lastInstability != currentInstability)
            {
                icrafting.sendProgressBarUpdate(this, 3, currentInstability);
            }
            if (lastPowerState != currentPowerState)
            {
                icrafting.sendProgressBarUpdate(this, 4, currentPowerState);
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
        TileStabilizerMain stabilizer = (TileStabilizerMain) object;

        //if (par1 == 1)
        //{
        //    stabilizer.getEnergyStorage().setEnergyStored(par2);
        //}
        if (par1 == 2)
        {
            stabilizer.intActiveConnections = par2;
        }
        if (par1 == 3)
        {
            stabilizer.instability = par2;
        }
        if (par1 == 4)
        {
            stabilizer.powerState = par2;
        }
    }
}
