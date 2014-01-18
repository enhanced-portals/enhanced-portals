package uk.co.shadeddimensions.ep3.client.gui;

import java.util.Random;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import uk.co.shadeddimensions.ep3.block.BlockFrame;
import uk.co.shadeddimensions.ep3.client.gui.elements.ElementGlyphIdentifier;
import uk.co.shadeddimensions.ep3.client.gui.elements.ElementGlyphSelector;
import uk.co.shadeddimensions.ep3.lib.Localization;
import uk.co.shadeddimensions.ep3.network.ClientProxy;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.portal.GlyphIdentifier;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;
import uk.co.shadeddimensions.ep3.util.GuiPayload;
import uk.co.shadeddimensions.library.gui.GuiBase;
import uk.co.shadeddimensions.library.gui.element.ElementItemIconWithCount;
import uk.co.shadeddimensions.library.gui.element.ElementItemStackPanel;

public class GuiPortalController extends GuiBase
{
    TilePortalController controller;
    GuiButton resetButton, saveButton;
    boolean overlayActive;

    ElementGlyphSelector selector;
    ElementGlyphIdentifier identifier;
    ElementItemStackPanel portalComponents;

    public GuiPortalController(TilePortalController tile)
    {
        super(new ResourceLocation("enhancedportals", "textures/gui/portalController.png"));
        ySize = 144;
        controller = tile;
        overlayActive = false;
        drawInventory = false;
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
                selector.setIdentifierTo(controller.getUniqueIdentifier());
                toggleState();
            }
            else if (button.id == saveButton.id) // Save Changes
            {
                GuiPayload payload = new GuiPayload();
                payload.data.setString("uniqueIdentifier", selector.getGlyphIdentifier().getGlyphString());
                ClientProxy.sendGuiPacket(payload);
                toggleState();
            }
        }
    }

    @Override
    public void addElements()
    {
        selector = new ElementGlyphSelector(this, 7, 57);
        identifier = new ElementGlyphIdentifier(this, 7, 20, selector);
        portalComponents = new ElementItemStackPanel(this, 10, 59, 158, 75);
        portalComponents.addElement(new ElementItemIconWithCount(this, 0, 0, new ItemStack(CommonProxy.blockPortal, controller.blockManager.getPortalCount())));
        portalComponents.addElement(new ElementItemIconWithCount(this, 0, 0, new ItemStack(CommonProxy.blockFrame, controller.blockManager.getFrameCount(), 0)));
        portalComponents.addElement(new ElementItemIconWithCount(this, 0, 0, new ItemStack(CommonProxy.blockFrame, 1, 1)));

        if (controller.blockManager.getRedstoneInterfaceCount() > 0)
        {
            portalComponents.addElement(new ElementItemIconWithCount(this, 0, 0, new ItemStack(CommonProxy.blockFrame, controller.blockManager.getRedstoneInterfaceCount(), BlockFrame.REDSTONE_INTERFACE)));
        }

        if (controller.blockManager.getHasNetworkInterface())
        {
            portalComponents.addElement(new ElementItemIconWithCount(this, 0, 0, new ItemStack(CommonProxy.blockFrame, 1, BlockFrame.NETWORK_INTERFACE)));
        }
        else if (controller.blockManager.getHasDialDevice())
        {
            portalComponents.addElement(new ElementItemIconWithCount(this, 0, 0, new ItemStack(CommonProxy.blockFrame, 1, BlockFrame.DIALLING_DEVICE)));
        }

        if (controller.blockManager.getHasBiometricIdentifier())
        {
            portalComponents.addElement(new ElementItemIconWithCount(this, 0, 0, new ItemStack(CommonProxy.blockFrame, 1, BlockFrame.BIOMETRIC_IDENTIFIER)));
        }

        if (controller.blockManager.getHasModuleManipulator())
        {
            portalComponents.addElement(new ElementItemIconWithCount(this, 0, 0, new ItemStack(CommonProxy.blockFrame, 1, BlockFrame.MODULE_MANIPULATOR)));
        }

        identifier.setDisabled(!overlayActive);
        portalComponents.setVisible(!overlayActive);
        selector.setVisible(overlayActive);
        selector.setIdentifierTo(controller.getUniqueIdentifier());

        addElement(identifier);
        addElement(portalComponents);
        addElement(selector);
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
        drawCenteredString(fontRenderer, Localization.getGuiString("portalController"), xSize / 2, -13, 0xFFFFFF);
        fontRenderer.drawString(Localization.getGuiString("uniqueIdentifier"), 8, 8, 0x404040);
        fontRenderer.drawString(overlayActive ? Localization.getGuiString("glyphs") : Localization.getGuiString("portalComponents"), 8, 44, 0x404040);

        if (!overlayActive)
        {
            if (x >= guiLeft + 7 && x <= guiLeft + xSize - 8)
            {
                if (y >= guiTop + 20 && y <= guiTop + 37)
                {
                    drawCreativeTabHoveringText(Localization.getGuiString("clickToModify"), x - guiLeft, y - guiTop);
                }
            }
        }

        super.drawGuiContainerForegroundLayer(x, y);
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

        resetButton.drawButton = saveButton.drawButton = overlayActive;
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
        portalComponents.setVisible(!overlayActive);
        identifier.setDisabled(!overlayActive);
        selector.setVisible(overlayActive);
        resetButton.drawButton = saveButton.drawButton = overlayActive;
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
