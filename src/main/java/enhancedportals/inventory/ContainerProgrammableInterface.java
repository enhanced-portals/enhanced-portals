package enhancedportals.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import enhancedportals.client.gui.GuiProgrammableInterface;
import enhancedportals.tileentity.TileProgrammableInterface;

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
