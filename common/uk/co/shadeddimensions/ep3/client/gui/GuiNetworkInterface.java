package uk.co.shadeddimensions.ep3.client.gui;

import java.util.Random;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import uk.co.shadeddimensions.ep3.client.gui.elements.ElementGlyphIdentifier;
import uk.co.shadeddimensions.ep3.client.gui.elements.ElementGlyphSelector;
import uk.co.shadeddimensions.ep3.lib.Localization;
import uk.co.shadeddimensions.ep3.network.ClientProxy;
import uk.co.shadeddimensions.ep3.portal.GlyphIdentifier;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;
import uk.co.shadeddimensions.ep3.util.GuiPayload;
import uk.co.shadeddimensions.library.gui.GuiBase;

public class GuiNetworkInterface extends GuiBase
{
    ElementGlyphSelector selector;
    ElementGlyphIdentifier identifier;
    TilePortalController controller;
    GuiButton resetButton, saveButton;
    boolean overlayActive;

    public GuiNetworkInterface(TilePortalController tile)
    {
        super(new ResourceLocation("enhancedportals", "textures/gui/networkInterface.png"));
        drawInventory = false;
        ySize = 144;
        controller = tile;
        overlayActive = false;
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (isShiftKeyDown())
        {
            if (button.id == resetButton.id) // Clear
            {
                 selector.reset();
            }
            else if (button.id == saveButton.id) // Random
            {
                Random random = new Random();
                GlyphIdentifier iden = new GlyphIdentifier();

                for (int i = 0; i < (isCtrlKeyDown() ? 9 : random.nextInt(8) + 1); i++)
                {
                    iden.addGlyph(random.nextInt(27));
                }

                 selector.setIdentifierTo(iden);
            }
        }
        else
        {
            if (button.id == resetButton.id) // Reset Changes
            {
                selector.setIdentifierTo(controller.getNetworkIdentifier());
                toggleState();
            }
            else if (button.id == saveButton.id) // Save Changes
            {
                GuiPayload payload = new GuiPayload();
                payload.data.setString("networkIdentifier", selector.getGlyphIdentifier().getGlyphString());
                ClientProxy.sendGuiPacket(payload);
                toggleState();
            }
        }
    }

    @Override
    protected void drawBackgroundTexture()
    {
        super.drawBackgroundTexture();
        
        if (overlayActive)
        {
            drawTexturedModalRect(guiLeft, guiTop + ySize - 106, 0, ySize, xSize, 106);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y)
    {
        super.drawGuiContainerForegroundLayer(x, y);

        drawCenteredString(fontRenderer, Localization.getGuiString("networkInterface"), xSize / 2, -13, 0xFFFFFF);
        fontRenderer.drawString(Localization.getGuiString("networkIdentifier"), 8, 8, 0x404040);
        fontRenderer.drawString(overlayActive ? Localization.getGuiString("glyphs") : Localization.getGuiString("networkInformation"), 8, 44, 0x404040);

        if (!overlayActive)
        {
            String s1 = controller.connectedPortals == -1 ? Localization.getGuiString("notSet") : "" + controller.connectedPortals;
            fontRenderer.drawString(Localization.getGuiString("networkedPortals"), 12, 57, 0x404040);
            fontRenderer.drawString(s1, xSize - 12 - fontRenderer.getStringWidth(s1), 57, 0x404040);

            if (x >= guiLeft + 7 && x <= guiLeft + xSize - 8)
            {
                if (y >= guiTop + 20 && y <= guiTop + 37)
                {
                    drawCreativeTabHoveringText(Localization.getGuiString("clickToModify"), x - guiLeft, y - guiTop);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();

        resetButton = new GuiButton(0, guiLeft + 10, guiTop + 117, (xSize - 20) / 2 - 5, 20, Localization.getGuiString("cancel"));
        saveButton = new GuiButton(1, guiLeft + xSize / 2 + 6, guiTop + 117, (xSize - 20) / 2 - 5, 20, Localization.getGuiString("save"));

        buttonList.add(resetButton);
        buttonList.add(saveButton);
        
        resetButton.drawButton = saveButton.drawButton = false;
    }
    
    @Override
    public void addElements()
    {
        selector = new ElementGlyphSelector(this, 7, 57);
        identifier = new ElementGlyphIdentifier(this, 7, 20, selector);
        
        identifier.setDisabled(!overlayActive);
        selector.setVisible(overlayActive);
        selector.setIdentifierTo(controller.getNetworkIdentifier());
        
        addElement(identifier);
        addElement(selector);
    }

    @Override
    protected void mouseClicked(int x, int y, int mouseButton)
    {
        if (!overlayActive)
        {
            if (x >= guiLeft + 7 && x <= guiLeft + xSize - 8)
            {
                if (y >= guiTop + 20 && y <= guiTop + 37)
                {
                    toggleState();
                    mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
                    return;
                }
            }
        }

        super.mouseClicked(x, y, mouseButton);
    }

    private void toggleState()
    {
        overlayActive = !overlayActive;
        resetButton.drawButton = saveButton.drawButton = overlayActive;
        identifier.setDisabled(!overlayActive);
        selector.setVisible(overlayActive);
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();

        if (overlayActive)
        {
            if (isShiftKeyDown())
            {
                resetButton.displayString = EnumChatFormatting.AQUA + Localization.getGuiString("clear");
                saveButton.displayString = (isCtrlKeyDown() ? EnumChatFormatting.GOLD : EnumChatFormatting.AQUA) + Localization.getGuiString("random");
            }
            else
            {
                resetButton.displayString = Localization.getGuiString("cancel");
                saveButton.displayString = Localization.getGuiString("save");
            }
        }
    }
}
