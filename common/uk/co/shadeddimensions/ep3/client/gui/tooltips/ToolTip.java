/**
 * Derived from BuildCraft released under the MMPL https://github.com/BuildCraft/BuildCraft http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package uk.co.shadeddimensions.ep3.client.gui.tooltips;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ForwardingList;

public class ToolTip extends ForwardingList<ToolTipLine>
{
    private final List<ToolTipLine> delegate = new ArrayList<ToolTipLine>();
    private final long delay;
    private long mouseOverStart;

    public ToolTip()
    {
        delay = 0;
    }

    public ToolTip(int delay)
    {
        this.delay = delay;
    }

    @Override
    protected final List<ToolTipLine> delegate()
    {
        return delegate;
    }

    public boolean isReady()
    {
        if (delay == 0)
        {
            return true;
        }
        if (mouseOverStart == 0)
        {
            return false;
        }
        return System.currentTimeMillis() - mouseOverStart >= delay;
    }

    public void onTick(boolean mouseOver)
    {
        if (delay == 0)
        {
            return;
        }
        if (mouseOver)
        {
            if (mouseOverStart == 0)
            {
                mouseOverStart = System.currentTimeMillis();
            }
        }
        else
        {
            mouseOverStart = 0;
        }
    }

    public void refresh()
    {
    }
}
