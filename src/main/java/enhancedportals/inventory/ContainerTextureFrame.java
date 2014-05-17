package enhancedportals.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import enhancedportals.client.gui.BaseGui;
import enhancedportals.client.gui.GuiTextureFrame;
import enhancedportals.tileentity.TileController;

public class ContainerTextureFrame extends BaseContainer
{
    protected TileController controller;

    public ContainerTextureFrame(TileController c, InventoryPlayer p)
    {
        super(null, p, GuiTextureFrame.CONTAINER_SIZE + BaseGui.bufferSpace + BaseGui.playerInventorySize, 7);
        controller = c;
    }

    @Override
    public void handleGuiPacket(NBTTagCompound tag, EntityPlayer player)
    {
        if (tag.hasKey("colour"))
        {
            controller.setFrameColour(tag.getInteger("colour"));
        }
        else if (tag.hasKey("custom"))
        {
            controller.setCustomFrameTexture(tag.getInteger("custom"));
        }
        else if (tag.hasKey("removeItem"))
        {
            controller.setFrameItem(null);
        }
        else if (tag.hasKey("id") && tag.hasKey("Damage"))
        {
            controller.setFrameItem(ItemStack.loadItemStackFromNBT(tag));
        }
    }
}
