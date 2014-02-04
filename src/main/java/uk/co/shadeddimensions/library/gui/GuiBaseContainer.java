package uk.co.shadeddimensions.library.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.library.container.ContainerBase;
import uk.co.shadeddimensions.library.gui.element.ElementBase;
import uk.co.shadeddimensions.library.gui.element.ElementFakeItemSlot;
import uk.co.shadeddimensions.library.gui.slot.SlotFalseCopy;
import uk.co.shadeddimensions.library.gui.tab.TabBase;
import uk.co.shadeddimensions.library.util.GuiUtils;
import codechicken.nei.VisiblityData;
import codechicken.nei.api.INEIGuiHandler;
import codechicken.nei.api.TaggedInventoryArea;

/**
 * Base class for a modular GUIs. Works with Elements {@link ElementBase} and Tabs {@link TabBase} which are both modular elements. Use if your GUI requires access to an inventory. Based off of
 * GuiBase by King Lemming.
 * 
 * @author Alz454
 * 
 */
public abstract class GuiBaseContainer extends GuiContainer implements INEIGuiHandler, IGuiBase
{
    protected boolean drawInventory = true;

    protected int mouseX = 0;
    protected int mouseY = 0;

    protected int lastIndex = -1;

    protected boolean drawName = true;

    protected String name;
    protected ResourceLocation texture;

    protected ArrayList<TabBase> tabs = new ArrayList<TabBase>();
    protected ArrayList<ElementBase> elements = new ArrayList<ElementBase>();

    public GuiBaseContainer()
    {
        super(new ContainerBase());
    }

    public GuiBaseContainer(Container container)
    {
        super(container);
    }

    public GuiBaseContainer(Container container, ResourceLocation texture)
    {
        super(container);
        this.texture = texture;
    }

    public GuiBaseContainer(ResourceLocation texture)
    {
        this(new ContainerBase(), texture);
    }

    @Override
    public ElementBase addElement(ElementBase element)
    {
        elements.add(element);

        return element;
    }

    @Override
    public void addElements()
    {

    }

    @Override
    public TabBase addTab(TabBase tab)
    {
        tabs.add(tab);

        if (TabTracker.getOpenedLeftTab() != null && tab.getClass().equals(TabTracker.getOpenedLeftTab()))
        {
            tab.setFullyOpen();
        }
        else if (TabTracker.getOpenedRightTab() != null && tab.getClass().equals(TabTracker.getOpenedRightTab()))
        {
            tab.setFullyOpen();
        }

        return tab;
    }

    @Override
    public void addTabs()
    {

    }

    @Override
    public void drawBackgroundTexture()
    {
        if (texture != null)
        {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            getMinecraft().renderEngine.bindTexture(texture);
            drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        }
    }

