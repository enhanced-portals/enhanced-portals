package uk.co.shadeddimensions.ep3.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.ep3.client.gui.elements.ElementGlyphIdentifier;
import uk.co.shadeddimensions.ep3.client.gui.elements.ElementGlyphSelector;
import uk.co.shadeddimensions.ep3.item.ItemPaintbrush;
import uk.co.shadeddimensions.ep3.item.ItemWrench;
import uk.co.shadeddimensions.ep3.lib.Localization;
import uk.co.shadeddimensions.ep3.network.ClientProxy;
import uk.co.shadeddimensions.ep3.network.PacketHandlerClient;
import uk.co.shadeddimensions.ep3.network.packet.PacketTextureData;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileController;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileDiallingDevice;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileDiallingDevice.GlyphElement;
import uk.co.shadeddimensions.library.gui.GuiBaseContainer;
import uk.co.shadeddimensions.library.gui.element.ElementButton;
import uk.co.shadeddimensions.library.gui.element.ElementButtonIcon;
import uk.co.shadeddimensions.library.gui.element.ElementScrollBar;
import uk.co.shadeddimensions.library.gui.element.ElementScrollPanel;
import uk.co.shadeddimensions.library.gui.element.ElementScrollPanelOverlay;
import cpw.mods.fml.common.network.PacketDispatcher;

public class GuiDiallingDevice extends GuiBaseContainer
{
    TileController controller;
    TileDiallingDevice dial;
    GuiButton cancelButton, acceptButton;
    GuiTextField textField;
    public boolean showOverlay = false;
    String warningMessage = "";
    int warningTimer = 0, elementsAdded;

    ElementGlyphIdentifier identifier;
    ElementGlyphSelector selector;
    ElementScrollPanel panel;

    public GuiDiallingDevice(TileDiallingDevice dialler)
    {
        super(new ResourceLocation("enhancedportals", "textures/gui/diallingDevice.png"));
        drawInventory = false;
        dial = dialler;
        controller = dialler.getPortalController();
        xSize = 256;
        ySize = 200;
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 1) // DIAL
        {
            if (controller.isPortalActive())
            {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setBoolean("DialTerminateRequest", true);
                PacketHandlerClient.sendGuiPacket(tag);
            }
            else if (selector.getGlyphIdentifier().size() > 0)
            {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setString("DialRequest", selector.getGlyphIdentifier().getGlyphString());
                PacketHandlerClient.sendGuiPacket(tag);
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
        }
        else if (button.id == cancelButton.id) // CANCEL
        {
            toggleState();
        }
        else if (button.id == acceptButton.id) // ACCEPT
        {
            if (!textField.getText().equals("") && selector.getGlyphIdentifier().size() > 0)
            {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setString("GlyphName", textField.getText());
                tag.setString("Glyphs", selector.getGlyphIdentifier().getGlyphString());
                PacketHandlerClient.sendGuiPacket(tag);
            }

            toggleState();
        }
    }

    @Override
    public void addElements()
    {
        selector = new ElementGlyphSelector(this, 7, 139);
        identifier = new ElementGlyphIdentifier(this, 7, 105, selector);
        panel = new ElementScrollPanelOverlay(this, 1, 20, xSize - 17, 81, texture, 20).setSides(true, true, false, false);

        addElement(panel);
        addElement(selector);
        addElement(identifier);
        addElement(new ElementScrollBar(this, xSize - 15, 20, 10, 82, panel));

        reloadList();
        
        if (controller.isPortalActive())
        {
            selector.setIdentifierTo(controller.getDestination());
            selector.setDisabled(true);
            panel.setDisabled(true);
        }
    }

