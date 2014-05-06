package enhancedportals.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import enhancedportals.EnhancedPortals;
import enhancedportals.client.gui.BaseGui;
import enhancedportals.client.gui.GuiNetworkInterfaceGlyphs;
import enhancedportals.network.GuiHandler;
import enhancedportals.portal.GlyphIdentifier;
import enhancedportals.tileentity.portal.TileController;

public class ContainerNetworkInterfaceGlyphs extends BaseContainer
{
    TileController controller;

    public ContainerNetworkInterfaceGlyphs(TileController c, InventoryPlayer p)
    {
        super(null, p, GuiNetworkInterfaceGlyphs.CONTAINER_SIZE + BaseGui.bufferSpace + BaseGui.playerInventorySize);
        controller = c;
        hideInventorySlots();
    }

    @Override
    public void handleGuiPacket(NBTTagCompound tag, EntityPlayer player)
    {
        if (tag.hasKey("nid"))
        {
            controller.setIdentifierNetwork(new GlyphIdentifier(tag.getString("nid")));
            player.openGui(EnhancedPortals.instance, GuiHandler.NETWORK_INTERFACE_A, controller.getWorldObj(), controller.xCoord, controller.yCoord, controller.zCoord);
        }
    }
}
