package uk.co.shadeddimensions.enhancedportals.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import uk.co.shadeddimensions.enhancedportals.container.ContainerPortalFrameController;
import uk.co.shadeddimensions.enhancedportals.lib.Reference;
import uk.co.shadeddimensions.enhancedportals.network.ClientProxy;
import uk.co.shadeddimensions.enhancedportals.portal.ClientBlockManager;
import uk.co.shadeddimensions.enhancedportals.tileentity.frame.TilePortalController;

public class GuiPortalFrameController extends GuiResizable
{
    class TipLedger extends Ledger
    {
        int headerColour = 0xe1c92f;
        int subheaderColour = 0xaaafb8;
        int textColour = 0x000000;
        int currentTip = 0;

        @SuppressWarnings("rawtypes")
        public TipLedger()
        {
            overlayColor = 0x5396da;

            if (maxHeight == 24)
            {
                List list = getMinecraft().fontRenderer.listFormattedStringToWidth(getTip(), maxWidth - 16);
                maxHeight = 24 + list.size() * getMinecraft().fontRenderer.FONT_HEIGHT + 5;
            }
        }

        @Override
        public void draw(int x, int y)
        {
            drawBackground(x, y);

            if (isFullyOpened())
            {
                drawString(fontRenderer, StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".didYouKnow"), x + 20, y + 8, headerColour);

                fontRenderer.drawSplitString(getTip(), x + 8, y + 22, maxWidth - 16, textColour);
            }
        }

        @Override
        public ArrayList<String> getTooltip()
        {
            ArrayList<String> strList = new ArrayList<String>();

            if (!isOpen())
            {
                strList.add("Useful Tips");
            }

            return strList;
        }

