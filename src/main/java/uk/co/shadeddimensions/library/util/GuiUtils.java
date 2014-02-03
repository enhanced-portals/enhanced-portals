package uk.co.shadeddimensions.library.util;

import java.util.Iterator;
import java.util.List;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import uk.co.shadeddimensions.library.gui.IGuiBase;

public class GuiUtils extends Gui
{
    public static GuiUtils instance = new GuiUtils();

    /**
     * Simple method used to draw a fluid of arbitrary size.
     */
    public static void drawFluid(IGuiBase gui, int x, int y, FluidStack fluid, int width, int height)
    {
        if (fluid == null || fluid.getFluid() == null)
        {
            return;
        }

        gui.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);

        int colour = fluid.getFluid().getColor(fluid);
        GL11.glColor3ub((byte) (colour >> 16 & 0xFF), (byte) (colour >> 8 & 0xFF), (byte) (colour & 0xFF));

        drawTiledTexture(gui, x, y, fluid.getFluid().getIcon(fluid), width, height);
    }

    public static void drawIcon(IGuiBase gui, Icon icon, int x, int y, int spriteSheet)
    {
        if (spriteSheet == 0)
        {
            gui.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        }
        else
        {
            gui.getTextureManager().bindTexture(TextureMap.locationItemsTexture);
        }

        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0F);
        instance.drawTexturedModelRectFromIcon(x, y, icon, 16, 16);
    }

    public static void drawItemStack(IGuiBase gui, ItemStack stack, int x, int y)
    {
        if (stack != null)
        {
            RenderHelper.enableGUIStandardItemLighting();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            gui.getItemRenderer().renderItemAndEffectIntoGUI(gui.getFontRenderer(), gui.getTextureManager(), stack, x, y);
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.disableStandardItemLighting();
        }
    }

    public static void drawScaledTexturedModelRectFromIcon(IGuiBase gui, int x, int y, Icon icon, int width, int height)
    {
        if (icon == null)
        {
            return;
        }

        double minU = icon.getMinU();
        double maxU = icon.getMaxU();
        double minV = icon.getMinV();
        double maxV = icon.getMaxV();

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x + 0, y + height, gui.getZLevel(), minU, minV + (maxV - minV) * height / 16F);
        tessellator.addVertexWithUV(x + width, y + height, gui.getZLevel(), minU + (maxU - minU) * width / 16F, minV + (maxV - minV) * height / 16F);
        tessellator.addVertexWithUV(x + width, y + 0, gui.getZLevel(), minU + (maxU - minU) * width / 16F, minV);
        tessellator.addVertexWithUV(x + 0, y + 0, gui.getZLevel(), minU, minV);
        tessellator.draw();
    }

    public static void drawSizedTexturedModalRect(IGuiBase gui, int x, int y, int u, int v, int width, int height, float texW, float texH)
    {
        float texU = 1 / texW;
        float texV = 1 / texH;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x + 0, y + height, gui.getZLevel(), (u + 0) * texU, (v + height) * texV);
        tessellator.addVertexWithUV(x + width, y + height, gui.getZLevel(), (u + width) * texU, (v + height) * texV);
        tessellator.addVertexWithUV(x + width, y + 0, gui.getZLevel(), (u + width) * texU, (v + 0) * texV);
        tessellator.addVertexWithUV(x + 0, y + 0, gui.getZLevel(), (u + 0) * texU, (v + 0) * texV);
        tessellator.draw();
    }

    public static void drawTiledTexture(IGuiBase gui, int x, int y, Icon icon, int width, int height)
    {
        int i = 0;
        int j = 0;

        int drawHeight = 0;
        int drawWidth = 0;

        for (i = 0; i < width; i += 16)
        {
            for (j = 0; j < height; j += 16)
            {
                drawWidth = Math.min(width - i, 16);
                drawHeight = Math.min(height - j, 16);
                drawScaledTexturedModelRectFromIcon(gui, x + i, y + j, icon, drawWidth, drawHeight);
            }
        }

        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0F);
    }

    public static void drawTooltipHoveringText(IGuiBase gui, List<String> list, int x, int y)
    {
        if (list == null || list.isEmpty())
        {
            return;
        }

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        int k = 0;
        Iterator<String> iterator = list.iterator();

        while (iterator.hasNext())
        {
            String s = iterator.next();
            int l = gui.getFontRenderer().getStringWidth(s);

            if (l > k)
            {
                k = l;
            }
        }

        int i1 = x + 12;
        int j1 = y - 12;
        int k1 = 8;

        if (list.size() > 1)
        {
            k1 += 2 + (list.size() - 1) * 10;
        }
        if (i1 + k > gui.getWidth())
        {
            i1 -= 28 + k;
        }
        if (j1 + k1 + 6 > gui.getHeight())
        {
            j1 = gui.getHeight() - k1 - 6;
        }

        gui.setZLevel(300.0F);
        gui.getItemRenderer().zLevel = 300.0F;
        int l1 = -267386864;
        instance.drawGradientRect(i1 - 3, j1 - 4, i1 + k + 3, j1 - 3, l1, l1);
        instance.drawGradientRect(i1 - 3, j1 + k1 + 3, i1 + k + 3, j1 + k1 + 4, l1, l1);
        instance.drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 + k1 + 3, l1, l1);
        instance.drawGradientRect(i1 - 4, j1 - 3, i1 - 3, j1 + k1 + 3, l1, l1);
        instance.drawGradientRect(i1 + k + 3, j1 - 3, i1 + k + 4, j1 + k1 + 3, l1, l1);
        int i2 = 1347420415;
        int j2 = (i2 & 16711422) >> 1 | i2 & -16777216;
        instance.drawGradientRect(i1 - 3, j1 - 3 + 1, i1 - 3 + 1, j1 + k1 + 3 - 1, i2, j2);
        instance.drawGradientRect(i1 + k + 2, j1 - 3 + 1, i1 + k + 3, j1 + k1 + 3 - 1, i2, j2);
        instance.drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 - 3 + 1, i2, i2);
        instance.drawGradientRect(i1 - 3, j1 + k1 + 2, i1 + k + 3, j1 + k1 + 3, j2, j2);

        for (int k2 = 0; k2 < list.size(); ++k2)
        {
            String s1 = list.get(k2);
            gui.getFontRenderer().drawStringWithShadow(s1, i1, j1, -1);

            if (k2 == 0)
            {
                j1 += 2;
            }
            j1 += 10;
        }

        gui.setZLevel(0.0F);
        gui.getItemRenderer().zLevel = 0.0F;
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
    }

    public static int getCenteredOffset(IGuiBase gui, String string, int xWidth)
    {
        return (xWidth - gui.getFontRenderer().getStringWidth(string)) / 2;
    }
}
