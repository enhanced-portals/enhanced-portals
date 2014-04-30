package enhancedportals.inventory;

import enhancedportals.client.gui.BaseGui;
import enhancedportals.client.gui.GuiTextureFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileController;

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
    }
}
