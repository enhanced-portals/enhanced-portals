package enhancedportals.client.gui;

import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

public class GuiItemStackButton
{
    public int x, y;
    public ItemStack itemStack;
    GuiPortalModifier parent;

    public boolean active, colourInside;
    public List<String> textList;
    public String value;

    public GuiItemStackButton(int x, int y, ItemStack stack, GuiPortalModifier p, List<String> list, String val)
    {
        this.x = x;
        this.y = y;
        itemStack = stack;
        parent = p;
        active = false;
        textList = list;
        value = val;
        colourInside = false;
    }

    public GuiItemStackButton(int x, int y, ItemStack stack, GuiPortalModifier p, List<String> list, String val, boolean colourinside)
    {
        this.x = x;
        this.y = y;
        itemStack = stack;
        parent = p;
        active = false;
        textList = list;
        value = val;
        colourInside = colourinside;
    }

    public GuiItemStackButton(ItemStack stack, GuiPortalModifier p, List<String> list, String val, boolean colourinside)
    {
        x = 0;
        y = 0;
        itemStack = stack;
        parent = p;
        active = false;
        textList = list;
        value = val;
        colourInside = colourinside;
    }

    public void drawElement(int xOffset, int yOffset, int x2, int y2, FontRenderer fontRenderer, RenderItem itemRenderer, RenderEngine renderEngine)
    {
        GL11.glDisable(GL11.GL_LIGHTING);

        if (!colourInside)
        {
            // BORDER
            if (active)
            {
                drawRect(x + xOffset - 1, y + yOffset - 1, x + xOffset + 17, y + yOffset + 17, 0xFF00CC00);
            }
            else
            {
                drawRect(x + xOffset - 1, y + yOffset - 1, x + xOffset + 17, y + yOffset + 17, 0xFFCC0000);
            }

            // BACKGROUND
            if (isPointInRegion(x + xOffset + 1, y + yOffset + 1, 14, 14, x2, y2))
            {
                drawRect(x + xOffset, y + yOffset, x + xOffset + 16, y + yOffset + 16, 0xFF888888);
            }
            else
            {
                drawRect(x + xOffset, y + yOffset, x + xOffset + 16, y + yOffset + 16, 0xFF444444);
            }
        }
        else
        {
            // BACKGROUND
            if (active)
            {
                drawRect(x + xOffset, y + yOffset, x + xOffset + 16, y + yOffset + 16, 0xFF00CC00);
            }
            else
            {
                drawRect(x + xOffset, y + yOffset, x + xOffset + 16, y + yOffset + 16, 0xFFCC0000);
            }
        }

        itemRenderer.renderItemAndEffectIntoGUI(fontRenderer, renderEngine, itemStack, x + xOffset, y + yOffset);

        if (isPointInRegion(x + xOffset + 1, y + yOffset + 1, 14, 14, x2, y2))
        {
            parent.drawText(textList, x2, y2);
        }

        GL11.glEnable(GL11.GL_LIGHTING);
    }

    private void drawRect(int par0, int par1, int par2, int par3, int par4)
    {
        int j1;

        if (par0 < par2)
        {
            j1 = par0;
            par0 = par2;
            par2 = j1;
        }

        if (par1 < par3)
        {
            j1 = par1;
            par1 = par3;
            par3 = j1;
        }

        float f = (par4 >> 24 & 255) / 255.0F;
        float f1 = (par4 >> 16 & 255) / 255.0F;
        float f2 = (par4 >> 8 & 255) / 255.0F;
        float f3 = (par4 & 255) / 255.0F;
        Tessellator tessellator = Tessellator.instance;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(f1, f2, f3, f);
        tessellator.startDrawingQuads();
        tessellator.addVertex(par0, par3, 0.0D);
        tessellator.addVertex(par2, par3, 0.0D);
        tessellator.addVertex(par2, par1, 0.0D);
        tessellator.addVertex(par0, par1, 0.0D);
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public void handleMouseClick(int offsetX, int offsetY, int x, int y, int button, int i)
    {
        if (this.x == 0 && this.y == 0)
        {
            if (isPointInRegion(this.x + offsetX + 1 + ((i - 2) * 18) + 8, this.y + offsetY + 1 + 15, 14, 14, x, y))
            {
                parent.elementClicked(this, button);
            }
        }
        else
        {
            if (isPointInRegion(this.x + offsetX + 1, this.y + offsetY + 1, 14, 14, x, y))
            {
                parent.elementClicked(this, button);
            }
        }
    }

    private boolean isPointInRegion(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        return par5 >= par1 - 1 && par5 < par1 + par3 + 1 && par6 >= par2 - 1 && par6 < par2 + par4 + 1;
    }

    public void drawElement(int guiLeft, int guiTop, int x2, int y2, FontRenderer fontRenderer, RenderItem itemRenderer, RenderEngine renderEngine, int i)
    {
        drawElement(guiLeft + 8 + (i * 18), guiTop + 15, x2, y2, fontRenderer, itemRenderer, renderEngine);
    }
}
