package enhancedportals.client.gui.tabs;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import enhancedportals.EnhancedPortals;
import enhancedportals.client.gui.BaseGui;

public abstract class BaseTab
{
    static int tabExpandSpeed = 8;
    protected BaseGui parent;
    protected boolean visible = true, disabled = false;
    protected ArrayList<String> hoverText;
    protected int posX, posY, sizeX, sizeY;
    protected String id;
    protected ResourceLocation texture;
    public int side = 1;
    public boolean open, drawName = true;
    public int backgroundColor = 0xffffff;
    public int minWidth = 22;
    public int maxWidth = 124;
    public int currentWidth = minWidth;
    public int minHeight = 22;
    public int maxHeight = 22;
    public int currentHeight = minHeight;
    public static final ResourceLocation DEFAULT_TEXTURE_LEFT = new ResourceLocation("alzlib", "textures/gui/tabLeft.png");
    public static final ResourceLocation DEFAULT_TEXTURE_RIGHT = new ResourceLocation("alzlib", "textures/gui/tabRight.png");
    public int titleColour = 0xFFFFFF;
    public IIcon icon;
    public ItemStack stack;
    public String name;
    public int currentShiftX = 0;
    public int currentShiftY = 0;

    public BaseTab(BaseGui gui)
    {
        this(gui, 1);
    }

    public BaseTab(BaseGui gui, int side)
    {
        parent = gui;
        this.side = side;

        if (side == 0)
        {
            texture = DEFAULT_TEXTURE_LEFT;
        }
        else
        {
            texture = DEFAULT_TEXTURE_RIGHT;
        }
        
        int guiLeft = gui.getGuiLeft();
        
        if (guiLeft < maxWidth)
        {
            maxWidth = guiLeft - 1;
        }
    }

