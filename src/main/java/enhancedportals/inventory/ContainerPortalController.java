package enhancedportals.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import enhancedportals.EnhancedPortals;
import enhancedportals.client.gui.BaseGui;
import enhancedportals.client.gui.GuiPortalController;
import enhancedportals.network.packet.PacketGui;
import enhancedportals.tileentity.portal.TileController;

public class ContainerPortalController extends BaseContainer
{
    TileController controller;

    public ContainerPortalController(TileController c, InventoryPlayer p)
    {
        super(null, p, GuiPortalController.CONTAINER_SIZE + BaseGui.bufferSpace + BaseGui.playerInventorySize);
        controller = c;
        hideInventorySlots();
    }

    @Override
    public void handleGuiPacket(NBTTagCompound tag, EntityPlayer player)
    {
        if (tag.hasKey("public"))
        {
            controller.isPublic = !controller.isPublic;
            EnhancedPortals.packetPipeline.sendTo(new PacketGui(controller), (EntityPlayerMP) player);
        }
    }
}
