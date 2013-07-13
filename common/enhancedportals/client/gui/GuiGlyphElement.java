package enhancedportals.client.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

public class GuiGlyphElement
{
    public int x, y;
    public String value;
    public ItemStack itemStack;
    GuiNetwork parent;
    GuiDialDeviceAddNetwork parent2;

    public int stackSize;
    public boolean isStack;

    public GuiGlyphElement(int x, int y, String value, ItemStack stack, GuiDialDeviceAddNetwork p)
    {
        this.x = x;
        this.y = y;
        this.value = value;
        itemStack = stack;
        stackSize = 0;
        parent2 = p;
        isStack = false;
    }

    public GuiGlyphElement(int x, int y, String value, ItemStack stack, GuiDialDeviceAddNetwork p, boolean isstack)
    {
        this.x = x;
        this.y = y;
        this.value = value;
        itemStack = stack;
        stackSize = 0;
        parent2 = p;
        isStack = isstack;
    }

    public GuiGlyphElement(int x, int y, String value, ItemStack stack, GuiNetwork p)
    {
        this.x = x;
        this.y = y;
        this.value = value;
        itemStack = stack;
        stackSize = 0;
        parent = p;
        isStack = false;
    }

    public GuiGlyphElement(int x, int y, String value, ItemStack stack, GuiNetwork p, boolean isstack)
    {
        this.x = x;
        this.y = y;
        this.value = value;
        itemStack = stack;
        stackSize = 0;
        parent = p;
        isStack = isstack;
    }

    public void drawElement(int xOffset, int yOffset, int x2, int y2, FontRenderer fontRenderer, RenderItem itemRenderer, TextureManager textureManager)
    {
        GL11.glDisable(GL11.GL_LIGHTING);

        if (stackSize >= 1)
        {
            drawRect(x + xOffset, y + yOffset, x + xOffset + 16, y + yOffset + 16, 0xFF00CC00);
        }
        else
        {
            if (isPointInRegion(x + xOffset + 1, y + yOffset + 1, 14, 14, x2, y2))
            {
                drawRect(x + xOffset, y + yOffset, x + xOffset + 16, y + yOffset + 16, 0xFFCCCCCC);
            }
        }

        if (itemStack.stackSize != stackSize)
        {
            itemStack.stackSize = stackSize;
        }

        itemRenderer.renderItemIntoGUI(fontRenderer, textureManager, itemStack, x + xOffset, y + yOffset);

        if (itemStack.stackSize >= 1)
        {
            String s1 = String.valueOf(stackSize);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            fontRenderer.drawStringWithShadow(s1, x + xOffset + 19 - 2 - fontRenderer.getStringWidth(s1), y + yOffset + 6 + 3, 16777215);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }

        if (isPointInRegion(x + xOffset + 1, y + yOffset + 1, 14, 14, x2, y2))
        {
            if (parent != null)
            {
                parent.drawItemStackTooltip(itemStack, x2, y2);
            }
            else
            {
                parent2.drawItemTooltip(itemStack, x2, y2);
            }
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

    public void handleMouseClick(int offsetX, int offsetY, int x, int y, int button)
    {
        if (isPointInRegion(this.x + offsetX + 1, this.y + offsetY + 1, 14, 14, x, y))
        {
            if (parent != null)
            {
                parent.elementClicked(this, button);
            }
            else
            {
                parent2.elementClicked(this, button);
            }
        }
    }

    private boolean isPointInRegion(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        return par5 >= par1 - 1 && par5 < par1 + par3 + 1 && par6 >= par2 - 1 && par6 < par2 + par4 + 1;
    }
}
