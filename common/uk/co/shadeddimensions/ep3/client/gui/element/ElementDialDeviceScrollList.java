package uk.co.shadeddimensions.ep3.client.gui.element;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.ep3.client.gui.GuiDiallingDevice;
import uk.co.shadeddimensions.ep3.client.gui.IElementHandler;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileDiallingDevice;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileDiallingDevice.GlyphElement;
import cofh.gui.GuiBase;

public class ElementDialDeviceScrollList extends ElementScrollListBase
{
    TileDiallingDevice dial;
    
    public ElementDialDeviceScrollList(GuiBase gui, ResourceLocation texture, TileDiallingDevice dialler, int posX, int posY, int width, int height)
    {
        super(gui, texture, posX, posY, width, height);
        dial = dialler;
    }

    @Override
    protected void drawElement(int i, int x, int y, boolean isSelected, int mouseX, int mouseY)
    {
        boolean yMouse = mouseY >= y && mouseY <= y + getEntrySize();
        boolean mouseOverMain = yMouse && mouseX >= x + 5 && mouseX <= x + sizeX - 38, mouseOverTexture = yMouse && mouseX >= x + sizeX - 36 && mouseX <= x + sizeX - 22, mouseOverRemove = yMouse && mouseX >= x + sizeX - 20 && mouseX <= x + sizeX - 6, additionalButtonsDisabled = gui instanceof GuiDiallingDevice ? ((GuiDiallingDevice) gui).showOverlay : false;
        float colour = isSelected ? 0.7f : 1f;
        int mainButtonWidth = sizeX - 37, halfButtonWidth = mainButtonWidth / 2;
        GlyphElement element = dial.glyphList.get(i);
        
        GL11.glColor3f(colour, colour, colour);
        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("enhancedportals", "textures/gui/buttons.png"));
        gui.drawTexturedModalRect(x + 5, y, 0, isSelected || mouseOverMain ? 110 : 95, halfButtonWidth, 15);
        gui.drawTexturedModalRect(x + halfButtonWidth, y, 200 - halfButtonWidth - 1, isSelected || mouseOverMain ? 110 : 95, halfButtonWidth + 1, 15);
        
        GL11.glColor3f(1f, 1f, 1f);
        gui.drawTexturedModalRect(x + sizeX - 20, y, 45, additionalButtonsDisabled ? 140 : mouseOverRemove ? 170 : 155, 15, 15);
        gui.drawTexturedModalRect(x + sizeX - 36, y, 60, additionalButtonsDisabled ? 140 : mouseOverTexture ? 170 : 155, 15, 15);
        
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(element.name, x + 10, y + 3, 0xFFFFFF);
    }
    
    @Override
    protected void entrySelected(int entry, int mouseX, int mouseY)
    {
        if (mouseX >= posX + 5 && mouseX <= posX + sizeX - 38) // Selected glyph entry
        {
            actualSelected = entry;
            
            if (gui instanceof IElementHandler)
            {
                ((IElementHandler) gui).onElementChanged(this, new int[] { 0, entry });
            }
        }
        else if (mouseX >= posX + sizeX - 36 && mouseX <= posX + sizeX - 22) // Selected texture button
        {
            if (gui instanceof IElementHandler)
            {
                ((IElementHandler) gui).onElementChanged(this, new int[] { 1, entry });
            }
        }
        else if (mouseX >= posX + sizeX - 20 && mouseX <= posX + sizeX - 6) // Selected remove button
        {
            if (gui instanceof IElementHandler)
            {
                ((IElementHandler) gui).onElementChanged(this, new int[] { 2, entry });
            }
        }
    }

    @Override
    protected int getEntrySize()
    {
        return 15;
    }

    @Override
    protected int getEntrySpacing()
    {
        return 1;
    }
    
    @Override
    protected int getElementCount()
    {
        return dial.glyphList.size();
    }
}
