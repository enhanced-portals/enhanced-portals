package enhancedportals.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import enhancedportals.client.gui.BaseGui;
import enhancedportals.client.gui.GuiTransferItem;
import enhancedportals.tileentity.portal.TileTransferItem;

public class ContainerTransferItem extends BaseContainer
{
    TileTransferItem item;

    public ContainerTransferItem(TileTransferItem i, InventoryPlayer p)
    {
        super(null, p, GuiTransferItem.CONTAINER_SIZE + BaseGui.bufferSpace + BaseGui.playerInventorySize);
        item = i;
    }

    @Override
    public void handleGuiPacket(NBTTagCompound tag, EntityPlayer player)
    {

    }
}
