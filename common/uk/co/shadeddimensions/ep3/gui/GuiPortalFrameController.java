package uk.co.shadeddimensions.ep3.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.ep3.container.ContainerPortalFrameController;
import uk.co.shadeddimensions.ep3.lib.Reference;
import uk.co.shadeddimensions.ep3.network.ClientProxy;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;
import uk.co.shadeddimensions.ep3.util.GuiPayload;

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
        super(new ContainerPortalFrameController(tile), tile, 176, 83, 176, 146);

        controller = tile;
        player = play;

        glyphSelector = new GuiGlyphSelector(7, 57, 0xffffff, this);
        glyphViewer = new GuiGlyphViewer(7, 20, 0xffffff, this, glyphSelector);

        glyphSelector.setSelectedToIdentifier(controller.uniqueIdentifier);
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
                glyphSelector.setSelectedToIdentifier(controller.uniqueIdentifier);
                toggleState();
            }
            else if (button.id == 1) // Save Changes
            {
                GuiPayload payload = new GuiPayload();
                payload.data.setInteger("id", 0);
                payload.data.setString("uniqueIdentifier", glyphViewer.getSelectedIdentifier());                
                ClientProxy.sendGuiPacket(payload);
                
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
    protected void drawBackgroundShrunk(float f, int i, int j)
    {
        int leftOffset = xSize / 2 - 6 * 18 / 2;

        getTextureManager().bindTexture(new ResourceLocation("enhancedportals", "textures/gui/inventorySlots.png"));
        drawTexturedModalRect(guiLeft + leftOffset, guiTop + 55, 0, 0, 108, 18);

        itemRenderer.renderItemAndEffectIntoGUI(fontRenderer, getTextureManager(), new ItemStack(CommonProxy.blockPortal, 1, 5), guiLeft + leftOffset + 1, 103);
        itemRenderer.renderItemOverlayIntoGUI(fontRenderer, getTextureManager(), new ItemStack(CommonProxy.blockPortal, controller.intPortal, 0), guiLeft + leftOffset + 1, 103);

        GL11.glDisable(GL11.GL_LIGHTING);

        itemRenderer.renderItemAndEffectIntoGUI(fontRenderer, getTextureManager(), new ItemStack(CommonProxy.blockFrame, 1, 0), guiLeft + leftOffset + 19, 103);
        itemRenderer.renderItemOverlayIntoGUI(fontRenderer, getTextureManager(), new ItemStack(CommonProxy.blockFrame, controller.intBasic, 0), guiLeft + leftOffset + 19, 103);

        GL11.glDisable(GL11.GL_LIGHTING);

        itemRenderer.renderItemAndEffectIntoGUI(fontRenderer, getTextureManager(), new ItemStack(CommonProxy.blockFrame, 1, 2), guiLeft + leftOffset + 37, 103);
        itemRenderer.renderItemOverlayIntoGUI(fontRenderer, getTextureManager(), new ItemStack(CommonProxy.blockFrame, controller.intRedstone, 2), guiLeft + leftOffset + 37, 103, controller.intRedstone + "");

        GL11.glDisable(GL11.GL_LIGHTING);

        int counter = 0;
        for (int k = 0; k < 4; k++)
        {
            ItemStack s = null;

            if (k == 0 && controller.boolNetwork)
            {
                s = new ItemStack(CommonProxy.blockFrame, 1, 3);
            }
            else if (k == 1 && controller.boolDialler)
            {
                s = new ItemStack(CommonProxy.blockFrame, 1, 4);
            }
            else if (k == 2 && controller.boolBiometric)
            {
                s = new ItemStack(CommonProxy.blockFrame, 1, 5);
            }
            else if (k == 3 && controller.frameModule != null)
            {
                s = new ItemStack(CommonProxy.blockFrame, 1, 6);
            }

            if (s != null)
            {
                itemRenderer.renderItemAndEffectIntoGUI(fontRenderer, getTextureManager(), s, guiLeft + leftOffset + 55 + 18 * counter, 103);

                GL11.glDisable(GL11.GL_LIGHTING);
                counter++;
            }
        }
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
        fontRenderer.drawString(StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".portalComponents"), 8, 42, 0x404040);
        // ClientBlockManager blockManager = (ClientBlockManager) controller.blockManager;

        /*String s1 = "" + blockManager.portalFrame, s2 = "" + blockManager.redstone, s3 = "" + blockManager.portal, s4 = StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".controller." + (blockManager.networkInterface ? "initialized" : "invalid")), s5 = StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".controller." + (blockManager.dialDevice ? "initialized" : "invalid")), s6 = StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".controller." + (blockManager.biometric ? "initialized" : "invalid")), s7 = StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".controller." + (blockManager.getModuleManipulatorCoord() != null ? "initialized" : "invalid"));

        fontRenderer.drawString(StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".controller.frameBlocks"), 12, 57, 0x777777);
        fontRenderer.drawString(StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".controller.redstoneControllers"), 12, 67, 0x777777);
        fontRenderer.drawString(StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".controller.portalBlocks"), 12, 77, 0x777777);
        fontRenderer.drawString(StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".controller.networkInterface"), 12, 87, 0x777777);
        fontRenderer.drawString(StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".controller.dialDevice"), 12, 97, 0x777777);
        fontRenderer.drawString(StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".controller.biometric"), 12, 107, 0x777777);
        fontRenderer.drawString(StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".controller.upgrade"), 12, 117, 0x777777);

        fontRenderer.drawString(s1, xSize - 12 - fontRenderer.getStringWidth(s1), 57, 0x404040);
        fontRenderer.drawString(s2, xSize - 12 - fontRenderer.getStringWidth(s2), 67, 0x404040);
        fontRenderer.drawString(s3, xSize - 12 - fontRenderer.getStringWidth(s3), 77, 0x404040);
        fontRenderer.drawString(s4, xSize - 12 - fontRenderer.getStringWidth(s4), 87, 0x404040);
        fontRenderer.drawString(s5, xSize - 12 - fontRenderer.getStringWidth(s5), 97, 0x404040);
        fontRenderer.drawString(s6, xSize - 12 - fontRenderer.getStringWidth(s6), 107, 0x404040);
        fontRenderer.drawString(s7, xSize - 12 - fontRenderer.getStringWidth(s7), 117, 0x404040);*/

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
}