    @Override
    public void drawGuiContainerBackgroundLayer(float f, int i, int j)
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
    public void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        fontRenderer.drawStringWithShadow(Localization.getGuiString("dialDevice"), xSize / 2 - fontRenderer.getStringWidth(Localization.getGuiString("dialDevice")) / 2, -13, showOverlay ? 0x444444 : 0xFFFFFF);
        fontRenderer.drawString(Localization.getGuiString("storedIdentifiers"), 7, 7, showOverlay ? 0x222222 : 0x404040);
        fontRenderer.drawString(Localization.getGuiString("glyphs"), 8, 127, showOverlay ? 0x222222 : 0x404040);

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
    public void handleElementButtonClick(String buttonName, int mouseButton)
    {
        int num = Integer.parseInt(buttonName.substring(1));

        if (buttonName.startsWith("D"))
        {
            selector.setIdentifierTo(dial.glyphList.get(num).identifier);
        }
        else if (buttonName.startsWith("T"))
        {
            ClientProxy.editingDialEntry = num;
            PacketDispatcher.sendPacketToServer(new PacketTextureData(num, dial.xCoord, dial.yCoord, dial.zCoord).getPacket());
            //GuiHandler.openGui(Minecraft.getMinecraft().thePlayer, dial, GuiHandler.TEXTURE_DIALLER);
        }
        else if (buttonName.startsWith("R"))
        {
            selector.reset();

            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("DeleteGlyph", num);
            PacketHandlerClient.sendGuiPacket(tag);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();

        cancelButton = new GuiButton(10, guiLeft + 20, guiTop + 70, 100, 20, Localization.getGuiString("cancel"));
        acceptButton = new GuiButton(11, guiLeft + xSize - 22 - 100, guiTop + 70, 100, 20, Localization.getGuiString("accept"));
        textField = new GuiTextField(fontRenderer, guiLeft + 20, guiTop + 47, xSize - 42, 20);

        buttonList.add(new GuiButton(1, guiLeft + 175, guiTop + 104, 75, 20, controller.isPortalActive() ? Localization.getGuiString("terminate") : Localization.getGuiString("dial")));
        buttonList.add(new GuiButton(2, guiLeft + 175, guiTop + 150, 75, 20, Localization.getGuiString("add")));
        buttonList.add(new GuiButton(3, guiLeft + 175, guiTop + 172, 75, 20, Localization.getGuiString("clear")));
        buttonList.add(cancelButton);
        buttonList.add(acceptButton);

        cancelButton.drawButton = acceptButton.drawButton = showOverlay;
        ((GuiButton) buttonList.get(0)).enabled = ((GuiButton) buttonList.get(1)).enabled = ((GuiButton) buttonList.get(2)).enabled = !showOverlay;
        
        if (controller.isPortalActive())
        {
            ((GuiButton) buttonList.get(1)).enabled = false;
            ((GuiButton) buttonList.get(2)).enabled = false;
        }
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

    void reloadList()
    {
        panel.clear();

        for (int i = 0; i < dial.glyphList.size(); i++)
        {
            GlyphElement glyph = dial.glyphList.get(i);
            panel.addElement(new ElementButton(this, 4, i * 21, 190, "D" + i, glyph.name));
            panel.addElement(new ElementButtonIcon(this, 195, i * 21, "T" + i, "Texture", ItemPaintbrush.instance.getIconFromDamage(0)));
            panel.addElement(new ElementButtonIcon(this, 216, i * 21, "R" + i, "Remove", ItemWrench.instance.getIconFromDamage(0)));
        }

        elementsAdded = dial.glyphList.size();
    }

    public void setWarningMessage(int type)
    {
        if (type == 0)
        {
            warningMessage = Localization.getGuiString("identifierCannotBeBlank");
            warningTimer = 100;
        }
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
    public void updateScreen()
    {
        super.updateScreen();

        if (showOverlay)
        {
            textField.updateCursorCounter();
            warningTimer = 0;
        }
        else
        {
            if (dial.glyphList.size() != elementsAdded)
            {
                reloadList();
            }
        }
        
        boolean portalActive = controller.isPortalActive();
        
        selector.setDisabled(portalActive);
        panel.setDisabled(portalActive);
        ((GuiButton) buttonList.get(1)).enabled = !showOverlay && !portalActive;
        ((GuiButton) buttonList.get(2)).enabled = !showOverlay && !portalActive;

        if (warningTimer > 0)
        {
            warningTimer--;
        }

        ((GuiButton) buttonList.get(0)).displayString = controller.isPortalActive() ? Localization.getGuiString("terminate") : Localization.getGuiString("dial");
    }
}
