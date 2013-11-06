package uk.co.shadeddimensions.ep3.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.ep3.client.gui.base.GuiEnhancedPortals;
import uk.co.shadeddimensions.ep3.client.gui.button.GuiBetterButton;
import uk.co.shadeddimensions.ep3.client.gui.elements.GuiGlyphIdentifierSelector;
import uk.co.shadeddimensions.ep3.client.gui.elements.GuiGlyphIdentifierViewer;
import uk.co.shadeddimensions.ep3.client.gui.scroll.GuiDialList;
import uk.co.shadeddimensions.ep3.container.ContainerDiallingDevice;
import uk.co.shadeddimensions.ep3.network.ClientProxy;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileDiallingDevice;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;
import uk.co.shadeddimensions.ep3.util.GuiPayload;

public class GuiDiallingDevice extends GuiEnhancedPortals
{
    TilePortalController controller;
    TileDiallingDevice dial;
    GuiGlyphIdentifierSelector glyphSelector;
    GuiGlyphIdentifierViewer glyphViewer;
    GuiDialList dialList;
    GuiBetterButton cancelButton, acceptButton;
    GuiTextField textField;
    public boolean showOverlay = false;
    String warningMessage;
    int warningTimer;

    public GuiDiallingDevice(TileDiallingDevice dialler, EntityPlayer player)
    {
        super(new ContainerDiallingDevice(dialler, player), dialler);
        dial = dialler;
        controller = dialler.getPortalController();
        xSize = 256;
        ySize = 200;        
        glyphSelector = new GuiGlyphIdentifierSelector(7, 140, this);
        glyphViewer = new GuiGlyphIdentifierViewer(7, 104, this, glyphSelector);
        dialList = new GuiDialList(this, xSize / 2 - 232 / 2, 20, 232, 80, dialler);
        warningMessage = "";
        warningTimer = 0;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        super.drawGuiContainerBackgroundLayer(f, i, j);

        GL11.glColor4f(1f, 1f, 1f, 1F);
        getMinecraft().renderEngine.bindTexture(new ResourceLocation("enhancedportals", "textures/gui/diallingDevice.png"));
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        dialList.drawBackground(i, j);
        glyphSelector.drawBackground(i, j);
        glyphViewer.drawBackground(i, j);

        if (showOverlay)
        {
            drawDefaultBackground();

            GL11.glColor4f(1f, 1f, 1f, 1F);
            getMinecraft().renderEngine.bindTexture(new ResourceLocation("enhancedportals", "textures/gui/diallingDevice.png"));
            drawTexturedModalRect(guiLeft + 14, guiTop + 40, 0, ySize, xSize - 30, 56);

            textField.drawTextBox();
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        super.drawGuiContainerForegroundLayer(par1, par2);

        fontRenderer.drawStringWithShadow(StatCollector.translateToLocal("tile.ep3.portalFrame.dialDevice.name"), xSize / 2 - fontRenderer.getStringWidth(StatCollector.translateToLocal("tile.ep3.portalFrame.dialDevice.name")) / 2, -13, showOverlay ? 0x444444 : 0xFFFFFF);

        fontRenderer.drawString("Stored Identifiers", 7, 7, showOverlay ? 0x222222 : 0x404040);
        fontRenderer.drawString("Glyphs", 7, 127, showOverlay ? 0x222222 : 0x404040);

        if (showOverlay)
        {
            fontRenderer.drawStringWithShadow("Identifier Name", xSize / 2 - fontRenderer.getStringWidth("Identifier Name") / 2, 25, 0xFFFFFF);
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
            if (acceptButton.mousePressed(this.mc, par1, par2))
            {
                this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
                this.actionPerformed(acceptButton);
            }
            else if (cancelButton.mousePressed(this.mc, par1, par2))
            {
                this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
                this.actionPerformed(cancelButton);
            }

            textField.mouseClicked(par1, par2, mouseButton);            
            return;
        }

        glyphSelector.mouseClicked(par1, par2, mouseButton);
        glyphViewer.mouseClicked(par1, par2, mouseButton);

        super.mouseClicked(par1, par2, mouseButton);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();

        cancelButton = new GuiBetterButton(10, guiLeft + 20, guiTop + 70, 100, "Cancel");
        acceptButton = new GuiBetterButton(11, guiLeft + xSize - 22 - 100, guiTop + 70, 100, "Accept");
        textField = new GuiTextField(fontRenderer, guiLeft + 20, guiTop + 47, xSize - 42, 20);

        buttonList.add(new GuiBetterButton(1, guiLeft + 175, guiTop + 103, 75, controller.isPortalActive ? "Terminate" : "Dial"));
        buttonList.add(new GuiBetterButton(2, guiLeft + 175, guiTop + 150, 75, "Add"));
        buttonList.add(new GuiBetterButton(3, guiLeft + 175, guiTop + 173, 75, "Clear"));
        buttonList.add(cancelButton);
        buttonList.add(acceptButton);

        cancelButton.drawButton = acceptButton.drawButton = showOverlay;
        ((GuiBetterButton) buttonList.get(0)).enabled = ((GuiBetterButton) buttonList.get(1)).enabled = ((GuiBetterButton) buttonList.get(2)).enabled = !showOverlay;

        dialList.init();
    }

    private void setWarningMessage(int type)
    {
        if (type == 0)
        {
            warningMessage = "Identifier cannot be blank";
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
            else if (glyphSelector.getSelectedIdentifier().size() > 0)
            {
                GuiPayload payload = new GuiPayload();
                payload.data.setString("DialRequest", glyphSelector.getSelectedIdentifier().getGlyphString());
                ClientProxy.sendGuiPacket(payload);
            }
            else
            {
                setWarningMessage(0);
            }
        }
        else if (button.id == 2) // ADD
        {
            if (glyphSelector.getSelectedIdentifier().size() == 0)
            {
                setWarningMessage(0);
                return;
            }

            toggleState();
        }
        else if (button.id == 3) // CLEAR
        {
            glyphSelector.clearSelection();
            dialList.selectedElement = -1;
        }
        else if (button.id == cancelButton.id) // CANCEL
        {
            toggleState();
        }
        else if (button.id == acceptButton.id) // ACCEPT
        {
            if (!textField.getText().equals("") && glyphSelector.getSelectedIdentifier().size() > 0)
            {
                GuiPayload payload = new GuiPayload();
                payload.data.setString("GlyphName", textField.getText());
                payload.data.setString("Glyphs", glyphSelector.getSelectedIdentifier().getGlyphString());
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

        ((GuiBetterButton) buttonList.get(0)).displayString = controller.isPortalActive ? "Terminate" : "Dial";
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
        textField.setText("Name");
        cancelButton.drawButton = acceptButton.drawButton = showOverlay;
        Keyboard.enableRepeatEvents(showOverlay);
        ((GuiBetterButton) buttonList.get(0)).enabled = ((GuiBetterButton) buttonList.get(1)).enabled = ((GuiBetterButton) buttonList.get(2)).enabled = !showOverlay;
    }

    public void selectionChanged(int newSelection, boolean wasRemoved)
    {
        glyphSelector.clearSelection();

        if (!wasRemoved)
        {
            if (newSelection >= dial.glyphList.size())
            {
                return;
            }

            glyphSelector.setGlyphsToIdentifier(dial.glyphList.get(newSelection).identifier);
        }
    }
}
