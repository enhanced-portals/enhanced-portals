package enhancedportals.client.gui.tabs;

import java.util.List;

import enhancedportals.EnhancedPortals;
import enhancedportals.client.gui.BaseGui;

public class TabTip extends BaseTab
{
    String tip;

    public TabTip(BaseGui gui, String n)
    {
        super(gui, 0);
        name = "tab." + n;
        tip = EnhancedPortals.localize("tab." + n + ".info");
        List l = gui.getFontRenderer().listFormattedStringToWidth(tip, maxWidth - 14);
        maxHeight = l.size() * gui.getFontRenderer().FONT_HEIGHT + 30;
        backgroundColor = 0x6666CC;
    }

    @Override
    public void drawFullyOpened()
    {
        parent.getFontRenderer().drawSplitString(tip, posX - maxWidth + 7, posY + 20, maxWidth - 14, 0x000000);
    }

    @Override
    public void drawFullyClosed()
    {

    }
}
