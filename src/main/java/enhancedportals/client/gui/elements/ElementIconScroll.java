package enhancedportals.client.gui.elements;

import java.util.List;

import net.minecraft.util.Icon;
import enhancedportals.client.gui.BaseGui;

public class ElementIconScroll extends BaseElement
{
    List<Icon> icons;

    public ElementIconScroll(BaseGui gui, int x, int y)
    {
        super(gui, x, y, 150, 200);
    }
    
    public ElementIconScroll addIcon(Icon i)
    {
        icons.add(i);
        return this;
    }

    @Override
    public void addTooltip(List<String> list)
    {

    }

    @Override
    protected void drawContent()
    {

    }

    @Override
    public void update()
    {
        
    }
}
