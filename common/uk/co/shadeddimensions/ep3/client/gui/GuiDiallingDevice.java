package uk.co.shadeddimensions.ep3.client.gui;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.ep3.client.gui.button.GuiBetterButton;
import uk.co.shadeddimensions.ep3.client.gui.elements.GuiGlyphIdentifierSelector;
import uk.co.shadeddimensions.ep3.client.gui.elements.GuiGlyphIdentifierViewer;
import uk.co.shadeddimensions.ep3.container.ContainerDiallingDevice;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;

public class GuiDiallingDevice extends GuiEnhancedPortals
{
    TilePortalController controller;
    GuiGlyphIdentifierSelector glyphSelector;
    GuiGlyphIdentifierViewer glyphViewer;
    
    public GuiDiallingDevice(TilePortalController control)
    {
        super(new ContainerDiallingDevice(control), control);
        controller = control;
        xSize = 256;
        ySize = 200;
        
        glyphSelector = new GuiGlyphIdentifierSelector(7, 140, this);
        glyphViewer = new GuiGlyphIdentifierViewer(7, 120, this, glyphSelector);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        super.drawGuiContainerBackgroundLayer(f, i, j);
        
        GL11.glColor4f(1f, 1f, 1f, 1F);
        getMinecraft().renderEngine.bindTexture(new ResourceLocation("enhancedportals", "textures/gui/diallingDevice.png"));
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        
        glyphSelector.drawBackground(i, j);
        glyphViewer.drawBackground(i, j);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        super.drawGuiContainerForegroundLayer(par1, par2);
                
        fontRenderer.drawStringWithShadow(StatCollector.translateToLocal("tile.ep3.portalFrame.dialDevice.name"), xSize / 2 - fontRenderer.getStringWidth(StatCollector.translateToLocal("tile.ep3.portalFrame.dialDevice.name")) / 2, -13, 0xFFFFFF);
        fontRenderer.drawString("Networks", 7, 7, 0x404040);
        fontRenderer.drawString("Glyphs", 7, 107, 0x404040);
    }
    
    @Override
    protected void mouseClicked(int par1, int par2, int mouseButton)
    {
        glyphSelector.mouseClicked(par1, par2, mouseButton);
        glyphViewer.mouseClicked(par1, par2, mouseButton);
        
        super.mouseClicked(par1, par2, mouseButton);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();
        
        buttonList.add(new GuiBetterButton(0, guiLeft + 175, guiTop + 120, 75, "Dial"));
        buttonList.add(new GuiBetterButton(2, guiLeft + 175, guiTop + 146, 75, "Add"));
        buttonList.add(new GuiBetterButton(3, guiLeft + 175, guiTop + 173, 75, "Clear"));
    }
}
