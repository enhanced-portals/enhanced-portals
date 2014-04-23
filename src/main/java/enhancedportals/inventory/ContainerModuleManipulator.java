package enhancedportals.inventory;

import enhancedportals.client.gui.BaseGui;
import enhancedportals.client.gui.GuiModuleManipulator;
import uk.co.shadeddimensions.ep3.client.gui.slot.SlotPortalModule;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileModuleManipulator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;

public class ContainerModuleManipulator extends BaseContainer
{
    TileModuleManipulator module;

    public ContainerModuleManipulator(TileModuleManipulator m, InventoryPlayer p)
    {
        super(m, p, GuiModuleManipulator.CONTAINER_SIZE + BaseGui.bufferSpace + BaseGui.playerInventorySize);
        module = m;
        
        for (int i = 0; i < 9; i++)
        {
            addSlotToContainer(new SlotPortalModule(module, i, 8 + i * 18, GuiModuleManipulator.CONTAINER_SIZE - 24));
        }
    }

    @Override
    public void handleGuiPacket(NBTTagCompound tag, EntityPlayer player)
    {

    }
}
