package enhancedportals.client.gui.elements;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import enhancedportals.client.gui.BaseGui;

public abstract class BaseElement
{
    protected BaseGui parent;
    protected boolean visible = true, disabled = false;
    protected ArrayList<String> hoverText;
    protected int posX, posY, sizeX, sizeY;
    protected String id;
    protected ResourceLocation texture;

    public BaseElement(BaseGui gui, int x, int y, int w, int h)
    {
        parent = gui;
        posX = gui.getGuiLeft() + x;
        posY = gui.getGuiTop() + y;
        sizeX = w;
        sizeY = h;
    }

    public void draw()
    {
        drawBackground();
        drawContent();
    }
    
    public abstract void addTooltip(List<String> list);

    public void draw(int x, int y)
    {
        posX = x;
        posY = y;
        draw();
    }
    
    protected abstract void drawContent();

    protected void drawBackground()
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        if (texture != null)
        {
            parent.getMinecraft().renderEngine.bindTexture(texture);
            parent.drawTexturedModalRect(posX, posY, 0, 0, sizeX, sizeY);
        }
    }

    public String getID()
    {
        return id;
    }

    /** Return true if this element handled this click **/
    public boolean handleMouseClicked(int x, int y, int mouseButton)
    {
        return false;
    }

    public boolean intersectsWith(int mouseX, int mouseY)
    {
        mouseX += parent.getGuiLeft();
        mouseY += parent.getGuiTop();

        if (mouseX >= posX && mouseX < posX + sizeX && mouseY >= posY && mouseY < posY + sizeY)
        {
            return true;
        }

        return false;
    }

    public boolean isDisabled()
    {
        return disabled;
    }

    public boolean isVisible()
    {
        return visible;
    }

    public BaseElement setDisabled(boolean disabled)
    {
        this.disabled = disabled;
        return this;
    }

    public BaseElement setId(String id)
    {
        this.id = id;
        return this;
    }

    public BaseElement setPosition(int posX, int posY)
    {
        this.posX = parent.getGuiLeft() + posX;
        this.posY = parent.getGuiTop() + posY;
        return this;
    }

    public BaseElement setSize(int sizeX, int sizeY)
    {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        return this;
    }

    public BaseElement setVisible(boolean visible)
    {
        this.visible = visible;
        return this;
    }

    public abstract void update();
    
    void drawTexturedModalRect(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + par6), 0, (double)((float)(par3 + 0) * f), (double)((float)(par4 + par6) * f1));
        tessellator.addVertexWithUV((double)(par1 + par5), (double)(par2 + par6), 0, (double)((float)(par3 + par5) * f), (double)((float)(par4 + par6) * f1));
        tessellator.addVertexWithUV((double)(par1 + par5), (double)(par2 + 0), 0, (double)((float)(par3 + par5) * f), (double)((float)(par4 + 0) * f1));
        tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + 0), 0, (double)((float)(par3 + 0) * f), (double)((float)(par4 + 0) * f1));
        tessellator.draw();
    }
}
