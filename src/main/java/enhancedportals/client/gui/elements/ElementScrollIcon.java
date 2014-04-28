package enhancedportals.client.gui.elements;

import java.util.List;

import net.minecraft.util.Icon;
import enhancedportals.client.gui.BaseGui;

public class ElementScrollIcon extends BaseElement
{
    List<Icon> icons;

    public ElementScrollIcon(BaseGui gui, int x, int y)
    {
        super(gui, x, y, 150, 200);
    }
    
    public ElementScrollIcon addIcon(Icon i)
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
