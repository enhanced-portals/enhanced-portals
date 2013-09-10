/**
 * Derived from BuildCraft released under the MMPL https://github.com/BuildCraft/BuildCraft http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package uk.co.shadeddimensions.enhancedportals.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import uk.co.shadeddimensions.enhancedportals.container.ContainerEnhancedPortals;
import uk.co.shadeddimensions.enhancedportals.gui.button.GuiBetterButton;
import uk.co.shadeddimensions.enhancedportals.gui.slots.SlotBase;
import uk.co.shadeddimensions.enhancedportals.gui.tooltips.ToolTip;
import uk.co.shadeddimensions.enhancedportals.gui.tooltips.ToolTipLine;
import uk.co.shadeddimensions.enhancedportals.util.Properties;
import uk.co.shadeddimensions.enhancedportals.util.SessionVars;
import cpw.mods.fml.client.FMLClientHandler;

public abstract class GuiEnhancedPortals extends GuiContainer
{
    protected abstract class Ledger
    {
        private boolean open;
        protected int overlayColor = 0xffffff;
        public int currentShiftX = 0;
        public int currentShiftY = 0;
        protected int limitWidth = 128;
        protected int maxWidth = 124;
        protected int minWidth = 24;
        protected int currentWidth = minWidth;
        protected int maxHeight = 24;
        protected int minHeight = 24;
        protected int currentHeight = minHeight;

        public abstract void draw(int x, int y);

        protected void drawBackground(int x, int y)
        {
            float colorR = (overlayColor >> 16 & 255) / 255.0F;
            float colorG = (overlayColor >> 8 & 255) / 255.0F;
            float colorB = (overlayColor & 255) / 255.0F;

            GL11.glColor4f(colorR, colorG, colorB, 1.0F);

            Properties.bindTexture(getMinecraft().renderEngine, "epcore", "textures/gui/ledger.png");
            drawTexturedModalRect(x, y, 0, 256 - currentHeight, 4, currentHeight);
            drawTexturedModalRect(x + 4, y, 256 - currentWidth + 4, 0, currentWidth - 4, 4);
            // Add in top left corner again
            drawTexturedModalRect(x, y, 0, 0, 4, 4);

            drawTexturedModalRect(x + 4, y + 4, 256 - currentWidth + 4, 256 - currentHeight + 4, currentWidth - 4, currentHeight - 4);

            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0F);
        }

        protected void drawIcon(Icon icon, int x, int y)
        {

            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0F);
            drawTexturedModelRectFromIcon(x, y, icon, 16, 16);
        }

        public int getHeight()
        {
            return currentHeight;
        }

        public abstract ArrayList<String> getTooltip();

        public boolean handleMouseClicked(int x, int y, int mouseButton)
        {
            return false;
        }

        public boolean intersectsWith(int mouseX, int mouseY, int shiftX, int shiftY)
        {

            if (mouseX >= shiftX && mouseX <= shiftX + currentWidth && mouseY >= shiftY && mouseY <= shiftY + getHeight())
            {
                return true;
            }

            return false;
        }

        protected boolean isFullyOpened()
        {
            return currentWidth >= maxWidth;
        }

        public boolean isOpen()
        {
            return open;
        }

        public boolean isVisible()
        {
            return true;
        }

        public void setFullyOpen()
        {
            open = true;
            currentWidth = maxWidth;
            currentHeight = maxHeight;
        }

        public void toggleOpen()
        {
            if (open)
            {
                open = false;
                SessionVars.setOpenedLedger(null);
            }
            else
            {
                open = true;
                SessionVars.setOpenedLedger(this.getClass());
            }
        }

        public void update()
        {
            // Width
            if (open && currentWidth < maxWidth)
            {
                currentWidth += 4;
            }
            else if (!open && currentWidth > minWidth)
            {
                currentWidth -= 4;
            }

            // Height
            if (open && currentHeight < maxHeight)
            {
                currentHeight += 4;
            }
            else if (!open && currentHeight > minHeight)
            {
                currentHeight -= 4;
            }
        }
    }

    protected class LedgerManager
    {
        private GuiEnhancedPortals gui;
        protected ArrayList<Ledger> ledgers = new ArrayList<Ledger>();

        public LedgerManager(GuiEnhancedPortals gui)
        {
            this.gui = gui;
        }

        public void add(Ledger ledger)
        {
            ledgers.add(ledger);
            if (SessionVars.getOpenedLedger() != null && ledger.getClass().equals(SessionVars.getOpenedLedger()))
            {
                ledger.setFullyOpen();
            }
        }

        protected void drawLedgers(int mouseX, int mouseY)
        {
            int xPos = 8;
            for (Ledger ledger : ledgers)
            {

                ledger.update();
                if (!ledger.isVisible())
                {
                    continue;
                }

                ledger.draw(xSize, xPos);
                xPos += ledger.getHeight();
            }

            Ledger ledger = getAtPosition(mouseX, mouseY);
            if (ledger != null)
            {
                int startX = mouseX - (gui.width - gui.xSize) / 2 + 12;
                int startY = mouseY - (gui.height - gui.ySize) / 2 - 12;

                List<String> tooltip = ledger.getTooltip();

                if (tooltip == null || tooltip.size() == 0)
                {
                    return;
                }

                drawTooltipText(tooltip, startX - 10, startY + 10, fontRenderer);
            }
        }

        @SuppressWarnings("rawtypes")
        protected void drawTooltipText(List par1List, int par2, int par3, FontRenderer font)
        {
            if (!par1List.isEmpty())
            {
                GL11.glDisable(GL12.GL_RESCALE_NORMAL);
                RenderHelper.disableStandardItemLighting();
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                int k = 0;
                Iterator iterator = par1List.iterator();

                while (iterator.hasNext())
                {
                    String s = (String) iterator.next();
                    int l = font.getStringWidth(s);

                    if (l > k)
                    {
                        k = l;
                    }
                }

                int i1 = par2 + 12;
                int j1 = par3 - 12;
                int k1 = 8;

                if (par1List.size() > 1)
                {
                    k1 += 2 + (par1List.size() - 1) * 10;
                }

                if (i1 + k > width)
                {
                    i1 -= 28 + k;
                }

                if (j1 + k1 + 6 > height)
                {
                    j1 = height - k1 - 6;
                }

                zLevel = 300.0F;
                itemRenderer.zLevel = 300.0F;
                int l1 = -267386864;
                drawGradientRect(i1 - 3, j1 - 4, i1 + k + 3, j1 - 3, l1, l1);
                drawGradientRect(i1 - 3, j1 + k1 + 3, i1 + k + 3, j1 + k1 + 4, l1, l1);
                drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 + k1 + 3, l1, l1);
                drawGradientRect(i1 - 4, j1 - 3, i1 - 3, j1 + k1 + 3, l1, l1);
                drawGradientRect(i1 + k + 3, j1 - 3, i1 + k + 4, j1 + k1 + 3, l1, l1);
                int i2 = 1347420415;
                int j2 = (i2 & 16711422) >> 1 | i2 & -16777216;
                drawGradientRect(i1 - 3, j1 - 3 + 1, i1 - 3 + 1, j1 + k1 + 3 - 1, i2, j2);
                drawGradientRect(i1 + k + 2, j1 - 3 + 1, i1 + k + 3, j1 + k1 + 3 - 1, i2, j2);
                drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 - 3 + 1, i2, i2);
                drawGradientRect(i1 - 3, j1 + k1 + 2, i1 + k + 3, j1 + k1 + 3, j2, j2);

                for (int k2 = 0; k2 < par1List.size(); ++k2)
                {
                    String s1 = (String) par1List.get(k2);
                    font.drawStringWithShadow(s1, i1, j1, -1);

                    if (k2 == 0)
                    {
                        j1 += 2;
                    }

                    j1 += 10;
                }

                zLevel = 0.0F;
                itemRenderer.zLevel = 0.0F;
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                RenderHelper.enableStandardItemLighting();
                GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            }
        }

        protected Ledger getAtPosition(int mX, int mY)
        {
            int xShift = (gui.width - gui.xSize) / 2 + gui.xSize;
            int yShift = (gui.height - gui.ySize) / 2 + 8;

            for (int i = 0; i < ledgers.size(); i++)
            {
                Ledger ledger = ledgers.get(i);
                if (!ledger.isVisible())
                {
                    continue;
                }

                ledger.currentShiftX = xShift;
                ledger.currentShiftY = yShift;
                if (ledger.intersectsWith(mX, mY, xShift, yShift))
                {
                    return ledger;
                }

                yShift += ledger.getHeight();
            }

            return null;
        }

        public void handleMouseClicked(int x, int y, int mouseButton)
        {
            Ledger ledger = getAtPosition(x, y);

            // Default action only if the mouse click was not handled by the
            // ledger itself.
            if (ledger != null && !ledger.handleMouseClicked(x, y, mouseButton) && mouseButton == 0)
            {

                for (Ledger other : ledgers)
                {
                    if (other != ledger && other.isOpen())
                    {
                        other.toggleOpen();
                    }
                }
                ledger.toggleOpen();
            }
        }

        /**
         * Inserts a ledger into the next-to-last position.
         * 
         * @param ledger
         */
        public void insert(Ledger ledger)
        {
            ledgers.add(ledgers.size() - 1, ledger);
        }
    }

    protected class TipLedger extends Ledger
    {
        int headerColour = 0xe1c92f;
        int subheaderColour = 0xaaafb8;
        int textColour = 0x000000;
        int tip = 0;

        protected String[] getTipsList()
        {
            return new String[] {};
        }

        public TipLedger()
        {
            overlayColor = 0x5396da;
        }

        @SuppressWarnings("rawtypes")
        @Override
        public void draw(int x, int y)
        {
            drawBackground(x, y);

            if (!isFullyOpened())
            {
                return;
            }

            fontRenderer.drawStringWithShadow("Did you know...", x + 22, y + 8, headerColour);

            if (tip == -1)
            {
                tip = 0;
                List list = fontRenderer.listFormattedStringToWidth(getTipsList()[tip], 115);
                maxHeight = 25 + list.size() * fontRenderer.FONT_HEIGHT;
            }

            fontRenderer.drawSplitString(getTipsList()[tip], x + 5, y + 20, 115, textColour);
        }

        @Override
        public ArrayList<String> getTooltip()
        {
            ArrayList<String> strList = new ArrayList<String>();

            if (!isOpen())
            {
                strList.add("Did you know...");
            }

            return strList;
        }

        @SuppressWarnings("rawtypes")
        @Override
        public boolean handleMouseClicked(int x, int y, int mouseButton)
        {
            if (!isOpen())
            {
                tip++;

                if (tip >= getTipsList().length)
                {
                    tip = 0;
                }

                List list = fontRenderer.listFormattedStringToWidth(getTipsList()[tip], 115);
                maxHeight = 25 + list.size() * fontRenderer.FONT_HEIGHT;
            }

            return super.handleMouseClicked(x, y, mouseButton);
        }
    }

    protected class RedstoneLedger extends Ledger
    {
        int headerColour = 0xe1c92f;
        int subheaderColour = 0xaaafb8;
        int textColour = 0x000000;
        int selected = 0;

        public RedstoneLedger()
        {
            overlayColor = 0xd46c1f;
            maxHeight = 47;
        }

        @Override
        public void draw(int x, int y)
        {
            drawBackground(x, y);

            Properties.bindTexture(FMLClientHandler.instance().getClient().renderEngine, 1);
            drawIcon(new ItemStack(Item.redstone).getIconIndex(), x + 3, y + 3);

            if (!isFullyOpened())
            {
                return;
            }

            int y2 = y + 22, xLoc = x + maxWidth / 2 - 22 * 4 / 2;
            fontRenderer.drawStringWithShadow("Redstone Control", x + 22, y + 8, headerColour);
            Properties.bindTexture(FMLClientHandler.instance().getClient().renderEngine, "epcore", "textures/gui/buttons.png");
            GL11.glColor3f(1F, 1F, 1F);

            for (int i = 0; i < 4; i++)
            {
                int x2 = xLoc + 3 + 22 * i;
                boolean isActive = false;

                if (i == 0)
                {
                    isActive = selected == 0;
                }
                else if (i == 1)
                {
                    isActive = selected == 1;
                }
                else if (i == 2)
                {
                    isActive = selected >= 2;
                }
                else if (i == 3)
                {
                    isActive = selected == -1;
                }

                drawTexturedModalRect(x2, y2, 0, isActive ? 40 : 20, 10, 10);
                drawTexturedModalRect(x2 + 10, y2, 190, isActive ? 40 : 20, 10, 10);
                drawTexturedModalRect(x2, y2 + 10, 0, isActive ? 50 : 30, 10, 10);
                drawTexturedModalRect(x2 + 10, y2 + 10, 190, isActive ? 50 : 30, 10, 10);
            }

            Properties.bindTexture(FMLClientHandler.instance().getClient().renderEngine, 0);
            drawIcon(new ItemStack(Block.torchRedstoneActive).getIconIndex(), xLoc + 5, y + 22);
            drawIcon(new ItemStack(Block.torchRedstoneIdle).getIconIndex(), xLoc + 27, y + 22);

            Properties.bindTexture(FMLClientHandler.instance().getClient().renderEngine, 1);
            drawIcon(new ItemStack(Item.redstone).getIconIndex(), xLoc + 49, y + 24);
            drawIcon(new ItemStack(Item.gunpowder).getIconIndex(), xLoc + 71, y + 24);

            if (selected >= 2)
            {
                fontRenderer.drawStringWithShadow("" + (selected - 2), xLoc + 49 + 17 - fontRenderer.getStringWidth("" + (selected - 2)), y + 33, 0xFFFFFFFF);
            }
        }

        @Override
        public ArrayList<String> getTooltip()
        {
            ArrayList<String> strList = new ArrayList<String>();

            if (!isOpen())
            {
                strList.add("Redstone Control");
                strList.add(EnumChatFormatting.GRAY + (selected == 0 ? "Normal" : selected == 1 ? "Inverted" : selected >= 2 ? "Analogue (" + (selected - 2) + ")" : selected == -1 ? "Disabled" : "Unknown"));
            }

            return strList;
        }

        @Override
        public boolean handleMouseClicked(int x, int y, int mouseButton)
        {
            int xLoc = currentShiftX + maxWidth / 2 - 22 * 4 / 2, y2 = currentShiftY + 22;

            for (int i = 0; i < 4; i++)
            {
                int x2 = xLoc + 3 + 22 * i;

                if (x >= x2 && x <= x2 + 19 && y >= y2 && y <= y2 + 19)
                {
                    if ((i == 0 || i == 1) && mouseButton == 0)
                    {
                        selected = i;
                    }
                    else if (i == 2)
                    {
                        if (selected < 2)
                        {
                            selected = 3;
                        }
                        else
                        {
                            selected = MathHelper.clamp_int(mouseButton == 0 ? selected + 1 : mouseButton == 1 ? selected - 1 : selected, 3, 17);
                        }
                    }
                    else if (i == 3 && mouseButton == 0)
                    {
                        selected = -1;
                    }

                    selectedChanged(selected);
                    getMinecraft().sndManager.playSoundFX("random.click", 1.0F, 1.0F);
                    return true;
                }
            }

            return false;
        }

        protected void setSelected(int i)
        {
            selected = i;
        }

        protected void selectedChanged(int i)
        {

        }
    }

    protected LedgerManager ledgerManager = new LedgerManager(this);

    protected TileEntity tile;

    public GuiEnhancedPortals(ContainerEnhancedPortals container, IInventory inventory)
    {
        super(container);

        if (inventory instanceof TileEntity)
        {
            tile = (TileEntity) inventory;
        }

        initLedgers(inventory);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        ledgerManager.drawLedgers(par1, par2);
        GL11.glDisable(GL11.GL_LIGHTING);
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float par3)
    {
        super.drawScreen(mouseX, mouseY, par3);
        int left = guiLeft;
        int top = guiTop;

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glPushMatrix();
        GL11.glTranslatef(left, top, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderHelper.disableStandardItemLighting();

        InventoryPlayer playerInv = mc.thePlayer.inventory;

        if (playerInv.getItemStack() == null)
        {
            for (Object button : buttonList)
            {
                if (!(button instanceof GuiBetterButton))
                {
                    continue;
                }
                GuiBetterButton betterButton = (GuiBetterButton) button;
                ToolTip tips = betterButton.getToolTip();
                if (tips == null)
                {
                    continue;
                }
                boolean mouseOver = betterButton.isMouseOverButton(mouseX, mouseY);
                tips.onTick(mouseOver);
                if (mouseOver && tips.isReady())
                {
                    tips.refresh();
                    drawToolTips(tips, mouseX, mouseY);
                }
            }
        }
        for (Object obj : inventorySlots.inventorySlots)
        {
            if (!(obj instanceof SlotBase))
            {
                continue;
            }
            SlotBase slot = (SlotBase) obj;
            if (slot.getStack() != null)
            {
                continue;
            }
            ToolTip tips = slot.getToolTip();
            if (tips == null)
            {
                continue;
            }
            boolean mouseOver = isMouseOverSlot(slot, mouseX, mouseY);
            tips.onTick(mouseOver);
            if (mouseOver && tips.isReady())
            {
                tips.refresh();
                drawToolTips(tips, mouseX, mouseY);
            }
        }

        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    private void drawToolTips(ToolTip toolTips, int mouseX, int mouseY)
    {
        if (toolTips.size() > 0)
        {
            int left = guiLeft;
            int top = guiTop;
            int length = 0;
            int x;
            int y;

            for (ToolTipLine tip : toolTips)
            {
                y = fontRenderer.getStringWidth(tip.text);

                if (y > length)
                {
                    length = y;
                }
            }

            x = mouseX - left + 12;
            y = mouseY - top - 12;
            int var14 = 8;

            if (toolTips.size() > 1)
            {
                var14 += 2 + (toolTips.size() - 1) * 10;
            }

            zLevel = 300.0F;
            itemRenderer.zLevel = 300.0F;
            int var15 = -267386864;
            drawGradientRect(x - 3, y - 4, x + length + 3, y - 3, var15, var15);
            drawGradientRect(x - 3, y + var14 + 3, x + length + 3, y + var14 + 4, var15, var15);
            drawGradientRect(x - 3, y - 3, x + length + 3, y + var14 + 3, var15, var15);
            drawGradientRect(x - 4, y - 3, x - 3, y + var14 + 3, var15, var15);
            drawGradientRect(x + length + 3, y - 3, x + length + 4, y + var14 + 3, var15, var15);
            int var16 = 1347420415;
            int var17 = (var16 & 16711422) >> 1 | var16 & -16777216;
            drawGradientRect(x - 3, y - 3 + 1, x - 3 + 1, y + var14 + 3 - 1, var16, var17);
            drawGradientRect(x + length + 2, y - 3 + 1, x + length + 3, y + var14 + 3 - 1, var16, var17);
            drawGradientRect(x - 3, y - 3, x + length + 3, y - 3 + 1, var16, var16);
            drawGradientRect(x - 3, y + var14 + 2, x + length + 3, y + var14 + 3, var17, var17);

            for (ToolTipLine tip : toolTips)
            {
                String line = tip.text;

                if (tip.color == -1)
                {
                    line = "\u00a77" + line;
                }
                else
                {
                    line = "\u00a7" + Integer.toHexString(tip.color) + line;
                }

                fontRenderer.drawStringWithShadow(line, x, y, -1);

                y += 10 + tip.getSpacing();
            }

            zLevel = 0.0F;
            itemRenderer.zLevel = 0.0F;
        }
    }

    protected int getCenteredOffset(String string)
    {
        return getCenteredOffset(string, xSize);
    }

    protected int getCenteredOffset(String string, int xWidth)
    {
        return (xWidth - fontRenderer.getStringWidth(string)) / 2;
    }

    protected void initLedgers(IInventory inventory)
    {
    }

    /**
     * Returns if the passed mouse position is over the specified slot.
     */
    private boolean isMouseOverSlot(Slot slot, int mouseX, int mouseY)
    {
        int left = guiLeft;
        int top = guiTop;
        mouseX -= left;
        mouseY -= top;
        return mouseX >= slot.xDisplayPosition - 1 && mouseX < slot.xDisplayPosition + 16 + 1 && mouseY >= slot.yDisplayPosition - 1 && mouseY < slot.yDisplayPosition + 16 + 1;
    }

    @Override
    protected void mouseClicked(int par1, int par2, int mouseButton)
    {
        super.mouseClicked(par1, par2, mouseButton);

        ledgerManager.handleMouseClicked(par1, par2, mouseButton);
    }

    public int getGuiLeft()
    {
        return guiLeft;
    }
    
    public int getGuiTop()
    {
        return guiTop;
    }
    
    public int getXSize()
    {
        return xSize;
    }
    
    public int getYSize()
    {
        return ySize;
    }
    
    public int getLeft()
    {
        return guiLeft;
    }

    public int getTop()
    {
        return guiTop;
    }

    public RenderItem getItemRenderer()
    {
        return itemRenderer;
    }
    
    public Minecraft getMinecraft()
    {
        return FMLClientHandler.instance().getClient();
    }

    protected void drawBorderedRectangle(int x, int y, int w, int h, int colour, int borderColour, boolean offset)
    {
        if (offset)
        {
            x += guiLeft;
            y += guiTop;
        }

        drawRectangle(x + 1, y + 1, w, h, colour, false); // centre
        drawRectangle(x, y, w + 2, 1, borderColour, false); // top border
        drawRectangle(x, y + h + 1, w + 2, 1, borderColour, false); // bottom border
        drawRectangle(x, y + 1, 1, h, borderColour, false); // left border
        drawRectangle(x + w + 1, y + 1, 1, h, borderColour, false); // right border
    }

    protected void drawRectangle(int x, int y, int w, int h, int colour, boolean offset)
    {
        if (offset)
        {
            x += guiLeft;
            y += guiTop;
        }

        drawRect(x, y, x + w, y + h, colour);
    }

    protected void drawColouredItemStack(int x, int y, int colour, ItemStack stack, boolean offset)
    {
        Color c = new Color(colour);
        drawColouredItemStack(x, y, c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha(), stack, offset);
    }

    /*** RGBA should be 0-255 */
    protected void drawColouredItemStack(int x, int y, float r, float g, float b, float a, ItemStack stack, boolean offset)
    {
        if (offset)
        {
            x += guiLeft;
            y += guiTop;
        }

        itemRenderer.renderWithColor = false;
        GL11.glColor4f(r / 255, g / 255, b / 255, a / 255);
        itemRenderer.renderItemIntoGUI(fontRenderer, mc.renderEngine, stack, x, y);
        itemRenderer.renderWithColor = true;
    }

    protected void drawParticle(int x, int y, int colour, int texture, boolean offset)
    {
        Color c = new Color(colour);
        drawParticle(x, y, c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha(), texture, offset);
    }

    /*** RGBA should be 0-255 */
    protected void drawParticle(int x, int y, float r, float g, float b, float a, int textureIndex, boolean offset)
    {
        if (offset)
        {
            x += guiLeft;
            y += guiTop;
        }

        GL11.glColor4f(r / 255, g / 255, b / 255, a / 255);
        mc.renderEngine.func_110577_a(new ResourceLocation("textures/particle/particles.png"));
        drawTexturedModalRect(guiLeft + 29, guiTop + 9, textureIndex % 16 * 16, textureIndex / 16 * 16, 16, 16);
    }
    
    protected void drawItemSlotBackground(int x, int y, int w, int h)
    {
        drawRectangle(x, y, w - 1, h, 0xFF8b8b8b, true); // background
        drawRectangle(x, y, w - 1, 1, 0xFF373737, true); // top
        drawRectangle(x, y, 1, h, 0xFF373737, true); // left
        drawRectangle(x, y + h, w - 1, 1, 0xFFffffff, true); // bottom
        drawRectangle(x + w - 1, y, 1, h + 1, 0xFFffffff, true); // right
        drawRectangle(x + w - 1, y, 1, 1, 0xFF8b8b8b, true); // corner
        drawRectangle(x, y + h, 1, 1, 0xFF8b8b8b, true); // corner
    }

    public TextureManager getTextureManager()
    {
        return mc.renderEngine;
    }
}
