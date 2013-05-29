package alz.core.gui;

import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiItemStackButton extends GuiButton
{
    public boolean      isActive, alternateDrawing;
    public ItemStack    itemStack;
    public List<String> hoverText;
    RenderItem          itemRenderer = new RenderItem();

    public GuiItemStackButton(int ID, int X, int Y, ItemStack stack)
    {
        super(ID, X, Y, 16, 16, "");
        isActive = false;
        itemStack = stack;
        alternateDrawing = false;
        displayString = "0";
    }

    public GuiItemStackButton(int ID, int X, int Y, ItemStack stack, boolean active)
    {
        super(ID, X, Y, 16, 16, "");
        isActive = active;
        itemStack = stack;
        alternateDrawing = false;
        displayString = "0";
    }

    public GuiItemStackButton(int ID, int X, int Y, ItemStack stack, boolean active, List<String> text)
    {
        super(ID, X, Y, 16, 16, "");
        isActive = active;
        itemStack = stack;
        hoverText = text;
        alternateDrawing = false;
        displayString = "0";
    }

    public GuiItemStackButton(int ID, int X, int Y, ItemStack stack, boolean active, List<String> text, boolean alternatedrawing)
    {
        super(ID, X, Y, 16, 16, "");
        isActive = active;
        itemStack = stack;
        hoverText = text;
        alternateDrawing = alternatedrawing;
        displayString = "0";
    }

    public GuiItemStackButton(int ID, int X, int Y, ItemStack stack, boolean active, List<String> text, String displayText)
    {
        super(ID, X, Y, 16, 16, "");
        isActive = active;
        itemStack = stack;
        hoverText = text;
        alternateDrawing = false;
        displayString = displayText;
    }

    @Override
    public void drawButton(Minecraft mc, int x, int y)
    {
        if (drawButton)
        {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            field_82253_i = x >= xPosition && y >= yPosition && x < xPosition + width && y < yPosition + height;

            if (!alternateDrawing)
            {
                drawRect(xPosition - 1, yPosition - 1, xPosition - 1 + width + 2, yPosition - 1 + height + 2, isActive ? 0xFF00CC00 : 0xFFCC0000);
                drawRect(xPosition, yPosition, xPosition + width, yPosition + height, field_82253_i ? 0xFF555555 : 0xFF333333);
            }

            itemRenderer.renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, itemStack, xPosition, yPosition);

            if (!displayString.equals("0"))
            {
                itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, mc.renderEngine, itemStack, xPosition, yPosition, displayString);
            }

            if (field_82253_i && hoverText != null && !hoverText.isEmpty())
            {
                drawHoverText(hoverText, x, y, mc.fontRenderer);
            }

            GL11.glDisable(GL11.GL_LIGHTING);
        }
    }

    @SuppressWarnings("rawtypes")
    private void drawHoverText(List par1List, int par2, int par3, FontRenderer font)
    {
        if (!par1List.isEmpty())
        {
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            int k = 0;
            Iterator iterator = par1List.iterator();

            while (iterator.hasNext())
            {
                String s = (String) iterator.next();
                int l = font.getStringWidth(s);

                if (l > k)
                {
                    k = l;
                }
            }

            int i1 = par2 + 12;
            int j1 = par3 - 12;
            int k1 = 8;

            if (par1List.size() > 1)
            {
                k1 += 2 + (par1List.size() - 1) * 10;
            }

            // Stop the hover box from flipping out over the size of the button            
            //if (i1 + k > width)
            //{
            //    i1 -= 28 + k;
            //}

            //if (j1 + k1 + 6 > height)
            //{
            //    j1 = height - k1 - 6;
            //}

            zLevel = 300.0F;
            itemRenderer.zLevel = 300.0F;
            int l1 = -267386864;
            drawGradientRect(i1 - 3, j1 - 4, i1 + k + 3, j1 - 3, l1, l1);
            drawGradientRect(i1 - 3, j1 + k1 + 3, i1 + k + 3, j1 + k1 + 4, l1, l1);
            drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 + k1 + 3, l1, l1);
            drawGradientRect(i1 - 4, j1 - 3, i1 - 3, j1 + k1 + 3, l1, l1);
            drawGradientRect(i1 + k + 3, j1 - 3, i1 + k + 4, j1 + k1 + 3, l1, l1);
            int i2 = 1347420415;
            int j2 = (i2 & 16711422) >> 1 | i2 & -16777216;
            drawGradientRect(i1 - 3, j1 - 3 + 1, i1 - 3 + 1, j1 + k1 + 3 - 1, i2, j2);
            drawGradientRect(i1 + k + 2, j1 - 3 + 1, i1 + k + 3, j1 + k1 + 3 - 1, i2, j2);
            drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 - 3 + 1, i2, i2);
            drawGradientRect(i1 - 3, j1 + k1 + 2, i1 + k + 3, j1 + k1 + 3, j2, j2);

            for (int k2 = 0; k2 < par1List.size(); ++k2)
            {
                String s1 = (String) par1List.get(k2);
                font.drawStringWithShadow(s1, i1, j1, -1);

                if (k2 == 0)
                {
                    j1 += 2;
                }

                j1 += 10;
            }

            zLevel = 0.0F;
            itemRenderer.zLevel = 0.0F;
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            RenderHelper.enableStandardItemLighting();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        }
    }

    @Override
    public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3)
    {
        boolean onSelf = super.mousePressed(par1Minecraft, par2, par3);

        if (onSelf)
        {
            isActive = !isActive;
        }

        return onSelf;
    }
}
