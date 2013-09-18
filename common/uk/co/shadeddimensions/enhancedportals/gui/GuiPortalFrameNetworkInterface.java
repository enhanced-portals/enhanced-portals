package uk.co.shadeddimensions.enhancedportals.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import uk.co.shadeddimensions.enhancedportals.container.ContainerNetworkInterface;
import uk.co.shadeddimensions.enhancedportals.network.ClientProxy;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameNetworkInterface;

public class GuiPortalFrameNetworkInterface extends GuiResizable
{
    GuiGlyphSelector glyphSelector;
    GuiGlyphViewer glyphViewer;
    
    TilePortalFrameNetworkInterface networkInterface;
    
    public GuiPortalFrameNetworkInterface(TilePortalFrameNetworkInterface tile)
    {
        super(new ContainerNetworkInterface(tile.getControllerValidated()), tile);
        
        networkInterface = tile;
        
        glyphSelector = new GuiGlyphSelector(7, 57, 0xffffff, this);
        glyphViewer = new GuiGlyphViewer(7, 20, 0xffffff, this, glyphSelector);
        
        MIN_HEIGHT = 143;
        MAX_HEIGHT = ySize;
        CURRENT_HEIGHT = MAX_HEIGHT;
        expanded = true;
        expanding = false;
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
        
        if (!expanded)
        {
            if (isShiftKeyDown())
            {
                ((GuiButton) buttonList.get(0)).displayString = EnumChatFormatting.AQUA + StatCollector.translateToLocal("gui.ep2.button.clear");
                ((GuiButton) buttonList.get(1)).displayString = (isCtrlKeyDown() ? EnumChatFormatting.GOLD : EnumChatFormatting.AQUA) + StatCollector.translateToLocal("gui.ep2.button.random");
            }
            else
            {
                ((GuiButton) buttonList.get(0)).displayString = StatCollector.translateToLocal("gui.ep2.button.cancel");
                ((GuiButton) buttonList.get(1)).displayString = StatCollector.translateToLocal("gui.ep2.button.save");
            }
        }
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        super.drawGuiContainerBackgroundLayer(f, i, j);
        
        GL11.glColor4f(1f, 1f, 1f, 1f);
        mc.renderEngine.bindTexture(new ResourceLocation("enhancedportals", "textures/gui/inventorySlots.png"));
        drawTexturedModalRect(guiLeft + 7, guiTop + 20, 0, 54, 162, 18);
        
        if (expanded)
        {
            drawTexturedModalRect(guiLeft + 7, guiTop + 57, 0, 54, 162, 16);
            drawTexturedModalRect(guiLeft + 7, guiTop + 142, 0, 56, 162, 16);
            drawTexturedModalRect(guiLeft + 7, guiTop + 73, 0, 56, 162, 14);
            drawTexturedModalRect(guiLeft + 7, guiTop + 87, 0, 56, 162, 14);
            drawTexturedModalRect(guiLeft + 7, guiTop + 100, 0, 56, 162, 14);
            drawTexturedModalRect(guiLeft + 7, guiTop + 114, 0, 56, 162, 14);
            drawTexturedModalRect(guiLeft + 7, guiTop + 128, 0, 56, 162, 14);
        }
        else if (((GuiButton) buttonList.get(0)).drawButton)
        {
            glyphSelector.drawBackground(i, j);
        }
        
        glyphViewer.drawBackground(i, j);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        super.drawGuiContainerForegroundLayer(par1, par2);
        
        fontRenderer.drawStringWithShadow(StatCollector.translateToLocal("tile.ep2.portalFrame.networkInterface.name"), xSize / 2 - fontRenderer.getStringWidth(StatCollector.translateToLocal("tile.ep2.portalFrame.networkInterface.name")) / 2, -13, 0xFFFFFF);
        fontRenderer.drawString(StatCollector.translateToLocal("gui.ep2.networkIdentifier"), 8, 8, 0x404040);
        fontRenderer.drawString(!((GuiButton) buttonList.get(0)).drawButton ? StatCollector.translateToLocal("gui.ep2.linkedPortals") : StatCollector.translateToLocal("gui.ep2.glyphs"), 8, 44, 0x404040);
        
        glyphViewer.drawForeground(par1, par2);
        
        if (((GuiButton) buttonList.get(0)).drawButton)
        {
            glyphSelector.drawForeground(par1, par2);
        }
        else if (!isChanging)
        {
            if (par1 >= guiLeft + 7 && par1 <= guiLeft + xSize - 8)
            {
                if (par2 >= guiTop + 20 && par2 <= guiTop + 37)
                {
                    List<String> list = new ArrayList<String>();
                    list.add(StatCollector.translateToLocal("gui.ep2.clickToModify"));

                    drawHoveringText(list, par1 - guiLeft, par2 - guiTop, fontRenderer);
                }
            }
        }
    }
    
    @Override
    protected void actionPerformed(GuiButton button)
    {        
        if (isShiftKeyDown())
        {
            if (button.id == 0) // Clear
            {
                glyphSelector.clearSelection();
            }
            else if (button.id == 1) // Random
            {
                glyphSelector.randomize(isCtrlKeyDown());
            }
        }
        else
        {
            if (button.id == 0) // Reset Changes
            {
                glyphSelector.setSelectedToIdentifier(networkInterface.NetworkIdentifier);
                
                if (!expanded)
                {
                    hideElements();
                }
                
                toggleState();
            }
            else if (button.id == 1) // Save Changes
            {
                ClientProxy.sendGuiPacket(0, glyphViewer.getSelectedIdentifier());
                
                if (!expanded)
                {
                    hideElements();
                }
                
                toggleState();
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();
        
        buttonList.add(new GuiButton(0, guiLeft + 7, guiTop + 115, ((xSize - 14) / 2) - 5, 20, StatCollector.translateToLocal("gui.ep2.button.cancel")));
        buttonList.add(new GuiButton(1, guiLeft + xSize - ((xSize - 14) / 2) - 2, guiTop + 115, ((xSize - 14) / 2) - 5, 20, StatCollector.translateToLocal("gui.ep2.button.save")));
        
        ((GuiButton) buttonList.get(0)).drawButton = ((GuiButton) buttonList.get(1)).drawButton = !expanded;
    }
        
    @Override
    protected void onExpandGui()
    {
        hideElements();
    }
    
    @Override
    protected void onShrinkGui()
    {
        showElements();
    }
    
    private void hideElements()
    {
        ((GuiButton) buttonList.get(0)).drawButton = ((GuiButton) buttonList.get(1)).drawButton = glyphSelector.canEdit = glyphViewer.canEdit = false;
    }
    
    private void showElements()
    {
        ((GuiButton) buttonList.get(0)).drawButton = ((GuiButton) buttonList.get(1)).drawButton = glyphSelector.canEdit = glyphViewer.canEdit = true;
    }

    @Override
    protected void mouseClicked(int par1, int par2, int mouseButton)
    {
        super.mouseClicked(par1, par2, mouseButton);
        
        glyphViewer.mouseClicked(par1, par2, mouseButton);
        glyphSelector.mouseClicked(par1, par2, mouseButton);

        if (par1 >= guiLeft + 7 && par1 <= guiLeft + xSize - 8)
        {
            if (par2 >= guiTop + 20 && par2 <= guiTop + 37)
            {
                if (expanded && !isChanging)
                {
                    toggleState();
                    mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
                }
            }
        }
    }
}
