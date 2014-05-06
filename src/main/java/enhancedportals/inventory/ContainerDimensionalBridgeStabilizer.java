package enhancedportals.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.nbt.NBTTagCompound;
import enhancedportals.EnhancedPortals;
import enhancedportals.client.gui.BaseGui;
import enhancedportals.client.gui.GuiDimensionalBridgeStabilizer;
import enhancedportals.inventory.slot.SlotDBS;
import enhancedportals.network.packet.PacketGuiData;
import enhancedportals.tileentity.TileStabilizerMain;
import enhancedportals.utility.GeneralUtils;

public class ContainerDimensionalBridgeStabilizer extends BaseContainer
{
    int lastPower, lastPortals, lastInstability, lastPowerState;
    TileStabilizerMain stabilizer;

    public ContainerDimensionalBridgeStabilizer(TileStabilizerMain s, InventoryPlayer p)
    {
        super(s, p, (GeneralUtils.hasEnergyCost() ? GuiDimensionalBridgeStabilizer.CONTAINER_SIZE : GuiDimensionalBridgeStabilizer.CONTAINER_SIZE_SMALL) + BaseGui.bufferSpace + BaseGui.playerInventorySize);
        stabilizer = s;

        int container = GeneralUtils.hasEnergyCost() ? GuiDimensionalBridgeStabilizer.CONTAINER_SIZE : GuiDimensionalBridgeStabilizer.CONTAINER_SIZE_SMALL;
        addSlotToContainer(new SlotDBS(s, 0, 152, container - 25));
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        int currentPower = stabilizer.getEnergyStorage().getEnergyStored(), currentPortals = stabilizer.getActiveConnections(), currentInstability = stabilizer.instability, currentPowerState = stabilizer.powerState;

        for (int i = 0; i < crafters.size(); i++)
        {
            ICrafting icrafting = (ICrafting) crafters.get(i);

            if (lastPower != currentPower)
            {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setInteger("energy", currentPower);
                EnhancedPortals.packetPipeline.sendTo(new PacketGuiData(tag), (EntityPlayerMP) icrafting);
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
    public void handleGuiPacket(NBTTagCompound tag, EntityPlayer player)
    {
        if (tag.hasKey("button"))
        {
            stabilizer.powerState++;

            if (stabilizer.powerState >= 4)
            {
                stabilizer.powerState = 0;
            }
        }
        else if (tag.hasKey("energy"))
        {
            stabilizer.getEnergyStorage().setEnergyStored(tag.getInteger("energy"));
        }
    }

    @Override
    public void updateProgressBar(int par1, int par2)
    {
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