    @Override
    public void drawElements()
    {
        for (ElementBase element : elements)
        {
            element.update();

            if (element.isVisible())
            {
                element.draw();
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        mouseX = mouseX - guiLeft;
        mouseY = mouseY - guiTop;

        drawBackgroundTexture();
        drawBackgroundTexture();
        drawElements();
        drawTabs();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        if (drawName)
        {
            fontRenderer.drawString(StatCollector.translateToLocal(name), GuiUtils.getCenteredOffset(this, StatCollector.translateToLocal(name), xSize), 6, 0x404040);
        }

        if (drawInventory)
        {
            fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 3, 0x404040);
        }

        TabBase tab = getTabAtPosition(mouseX - guiLeft, mouseY - guiTop);

        if (tab != null && tab.isVisible())
        {
            List<String> list = new ArrayList<String>();
            tab.addTooltip(list);

            if (!list.isEmpty())
            {
                GuiUtils.drawTooltipHoveringText(this, list, mouseX - guiLeft, mouseY - guiTop);
                return;
            }
        }

        ElementBase element = getElementAtPosition(mouseX - guiLeft, mouseY - guiTop);

        if (element != null && !element.isDisabled() && element.isVisible())
        {
            List<String> list = new ArrayList<String>();
            element.addTooltip(list);

            if (!list.isEmpty())
            {
                GuiUtils.drawTooltipHoveringText(this, list, mouseX - guiLeft, mouseY - guiTop);
                return;
            }
        }
    }

    @Override
    public void drawTabs()
    {
        int yPosRight = 4;
        int yPosLeft = 4;

        for (TabBase tab : tabs)
        {
            tab.update();

            if (tab.isVisible())
            {
                if (tab.side == 0)
                {
                    tab.draw(guiLeft, guiTop + yPosLeft);
                    yPosLeft += tab.currentHeight;
                }
                else
                {
                    tab.draw(guiLeft + xSize, guiTop + yPosRight);
                    yPosRight += tab.currentHeight;
                }
            }
        }

    }

    @Override
    public ElementBase getElementAtPosition(int mouseX, int mouseY)
    {
        for (ElementBase element : elements)
        {
            if (element.isVisible() && element.intersectsWith(mouseX, mouseY))
            {
                return element;
            }
        }

        return null;
    }

    @Override
    public FontRenderer getFontRenderer()
    {
        return fontRenderer;
    }

    @Override
    public int getGuiLeft()
    {
        return guiLeft;
    }

    @Override
    public int getGuiTop()
    {
        return guiTop;
    }

    @Override
    public int getHeight()
    {
        return height;
    }

    @Override
    public List<TaggedInventoryArea> getInventoryAreas(GuiContainer gui)
    {
        return null;
    }

    @Override
    public RenderItem getItemRenderer()
    {
        return itemRenderer;
    }

    @Override
    public int getItemSpawnSlot(GuiContainer gui, ItemStack item)
    {
        return 0;
    }

    @Override
    public Minecraft getMinecraft()
    {
        return mc;
    }

    @Override
    public int getMouseX()
    {
        return mouseX;
    }

    @Override
    public int getMouseY()
    {
        return mouseY;
    }

    @Override
    public TabBase getTabAtPosition(int mouseX, int mouseY)
    {
        int xShift = 0;
        int yShift = 4;

        for (TabBase tab : tabs)
        {
            if (!tab.isVisible() || tab.side == 1)
            {
                continue;
            }

            tab.currentShiftX = xShift;
            tab.currentShiftY = yShift;

            if (tab.intersectsWith(mouseX, mouseY, xShift, yShift))
            {
                return tab;
            }

            yShift += tab.currentHeight;
        }

        xShift = xSize;
        yShift = 4;

        for (TabBase tab : tabs)
        {
            if (!tab.isVisible() || tab.side == 0)
            {
                continue;
            }

            tab.currentShiftX = xShift;
            tab.currentShiftY = yShift;

            if (tab.intersectsWith(mouseX, mouseY, xShift, yShift))
            {
                return tab;
            }

            yShift += tab.currentHeight;
        }

        return null;
    }

    @Override
    public TextureManager getTextureManager()
    {
        return getMinecraft().renderEngine;
    }

    @Override
    public int getWidth()
    {
        return width;
    }

    @Override
    public float getZLevel()
    {
        return zLevel;
    }

    @Override
    public boolean handleDragNDrop(GuiContainer gui, int mousex, int mousey, ItemStack draggedStack, int button)
    {
        return false;
    }

    @Override
    public void handleElementButtonClick(String buttonName, int mouseButton)
    {

    }

    @Override
    public void handleElementFakeSlotItemChange(ElementFakeItemSlot slot)
    {

    }

    @Override
    public boolean hideItemPanelSlot(GuiContainer gui, int x, int y, int w, int h)
    {
        for (TabBase tab : tabs)
        {
            if (tab.isVisible() && tab.side == 1)
            {
                int xPos = guiLeft + xSize + tab.getRelativeX(), yPos = guiTop + tab.getRelativeY();

                if (x >= xPos && x <= xPos + tab.getWidth() && y >= yPos && y <= yPos + tab.getHeight())
                {
                    return true;
                }
                else if (x + w >= xPos && x + w <= xPos + tab.getWidth() && y + h >= yPos && y + h <= yPos + tab.getHeight())
                {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void initGui()
    {
        super.initGui();

        tabs.clear();
        elements.clear();
        buttonList.clear();

        addElements();
        addTabs();
    }

    @Override
    public boolean isItemStackAllowedInFakeSlot(ElementFakeItemSlot slot, ItemStack stack)
    {
        return false;
    }

    @Override
    public VisiblityData modifyVisiblity(GuiContainer gui, VisiblityData currentVisibility)
    {
        return null;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        TabBase tab = getTabAtPosition(mouseX - guiLeft, mouseY - guiTop);

        if (tab != null && tab.isVisible() && !tab.handleMouseClicked(mouseX - guiLeft, mouseY - guiTop, mouseButton))
        {
            for (TabBase other : tabs)
            {
                if (other != tab && other.open && other.side == tab.side)
                {
                    other.toggleOpen();
                }
            }

            tab.toggleOpen();
        }

        ElementBase element = getElementAtPosition(mouseX - guiLeft, mouseY - guiTop);

        if (element != null)
        {
            if (element.isVisible())
            {
                element.handleMouseClicked(mouseX - guiLeft, mouseY - guiTop, mouseButton);
            }
        }
    }

    Slot getSlotAtPos(int par1, int par2)
    {
        for (int k = 0; k < this.inventorySlots.inventorySlots.size(); ++k)
        {
            Slot slot = (Slot)this.inventorySlots.inventorySlots.get(k);

            if (this.isMouseOnSlot(slot, par1, par2))
            {
                return slot;
            }
        }

        return null;
    }
    
    boolean isMouseOnSlot(Slot par1Slot, int par2, int par3)
    {
        return this.isPointInRegion(par1Slot.xDisplayPosition, par1Slot.yDisplayPosition, 16, 16, par2, par3);
    }
    
    @Override
    protected void mouseClickMove(int mX, int mY, int lastClick, long timeSinceClick)
    {
        Slot slot = getSlotAtPos(mX, mY);
        ItemStack itemstack = getMinecraft().thePlayer.inventory.getItemStack();

        if (field_94076_q && slot != null && itemstack != null && slot instanceof SlotFalseCopy)
        {
            if (lastIndex != slot.slotNumber)
            {
                lastIndex = slot.slotNumber;
                handleMouseClick(slot, slot.slotNumber, 0, 0);
            }
        }
        else
        {
            lastIndex = -1;
            super.mouseClickMove(mX, mY, lastClick, timeSinceClick);
        }
    }

    @Override
    public void setZLevel(float zlevel)
    {
        zLevel = zlevel;
    }
}
