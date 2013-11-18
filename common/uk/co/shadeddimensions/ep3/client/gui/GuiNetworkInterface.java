package uk.co.shadeddimensions.ep3.client.gui;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import uk.co.shadeddimensions.ep3.client.gui.element.ElementGlyphSelector;
import uk.co.shadeddimensions.ep3.client.gui.element.ElementGlyphViewer;
import uk.co.shadeddimensions.ep3.container.ContainerNetworkInterface;
import uk.co.shadeddimensions.ep3.lib.Reference;
import uk.co.shadeddimensions.ep3.network.ClientProxy;
import uk.co.shadeddimensions.ep3.portal.GlyphIdentifier;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;
import uk.co.shadeddimensions.ep3.util.GuiPayload;
import cofh.gui.GuiBase;

public class GuiNetworkInterface extends GuiBase
{    
    ElementGlyphSelector selector;
    ElementGlyphViewer viewer;
    TilePortalController controller;
    GuiButton resetButton, saveButton;
    boolean overlayActive;

    public GuiNetworkInterface(TilePortalController tile, EntityPlayer play)
    {
        super(new ContainerNetworkInterface(tile, play), new ResourceLocation("enhancedportals", "textures/gui/networkInterface.png"));
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
                
                for (int i = 0; i < (isCtrlKeyDown() ? 9 : (random.nextInt(8) + 1)); i++)
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
    protected void drawElements()
    {
        if (overlayActive)
        {
            Minecraft.getMinecraft().renderEngine.bindTexture(texture);
            drawTexturedModalRect(guiLeft, guiTop + ySize - 106, 0, ySize, xSize, 106);
        }
        
        super.drawElements();
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y)
    {
        super.drawGuiContainerForegroundLayer(x, y);

        drawCenteredString(fontRenderer, StatCollector.translateToLocal("tile." + Reference.SHORT_ID + ".portalFrame.networkInterface.name"), xSize / 2, -13, 0xFFFFFF);
        fontRenderer.drawString(StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".networkIdentifier"), 8, 8, 0x404040);
        fontRenderer.drawString(StatCollector.translateToLocal(overlayActive ? StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".glyphs") : "gui." + Reference.SHORT_ID + ".networkInfo"), 8, 44, 0x404040);
   
        if (!overlayActive)
        {
            String s1 = controller.connectedPortals == -1 ? StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".networkIdentifier.notSet") : "" + controller.connectedPortals;
            fontRenderer.drawString(StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".networkIdentifier.networkedPortals"), 12, 57, 0x404040);
            fontRenderer.drawString(s1, xSize - 12 - fontRenderer.getStringWidth(s1), 57, 0x404040);
            
            if (x >= guiLeft + 7 && x <= guiLeft + xSize - 8)
            {
                if (y >= guiTop + 20 && y <= guiTop + 37)
                {
                    drawCreativeTabHoveringText("Click to modify", x - guiLeft, y - guiTop);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();

        resetButton = new GuiButton(0, guiLeft + 10, guiTop + 117, (xSize - 20) / 2 - 5, 20, StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".button.cancel"));
        saveButton = new GuiButton(1, guiLeft + xSize / 2 + 6, guiTop + 117, (xSize - 20) / 2 - 5, 20, StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".button.save"));
        selector = (ElementGlyphSelector) new ElementGlyphSelector(this, 7, 57).setVisible(false);
        viewer = new ElementGlyphViewer(this, selector, 7, 20);

        buttonList.add(resetButton);
        buttonList.add(saveButton);
        addElement(selector);
        addElement(viewer);
        
        selector.setIdentifierTo(controller.getNetworkIdentifier());
        resetButton.drawButton = saveButton.drawButton = false;
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
                resetButton.displayString = EnumChatFormatting.AQUA + StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".button.clear");
                saveButton.displayString = (isCtrlKeyDown() ? EnumChatFormatting.GOLD : EnumChatFormatting.AQUA) + StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".button.random");
            }
            else
            {
                resetButton.displayString = StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".button.cancel");
                saveButton.displayString = StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".button.save");
            }
        }
    }
}
