package uk.co.shadeddimensions.enhancedportals.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiGlyphElement extends Gui
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
    
    public void draw(FontRenderer fontRenderer, TextureManager textureManager, RenderItem itemRenderer, int offsetX, int offsetY, int mouseX, int mouseY)
    {
        GL11.glDisable(GL11.GL_LIGHTING);
        
        if (DisplayItem == null)
        {
            return;
        }
        
        if (mouseX >= xPos + offsetX && mouseX <= xPos + offsetX + 16 && mouseY >= yPos + offsetY && mouseY <= yPos + offsetY + 16 && canEdit)
        {
            drawRect(xPos + offsetX, yPos + offsetY, xPos + offsetX + 16, yPos + offsetY + 16, 0x33FFFFFF);
        }
        
        itemRenderer.renderItemIntoGUI(fontRenderer, textureManager, DisplayItem, xPos + offsetX, yPos + offsetY);
        itemRenderer.renderItemOverlayIntoGUI(fontRenderer, textureManager, DisplayItem, xPos + offsetX, yPos + offsetY, DisplayItem.stackSize == 1 ? "1" : null);     
    }

    public void drawForeground(FontRenderer fontRenderer, TextureManager renderEngine, RenderItem itemRenderer, int offsetX, int offsetY, int mouseX, int mouseY)
    {
        if (DisplayItem == null)
        {
            return;
        }
        
        if (mouseX >= xPos + offsetX && mouseX <= xPos + offsetX + 16 && mouseY >= yPos + offsetY && mouseY <= yPos + offsetY + 16)
        {
            List<String> strList = new ArrayList<String>();
            strList.add(I18n.func_135053_a(DisplayItem.getItemName() + ".name"));
            drawHoveringText(strList, mouseX, mouseY, fontRenderer, itemRenderer);
        }
    }
    
    @SuppressWarnings("rawtypes")
    protected void drawHoveringText(List par1List, int par2, int par3, FontRenderer font, RenderItem itemRenderer)
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
                String s = (String)iterator.next();
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

            this.zLevel = 300.0F;
            itemRenderer.zLevel = 300.0F;
            int l1 = -267386864;
            this.drawGradientRect(i1 - 3, j1 - 4, i1 + k + 3, j1 - 3, l1, l1);
            this.drawGradientRect(i1 - 3, j1 + k1 + 3, i1 + k + 3, j1 + k1 + 4, l1, l1);
            this.drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 + k1 + 3, l1, l1);
            this.drawGradientRect(i1 - 4, j1 - 3, i1 - 3, j1 + k1 + 3, l1, l1);
            this.drawGradientRect(i1 + k + 3, j1 - 3, i1 + k + 4, j1 + k1 + 3, l1, l1);
            int i2 = 1347420415;
            int j2 = (i2 & 16711422) >> 1 | i2 & -16777216;
            this.drawGradientRect(i1 - 3, j1 - 3 + 1, i1 - 3 + 1, j1 + k1 + 3 - 1, i2, j2);
            this.drawGradientRect(i1 + k + 2, j1 - 3 + 1, i1 + k + 3, j1 + k1 + 3 - 1, i2, j2);
            this.drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 - 3 + 1, i2, i2);
            this.drawGradientRect(i1 - 3, j1 + k1 + 2, i1 + k + 3, j1 + k1 + 3, j2, j2);

            for (int k2 = 0; k2 < par1List.size(); ++k2)
            {
                String s1 = (String)par1List.get(k2);
                font.drawStringWithShadow(s1, i1, j1, -1);

                if (k2 == 0)
                {
                    j1 += 2;
                }

                j1 += 10;
            }

            this.zLevel = 0.0F;
            itemRenderer.zLevel = 0.0F;
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            RenderHelper.enableStandardItemLighting();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        }
    }

    public byte mouseClicked(int offsetX, int offsetY, int mouseX, int mouseY, int mouseButton, boolean canIncrement)
    {
        if (DisplayItem == null)
        {
            return 0;
        }
        
        if (mouseX >= xPos + offsetX && mouseX <= xPos + offsetX + 16 && mouseY >= yPos + offsetY && mouseY <= yPos + offsetY + 16)
        {
            if (mouseButton == 0 && canIncrement)
            {
                if (!selected)
                {
                    selected = true;
                    DisplayItem.stackSize++;
                }
                else
                {
                    DisplayItem.stackSize++;
                }
                
                return 1;
            }
            else if (mouseButton == 1)
            {
                if (selected && DisplayItem.stackSize > 1)
                {
                    DisplayItem.stackSize--;
                }
                else if (selected && DisplayItem.stackSize == 1)
                {
                    DisplayItem.stackSize--;
                    selected = false;
                }
                
                return 2;
            }
        }
        
        return 0;
    }
}
