package enhancedportals.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import enhancedportals.client.gui.BaseGui;
import enhancedportals.client.gui.GuiTransferEnergy;
import enhancedportals.tileentity.portal.TileTransferEnergy;

public class ContainerTransferEnergy extends BaseContainer
{
    TileTransferEnergy energy;

    public ContainerTransferEnergy(TileTransferEnergy e, InventoryPlayer p)
    {
        super(null, p, GuiTransferEnergy.CONTAINER_SIZE + BaseGui.bufferSpace + BaseGui.playerInventorySize);
        energy = e;
    }

    @Override
    public void handleGuiPacket(NBTTagCompound tag, EntityPlayer player)
    {

    }
}
