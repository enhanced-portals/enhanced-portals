package uk.co.shadeddimensions.library.gui.element;

import java.util.List;

import uk.co.shadeddimensions.library.gui.IGuiBase;

public class ElementItemStackPanel extends ElementBaseContainer
{
    int lastX = 0, lastY = 0, highestRow = 0;

    public ElementItemStackPanel(IGuiBase parent, int x, int y, int w, int h)
    {
        super(parent, x, y, w, h);
    }

    @Override
    public ElementBaseContainer addElement(ElementBase element)
    {
        if (lastX + element.getWidth() < sizeX)
        {
            if (element.getHeight() > highestRow)
            {
                highestRow = element.getHeight();
            }

            element.setPosition(lastX, lastY);
            lastX += element.getWidth() + 4;
        }
        else
        {
            lastX = 0;
            lastY += highestRow + 4;
            element.setPosition(lastX, lastY);
            lastX += element.getWidth() + 4;
            highestRow = element.getHeight();
        }

        return super.addElement(element);
    }

    @Override
    public void addTooltip(List<String> list)
    {
        for (ElementBase element : elements)
        {
            if (element.intersectsWith(gui.getMouseX(), gui.getMouseY()))
            {
                element.addTooltip(list);
                ElementItemIconWithCount el = (ElementItemIconWithCount) element;

                if (!list.isEmpty() && el.item.stackSize > 1)
                {
                    list.set(0, el.item.stackSize + "x " + list.get(0));
                    return;
                }
            }
        }
    }
}
