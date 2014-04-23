package enhancedportals.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileController;
import enhancedportals.client.gui.BaseGui;
import enhancedportals.client.gui.GuiTexturePortal;

public class ContainerTexturePortal extends BaseContainer
{
    TileController controller;

    public ContainerTexturePortal(TileController c, InventoryPlayer p)
    {
        super(null, p, GuiTexturePortal.CONTAINER_SIZE + BaseGui.bufferSpace + BaseGui.playerInventorySize);
        controller = c;
    }

    @Override
    public void handleGuiPacket(NBTTagCompound tag, EntityPlayer player)
    {
        if (tag.hasKey("colour"))
        {
            controller.setPortalColour(tag.getInteger("colour"));
        }
    }
}
