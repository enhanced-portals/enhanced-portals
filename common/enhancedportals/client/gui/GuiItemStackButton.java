package enhancedportals.client.gui;

import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

public class GuiItemStackButton extends Gui
{
    public int x, y;
    public ItemStack itemStack;

    public boolean active, colourInside;
    public List<String> textList;
    public String value;
    GuiPortalModifier parent;

    public GuiItemStackButton(int x, int y, ItemStack stack, List<String> list, String val, boolean colourinside, GuiPortalModifier par)
    {
        this.x = x;
        this.y = y;
        itemStack = stack;
        active = false;
        textList = list;
        value = val;
        colourInside = colourinside;
        parent = par;
    }

    public GuiItemStackButton(int x, int y, ItemStack stack, List<String> list, String val, GuiPortalModifier par)
    {
        this.x = x;
        this.y = y;
        itemStack = stack;
        active = false;
        textList = list;
        value = val;
        colourInside = false;
        parent = par;
    }

    public GuiItemStackButton(ItemStack stack, List<String> list, String val, boolean colourinside, GuiPortalModifier par)
    {
        x = 0;
        y = 0;
        itemStack = stack;
        active = false;
        textList = list;
        value = val;
        colourInside = colourinside;
        parent = par;
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

    public void drawElement(int guiLeft, int guiTop, int x2, int y2, FontRenderer fontRenderer, RenderItem itemRenderer, RenderEngine renderEngine, int i)
    {
        drawElement(guiLeft + 8 + i * 18, guiTop + 15, x2, y2, fontRenderer, itemRenderer, renderEngine);
    }

    public void handleMouseClick(int offsetX, int offsetY, int x, int y, int button, int i)
    {
        if (this.x == 0 && this.y == 0)
        {
            if (isPointInRegion(this.x + offsetX + 1 + (i - 2) * 18 + 8, this.y + offsetY + 1 + 15, 14, 14, x, y))
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

    boolean isPointInRegion(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        return par5 >= par1 - 1 && par5 < par1 + par3 + 1 && par6 >= par2 - 1 && par6 < par2 + par4 + 1;
    }
}
