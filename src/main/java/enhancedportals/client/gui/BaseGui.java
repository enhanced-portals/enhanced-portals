package enhancedportals.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import truetyper.FontHelper;
import enhancedportals.EnhancedPortals;
import enhancedportals.client.gui.elements.BaseElement;
import enhancedportals.client.gui.tabs.BaseTab;
import enhancedportals.client.gui.tabs.TabTracker;
import enhancedportals.inventory.BaseContainer;
import enhancedportals.network.ClientProxy;
import enhancedportals.utility.GeneralUtils;

public abstract class BaseGui extends GuiContainer
{
    RenderItem itemRenderer = new RenderItem();
    protected static final ResourceLocation playerInventoryTexture = new ResourceLocation("enhancedportals", "textures/gui/player_inventory.png"), resizableInterfaceTexture = new ResourceLocation("enhancedportals", "textures/gui/resizable_interace.png");
    protected int mouseX = 0, mouseY = 0;
    protected ResourceLocation texture;
    protected String name;
    protected ArrayList<BaseTab> tabs = new ArrayList<BaseTab>();
    protected ArrayList<BaseElement> elements = new ArrayList<BaseElement>();
    public static final int defaultContainerSize = 144, playerInventorySize = 90, bufferSpace = 2, defaultGuiSize = defaultContainerSize + playerInventorySize + bufferSpace;
    protected int containerSize = defaultContainerSize, guiSize = defaultGuiSize, leftNudge = 0;
    protected boolean hasSeperateInventories = true, isHidingPlayerInventory = false, hasSingleTexture = false;

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
    
    @Override
    protected void keyTyped(char par1, int par2)
    {
        for (BaseElement e : elements)
        {
            if (e.keyPressed(par2, par1))
            {
                return;
            }
        }
        
        super.keyTyped(par1, par2);
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
    
    protected void drawBackgroundTexture()
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        if (!isHidingPlayerInventory && !hasSingleTexture)
        {
            mc.renderEngine.bindTexture(playerInventoryTexture);
            drawTexturedModalRect(guiLeft + leftNudge, guiTop + containerSize + bufferSpace, 0, 0, xSize, playerInventorySize);
        }

        if (texture != null)
        {
            mc.renderEngine.bindTexture(texture);
            drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, (hasSingleTexture ? ySize : containerSize + (hasSeperateInventories ? 0 : 6)));
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
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        drawBackgroundTexture();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        mouseX = i - guiLeft;
        mouseY = j - guiTop;

        for (BaseElement element : elements)
        {
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
    
    @Override
    public void updateScreen()
    {
        super.updateScreen();
        
        for (BaseElement element : elements)
        {
            element.update();
        }

        for (BaseTab tab : tabs)
        {
            tab.update();
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
            getFontRenderer().drawString(EnhancedPortals.localize(name), (xSize - mc.fontRenderer.getStringWidth(EnhancedPortals.localize(name))) / 2, 6, 0x404040);
        }

        BaseElement element = getElementAtPosition(mouseX, mouseY);

        if (element != null && !element.isDisabled())
        {
            List<String> list = new ArrayList<String>();
            element.addTooltip(list);
            
            if (!list.isEmpty())
            {
                boolean needsOffset = list.get(0).equals("offset");
                
                if (needsOffset)
                {
                    list.remove(0);
                }
                
                drawHoveringText(list, mouseX + (needsOffset ? getGuiLeft() : 0), mouseY + (needsOffset ? getGuiTop() : 0), getFontRenderer());
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
                drawHoveringText(list, mouseX, mouseY, getFontRenderer());
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
        return fontRendererObj;
    }

    public TextureManager getTextureManager()
    {
        return getMinecraft().renderEngine;
    }

    public RenderItem getItemRenderer()
    {
        return itemRenderer;
    }
    
    public void drawIconNoReset(IIcon icon, int x, int y, int spriteSheet)
    {
        if (spriteSheet == 0)
        {
            getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        }
        else
        {
            getTextureManager().bindTexture(TextureMap.locationItemsTexture);
        }

        drawTexturedModelRectFromIcon(x, y, icon, 16, 16);
    }
    
    public void drawIcon(IIcon icon, int x, int y, int spriteSheet)
    {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0F);
        drawIconNoReset(icon, x, y, spriteSheet);
    }
    
    public void drawItemStack(ItemStack stack, int x, int y)
    {
        if (stack != null)
        {
            RenderHelper.enableGUIStandardItemLighting();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            getItemRenderer().renderItemAndEffectIntoGUI(getFontRenderer(), getTextureManager(), stack, x, y);
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.disableStandardItemLighting();
        }
    }
    
    public int drawSplitTrueType(int x, int y, int w, String s, int offset, float... rgba)
    {
        if (offset == 1)
        {
            x += getGuiLeft();
            y += getGuiTop();
        }
        
        List strs = GeneralUtils.listFormattedStringToWidth(s, w);
        int fontHeight = 8;
        for (int i = 0; i < strs.size(); i++)
        {
            if (i == 1 && offset == 0)
            {
                x += getGuiLeft();
                y += getGuiTop();
            }
            
            FontHelper.drawString((String) strs.get(i), x, y + (i * fontHeight), ClientProxy.bookFont, 1f, 1f, rgba);
        }
        
        return strs.size() * fontHeight;
    }
}
