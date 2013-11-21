package uk.co.shadeddimensions.ep3.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.ep3.client.gui.element.ElementDialDeviceScrollList;
import uk.co.shadeddimensions.ep3.client.gui.element.ElementGlyphSelector;
import uk.co.shadeddimensions.ep3.client.gui.element.ElementGlyphViewer;
import uk.co.shadeddimensions.ep3.container.ContainerDiallingDevice;
import uk.co.shadeddimensions.ep3.lib.GUIs;
import uk.co.shadeddimensions.ep3.lib.Localization;
import uk.co.shadeddimensions.ep3.network.ClientProxy;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.network.packet.PacketTextureData;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileDiallingDevice;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;
import uk.co.shadeddimensions.ep3.util.GuiPayload;
import cofh.gui.GuiBase;
import cofh.gui.element.ElementBase;
import cpw.mods.fml.common.network.PacketDispatcher;

public class GuiDiallingDevice extends GuiBase implements IElementHandler
{
    TilePortalController controller;
    TileDiallingDevice dial;
    GuiButton cancelButton, acceptButton;
    GuiTextField textField;
    public boolean showOverlay;
    String warningMessage;
    int warningTimer;

    ElementGlyphSelector selector;
    ElementDialDeviceScrollList list;

    public GuiDiallingDevice(TileDiallingDevice dialler, EntityPlayer player)
    {
        super(new ContainerDiallingDevice(dialler, player), new ResourceLocation("enhancedportals", "textures/gui/diallingDevice.png"));
        dial = dialler;
        controller = dialler.getPortalController();
        xSize = 256;
        ySize = 200;
        warningMessage = "";
        warningTimer = 0;
        drawInventory = showOverlay = false;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        super.drawGuiContainerBackgroundLayer(f, i, j);

        if (showOverlay)
        {
            drawDefaultBackground();

            GL11.glColor4f(1f, 1f, 1f, 1F);
            Minecraft.getMinecraft().renderEngine.bindTexture(texture);
            drawTexturedModalRect(guiLeft + 14, guiTop + 40, 0, ySize, xSize - 30, 56);

            textField.drawTextBox();
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        super.drawGuiContainerForegroundLayer(par1, par2);

        fontRenderer.drawStringWithShadow(Localization.getGuiString("dialDevice"), xSize / 2 - fontRenderer.getStringWidth(Localization.getGuiString("dialDevice")) / 2, -13, showOverlay ? 0x444444 : 0xFFFFFF);
        fontRenderer.drawString(Localization.getGuiString("storedIdentifiers"), 7, 7, showOverlay ? 0x222222 : 0x404040);
        fontRenderer.drawString(Localization.getGuiString("glyphs"), 7, 127, showOverlay ? 0x222222 : 0x404040);

        if (showOverlay)
        {
            fontRenderer.drawStringWithShadow(Localization.getGuiString("identifierName"), xSize / 2 - fontRenderer.getStringWidth(Localization.getGuiString("identifierName")) / 2, 25, 0xFFFFFF);
        }

        if (warningTimer > 0)
        {
            fontRenderer.drawString(warningMessage, xSize - 10 - fontRenderer.getStringWidth(warningMessage), 128, 0xFF0000);
        }
    }

    @Override
    protected void mouseClicked(int par1, int par2, int mouseButton)
    {
        if (showOverlay)
        {
            if (acceptButton.mousePressed(mc, par1, par2))
            {
                mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
                actionPerformed(acceptButton);
            }
            else if (cancelButton.mousePressed(mc, par1, par2))
            {
                mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
                actionPerformed(cancelButton);
            }

            textField.mouseClicked(par1, par2, mouseButton);
            return;
        }

        super.mouseClicked(par1, par2, mouseButton);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();

        selector = new ElementGlyphSelector(this, 7, 139);
        list = new ElementDialDeviceScrollList(this, texture, dial, 0, 20, xSize, 80);

        addElement(list);
        addElement(selector);
        addElement(new ElementGlyphViewer(this, selector, 7, 105));

        cancelButton = new GuiButton(10, guiLeft + 20, guiTop + 70, 100, 20, Localization.getGuiString("cancel"));
        acceptButton = new GuiButton(11, guiLeft + xSize - 22 - 100, guiTop + 70, 100, 20, Localization.getGuiString("accept"));
        textField = new GuiTextField(fontRenderer, guiLeft + 20, guiTop + 47, xSize - 42, 20);

        buttonList.add(new GuiButton(1, guiLeft + 175, guiTop + 104, 75, 20, controller.isPortalActive ? Localization.getGuiString("terminate") : Localization.getGuiString("dial")));
        buttonList.add(new GuiButton(2, guiLeft + 175, guiTop + 150, 75, 20, Localization.getGuiString("add")));
        buttonList.add(new GuiButton(3, guiLeft + 175, guiTop + 172, 75, 20, Localization.getGuiString("clear")));
        buttonList.add(cancelButton);
        buttonList.add(acceptButton);

        cancelButton.drawButton = acceptButton.drawButton = showOverlay;
        ((GuiButton) buttonList.get(0)).enabled = ((GuiButton) buttonList.get(1)).enabled = ((GuiButton) buttonList.get(2)).enabled = !showOverlay;

    }

    private void setWarningMessage(int type)
    {
        if (type == 0)
        {
            warningMessage = Localization.getGuiString("identifierCannotBeBlank");
            warningTimer = 100;
        }
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 1) // DIAL
        {
            if (controller.isPortalActive)
            {
                GuiPayload payload = new GuiPayload();
                payload.data.setBoolean("DialTerminateRequest", true);
                ClientProxy.sendGuiPacket(payload);
            }
            else if (selector.getGlyphIdentifier().size() > 0)
            {
                GuiPayload payload = new GuiPayload();
                payload.data.setString("DialRequest", selector.getGlyphIdentifier().getGlyphString());
                ClientProxy.sendGuiPacket(payload);
            }
            else
            {
                setWarningMessage(0);
            }
        }
        else if (button.id == 2) // ADD
        {
            if (selector.getGlyphIdentifier().size() == 0)
            {
                setWarningMessage(0);
                return;
            }

            toggleState();
        }
        else if (button.id == 3) // CLEAR
        {
            selector.reset();
            list.setSelected(-1);
        }
        else if (button.id == cancelButton.id) // CANCEL
        {
            toggleState();
        }
        else if (button.id == acceptButton.id) // ACCEPT
        {
            if (!textField.getText().equals("") && selector.getGlyphIdentifier().size() > 0)
            {
                GuiPayload payload = new GuiPayload();
                payload.data.setString("GlyphName", textField.getText());
                payload.data.setString("Glyphs", selector.getGlyphIdentifier().getGlyphString());
                ClientProxy.sendGuiPacket(payload);
            }

            toggleState();
        }
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();

        if (showOverlay)
        {
            textField.updateCursorCounter();
            warningTimer = 0;
        }

        if (warningTimer > 0)
        {
            warningTimer--;
        }

        ((GuiButton) buttonList.get(0)).displayString = controller.isPortalActive ? Localization.getGuiString("terminate") : Localization.getGuiString("dial");
    }

