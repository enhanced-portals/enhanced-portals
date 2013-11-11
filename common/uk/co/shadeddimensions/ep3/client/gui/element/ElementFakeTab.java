package uk.co.shadeddimensions.ep3.client.gui.element;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.ep3.client.gui.IElementHandler;
import cofh.gui.GuiBase;
import cofh.gui.element.ElementBase;

public class ElementFakeTab extends ElementBase
{
    String displayText;
    boolean isActive;
    
    public ElementFakeTab(GuiBase gui, int posX, int posY, String name, boolean active)
    {
        super(gui, posX, posY);
        displayText = name;
        isActive = active;
        sizeX = Minecraft.getMinecraft().fontRenderer.getStringWidth(name) + 14;
        sizeY = 18;
    }

    @Override
    public void draw()
    {
        float colour = isActive ? 1f : 0.7f;
        GL11.glColor3f(colour, colour, colour);
        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("enhancedportals", "textures/gui/sizableGui.png"));
        drawTexturedModalRect(posX, posY, 0, 0, sizeX - 4, sizeY);
        drawTexturedModalRect(posX + sizeX - 4, posY, 252, 0, 4, sizeY);
        Minecraft.getMinecraft().fontRenderer.drawString(displayText, posX + 7, posY + 7, isActive ? 0xFF000000 : 0xFF404040);
    }
    
    @Override
    public boolean handleMouseClicked(int x, int y, int mouseButton)
    {
        if (!isActive)
        {
            isActive = true;
            
            if (gui instanceof IElementHandler)
            {
                ((IElementHandler) gui).onElementChanged(this, null);
            }
            
            return true;
        }
        
        return false;
    }
    
    public void setActive(boolean b)
    {
        isActive = b;
    }

    @Override
    public String getTooltip()
    {
        return null;
    }
}
