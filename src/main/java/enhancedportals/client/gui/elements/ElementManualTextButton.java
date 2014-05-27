package enhancedportals.client.gui.elements;

import java.util.List;

import enhancedportals.EnhancedPortals;
import enhancedportals.client.gui.GuiManual;
import enhancedportals.network.ClientProxy;

public class ElementManualTextButton extends BaseElement
{
    String entry;
    String displayStr;
    boolean tooLong = false;
    int length = 19;

    public ElementManualTextButton(GuiManual gui, int x, int y, String mEntry)
    {
        super(gui, x, y, 115, 8);
        entry = mEntry;

        if (entry != null)
        {
            displayStr = EnhancedPortals.localize("manual." + entry + ".title");

            if (displayStr.length() > length)
            {
                displayStr = displayStr.substring(0, length);

                if (displayStr.endsWith(" "))
                {
                    displayStr = displayStr.substring(0, displayStr.length() - 1);
                }

                displayStr += "...";
                tooLong = true;
            }
        }
    }

    public void updateEntry(String s)
    {
        entry = s;
        tooLong = false;

        if (entry != null)
        {
            displayStr = EnhancedPortals.localize("manual." + entry + ".title");

            if (displayStr.length() > length)
            {
                displayStr = displayStr.substring(0, length);

                if (displayStr.endsWith(" "))
                {
                    displayStr = displayStr.substring(0, displayStr.length() - 1);
                }

                displayStr += "...";
                tooLong = true;
            }
        }
    }

    @Override
    public void addTooltip(List<String> list)
    {
        if (tooLong)
        {
            list.add(EnhancedPortals.localize("manual." + entry + ".title"));
        }
    }

    @Override
    protected void drawBackground()
    {

    }

    @Override
    public boolean handleMouseClicked(int x, int y, int mouseButton)
    {
        if (entry == null)
        {
            return false;
        }

        ClientProxy.manualChangeEntry(entry);
        ((GuiManual) parent).pageChanged();
        return true;
    }

    @Override
    protected void drawContent()
    {
        if (entry != null)
        {
            boolean isHovering = intersectsWith(parent.getMouseX(), parent.getMouseY());
            //parent.drawTrueType(posX, posY - 2, (isHovering ? ">" : "") + displayStr, false, isHovering ? 0.2f : 0f, isHovering ? 0.2f : 0f, isHovering ? 0.7f : 0f, 1f);
            parent.getFontRenderer().drawString((isHovering ? ">" : "") + displayStr, posX, posY, isHovering ? 0x3333FF : 0x000000);
        }
    }

    @Override
    public void update()
    {

    }
}
