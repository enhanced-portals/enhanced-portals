package uk.co.shadeddimensions.ep3.gui.elements;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import uk.co.shadeddimensions.ep3.gui.GuiEnhancedPortals;
import uk.co.shadeddimensions.ep3.portal.NetworkManager;

public class GuiGlyphSelector extends Gui
{
    static final int MAX_COUNT = 9, ITEMS_PER_LINE = 9;
    static ArrayList<ItemStack> Glyphs = new ArrayList<ItemStack>();

    public static ItemStack getGlyph(int id)
    {
        return id >= 0 && id < Glyphs.size() ? Glyphs.get(id) : null;
    }

    private static int getGlyphID(String s)
    {
        for (int i = 0; i < Glyphs.size(); i++)
        {
            if (Glyphs.get(i).getUnlocalizedName().replace("item.", "").equals(s))
            {
                return i;
            }
        }

        return -1;
    }

    ArrayList<ItemStack> selectedGlyphs;

    static
    {
        Glyphs.add(new ItemStack(Item.diamond, 0));
        Glyphs.add(new ItemStack(Item.emerald, 0));
        Glyphs.add(new ItemStack(Item.goldNugget, 0));
        Glyphs.add(new ItemStack(Item.redstone, 0));
        Glyphs.add(new ItemStack(Item.ingotIron, 0));
        Glyphs.add(new ItemStack(Item.glowstone, 0));
        Glyphs.add(new ItemStack(Item.netherQuartz, 0));
        Glyphs.add(new ItemStack(Item.bucketLava, 0));
        Glyphs.add(new ItemStack(Item.dyePowder, 0, 4));

        Glyphs.add(new ItemStack(Item.appleGold, 0));
        Glyphs.add(new ItemStack(Item.blazeRod, 0));
        Glyphs.add(new ItemStack(Item.slimeBall, 0));
        Glyphs.add(new ItemStack(Item.goldenCarrot, 0));
        Glyphs.add(new ItemStack(Item.enderPearl, 0));
        Glyphs.add(new ItemStack(Item.fireballCharge, 0));
        Glyphs.add(new ItemStack(Item.netherStar, 0));
        Glyphs.add(new ItemStack(Item.ghastTear, 0));
        Glyphs.add(new ItemStack(Item.magmaCream, 0));

        Glyphs.add(new ItemStack(Item.eyeOfEnder, 0));
        Glyphs.add(new ItemStack(Item.firework, 0));
        Glyphs.add(new ItemStack(Item.ingotGold, 0));
        Glyphs.add(new ItemStack(Item.pickaxeDiamond, 0));
        Glyphs.add(new ItemStack(Item.gunpowder, 0));
        Glyphs.add(new ItemStack(Item.pocketSundial, 0));
        Glyphs.add(new ItemStack(Item.writableBook, 0));
        Glyphs.add(new ItemStack(Item.potion, 0, 5));
        Glyphs.add(new ItemStack(Item.cake, 0));
    }

    public static ArrayList<ItemStack> getGlyphsFromIdentifier(String identifier)
    {
        ArrayList<ItemStack> list = new ArrayList<ItemStack>();

        if (identifier.contains(" "))
        {
            for (String s : identifier.split(" "))
            {
                int id = getGlyphID(s);

                if (id >= 0)
                {
                    list.add(Glyphs.get(id));
                }
            }
        }
        else
        {
            int id = getGlyphID(identifier);

            if (id >= 0)
            {
                list.add(Glyphs.get(id));
            }
        }

        return list;
    }

    protected int x, y, colour;
    int[] counter;

    protected GuiEnhancedPortals gui;

    public boolean canEdit;

    public GuiGlyphSelector(int x, int y, GuiEnhancedPortals parent)
    {
        this.x = x;
        this.y = y;
        counter = new int[Glyphs.size()];
        colour = 0xFFFFFF;
        selectedGlyphs = new ArrayList<ItemStack>();
        gui = parent;
        canEdit = false;
    }

    public GuiGlyphSelector(int x, int y, int color, GuiEnhancedPortals parent)
    {
        this.x = x;
        this.y = y;
        counter = new int[Glyphs.size()];
        colour = color;
        selectedGlyphs = new ArrayList<ItemStack>();
        gui = parent;
        canEdit = false;
    }

    public boolean canIncrementGlyph()
    {
        return getCurrentSelectedCount() < MAX_COUNT;
    }

    public void clearSelection()
    {
        selectedGlyphs.clear();

        for (int i = 0; i < counter.length; i++)
        {
            counter[i] = 0;
        }
    }