        private String getTip()
        {
            if (currentTip >= 0 && currentTip <= 2)
            {
                return StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".controller.tip." + currentTip);
            }
            else
            {
                currentTip = 0;
                return StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".controller.tip.0");
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public boolean handleMouseClicked(int x, int y, int mouseButton)
        {
            if (mouseButton == 1)
            {
                currentTip++;

                List list = fontRenderer.listFormattedStringToWidth(getTip(), maxWidth - 16);
                maxHeight = 24 + list.size() * fontRenderer.FONT_HEIGHT + 5;
                setFullyOpen();

                return true;
            }

            return super.handleMouseClicked(x, y, mouseButton);
        }
    }

    GuiGlyphSelector glyphSelector;
    GuiGlyphViewer glyphViewer;

    TilePortalController controller;
    EntityPlayer player;

    public GuiPortalFrameController(EntityPlayer play, TilePortalController tile)
    {
        super(new ContainerPortalFrameController(tile), tile, 176, 126, 176, 146);

        controller = tile;
        player = play;

        glyphSelector = new GuiGlyphSelector(7, 57, 0xffffff, this);
        glyphViewer = new GuiGlyphViewer(7, 20, 0xffffff, this, glyphSelector);

        glyphSelector.setSelectedToIdentifier(controller.UniqueIdentifier);
    }
    
    @Override
    protected void onExpandGui()
    {
        updateButtons();
    }
    
    @Override
    protected void onShrinkGui()
    {
        updateButtons();
    }

    private void updateButtons()
    {
        ((GuiButton) buttonList.get(0)).drawButton = expanded;
        ((GuiButton) buttonList.get(1)).drawButton = expanded;
        glyphSelector.setEditable(expanded);
        glyphViewer.setEditable(expanded);
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();

        if (expanded)
        {
            if (isShiftKeyDown())
            {
                ((GuiButton) buttonList.get(0)).displayString = EnumChatFormatting.AQUA + StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".button.clear");
                ((GuiButton) buttonList.get(1)).displayString = (isCtrlKeyDown() ? EnumChatFormatting.GOLD : EnumChatFormatting.AQUA) + StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".button.random");
            }
            else
            {
                ((GuiButton) buttonList.get(0)).displayString = StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".button.cancel");
                ((GuiButton) buttonList.get(1)).displayString = StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".button.save");
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();

        buttonList.add(new GuiButton(0, guiLeft + 10, guiTop + 117, (xSize - 20) / 2 - 5, 20, StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".button.cancel")));
        buttonList.add(new GuiButton(1, guiLeft + xSize / 2 + 6, guiTop + 117, (xSize - 20) / 2 - 5, 20, StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".button.save")));

        updateButtons();
    }

    @Override
    protected void initLedgers(IInventory inventory)
    {
        ledgerManager.add(new TipLedger());
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
                glyphSelector.setSelectedToIdentifier(controller.UniqueIdentifier);
                toggleState();
            }
            else if (button.id == 1) // Save Changes
            {
                ClientProxy.sendGuiPacket(0, glyphViewer.getSelectedIdentifier());
                toggleState();
            }
        }
    }
    
    @Override
    protected void drawBackground(float f, int i, int j)
    {
        glyphViewer.drawBackground(i, j);
    }
    
    @Override
    protected void drawBackgroundExpanded(float f, int i, int j)
    {
        glyphSelector.drawBackground(i, j);
    }
    
    @Override
    protected void drawForeground(int par1, int par2)
    {
        fontRenderer.drawStringWithShadow(StatCollector.translateToLocal("tile." + Reference.SHORT_ID + ".portalFrame.controller.name"), xSize / 2 - fontRenderer.getStringWidth(StatCollector.translateToLocal("tile." + Reference.SHORT_ID + ".portalFrame.controller.name")) / 2, -13, 0xFFFFFF);
        fontRenderer.drawString(StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".uniqueIdentifier"), 8, 8, 0x404040);
        
        glyphViewer.drawForeground(par1, par2);
    }
    
    @Override
    protected void drawForegroundExpanded(int par1, int par2)
    {
        fontRenderer.drawString(StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".glyphs"), 8, 44, 0x404040);
        
        glyphSelector.drawForeground(par1, par2);
    }
    
    @Override
    protected void drawForegroundShrunk(int par1, int par2)
    {
        fontRenderer.drawString(StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".portalComponents"), 8, 44, 0x404040);
        ClientBlockManager blockManager = (ClientBlockManager) controller.blockManager;        
        
        String s1 = "" + blockManager.portalFrame, s2 = "" + blockManager.redstone, s3 = "" + blockManager.portal, s4 = StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".controller." + (blockManager.networkInterface ? "initialized" : "invalid")), s5 = StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".controller." + (blockManager.dialDevice ? "initialized" : "invalid")), s6 = StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".controller." + (blockManager.biometric ? "initialized" : "invalid"));

        fontRenderer.drawString(StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".controller.frameBlocks"), 12, 57, 0x777777);
        fontRenderer.drawString(StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".controller.redstoneControllers"), 12, 67, 0x777777);
        fontRenderer.drawString(StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".controller.portalBlocks"), 12, 77, 0x777777);
        fontRenderer.drawString(StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".controller.networkInterface"), 12, 87, 0x777777);
        fontRenderer.drawString(StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".controller.dialDevice"), 12, 97, 0x777777);
        fontRenderer.drawString(StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".controller.biometric"), 12, 107, 0x777777);

        fontRenderer.drawString(s1, xSize - 12 - fontRenderer.getStringWidth(s1), 57, 0x404040);
        fontRenderer.drawString(s2, xSize - 12 - fontRenderer.getStringWidth(s2), 67, 0x404040);
        fontRenderer.drawString(s3, xSize - 12 - fontRenderer.getStringWidth(s3), 77, 0x404040);
        fontRenderer.drawString(s4, xSize - 12 - fontRenderer.getStringWidth(s4), 87, 0x404040);
        fontRenderer.drawString(s5, xSize - 12 - fontRenderer.getStringWidth(s5), 97, 0x404040);
        fontRenderer.drawString(s6, xSize - 12 - fontRenderer.getStringWidth(s6), 107, 0x404040);

        if (par1 >= guiLeft + 7 && par1 <= guiLeft + xSize - 8)
        {
            if (par2 >= guiTop + 20 && par2 <= guiTop + 37)
            {
                List<String> list = new ArrayList<String>();
                list.add(StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".clickToModify"));

                drawHoveringText(list, par1 - guiLeft, par2 - guiTop, fontRenderer);
            }
        }
    }
    
    @Override
    protected void mouseClickExpanded(int par1, int par2, int mouseButton)
    {
        glyphViewer.mouseClicked(par1, par2, mouseButton);
        glyphSelector.mouseClicked(par1, par2, mouseButton);
    }
    
    @Override
    protected void mouseClickShrunk(int par1, int par2, int mouseButton)
    {
        if (par1 >= guiLeft + 7 && par1 <= guiLeft + xSize - 8)
        {
            if (par2 >= guiTop + 20 && par2 <= guiTop + 37)
            {
                toggleState();
                mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
            }
        }
    }
}
