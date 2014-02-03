package uk.co.shadeddimensions.library.gui.element;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.library.gui.IGuiBase;
import uk.co.shadeddimensions.library.gui.Parser;

public abstract class ElementBaseContainer extends ElementBase
{
    protected ArrayList<ElementBase> elements;

    public ElementBaseContainer(IGuiBase parent, int x, int y, int w, int h)
    {
        super(parent, x, y, w, h);
        elements = new ArrayList<ElementBase>();
    }

    public ElementBaseContainer addElement(ElementBase element)
    {
        elements.add(element);

        return this;
    }

    @Override
    public void addTooltip(List<String> list)
    {
        for (ElementBase element : elements)
        {
            if (element.intersectsWith(gui.getMouseX(), gui.getMouseY()))
            {
                element.addTooltip(list);

                if (!list.isEmpty())
                {
                    return;
                }
            }
        }
    }

    public void clear()
    {
        elements.clear();
    }

    @Override
    public void draw()
    {
        for (ElementBase element : elements)
        {
            if (element.isVisible())
            {
                element.draw(posX + element.getRelativeX(), posY + element.getRelativeY());
                GL11.glDisable(GL11.GL_LIGHTING);
            }
        }
    }

    public ArrayList<ElementBase> getElements()
    {
        return elements;
    }

    @Override
    public boolean handleMouseClicked(int x, int y, int mouseButton)
    {
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

    public ElementBaseContainer parseElements(String string)
    {
        for (ElementBase element : new Parser(gui).setMaxWidth(sizeX).parse(string))
        {
            addElement(element);
        }

        return this;
    }

    @Override
    public void update()
    {
        for (ElementBase element : elements)
        {
            element.update();
        }
    }
}
