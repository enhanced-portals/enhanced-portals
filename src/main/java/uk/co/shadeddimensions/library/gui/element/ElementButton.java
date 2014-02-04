package uk.co.shadeddimensions.library.gui.element;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.library.gui.IGuiBase;

public class ElementButton extends ElementBase
{
    protected String ID, displayText, hoverString;

    public ElementButton(IGuiBase parent, int x, int y, int w, String id, String text)
    {
        this(parent, x, y, w, id, text, "");
    }

    public ElementButton(IGuiBase parent, int x, int y, int w, String id, String text, ArrayList<String> hover)
    {
        this(parent, x, y, w, id, text, "");
        hoverText = hover;
    }

    public ElementButton(IGuiBase parent, int x, int y, int w, String id, String text, String hover)
    {
        super(parent, x, y, w, 20);
        ID = id;
        displayText = text;
        hoverString = hover;
    }
    
    protected ElementButton(IGuiBase parent, int x, int y, int w, int h, String id, String text, String hover)
    {
        super(parent, x, y, w, h);
        ID = id;
        displayText = text;
        hoverString = hover;
    }
    
    protected ElementButton(IGuiBase parent, int x, int y, int w, int h, String id, String text, ArrayList<String> hover)
    {
        super(parent, x, y, w, h);
        ID = id;
        displayText = text;
        hoverText = hover;
    }

    @Override
    public void addTooltip(List<String> list)
    {
        if (hoverText != null)
        {
            for (String s : hoverText)
            {
                list.add(s);
            }
        }
        else if (hoverString != null && hoverString.length() > 0)
        {
            list.add(hoverString);
        }
    }

    @Override
    public void draw()
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        gui.getTextureManager().bindTexture(texture);
        drawTexturedModalRect(posX, posY, 0, 196 + (!isDisabled() ? intersectsWith(gui.getMouseX(), gui.getMouseY()) ? sizeY * 2 : sizeY : 0), sizeX / 2, sizeY);
        drawTexturedModalRect(posX + sizeX / 2, posY, 200 - sizeX / 2, 196 + (!isDisabled() ? intersectsWith(gui.getMouseX(), gui.getMouseY()) ? sizeY * 2 : sizeY : 0), sizeX / 2, sizeY);

        if (displayText != null)
        {
            gui.getFontRenderer().drawStringWithShadow(displayText, posX + sizeX / 2 - gui.getFontRenderer().getStringWidth(displayText) / 2, posY + sizeY / 2 - gui.getFontRenderer().FONT_HEIGHT / 2, !isDisabled() ? intersectsWith(gui.getMouseX(), gui.getMouseY()) ? 16777120 : 14737632 : -6250336);
        }
    }

    public String getID()
    {
        return ID;
    }

    @Override
    public boolean handleMouseClicked(int x, int y, int mouseButton)
    {
        if (!isDisabled() && isVisible())
        {
            Minecraft.getMinecraft().sndManager.playSoundFX("random.click", 1.0F, 1.0F);
            gui.handleElementButtonClick(ID, mouseButton);
            return true;
        }

        return false;
    }

    public void setText(String string)
    {
        displayText = string;
    }
}