    public void drawBackground(int mouseX, int mouseY)
    {
        int X = x + gui.getGuiLeft(), Y = y + gui.getGuiTop();

        Color c = new Color(colour);
        GL11.glColor4f(c.getRed() / 255, c.getGreen() / 255, c.getBlue() / 255, 1f);

        gui.getTextureManager().bindTexture(new ResourceLocation("enhancedportals", "textures/gui/inventorySlots.png"));
        drawTexturedModalRect(X, Y, 0, 0, 162, 54);

        for (int i = 0; i < Glyphs.size(); i++)
        {
            int tX = X + i % ITEMS_PER_LINE * 18 + 1, tY = Y + i / ITEMS_PER_LINE * 18 + 1;

            gui.getItemRenderer().renderItemAndEffectIntoGUI(gui.getFontRenderer(), gui.getTextureManager(), Glyphs.get(i), tX, tY);
            gui.getItemRenderer().renderItemOverlayIntoGUI(gui.getFontRenderer(), gui.getTextureManager(), Glyphs.get(i), tX, tY, counter[i] > 0 ? counter[i] + "" : null);
        }

        GL11.glColor4f(1f, 1f, 1f, 1f);
        GL11.glDisable(GL11.GL_LIGHTING);
    }

    public void drawForeground(int mouseX, int mouseY)
    {
        if (isOnSelf(mouseX, mouseY))
        {
            for (int i = 0; i < Glyphs.size(); i++)
            {
                if (isOnElement(mouseX, mouseY, i))
                {
                    List<String> strList = new ArrayList<String>();
                    strList.add(Glyphs.get(i).getDisplayName());
                    drawHoveringText(strList, mouseX - gui.getGuiLeft(), mouseY - gui.getGuiTop(), gui.getFontRenderer(), gui.getItemRenderer());
                    GL11.glDisable(GL11.GL_LIGHTING);
                }
            }
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

    public int getCurrentSelectedCount()
    {
        return selectedGlyphs.size();
    }

    public ArrayList<ItemStack> getSelectedGlyphs()
    {
        return selectedGlyphs;
    }

    public String getSelectedIdentifier()
    {
        String s = "";

        for (ItemStack stack : selectedGlyphs)
        {
            if (s.length() == 0)
            {
                s = stack.getUnlocalizedName().replace("item.", "");
            }
            else
            {
                s += " " + stack.getUnlocalizedName().replace("item.", "");
            }
        }

        return s.length() == 0 ? NetworkManager.BLANK_IDENTIFIER : s;
    }

    protected boolean isOnElement(int mouseX, int mouseY, int id)
    {
        int X = x + id % ITEMS_PER_LINE * 18 + gui.getGuiLeft();
        int Y = y + id / ITEMS_PER_LINE * 18 + gui.getGuiTop();

        return mouseX > X && mouseY > Y && mouseX < X + 17 && mouseY < Y + 17;
    }

    protected boolean isOnSelf(int mouseX, int mouseY)
    {
        return mouseX > x + gui.getGuiLeft() && mouseY > y + gui.getGuiTop() && mouseX < x + 162 + gui.getGuiLeft() && mouseY < y + 54 + gui.getGuiTop();
    }

    public void mouseClicked(int mouseX, int mouseY, int button)
    {
        if (canEdit && isOnSelf(mouseX, mouseY))
        {
            for (int i = 0; i < Glyphs.size(); i++)
            {
                if (isOnElement(mouseX, mouseY, i))
                {
                    if (button == 0) // LMB - Increment
                    {
                        if (canIncrementGlyph())
                        {
                            counter[i]++;
                            selectedGlyphs.add(Glyphs.get(i)); // Add Glyph to the end
                            gui.getMinecraft().sndManager.playSoundFX("random.click", 1.0F, 1.0F);
                        }
                    }
                    else if (button == 1) // RMB - Decrement
                    {
                        if (counter[i] > 0)
                        {
                            counter[i]--;
                            selectedGlyphs.remove(selectedGlyphs.lastIndexOf(Glyphs.get(i))); // Remove the last Glyph
                            gui.getMinecraft().sndManager.playSoundFX("random.click", 1.0F, 1.0F);
                        }
                    }
                }
            }
        }
    }

    public void randomize(boolean forceMaximum)
    {
        clearSelection();
        Random rand = new Random();

        for (int i = 0; i < (forceMaximum ? MAX_COUNT : rand.nextInt(MAX_COUNT)); i++)
        {
            int glyphID = rand.nextInt(Glyphs.size());

            selectedGlyphs.add(Glyphs.get(glyphID));
            counter[glyphID]++;
        }
    }

    protected void removeAtIndex(int i)
    {
        if (i >= 0 && selectedGlyphs.size() > i && selectedGlyphs.get(i) != null)
        {
            counter[Glyphs.indexOf(selectedGlyphs.get(i))]--;
            selectedGlyphs.remove(i);
            gui.getMinecraft().sndManager.playSoundFX("random.click", 1.0F, 1.0F);
        }
    }

    public void setEditable(boolean edit)
    {
        canEdit = edit;
    }

    public void setSelectedToIdentifier(String identifier)
    {
        clearSelection();

        if (identifier.contains(" "))
        {
            for (String s : identifier.split(" "))
            {
                int id = getGlyphID(s);

                if (id >= 0)
                {
                    selectedGlyphs.add(Glyphs.get(id));
                    counter[id]++;
                }
            }
        }
        else
        {
            int id = getGlyphID(identifier);

            if (id >= 0)
            {
                selectedGlyphs.add(Glyphs.get(id));
                counter[id]++;
            }
        }
    }
}