    @Override
    protected void keyTyped(char par1, int par2)
    {
        if (showOverlay)
        {
            if (textField.textboxKeyTyped(par1, par2))
            {
                return;
            }
        }

        super.keyTyped(par1, par2);
    }

    private void toggleState()
    {
        showOverlay = !showOverlay;
        textField.setText(Localization.getGuiString("name"));
        cancelButton.drawButton = acceptButton.drawButton = showOverlay;
        Keyboard.enableRepeatEvents(showOverlay);
        ((GuiButton) buttonList.get(0)).enabled = ((GuiButton) buttonList.get(1)).enabled = ((GuiButton) buttonList.get(2)).enabled = !showOverlay;
    }

    @Override
    public void onElementChanged(ElementBase element, Object data)
    {
        if (element instanceof ElementDialDeviceScrollList && !showOverlay)
        {
            int[] obj = (int[]) data;
            int type = obj[0], entry = obj[1];

            if (type == 0 && entry >= 0 && entry < dial.glyphList.size())
            {
                // select
                selector.setIdentifierTo(dial.glyphList.get(entry).identifier);
                Minecraft.getMinecraft().sndManager.playSoundFX("random.click", 1.0F, 1.0F);
            }
            else if (type == 1 && entry >= 0 && entry < dial.glyphList.size() && !controller.isPortalActive)
            {
                // texture
                ClientProxy.editingDialEntry = entry;
                PacketDispatcher.sendPacketToServer(new PacketTextureData(entry, dial.xCoord, dial.yCoord, dial.zCoord).getPacket());
                CommonProxy.openGui(Minecraft.getMinecraft().thePlayer, GUIs.TexturesDiallingDevice, dial);
                Minecraft.getMinecraft().sndManager.playSoundFX("random.click", 1.0F, 1.0F);

            }
            else if (type == 2 && entry >= 0 && entry < dial.glyphList.size() && !controller.isPortalActive)
            {
                // remove
                selector.reset();

                GuiPayload p = new GuiPayload();
                p.data.setInteger("DeleteGlyph", entry);
                ClientProxy.sendGuiPacket(p);
                Minecraft.getMinecraft().sndManager.playSoundFX("random.click", 1.0F, 1.0F);
            }
        }
    }
}
