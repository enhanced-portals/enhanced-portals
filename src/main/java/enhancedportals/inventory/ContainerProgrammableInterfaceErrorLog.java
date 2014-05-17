package enhancedportals.inventory;

import enhancedportals.client.gui.GuiProgrammableInterface;
import enhancedportals.tileentity.portal.TileProgrammableInterface;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;

public class ContainerProgrammableInterfaceErrorLog extends BaseContainer
{
    TileProgrammableInterface program;

    public ContainerProgrammableInterfaceErrorLog(TileProgrammableInterface pi, InventoryPlayer p)
    {
        super(null, p, GuiProgrammableInterface.CONTAINER_SIZE);
        program = pi;
        hideInventorySlots();
    }

    @Override
    public void handleGuiPacket(NBTTagCompound tag, EntityPlayer player)
    {

    }
}
