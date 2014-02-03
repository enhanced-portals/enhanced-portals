package uk.co.shadeddimensions.library.gui.tab;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.library.gui.GuiBaseContainer;
import uk.co.shadeddimensions.library.gui.IGuiBase;
import uk.co.shadeddimensions.library.gui.TabTracker;
import uk.co.shadeddimensions.library.gui.element.ElementBaseContainer;
import uk.co.shadeddimensions.library.util.GuiUtils;

/**
 * Base class for a tab element. Has self-contained rendering methods and a link back to the {@link GuiBaseContainer} it is a part of.
 * 
 * @author King Lemming
 * 
 */
public abstract class TabBase extends ElementBaseContainer
{
    public static int tabExpandSpeed = 8;

    public boolean open, drawName = true;
    public int side = 1;

    public int backgroundColor = 0xffffff;

    public int currentShiftX = 0;
    public int currentShiftY = 0;

    public int minWidth = 22;
    public int maxWidth = 124;
    public int currentWidth = minWidth;

    public int minHeight = 22;
    public int maxHeight = 22;
    public int currentHeight = minHeight;

    public static final ResourceLocation DEFAULT_TEXTURE_LEFT = new ResourceLocation("alzlib", "textures/gui/tabLeft.png");
    public static final ResourceLocation DEFAULT_TEXTURE_RIGHT = new ResourceLocation("alzlib", "textures/gui/tabRight.png");

    public int titleColour = 0xFFFFFF;
    public Icon icon;
    public ItemStack stack;
    public String ID;

    public TabBase(IGuiBase gui)
    {
        super(gui, 0, 0, 0, 0);
        texture = DEFAULT_TEXTURE_RIGHT;
    }

    public TabBase(IGuiBase gui, int side)
    {
        super(gui, 0, 0, 0, 0);
        this.side = side;

        if (side == 0)
        {
            texture = DEFAULT_TEXTURE_LEFT;
        }
        else
        {
            texture = DEFAULT_TEXTURE_RIGHT;
        }
    }

    @Override
    public void draw()
    {
        drawBackground();

        if (icon != null)
        {
            int offsetX = side == 0 ? 4 - currentWidth : 2;
            GuiUtils.drawIcon(gui, icon, posX + offsetX, posY + 3, 1);
        }
        else if (stack != null)
        {
            int offsetX = side == 0 ? 4 - currentWidth : 2;
            GuiUtils.drawItemStack(gui, stack, posX + offsetX, posY + 3);
        }

        if (isFullyOpened() && drawName)
        {
            int offset = icon != null || stack != null ? 22 : 4;
            int offsetX = side == 0 ? offset - currentWidth + 2 : offset;
            gui.getFontRenderer().drawStringWithShadow(name, posX + offsetX, posY + 7, titleColour);
        }
        
        if (isFullyOpened())
        {
            super.draw();
        }
    }

    protected void drawBackground()
    {
        float colorR = (backgroundColor >> 16 & 255) / 255.0F;
        float colorG = (backgroundColor >> 8 & 255) / 255.0F;
        float colorB = (backgroundColor & 255) / 255.0F;

        GL11.glColor4f(colorR, colorG, colorB, 1.0F);

        gui.getTextureManager().bindTexture(texture);

        if (side == 0)
        {
            GuiUtils.instance.drawTexturedModalRect(posX - currentWidth, posY + 4, 0, 256 - currentHeight + 4, 4, currentHeight - 4);
            GuiUtils.instance.drawTexturedModalRect(posX - currentWidth + 4, posY, 256 - currentWidth + 4, 0, currentWidth - 4, 4);
            GuiUtils.instance.drawTexturedModalRect(posX - currentWidth, posY, 0, 0, 4, 4);
            GuiUtils.instance.drawTexturedModalRect(posX - currentWidth + 4, posY + 4, 256 - currentWidth + 4, 256 - currentHeight + 4, currentWidth - 4, currentHeight - 4);
        }
        else
        {
            GuiUtils.instance.drawTexturedModalRect(posX, posY, 0, 256 - currentHeight, 4, currentHeight);
            GuiUtils.instance.drawTexturedModalRect(posX + 4, posY, 256 - currentWidth + 4, 0, currentWidth - 4, 4);
            GuiUtils.instance.drawTexturedModalRect(posX, posY, 0, 0, 4, 4);
            GuiUtils.instance.drawTexturedModalRect(posX + 4, posY + 4, 256 - currentWidth + 4, 256 - currentHeight + 4, currentWidth - 4, currentHeight - 4);
        }

        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0F);
    }

    @Override
    public int getHeight()
    {
        return currentHeight;
    }

    @Override
    public int getWidth()
    {
        return currentWidth;
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

    @Override
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
}
