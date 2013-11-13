package uk.co.shadeddimensions.ep3.client.gui.element;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.ep3.client.gui.IElementHandler;
import cofh.gui.GuiBase;
import cofh.gui.element.ElementBase;

public class ElementFakeTab extends ElementBase
{
    RenderItem itemRenderer = new RenderItem();
    String displayText;
    boolean isActive;
    int maxX = 0;
    ItemStack displayItem;
    
    public ElementFakeTab(GuiBase gui, int posX, int posY, String name, ItemStack s, boolean active)
    {
        super(gui, posX, posY);
        displayText = name;
        isActive = active;
        sizeX = sizeY = 22;
        maxX = Minecraft.getMinecraft().fontRenderer.getStringWidth(name) + 30;
        displayItem = s;
    }

    @Override
    public void draw()
    {
        float colour = isActive ? 1f : 0.5f;
        GL11.glColor3f(colour, colour, colour);
        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("cofh", "textures/gui/elements/Tab_Left.png"));
        drawTexturedModalRect(posX, posY, 0, 0, sizeX - 4, sizeY - 4);
        drawTexturedModalRect(posX, posY + sizeY - 4, 0, 252, sizeX - 4, 4);
        drawTexturedModalRect(posX + sizeX - 4, posY, 252, 0, 4, sizeY - 4);
        drawTexturedModalRect(posX + sizeX - 4, posY + sizeY - 4, 252, 252, 4, 4);
        
        if (isActive)
        {
            if (sizeX < maxX)
            {
                sizeX += 2;
            }
            else
            {
                Minecraft.getMinecraft().fontRenderer.drawString(displayText, posX + 25, posY + 7, 0xFF000000);
            }
        }
        else
        {
            if (sizeX > sizeY)
            {
                sizeX -= 2;
            }
        }
        
        itemRenderer.renderItemAndEffectIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().renderEngine, displayItem, posX + 4, posY + 3);
        posX = gui.getGuiLeft() - sizeX;
        GL11.glColor4f(1f, 1f, 1f, 1f);
        GL11.glDisable(GL11.GL_LIGHTING);
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
