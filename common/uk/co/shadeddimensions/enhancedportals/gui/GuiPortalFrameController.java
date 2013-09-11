package uk.co.shadeddimensions.enhancedportals.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.enhancedportals.container.ContainerPortalFrameController;
import uk.co.shadeddimensions.enhancedportals.network.ClientProxy;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameController;

public class GuiPortalFrameController extends GuiEnhancedPortals
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
                drawString(fontRenderer, "Did you know...", x + 20, y + 8, headerColour);

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
            if (currentTip == 0)
            {
                return "You can right-click this tab to get more tips! This also works for other information tabs.";
            }
            else if (currentTip == 1)
            {
                return String.format("You can hold %s while selecting Glyphs to get additional options.", EnumChatFormatting.GRAY + "shift" + EnumChatFormatting.BLACK);
            }
            else if (currentTip == 2)
            {
                return String.format("Holding %s will force the Random button to use all %s Glyphs.", EnumChatFormatting.GRAY + "control and shift" + EnumChatFormatting.BLACK, GuiGlyphSelector.MAX_COUNT);
            }
            else
            {
                currentTip = 0;
                return getTip();
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

    TilePortalFrameController controller;
    EntityPlayer player;

    static boolean isChanging = false, expanding = false, expanded = false;
    static final int MIN_SIZE = 170, MAX_SIZE = 220;
    static int currentSize = MIN_SIZE;

    public GuiPortalFrameController(EntityPlayer play, TilePortalFrameController tile)
    {
        super(new ContainerPortalFrameController(tile), tile);

        controller = tile;
        player = play;
        ySize = MIN_SIZE - 20;

        expanding = true;
        isChanging = false;
        expanded = false;
        currentSize = MIN_SIZE;

        glyphSelector = new GuiGlyphSelector(7, 57, 0xffffff, this);
        glyphViewer = new GuiGlyphViewer(7, 20, 0xffffff, this, glyphSelector);

        glyphSelector.setSelectedToIdentifier(controller.UniqueIdentifier);
    }

    private void toggleState()
    {
        isChanging = true;

        if (expanded)
        {
            expanded = false;
            updateButtons();
        }
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
                ((GuiButton) buttonList.get(0)).displayString = EnumChatFormatting.AQUA + "Clear";
                ((GuiButton) buttonList.get(1)).displayString = (isCtrlKeyDown() ? EnumChatFormatting.GOLD : EnumChatFormatting.AQUA) + "Random";
            }
            else
            {
                ((GuiButton) buttonList.get(0)).displayString = "Cancel";
                ((GuiButton) buttonList.get(1)).displayString = "Save";
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) // 0, 0 = 0, 0
    {
        mc.renderEngine.func_110577_a(new ResourceLocation("enhancedportals", "textures/gui/frameController.png"));
        GL11.glColor4f(1f, 1f, 1f, 1f);

        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, 43); // Draw in the static top
        drawTexturedModalRect(guiLeft, guiTop + 39, 0, 6 - currentSize, xSize, currentSize - 50);

        glyphViewer.drawBackground(i, j);

        if (expanded)
        {
            glyphSelector.drawBackground(i, j);
        }

        // Logic for updating background
        if (isChanging)
        {
            if (expanding)
            {
                if (currentSize < MAX_SIZE)
                {
                    currentSize += 2;
                }
                else
                {
                    expanding = false;
                    expanded = true;
                    updateButtons();
                    isChanging = false;
                }
            }
            else
            {
                if (currentSize > MIN_SIZE)
                {
                    currentSize -= 2;
                }
                else
                {
                    expanding = true;
                    isChanging = false;
                }
            }
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) // 0, 0 = guiLeft, guiTop
    {
        super.drawGuiContainerForegroundLayer(par1, par2);

        fontRenderer.drawStringWithShadow("Portal Controller", xSize / 2 - fontRenderer.getStringWidth("Portal Controller") / 2, -13, 0xFFFFFF);
        fontRenderer.drawString("Unique Identifier", 8, 8, 0x404040);
        fontRenderer.drawString(isChanging ? "" : expanded ? "Glyphs" : "Portal Components", 8, 44, 0x404040);

        glyphViewer.drawForeground(par1, par2);

        if (expanded)
        {
            glyphSelector.drawForeground(par1, par2);
        }
        else if (!expanded && !isChanging)
        {
            String s1 = "" + (controller.getAttachedFrames() - controller.getAttachedFrameRedstone()), s2 = "" + controller.getAttachedFrameRedstone(), s3 = "" + controller.getAttachedPortals();

            fontRenderer.drawString("Frame Blocks", 12, 57, 0x777777);
            fontRenderer.drawString("Redstone Controllers", 12, 67, 0x777777);
            fontRenderer.drawString("Portal Blocks", 12, 77, 0x777777);

            fontRenderer.drawString(s1, xSize - 12 - fontRenderer.getStringWidth(s1), 57, 0x404040);
            fontRenderer.drawString(s2, xSize - 12 - fontRenderer.getStringWidth(s2), 67, 0x404040);
            fontRenderer.drawString(s3, xSize - 12 - fontRenderer.getStringWidth(s3), 77, 0x404040);

            if (par1 >= guiLeft + 7 && par1 <= guiLeft + xSize - 8)
            {
                if (par2 >= guiTop + 20 && par2 <= guiTop + 37)
                {
                    List<String> list = new ArrayList<String>();
                    list.add("Click to modify");

                    drawHoveringText(list, par1 - guiLeft, par2 - guiTop, fontRenderer);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();

        buttonList.add(new GuiButton(0, guiLeft + 10, guiTop + 117, (xSize - 20) / 2 - 5, 20, "Cancel"));
        buttonList.add(new GuiButton(1, guiLeft + xSize / 2 + 6, guiTop + 117, (xSize - 20) / 2 - 5, 20, "Save"));

        updateButtons();
    }

    @Override
    protected void initLedgers(IInventory inventory)
    {
        ledgerManager.add(new TipLedger());
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
                if (!expanded && !isChanging)
                {
                    toggleState();
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
}
