package enhancedportals.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import enhancedportals.EnhancedPortals;
import enhancedportals.client.gui.elements.BaseElement;
import enhancedportals.client.gui.tabs.BaseTab;
import enhancedportals.client.gui.tabs.TabTracker;
import enhancedportals.inventory.BaseContainer;

public abstract class BaseGui extends GuiContainer
{
    RenderItem itemRenderer = new RenderItem();
    protected static final ResourceLocation playerInventoryTexture = new ResourceLocation("enhancedportals", "textures/gui/playerInventory.png"), resizableInterfaceTexture = new ResourceLocation("enhancedportals", "textures/gui/resizableInterace.png");
    protected int mouseX = 0, mouseY = 0;
    protected ResourceLocation texture;
    protected String name;
    protected ArrayList<BaseTab> tabs = new ArrayList<BaseTab>();
    protected ArrayList<BaseElement> elements = new ArrayList<BaseElement>();
    public static final int defaultContainerSize = 144, playerInventorySize = 90, bufferSpace = 2, defaultGuiSize = defaultContainerSize + playerInventorySize + bufferSpace;
    protected int containerSize = defaultContainerSize, guiSize = defaultGuiSize, leftNudge = 0;
    protected boolean hasSeperateInventories = true, isHidingPlayerInventory = false;

    public BaseGui(BaseContainer container)
    {
        this(container, defaultContainerSize);
    }

    public int getSizeX()
    {
        return xSize;
    }

    public ResourceLocation getTexture()
    {
        return texture;
    }
    
    public int getSizeY()
    {
        return ySize;
    }
    
    public BaseGui(BaseContainer container, int cSize)
    {
        super(container);
        containerSize = cSize;
        guiSize = containerSize + playerInventorySize + bufferSpace;
        ySize = guiSize;
    }

    /** It's stupid that I'm forced to do this, some range/null checks in certain other mods would make this unnecessary. **/
    public void setHidePlayerInventory()
    {
        isHidingPlayerInventory = true;
        guiSize = containerSize;
        ySize = guiSize;
    }

    public void setCombinedInventory()
    {
        hasSeperateInventories = false;
        guiSize = containerSize + playerInventorySize;
        ySize = guiSize;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        BaseElement element = getElementAtPosition(mouseX - guiLeft, mouseY - guiTop);

        if (element != null)
        {
            if (element.isVisible())
            {
                element.handleMouseClicked(mouseX - guiLeft, mouseY - guiTop, mouseButton);
            }
        }

        BaseTab tab = getTabAtPosition(mouseX - guiLeft, mouseY - guiTop);

        if (tab != null && tab.isVisible() && !tab.handleMouseClicked(mouseX - guiLeft, mouseY - guiTop, mouseButton))
        {
            for (BaseTab other : tabs)
            {
                if (other != tab && other.open && other.side == tab.side)
                {
                    other.toggleOpen();
                }
            }

            tab.toggleOpen();
        }
    }

    public BaseTab getTabAtPosition(int mouseX, int mouseY)
    {
        int xShift = 0;
        int yShift = 4;

        for (BaseTab tab : tabs)
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

        for (BaseTab tab : tabs)
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

    public BaseElement getElementAtPosition(int mouseX, int mouseY)
    {
        for (BaseElement element : elements)
        {
            if (element.isVisible() && element.intersectsWith(mouseX, mouseY))
            {
                return element;
            }
        }

        return null;
    }

    public BaseElement addElement(BaseElement element)
    {
        elements.add(element);
        return element;
    }

    public BaseTab addTab(BaseTab tab)
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
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        if (!isHidingPlayerInventory)
        {
            mc.renderEngine.bindTexture(playerInventoryTexture);
            drawTexturedModalRect(guiLeft + leftNudge, guiTop + containerSize + bufferSpace, 0, 0, xSize, playerInventorySize);
        }

        if (texture != null)
        {
            mc.renderEngine.bindTexture(texture);
            drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, containerSize + (hasSeperateInventories ? 0 : 6));
        }
        else
        {
            mc.renderEngine.bindTexture(resizableInterfaceTexture);

            if (hasSeperateInventories)
            {
                drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize - 4, containerSize - 4);
                drawTexturedModalRect(guiLeft + xSize - 4, guiTop, 252, 0, 4, containerSize - 4);
                drawTexturedModalRect(guiLeft, guiTop + containerSize - 4, 0, 252, xSize - 4, 4);
                drawTexturedModalRect(guiLeft + xSize - 4, guiTop + containerSize - 4, 252, 252, 4, 4);
            }
            else
            {
                drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize - 4, containerSize + 4 + bufferSpace);
                drawTexturedModalRect(guiLeft + xSize - 4, guiTop, 252, 0, 4, containerSize + 4 + bufferSpace);
            }
        }

        mouseX = i - guiLeft;
        mouseY = j - guiTop;

        for (BaseElement element : elements)
        {
            element.update();

            if (element.isVisible())
            {
                element.draw();
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            }
        }

        int yPosRight = 4;
        int yPosLeft = 4;

        for (BaseTab tab : tabs)
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
            
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    public int getMouseX()
    {
        return mouseX;
    }

    public int getMouseY()
    {
        return mouseY;
    }

    public int getGuiLeft()
    {
        return guiLeft;
    }

    public int getGuiTop()
    {
        return guiTop;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        if (name != null)
        {
            fontRenderer.drawString(EnhancedPortals.localize(name), (xSize - mc.fontRenderer.getStringWidth(EnhancedPortals.localize(name))) / 2, 6, 0x404040);
        }

        BaseElement element = getElementAtPosition(mouseX, mouseY);

        if (element != null && !element.isDisabled())
        {
            List<String> list = new ArrayList<String>();
            element.addTooltip(list);

            if (!list.isEmpty())
            {
                drawHoveringText(list, mouseX, mouseY, fontRenderer);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glDisable(GL11.GL_LIGHTING);
                return;
            }
        }

        BaseTab tab = getTabAtPosition(mouseX, mouseY);

        if (tab != null)
        {
            List<String> list = new ArrayList<String>();
            tab.addTooltip(list);

            if (!list.isEmpty())
            {
                drawHoveringText(list, mouseX, mouseY, fontRenderer);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glDisable(GL11.GL_LIGHTING);
                return;
            }
        }
    }

    public Minecraft getMinecraft()
    {
        return mc;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        tabs.clear();
        elements.clear();
        buttonList.clear();
    }

    public void onElementClicked(BaseElement element)
    {

    }

    public FontRenderer getFontRenderer()
    {
        return fontRenderer;
    }

    public TextureManager getTextureManager()
    {
        return getMinecraft().renderEngine;
    }

    public RenderItem getItemRenderer()
    {
        return itemRenderer;
    }
}
