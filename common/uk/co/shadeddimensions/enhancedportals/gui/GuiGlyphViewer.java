package uk.co.shadeddimensions.enhancedportals.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiGlyphViewer extends GuiGlyphSelector
{
    GuiGlyphSelector selector;

    public GuiGlyphViewer(int x, int y, int colour, GuiEnhancedPortals gui, GuiGlyphSelector sel)
    {
        super(x, y, colour, gui);

        selector = sel;
    }

    @Override
    public ArrayList<ItemStack> getSelectedGlyphs()
    {
        return selector.getSelectedGlyphs();
    }

    @Override
    public int getCurrentSelectedCount()
    {
        return selector.getCurrentSelectedCount();
    }

    @Override
    public String getSelectedIdentifier()
    {
        return selector.getSelectedIdentifier();
    }

    @Override
    public void drawBackground(int mouseX, int mouseY)
    {
        int X = x + gui.getGuiLeft(), Y = y + gui.getGuiTop();
        ArrayList<ItemStack> glyphs = getSelectedGlyphs();

        Color c = new Color(colour);
        GL11.glColor4f(c.getRed() / 255, c.getGreen() / 255, c.getBlue() / 255, 1f);

        gui.getTextureManager().func_110577_a(new ResourceLocation("enhancedportals", "textures/gui/inventorySlots.png"));
        drawTexturedModalRect(X, Y, 0, 54, 162, 18);

        for (int i = 0; i < glyphs.size(); i++)
        {
            int tX = X + i % ITEMS_PER_LINE * 18 + 1, tY = Y + i / ITEMS_PER_LINE * 18 + 1;

            gui.getItemRenderer().renderItemIntoGUI(gui.fontRenderer, gui.getTextureManager(), glyphs.get(i), tX, tY, false);
        }

        GL11.glColor4f(1f, 1f, 1f, 1f);
        GL11.glDisable(GL11.GL_LIGHTING);
    }

    @Override
    public void drawForeground(int mouseX, int mouseY)
    {
        if (canEdit && isOnSelf(mouseX, mouseY))
        {
            ArrayList<ItemStack> glyphs = getSelectedGlyphs();

            for (int i = 0; i < glyphs.size(); i++)
            {
                if (isOnElement(mouseX, mouseY, i))
                {
                    List<String> strList = new ArrayList<String>();
                    strList.add(I18n.func_135053_a(glyphs.get(i).getItemName() + ".name"));
                    strList.add(EnumChatFormatting.DARK_GRAY + "Click to remove");
                    drawHoveringText(strList, mouseX - gui.getGuiLeft(), mouseY - gui.getGuiTop(), gui.fontRenderer, gui.getItemRenderer());
                }
            }
        }

        GL11.glColor4f(1f, 1f, 1f, 1f);
        GL11.glDisable(GL11.GL_LIGHTING);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button)
    {
        if (canEdit && isOnSelf(mouseX, mouseY))
        {
            for (int i = 0; i < Glyphs.size(); i++)
            {
                if (isOnElement(mouseX, mouseY, i))
                {
                    if (button == 0 || button == 1) // LMB - Increment
                    {
                        selector.removeAtIndex(i);
                    }
                }
            }
        }
    }

    @Override
    public boolean canIncrementGlyph()
    {
        return false;
    }
}
