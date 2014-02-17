package uk.co.shadeddimensions.library.gui.element;

import net.minecraft.client.gui.Gui;

import org.lwjgl.input.Mouse;

import uk.co.shadeddimensions.library.gui.IGuiBase;

public class ElementScrollBar extends ElementBase
{
    ElementScrollPanel panel;
    int barSize;
    boolean isMouseButtonDown;
    int colour, colour2;
    float scroll, oldMouse;

    public ElementScrollBar(IGuiBase parent, int x, int y, int w, int h, ElementScrollPanel scroll)
    {
        super(parent, x, y, w, h);
        panel = scroll;
        colour = 0x33000000;
        colour2 = 0x77000000;
    }

    public ElementScrollBar(IGuiBase parent, int x, int y, int w, int h, ElementScrollPanel scroll, int c1, int c2)
    {
        this(parent, x, y, w, h, scroll);
        colour = c1;
        colour2 = c2;
    }

    @Override
    public void draw()
    {
        int scr = Math.min(sizeY - barSize - 1, (int) scroll);
        Gui.drawRect(posX, posY, posX + sizeX, posY + sizeY - 1, colour);
        Gui.drawRect(posX, posY + scr, posX + sizeX, posY + scr + barSize, colour2);
    }

    @Override
    public boolean isDisabled()
    {
        disabled = panel.isDisabled();
        return super.isDisabled();
    }

    @Override
    public boolean isVisible()
    {
        visible = panel.isVisible();
        return super.isVisible();
    }

    @Override
    public void update()
    {
        barSize = (int) ((float) panel.sizeY / (float) panel.contentHeight * sizeY);

        if (barSize >= sizeY)
        {
            barSize = 0;
        }

        if (panel.contentHeight < panel.sizeY || !isVisible() || isDisabled())
        {
            return;
        }

        if (Mouse.isButtonDown(0))
        {
            if (intersectsWith(gui.getMouseX(), gui.getMouseY()))
            {
                if (gui.getMouseY() + gui.getGuiTop() > posY + (int) scroll && gui.getMouseY() + gui.getGuiTop() < posY + (int) scroll + barSize)
                {
                    isMouseButtonDown = true;
                }
            }

            if (isMouseButtonDown)
            {
                scroll += gui.getMouseY() - oldMouse;

                if (scroll < 0)
                {
                    scroll = 0;
                }

                if (scroll > sizeY - barSize)
                {
                    scroll = sizeY - barSize;
                }

                panel.scrollY = -(scroll / sizeY * panel.contentHeight);
            }
        }
        else
        {
            scroll = -panel.scrollY / panel.contentHeight * sizeY;
            isMouseButtonDown = false;
        }

        if (!isMouseButtonDown)
        {
            scroll = -panel.scrollY / panel.contentHeight * sizeY;
        }

        oldMouse = gui.getMouseY();

        if (scroll < 0)
        {
            scroll = 0;
        }

        if (scroll > sizeY - barSize)
        {
            scroll = sizeY - barSize;
        }
    }
}
