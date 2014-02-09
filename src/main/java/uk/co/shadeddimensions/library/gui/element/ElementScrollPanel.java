package uk.co.shadeddimensions.library.gui.element;

import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.library.gui.IGuiBase;

public class ElementScrollPanel extends ElementBaseContainer
{
    protected float scrollX, scrollY;
    protected int contentHeight, contentWidth, oldMouseX, oldMouseY;
    protected boolean isMouseButtonDown = false;

    public ElementScrollPanel(IGuiBase parent, int x, int y, int w, int h)
    {
        super(parent, x, y, w, h);
        scrollX = scrollY = 0;
    }

    @Override
    public ElementScrollPanel addElement(ElementBase element)
    {
        super.addElement(element);

        if (element.getRelativeY() + element.getHeight() > contentHeight)
        {
            contentHeight = element.getRelativeY() + element.getHeight();
        }

        if (element.getRelativeX() + element.getWidth() > contentWidth)
        {
            contentWidth = element.getRelativeX() + element.getWidth();
        }

        return this;
    }

    @Override
    public void addTooltip(List<String> list)
    {
        for (ElementBase element : elements)
        {
            if (element.isVisible() && element.intersectsWith(gui.getMouseX(), gui.getMouseY()) && isElementVisible(element))
            {
                element.addTooltip(list);

                if (!list.isEmpty())
                {
                    return;
                }
            }
        }
    }

    @Override
    public void clear()
    {
        super.clear();
        contentHeight = 0;
        contentWidth = 0;
        scrollX = scrollY = 0f;
    }

    @Override
    public void draw()
    {
        for (ElementBase element : elements)
        {
            int x = posX + (int) scrollX + element.getRelativeX(), y = posY + (int) scrollY + element.getRelativeY();

            if (element.isVisible() && isElementVisible(element))
            {
                element.draw(x, y);
            }
        }

        GL11.glDisable(GL11.GL_DEPTH_TEST); // Stops blocks from being rendered above GUI elements that are rendered afterwards
    }

    @Override
    public boolean handleMouseClicked(int x, int y, int mouseButton)
    {
        if (disabled)
        {
            return false;
        }
        
        for (ElementBase element : elements)
        {
            if (element.isVisible() && element.intersectsWith(x, y))
            {
                if (element.handleMouseClicked(x, y, mouseButton))
                {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isCoordinateVisible(int x, int y)
    {
        return x > posX + gui.getGuiLeft() && x < posX + sizeX + gui.getGuiLeft() && y > posY + gui.getGuiTop() && y < posY + sizeY + gui.getGuiTop();
    }

    public boolean isElementVisible(ElementBase element)
    {
        int x = posX + (int) scrollX + element.getRelativeX(), y = posY + (int) scrollY + element.getRelativeY();

        if (isCoordinateVisible(x + gui.getGuiLeft(), y + gui.getGuiTop()))
        {
            return true;
        }
        else if (isCoordinateVisible(x + element.getWidth() + gui.getGuiLeft(), y + element.getHeight() + gui.getGuiTop()))
        {
            return true;
        }

        for (int i = 0; i < 3; i++)
        {
            int offsetWidth = element.getWidth() / 3 * i, offsetHeight = element.getHeight() / 3 * i;

            if (isCoordinateVisible(x + offsetWidth + gui.getGuiLeft(), y + offsetHeight + gui.getGuiTop()))
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public void update()
    {
        if (!isVisible() || isDisabled())
        {
            return;
        }

        for (ElementBase element : elements)
        {
            element.update();
        }

        int mouseX = gui.getMouseX() + gui.getGuiLeft(), mouseY = gui.getMouseY() + gui.getGuiTop();

        if (Mouse.isButtonDown(0))
        {
            if (mouseX >= posX && mouseX <= posX + sizeX && mouseY >= posY && mouseY <= posY + sizeY)
            {
                if (isMouseButtonDown)
                {
                    scrollX += mouseX - oldMouseX;
                    scrollY += mouseY - oldMouseY;
                }
                else
                {
                    isMouseButtonDown = true;
                }
            }

            oldMouseX = mouseX;
            oldMouseY = mouseY;
        }
        else
        {
            isMouseButtonDown = false;

            int wheel = Mouse.getDWheel();
            scrollY -= wheel == 0 ? 0 : wheel > 0 ? -10 : 10;
        }

        if (scrollX > 0)
        {
            scrollX = 0;
        }

        if (scrollY > 0)
        {
            scrollY = 0;
        }

        if (contentWidth < sizeX)
        {
            scrollX = 0;
        }
        else if (scrollX < -contentWidth + sizeX)
        {
            scrollX = -contentWidth + sizeX;
        }

        if (contentHeight < sizeY)
        {
            scrollY = 0;
        }
        else if (scrollY < -contentHeight + sizeY)
        {
            scrollY = -contentHeight + sizeY;
        }
    }
}
