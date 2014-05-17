package enhancedportals.inventory;

import enhancedportals.client.gui.GuiProgrammableInterface;
import enhancedportals.tileentity.TileProgrammableInterface;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;

public class ContainerProgrammableInterface extends BaseContainer
{
    TileProgrammableInterface program;

    public ContainerProgrammableInterface(TileProgrammableInterface pr, InventoryPlayer p)
    {
        super(null, p, GuiProgrammableInterface.CONTAINER_SIZE);
        program = pr;
        hideInventorySlots();
    }

    @Override
    public void handleGuiPacket(NBTTagCompound tag, EntityPlayer player)
    {

    }
}
