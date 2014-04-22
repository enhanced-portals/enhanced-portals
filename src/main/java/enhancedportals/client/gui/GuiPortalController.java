package enhancedportals.client.gui;

import java.util.Arrays;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import uk.co.shadeddimensions.ep3.network.GuiHandler;
import uk.co.shadeddimensions.ep3.network.PacketHandlerClient;
import uk.co.shadeddimensions.ep3.network.packet.PacketRequestGui;
import uk.co.shadeddimensions.ep3.portal.GlyphIdentifier;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileController;
import cpw.mods.fml.common.network.PacketDispatcher;
import enhancedportals.EnhancedPortals;
import enhancedportals.client.gui.elements.ElementGlyphSelector;
import enhancedportals.client.gui.tabs.TabTip;
import enhancedportals.inventory.ContainerPortalController;

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
    }

    @Override
    public void initGui()
    {
        super.initGui();
        buttonLock = new GuiButton(10, guiLeft + 7, guiTop + containerSize - 27, 162, 20, EnhancedPortals.localize("gui." + (controller.isPublic ? "public" : "private")));
        buttonList.add(buttonLock);
        addTab(new TabTip(this, "privatePublic"));
    }

    @Override
    protected void mouseClicked(int x, int y, int button)
    {
        super.mouseClicked(x, y, button);

        if (x >= guiLeft + 7 && x <= guiLeft + 169 && y >= guiTop + 32 && y < guiTop + 50)
        {
            PacketDispatcher.sendPacketToServer(new PacketRequestGui(controller, GuiHandler.PORTAL_CONTROLLER_B).getPacket());
        }
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == buttonLock.id)
        {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setBoolean("public", true);
            PacketHandlerClient.sendGuiPacket(tag);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        super.drawGuiContainerBackgroundLayer(f, i, j);
        getMinecraft().renderEngine.bindTexture(new ResourceLocation("enhancedportals", "textures/gui/playerInventory.png"));
        drawTexturedModalRect(guiLeft + 7, guiTop + 29, 7, 7, 18 * 9, 18);
        getMinecraft().renderEngine.bindTexture(ElementGlyphSelector.glyphs);
        GlyphIdentifier g = controller.getIdentifierUnique();
        
        if (g != null)
        {
            for (int k = 0; k < 9; k++)
            {
                if (g.size() <= k)
                {
                    break;
                }
    
                int glyph = g.get(k), X2 = k % 9 * 18, X = glyph % 9 * 18, Y = glyph / 9 * 18;
                drawTexturedModalRect(guiLeft + 7 + X2, guiTop + 29, X, Y, 18, 18);
            }
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y)
    {
        super.drawGuiContainerForegroundLayer(x, y);
        fontRenderer.drawString(EnhancedPortals.localize("gui.uniqueIdentifier"), 7, 19, 0x404040);

        if (x >= guiLeft + 7 && x <= guiLeft + 169 && y >= guiTop + 32 && y < guiTop + 50)
        {
            drawHoveringText(Arrays.asList(new String[] { EnhancedPortals.localize("gui.clickToModify") }), x - guiLeft, y - guiTop, fontRenderer);
        }
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
        buttonLock.displayString = EnhancedPortals.localize("gui." + (controller.isPublic ? "public" : "private"));
    }
}
