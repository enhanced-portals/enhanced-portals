package enhancedportals.inventory;

import enhancedportals.client.gui.GuiManual;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;

public class ContainerManual extends BaseContainer
{
    public ContainerManual(InventoryPlayer p)
    {
        super(null, p);
        hideInventorySlots();
    }

    @Override
    public void handleGuiPacket(NBTTagCompound tag, EntityPlayer player)
    {
        
    }
}
