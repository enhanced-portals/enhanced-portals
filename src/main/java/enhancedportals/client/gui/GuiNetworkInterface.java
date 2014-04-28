package enhancedportals.client.gui;

import java.util.Arrays;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import uk.co.shadeddimensions.ep3.network.GuiHandler;
import uk.co.shadeddimensions.ep3.network.packet.PacketRequestGui;
import uk.co.shadeddimensions.ep3.portal.GlyphIdentifier;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileController;
import cpw.mods.fml.common.network.PacketDispatcher;
import enhancedportals.EnhancedPortals;
import enhancedportals.client.gui.elements.ElementGlyphDisplay;
import enhancedportals.client.gui.elements.ElementGlyphSelector;
import enhancedportals.inventory.ContainerNetworkInterface;

public class GuiNetworkInterface extends BaseGui
{
    public static final int CONTAINER_SIZE = 68;
    TileController controller;

    public GuiNetworkInterface(TileController c, EntityPlayer p)
    {
        super(new ContainerNetworkInterface(c, p.inventory), CONTAINER_SIZE);
        controller = c;
        name = "gui.networkInterface";
        setHidePlayerInventory();
    }
    
    @Override
    protected void mouseClicked(int x, int y, int button)
    {
        super.mouseClicked(x, y, button);

        if (x >= guiLeft + 7 && x <= guiLeft + 169 && y >= guiTop + 29 && y < guiTop + 47)
        {
            PacketDispatcher.sendPacketToServer(new PacketRequestGui(controller, GuiHandler.NETWORK_INTERFACE_B).getPacket());
        }
    }
    
    @Override
    public void initGui()
    {
        super.initGui();
        addElement(new ElementGlyphDisplay(this, 7, 29, controller.getIdentifierNetwork()));
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y)
    {
        super.drawGuiContainerForegroundLayer(x, y);
        fontRenderer.drawString(EnhancedPortals.localize("gui.networkIdentifier"), 7, 19, 0x404040);
        fontRenderer.drawString(EnhancedPortals.localize("gui.networkedPortals"), 7, 52, 0x404040);
        String s = controller.connectedPortals == -1 ? EnhancedPortals.localize("gui.notSet") : "" + controller.connectedPortals;
        fontRenderer.drawString(s, xSize - fontRenderer.getStringWidth(s) - 7, 52, 0x404040);

        if (x >= guiLeft + 7 && x <= guiLeft + 169 && y >= guiTop + 29 && y < guiTop + 47)
        {
            drawHoveringText(Arrays.asList(new String[] { EnhancedPortals.localize("gui.clickToModify") }), x - guiLeft, y - guiTop, fontRenderer);
        }
    }
}
