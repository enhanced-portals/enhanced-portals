package uk.co.shadeddimensions.ep3.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiGlyphSelectorNetwork extends GuiGlyphSelector
{
    static ArrayList<ItemStack> networkGlyphs = new ArrayList<ItemStack>();

    static
    {
        networkGlyphs.add(new ItemStack(Block.jukebox, 0));
        networkGlyphs.add(new ItemStack(Block.furnaceIdle, 0));
        networkGlyphs.add(new ItemStack(Block.enchantmentTable, 0));
        networkGlyphs.add(new ItemStack(Block.dropper, 0));
        networkGlyphs.add(new ItemStack(Block.pistonBase, 0));
        networkGlyphs.add(new ItemStack(Block.mobSpawner, 0));
        networkGlyphs.add(new ItemStack(Block.anvil, 0));
        networkGlyphs.add(new ItemStack(Block.enderChest, 0));
        networkGlyphs.add(new ItemStack(Block.beacon, 0, 0));

        networkGlyphs.add(new ItemStack(Block.stoneBrick, 0));
        networkGlyphs.add(new ItemStack(Block.brick, 0));
        networkGlyphs.add(new ItemStack(Block.blockNetherQuartz, 0));
        networkGlyphs.add(new ItemStack(Block.netherBrick, 0));
        networkGlyphs.add(new ItemStack(Block.obsidian, 0));
        networkGlyphs.add(new ItemStack(Block.hay, 0));
        networkGlyphs.add(new ItemStack(Block.carpet, 0));
        networkGlyphs.add(new ItemStack(Block.mycelium, 0));
        networkGlyphs.add(new ItemStack(Block.glass, 0));

        networkGlyphs.add(new ItemStack(Block.blockDiamond, 0));
        networkGlyphs.add(new ItemStack(Block.blockGold, 0));
        networkGlyphs.add(new ItemStack(Block.blockIron, 0));
        networkGlyphs.add(new ItemStack(Block.blockRedstone, 0));
        networkGlyphs.add(new ItemStack(Block.coalBlock, 0));
        networkGlyphs.add(new ItemStack(Block.blockEmerald, 0));
        networkGlyphs.add(new ItemStack(Block.blockLapis, 0));
        networkGlyphs.add(new ItemStack(Block.dragonEgg, 0, 0));
        networkGlyphs.add(new ItemStack(Block.hardenedClay, 0));
    }

    public static ItemStack getGlyph(int id)
    {
        return id >= 0 && id < networkGlyphs.size() ? networkGlyphs.get(id) : null;
    }

    private static int getGlyphID(String s)
    {
        for (int i = 0; i < networkGlyphs.size(); i++)
        {
            if (networkGlyphs.get(i).getUnlocalizedName().replace("item.", "").equals(s))
            {
                return i;
            }
        }

        return -1;
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
                    list.add(networkGlyphs.get(id));
                }
            }
        }
        else
        {
            int id = getGlyphID(identifier);

            if (id >= 0)
            {
                list.add(networkGlyphs.get(id));
            }
        }

        return list;
    }

    public GuiGlyphSelectorNetwork(int x, int y, GuiEnhancedPortals parent)
    {
        super(x, y, parent);
    }

    public GuiGlyphSelectorNetwork(int x, int y, int color, GuiEnhancedPortals parent)
    {
        super(x, y, color, parent);
    }

    @Override
    public void drawBackground(int mouseX, int mouseY)
    {
        int X = x + gui.getGuiLeft(), Y = y + gui.getGuiTop();

        Color c = new Color(colour);
        GL11.glColor4f(c.getRed() / 255, c.getGreen() / 255, c.getBlue() / 255, 1f);

        gui.getTextureManager().bindTexture(new ResourceLocation("enhancedportals", "textures/gui/inventorySlots.png"));
        drawTexturedModalRect(X, Y, 0, 0, 162, 54);

        for (int i = 0; i < networkGlyphs.size(); i++)
        {
            int tX = X + i % ITEMS_PER_LINE * 18 + 1, tY = Y + i / ITEMS_PER_LINE * 18 + 1;

            gui.getItemRenderer().renderItemAndEffectIntoGUI(gui.getFontRenderer(), gui.getTextureManager(), networkGlyphs.get(i), tX, tY);
            gui.getItemRenderer().renderItemOverlayIntoGUI(gui.getFontRenderer(), gui.getTextureManager(), networkGlyphs.get(i), tX, tY, counter[i] > 0 ? counter[i] + "" : null);
            GL11.glDisable(GL11.GL_LIGHTING);
        }

        GL11.glColor4f(1f, 1f, 1f, 1f);
    }

    @Override
    public void drawForeground(int mouseX, int mouseY)
    {
        if (isOnSelf(mouseX, mouseY))
        {
            for (int i = 0; i < networkGlyphs.size(); i++)
            {
                if (isOnElement(mouseX, mouseY, i))
                {
                    List<String> strList = new ArrayList<String>();
                    strList.add(networkGlyphs.get(i).getDisplayName());
                    drawHoveringText(strList, mouseX - gui.getGuiLeft(), mouseY - gui.getGuiTop(), gui.getFontRenderer(), gui.getItemRenderer());
                    GL11.glDisable(GL11.GL_LIGHTING);
                }
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button)
    {
        if (canEdit && isOnSelf(mouseX, mouseY))
        {
            for (int i = 0; i < networkGlyphs.size(); i++)
            {
                if (isOnElement(mouseX, mouseY, i))
                {
                    if (button == 0) // LMB - Increment
                    {
                        if (canIncrementGlyph())
                        {
                            counter[i]++;
                            selectedGlyphs.add(networkGlyphs.get(i)); // Add Glyph to the end
                            gui.getMinecraft().sndManager.playSoundFX("random.click", 1.0F, 1.0F);
                        }
                    }
                    else if (button == 1) // RMB - Decrement
                    {
                        if (counter[i] > 0)
                        {
                            counter[i]--;
                            selectedGlyphs.remove(selectedGlyphs.lastIndexOf(networkGlyphs.get(i))); // Remove the last Glyph
                            gui.getMinecraft().sndManager.playSoundFX("random.click", 1.0F, 1.0F);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void randomize(boolean forceMaximum)
    {
        clearSelection();
        Random rand = new Random();

        for (int i = 0; i < (forceMaximum ? MAX_COUNT : rand.nextInt(MAX_COUNT)); i++)
        {
            int glyphID = rand.nextInt(networkGlyphs.size());

            selectedGlyphs.add(networkGlyphs.get(glyphID));
            counter[glyphID]++;
        }
    }

    @Override
    protected void removeAtIndex(int i)
    {
        if (i >= 0 && selectedGlyphs.size() > i && selectedGlyphs.get(i) != null)
        {
            counter[networkGlyphs.indexOf(selectedGlyphs.get(i))]--;
            selectedGlyphs.remove(i);
            gui.getMinecraft().sndManager.playSoundFX("random.click", 1.0F, 1.0F);
        }
    }

    @Override
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
                    selectedGlyphs.add(networkGlyphs.get(id));
                    counter[id]++;
                }
            }
        }
        else
        {
            int id = getGlyphID(identifier);

            if (id >= 0)
            {
                selectedGlyphs.add(networkGlyphs.get(id));
                counter[id]++;
            }
        }
    }
}
