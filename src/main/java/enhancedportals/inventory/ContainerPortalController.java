package enhancedportals.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import uk.co.shadeddimensions.ep3.network.PacketHandlerServer;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileController;
import enhancedportals.client.gui.BaseGui;
import enhancedportals.client.gui.GuiPortalController;

public class ContainerPortalController extends BaseContainer
{
    TileController controller;

    public ContainerPortalController(TileController c, InventoryPlayer p)
    {
        super(null, p, GuiPortalController.CONTAINER_SIZE + BaseGui.bufferSpace + BaseGui.playerInventorySize);
        controller = c;
    }

    @Override
    public void handleGuiPacket(NBTTagCompound tag, EntityPlayer player)
    {
        if (tag.hasKey("public"))
        {
            controller.isPublic = !controller.isPublic;
            PacketHandlerServer.sendGuiPacketToPlayer(controller, player);
        }
    }
}