    void drawIcon(IIcon icon, int x, int y, int spriteSheet)
    {
        if (spriteSheet == 0)
        {
            parent.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        }
        else
        {
            parent.getTextureManager().bindTexture(TextureMap.locationItemsTexture);
        }

        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0F);
    }
    
    void drawItemStack(ItemStack stack, int x, int y)
    {
        if (stack != null)
        {
            RenderHelper.enableGUIStandardItemLighting();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            parent.getItemRenderer().renderItemAndEffectIntoGUI(parent.getFontRenderer(), parent.getTextureManager(), stack, x, y);
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.disableStandardItemLighting();
        }
    }
    
    public void draw()
    {
        drawBackground();
        
        if (icon != null)
        {
            int offsetX = side == 0 ? 4 - currentWidth : 2;
            drawIcon(icon, posX + offsetX, posY + 3, 1);
        }
        else if (stack != null)
        {
            int offsetX = side == 0 ? 4 - currentWidth : 2;
            drawItemStack(stack, posX + offsetX, posY + 3);
        }

        if (isFullyOpened() && drawName)
        {
            int offset = icon != null || stack != null ? 22 : 4;
            int offsetX = side == 0 ? offset - currentWidth + 2 : offset;
            parent.getFontRenderer().drawStringWithShadow(EnhancedPortals.localize(name), posX + offsetX, posY + 7, titleColour);
        }
        
        if (isFullyOpened())
        {
            drawFullyOpened();
        }
        else if (isFullyClosed())
        {
            drawFullyClosed();
        }
    }
    
    public abstract void drawFullyOpened();
    public abstract void drawFullyClosed();

    public void draw(int x, int y)
    {
        posX = x;
        posY = y;
        draw();
    }

    protected void drawBackground()
    {
        float colorR = (backgroundColor >> 16 & 255) / 255.0F;
        float colorG = (backgroundColor >> 8 & 255) / 255.0F;
        float colorB = (backgroundColor & 255) / 255.0F;

        GL11.glColor4f(colorR, colorG, colorB, 1.0F);
        parent.getTextureManager().bindTexture(texture);

        if (side == 0)
        {
            drawTexturedModalRect(posX - currentWidth, posY + 4, 0, 256 - currentHeight + 4, 4, currentHeight - 4);
            drawTexturedModalRect(posX - currentWidth + 4, posY, 256 - currentWidth + 4, 0, currentWidth - 4, 4);
            drawTexturedModalRect(posX - currentWidth, posY, 0, 0, 4, 4);
            drawTexturedModalRect(posX - currentWidth + 4, posY + 4, 256 - currentWidth + 4, 256 - currentHeight + 4, currentWidth - 4, currentHeight - 4);
        }
        else
        {
            drawTexturedModalRect(posX, posY, 0, 256 - currentHeight, 4, currentHeight);
            drawTexturedModalRect(posX + 4, posY, 256 - currentWidth + 4, 0, currentWidth - 4, 4);
            drawTexturedModalRect(posX, posY, 0, 0, 4, 4);
            drawTexturedModalRect(posX + 4, posY + 4, 256 - currentWidth + 4, 256 - currentHeight + 4, currentWidth - 4, currentHeight - 4);
        }

        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0F);
    }

    public String getID()
    {
        return id;
    }

    /** Return true if this element handled this click **/
    public boolean handleMouseClicked(int x, int y, int mouseButton)
    {
        return false;
    }

    public boolean intersectsWith(int mouseX, int mouseY)
    {
        mouseX += parent.getGuiLeft();
        mouseY += parent.getGuiTop();

        if (mouseX >= posX && mouseX < posX + sizeX && mouseY >= posY && mouseY < posY + sizeY)
        {
            return true;
        }

        return false;
    }

    public boolean isDisabled()
    {
        return disabled;
    }

    public boolean isVisible()
    {
        return visible;
    }

    public BaseTab setDisabled(boolean disabled)
    {
        this.disabled = disabled;
        return this;
    }

    public BaseTab setId(String id)
    {
        this.id = id;
        return this;
    }

    public BaseTab setPosition(int posX, int posY)
    {
        this.posX = parent.getGuiLeft() + posX;
        this.posY = parent.getGuiTop() + posY;
        return this;
    }

    public BaseTab setSize(int sizeX, int sizeY)
    {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        return this;
    }

    public BaseTab setVisible(boolean visible)
    {
        this.visible = visible;
        return this;
    }

    public boolean intersectsWith(int mouseX, int mouseY, int shiftX, int shiftY)
    {
        if (side == 0)
        {
            if (mouseX <= shiftX && mouseX >= shiftX - currentWidth && mouseY >= shiftY && mouseY <= shiftY + currentHeight)
            {
                return true;
            }
        }
        else if (mouseX >= shiftX && mouseX <= shiftX + currentWidth && mouseY >= shiftY && mouseY <= shiftY + currentHeight)
        {
            return true;
        }

        return false;
    }

    public boolean isFullyOpened()
    {
        return currentWidth >= maxWidth;
    }
    
    public boolean isFullyClosed()
    {
        return currentWidth <= minWidth;
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

            if (side == 0)
            {
                TabTracker.setOpenedLeftTab(null);
            }
            else
            {
                TabTracker.setOpenedRightTab(null);
            }
        }
        else
        {
            open = true;

            if (side == 0)
            {
                TabTracker.setOpenedLeftTab(this.getClass());
            }
            else
            {
                TabTracker.setOpenedRightTab(this.getClass());
            }
        }
    }

    public void update()
    {
        if (open && currentWidth < maxWidth)
        {
            currentWidth += tabExpandSpeed;
        }
        else if (!open && currentWidth > minWidth)
        {
            currentWidth -= tabExpandSpeed;
        }

        if (currentWidth > maxWidth)
        {
            currentWidth = maxWidth;
        }
        else if (currentWidth < minWidth)
        {
            currentWidth = minWidth;
        }

        if (open && currentHeight < maxHeight)
        {
            currentHeight += tabExpandSpeed;
        }
        else if (!open && currentHeight > minHeight)
        {
            currentHeight -= tabExpandSpeed;
        }

        if (currentHeight > maxHeight)
        {
            currentHeight = maxHeight;
        }
        else if (currentHeight < minHeight)
        {
            currentHeight = minHeight;
        }

        if (open && currentWidth == maxWidth && currentHeight == maxHeight)
        {
            setFullyOpen();
        }
    }
    
    void drawTexturedModalRect(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + par6), 0, (double)((float)(par3 + 0) * f), (double)((float)(par4 + par6) * f1));
        tessellator.addVertexWithUV((double)(par1 + par5), (double)(par2 + par6), 0, (double)((float)(par3 + par5) * f), (double)((float)(par4 + par6) * f1));
        tessellator.addVertexWithUV((double)(par1 + par5), (double)(par2 + 0), 0, (double)((float)(par3 + par5) * f), (double)((float)(par4 + 0) * f1));
        tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + 0), 0, (double)((float)(par3 + 0) * f), (double)((float)(par4 + 0) * f1));
        tessellator.draw();
    }

    public void addTooltip(List<String> list)
    {
        if (isFullyClosed())
        {
            list.add(EnhancedPortals.localize(name));
        }
    }
}
