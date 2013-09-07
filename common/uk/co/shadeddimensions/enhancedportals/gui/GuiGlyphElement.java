package uk.co.shadeddimensions.enhancedportals.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;

public class GuiGlyphElement
{
    int xPos, yPos;
    ItemStack DisplayItem;
    boolean canEdit, selected;
    
    public GuiGlyphElement(ItemStack item, boolean edit, int x, int y)
    {
        DisplayItem = item;
        canEdit = edit;
        xPos = x;
        yPos = y;
        selected = false;
    }

    public void mouseClick(int mX, int mY, int button)
    {
        if (mX >= xPos && mX <= xPos + 16 && mY >= yPos && mY <= yPos + 16)
        {
            if (button == 0)
            {
                selected = !selected;
            }
            else if (button == 1)
            {
                selected = false;
            }
        }
    }
    
    public void draw(FontRenderer fontRenderer, TextureManager textureManager, RenderItem itemRenderer, int offsetX, int offsetY)
    {
        GL11.glDisable(GL11.GL_LIGHTING);
        itemRenderer.renderItemIntoGUI(fontRenderer, textureManager, DisplayItem, xPos + offsetX, yPos + offsetY);
    }
}
