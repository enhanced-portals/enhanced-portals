package uk.co.shadeddimensions.ep3.client.gui.scroll;

import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.network.PacketDispatcher;
import uk.co.shadeddimensions.ep3.client.gui.GuiDiallingDevice;
import uk.co.shadeddimensions.ep3.client.gui.base.GuiEnhancedPortals;
import uk.co.shadeddimensions.ep3.lib.GUIs;
import uk.co.shadeddimensions.ep3.network.ClientProxy;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.network.packet.PacketTextureData;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileDiallingDevice;
import uk.co.shadeddimensions.ep3.util.GuiPayload;

public class GuiDialList extends GuiScrollList
{
    TileDiallingDevice dialDevice;
    public int selectedElement;

    public GuiDialList(GuiEnhancedPortals p, int X, int Y, int W, int H, TileDiallingDevice tile)
    {
        super(p, X, Y, W, H);
        dialDevice = tile;
        selectedElement = -1;
    }

    @Override
    protected void elementClicked(int i, int mouseX, int mouseY, boolean wasDoubleClick)
    {
        if (wasDoubleClick)
        {
            if (mouseX >= parent.getGuiLeft() + x && mouseX <= parent.getGuiLeft() + x + w - 33)
            {
                selectedElement = i;
                ((GuiDiallingDevice) parent).selectionChanged(i, false);
                parent.getMinecraft().sndManager.playSoundFX("random.click", 1.0F, 1.0F);
            }
            else if (mouseX >= parent.getGuiLeft() + x + w - 15 && mouseX <= parent.getGuiLeft() + x + w && !dialDevice.getPortalController().isPortalActive)
            {
                GuiPayload p = new GuiPayload();
                p.data.setInteger("DeleteGlyph", i);
                ClientProxy.sendGuiPacket(p);
                selectedElement = -1;
                ((GuiDiallingDevice) parent).selectionChanged(i, true);
                parent.getMinecraft().sndManager.playSoundFX("random.click", 1.0F, 1.0F);
            }
            else if (mouseX >= parent.getGuiLeft() + x + w - 31 && mouseX <= parent.getGuiLeft() + x + w - 17 && !dialDevice.getPortalController().isPortalActive)
            {
                ClientProxy.editingDialEntry = i;
                PacketDispatcher.sendPacketToServer(new PacketTextureData(i, dialDevice.xCoord, dialDevice.yCoord, dialDevice.zCoord).getPacket());
                CommonProxy.openGui(parent.getMinecraft().thePlayer, GUIs.TexturesDiallingDevice, dialDevice.getPortalController());
                parent.getMinecraft().sndManager.playSoundFX("random.click", 1.0F, 1.0F);
            }
        }
    }

    @Override
    protected void drawElement(int i, int x, int y, boolean isSelected, int mouseX, int mouseY)
    {
        float colour = i == selectedElement ? 0.7f : 1f;
        boolean isMouseOver = isMouseOver(i, mouseX, mouseY);
        String s = dialDevice.glyphList.get(i).name;

        parent.drawSmallButton(x, y, w - 32, (isMouseOver && mouseX >= x && mouseX <= x + w - 33) || i == selectedElement, false, colour, colour, colour);
        parent.drawTinyButton(x + w - 15, y, isMouseOver && mouseX >= x + w - 15 && mouseX <= x + w, false, 1f, 1f, 1f, 3, !dialDevice.getPortalController().isPortalActive);
        parent.drawTinyButton(x + w - 31, y, isMouseOver && mouseX >= x + w - 31 && mouseX <= x + w - 17, false, 1f, 1f, 1f, 4, !dialDevice.getPortalController().isPortalActive);
        parent.getFontRenderer().drawStringWithShadow(s, x + ((w - 32) / 2) - (parent.getFontRenderer().getStringWidth(s) / 2), y + 3, 0xFFFFFF);
    }

    @Override
    protected void drawOverlays()
    {
        parent.getMinecraft().renderEngine.bindTexture(new ResourceLocation("enhancedportals", "textures/gui/diallingDevice.png"));
        GL11.glColor4f(1f, 1f, 1f, 1f);
        drawTexturedModalRect(parent.getGuiLeft() + x, parent.getGuiTop() + y - getEntryHeight(), x, y - getEntryHeight(), w, getEntryHeight());
        drawTexturedModalRect(parent.getGuiLeft() + x, parent.getGuiTop() + y + h, x, y + h, w, getEntryHeight());
    }

    @Override
    protected int getElementCount()
    {
        return dialDevice.glyphList.size();
    }

    @Override
    protected int getEntryHeight()
    {
        return 15;
    }

    @Override
    protected int getEntrySpacing()
    {
        return 1;
    }

    @Override
    protected void drawBackgroundStart()
    {

    }

    @Override
    protected void drawBackgroundEnd()
    {

    }
}
