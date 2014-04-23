package enhancedportals.inventory;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import enhancedportals.client.gui.BaseGui;
import enhancedportals.client.gui.GuiDimensionalBridgeStabilizer;
import uk.co.shadeddimensions.ep3.client.gui.slot.SlotDBS;
import uk.co.shadeddimensions.ep3.network.packet.PacketGuiData;
import uk.co.shadeddimensions.ep3.tileentity.TileStabilizerMain;
import uk.co.shadeddimensions.ep3.util.GeneralUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;

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
                PacketDispatcher.sendPacketToPlayer(new PacketGuiData(tag).getPacket(), (Player) icrafting);
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
