package uk.co.shadeddimensions.library.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import uk.co.shadeddimensions.library.gui.element.ElementBase;
import uk.co.shadeddimensions.library.gui.element.ElementBaseContainer;
import uk.co.shadeddimensions.library.gui.element.ElementCrafting;
import uk.co.shadeddimensions.library.gui.element.ElementScrollPanel;
import uk.co.shadeddimensions.library.gui.element.ElementText;

/***
 * Turns a string into multiple GUI elements for use within a {@link ElementBaseContainer} or {@link ElementScrollPanel}.
 * 
 * @author Alz454
 */
public class Parser
{
    FontRenderer fontRenderer;
    IGuiBase parentGui;
    ArrayList<ElementBase> parsedElements;
    int maxWidth, currentX, currentY;

    public Parser(IGuiBase parent)
    {
        parsedElements = new ArrayList<ElementBase>();
        parentGui = parent;
        maxWidth = -1;
        currentX = currentY = 0;
        fontRenderer = Minecraft.getMinecraft().fontRenderer;
    }

    public ArrayList<ElementBase> parse(String string)
    {
        // testing, for now
        parsedElements.add(new ElementCrafting(parentGui, maxWidth / 2 - 5, currentY, 0).addAllGridSlots(new ItemStack[] { new ItemStack(Block.stone), new ItemStack(Block.stone), null, new ItemStack(Block.stone), new ItemStack(Block.stone), null, null, null, null }).addOutputSlot(new ItemStack(Block.stoneBrick)));
        parsedElements.add(new ElementCrafting(parentGui, 0, currentY, 1).addBothFurnaceSlots(new ItemStack[] { new ItemStack(Block.cobblestone), new ItemStack(Item.coal) }).addOutputSlot(new ItemStack(Block.stone)));
        currentY += parsedElements.get(parsedElements.size() - 1).getHeight() + 5;
        parseText(string);
        currentY += 5;
        parsedElements.add(new ElementCrafting(parentGui, maxWidth / 2 - 5, currentY, 0).addAllGridSlots(new ItemStack[] { new ItemStack(Block.stone), new ItemStack(Block.stone), null, new ItemStack(Block.stone), new ItemStack(Block.stone), null, null, null, null }).addOutputSlot(new ItemStack(Block.stoneBrick)));
        parsedElements.add(new ElementCrafting(parentGui, 0, currentY, 1).addBothFurnaceSlots(new ItemStack[] { new ItemStack(Block.cobblestone), new ItemStack(Item.coal) }).addOutputSlot(new ItemStack(Block.stone)));
        currentY += parsedElements.get(parsedElements.size() - 1).getHeight() + 5;
        parseText("And that's how you make Stone Bricks!");

        return parsedElements;
    }

    @SuppressWarnings("unchecked")
    private void parseText(String string)
    {
        if (maxWidth != -1 && fontRenderer.getStringWidth(string) > maxWidth)
        {
            List<String> list = fontRenderer.listFormattedStringToWidth(string, maxWidth);

            for (Iterator<String> iterator = list.iterator(); iterator.hasNext(); currentY += fontRenderer.FONT_HEIGHT)
            {
                parsedElements.add(new ElementText(parentGui, currentX, currentY, iterator.next(), null, 0xFFFFFF, false));
            }
        }
        else
        {
            parsedElements.add(new ElementText(parentGui, currentX, currentY, string, null, 0xFFFFFF, false));
            currentX += fontRenderer.getStringWidth(string);
        }
    }

    public Parser setMaxWidth(int width)
    {
        maxWidth = width;
        return this;
    }
}
