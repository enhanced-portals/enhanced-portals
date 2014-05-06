package enhancedportals.client.gui;

import java.util.Arrays;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import enhancedportals.EnhancedPortals;
import enhancedportals.client.gui.elements.ElementGlyphDisplay;
import enhancedportals.client.gui.tabs.TabTip;
import enhancedportals.inventory.ContainerPortalController;
import enhancedportals.network.GuiHandler;
import enhancedportals.network.packet.PacketGuiData;
import enhancedportals.network.packet.PacketRequestGui;
import enhancedportals.tileentity.portal.TileController;

public class GuiPortalController extends BaseGui
{
    public static final int CONTAINER_SIZE = 78;
    TileController controller;
    GuiButton buttonLock;

    public GuiPortalController(TileController c, EntityPlayer p)
    {
        super(new ContainerPortalController(c, p.inventory), CONTAINER_SIZE);
        controller = c;
        name = "gui.portalController";
        setHidePlayerInventory();
    }

    @Override
    public void initGui()
    {
        super.initGui();
        buttonLock = new GuiButton(10, guiLeft + 7, guiTop + containerSize - 27, 162, 20, EnhancedPortals.localize("gui." + (controller.isPublic ? "public" : "private")));
        buttonList.add(buttonLock);
        addElement(new ElementGlyphDisplay(this, 7, 29, controller.getIdentifierUnique()));
        addTab(new TabTip(this, "privatePublic"));
    }

    @Override
    protected void mouseClicked(int x, int y, int button)
    {
        super.mouseClicked(x, y, button);

        if (x >= guiLeft + 7 && x <= guiLeft + 168 && y >= guiTop + 32 && y < guiTop + 47)
        {
            EnhancedPortals.packetPipeline.sendToServer(new PacketRequestGui(controller, GuiHandler.PORTAL_CONTROLLER_B));
        }
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == buttonLock.id)
        {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setBoolean("public", true);
            EnhancedPortals.packetPipeline.sendToServer(new PacketGuiData(tag));
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y)
    {
        super.drawGuiContainerForegroundLayer(x, y);
        getFontRenderer().drawString(EnhancedPortals.localize("gui.uniqueIdentifier"), 7, 19, 0x404040);

        if (x >= guiLeft + 7 && x <= guiLeft + 168 && y >= guiTop + 32 && y < guiTop + 47)
        {
            drawHoveringText(Arrays.asList(new String[] { EnhancedPortals.localize("gui.clickToModify") }), x - guiLeft, y - guiTop, getFontRenderer());
        }
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
        buttonLock.displayString = EnhancedPortals.localize("gui." + (controller.isPublic ? "public" : "private"));
    }
}
