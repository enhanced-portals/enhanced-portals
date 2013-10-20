package uk.co.shadeddimensions.ep3.gui.elements;

import java.util.ArrayList;

import net.minecraft.client.gui.Gui;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.ep3.gui.GuiEnhancedPortals;

public class GuiIdentifierList extends Gui
{
    GuiEnhancedPortals gui;
    int x, y, w, h;
    ArrayList<String> identifierList;

    public GuiIdentifierList(GuiEnhancedPortals parent, int x, int y, int w, int h)
    {
        gui = parent;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        identifierList = new ArrayList<String>();

        for (int i = 0; i < 3; i++)
        {
            identifierList.add("diamond diamond diamond diamond diamond diamond diamond diamond diamond");
        }
    }

    public void drawBackground(float f, int i, int j)
    {
        for (int k = 0; k < 5; k++)
        {
            gui.drawItemSlotBackground(x, y + k * 19, w, 16);
        }
    }

    public void drawForeground(int par1, int par2)
    {
        for (int i = 0; i < identifierList.size(); i++)
        {
            drawGlyphs(identifierList.get(i), x + 1, y + i * 19);
        }

        GL11.glColor4f(1f, 1f, 1f, 1f);
        GL11.glDisable(GL11.GL_LIGHTING);
    }

    void drawGlyphs(String s, int xPos, int yPos)
    {
        ArrayList<ItemStack> glyphs = GuiGlyphSelector.getGlyphsFromIdentifier(s);

        for (int i = 0; i < glyphs.size(); i++)
        {
            gui.getItemRenderer().renderItemIntoGUI(gui.getFontRenderer(), gui.getTextureManager(), glyphs.get(i), xPos + i * 18, yPos, false);
        }
    }
}
