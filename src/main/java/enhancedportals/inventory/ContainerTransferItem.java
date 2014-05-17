package enhancedportals.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import enhancedportals.EnhancedPortals;
import enhancedportals.client.gui.BaseGui;
import enhancedportals.client.gui.GuiTransferItem;
import enhancedportals.network.packet.PacketGuiData;
import enhancedportals.tileentity.TileTransferItem;

public class ContainerTransferItem extends BaseContainer
{
    TileTransferItem item;
    byte wasSending = -1;

    public ContainerTransferItem(TileTransferItem i, InventoryPlayer p)
    {
        super(i, p, GuiTransferItem.CONTAINER_SIZE + BaseGui.bufferSpace + BaseGui.playerInventorySize);
        item = i;
        addSlotToContainer(new Slot(i, 0, 152, 23));
    }

    @Override
    public void handleGuiPacket(NBTTagCompound tag, EntityPlayer player)
    {
        item.isSending = !item.isSending;
    }

    @Override
    public void updateProgressBar(int id, int val)
    {
        if (id == 0)
        {
            item.isSending = val == 1;
        }
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        byte isSending = (byte) (item.isSending ? 1 : 0);

        for (int i = 0; i < crafters.size(); i++)
        {
            ICrafting icrafting = (ICrafting) crafters.get(i);

            if (wasSending != isSending)
            {
                icrafting.sendProgressBarUpdate(this, 0, isSending);
            }

            wasSending = isSending;
        }
    }
}
