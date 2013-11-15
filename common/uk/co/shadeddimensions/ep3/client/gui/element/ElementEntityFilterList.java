package uk.co.shadeddimensions.ep3.client.gui.element;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.ep3.client.gui.IElementHandler;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileBiometricIdentifier;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileBiometricIdentifier.EntityPair;
import cofh.gui.GuiBase;

public class ElementEntityFilterList extends ElementDialDeviceScrollList
{
    TileBiometricIdentifier biometric;
    boolean sendList;

    public ElementEntityFilterList(GuiBase gui, ResourceLocation texture, TileBiometricIdentifier bio, int posX, int posY, int width, int height, boolean isSendList)
    {
        super(gui, texture, null, posX, posY, width, height);
        biometric = bio;
        sendList = isSendList;
    }

    @Override
    protected void drawElement(int i, int x, int y, boolean isSelected, int mouseX, int mouseY)
    {
        boolean yMouse = mouseY >= y && mouseY <= y + getEntrySize();
        boolean mouseOverMain = yMouse && mouseX >= x + 5 && mouseX <= x + sizeX - 38, mouseOverTexture = yMouse && mouseX >= x + sizeX - 36 && mouseX <= x + sizeX - 22, mouseOverRemove = yMouse && mouseX >= x + sizeX - 20 && mouseX <= x + sizeX - 6;
        float colour = isSelected ? 0.7f : 1f;
        int mainButtonWidth = sizeX - 37, halfButtonWidth = mainButtonWidth / 2;
        EntityPair pair = (sendList ? biometric.sendingEntityTypes : biometric.recievingEntityTypes).get(i);
        
        GL11.glColor3f(colour, colour, colour);
        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("enhancedportals", "textures/gui/buttons.png"));
        gui.drawTexturedModalRect(x + 5, y, 0, isSelected || mouseOverMain ? 110 : 95, halfButtonWidth, 15);
        gui.drawTexturedModalRect(x + halfButtonWidth, y, 200 - halfButtonWidth - 1, isSelected || mouseOverMain ? 110 : 95, halfButtonWidth + 1, 15);
        
        GL11.glColor3f(1f, 1f, 1f);
        gui.drawTexturedModalRect(x + sizeX - 20, y, 45, mouseOverRemove ? 170 : 155, 15, 15);
        gui.drawTexturedModalRect(x + sizeX - 36, y, 30, mouseOverTexture ? 170 : 155, 15, 15);
        
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(pair.name, x + 10, y + 3, pair.inverted ? 0xFF0000 : 0x00FF00);
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("" + pair.fuzzy, x + 89, y + 3, 0xFFFFFF);
        GL11.glColor3f(1f, 1f, 1f);
    }
    
    protected void drawForeground()
    {
        if (!sendList && !biometric.hasSeperateLists)
        {
            Minecraft.getMinecraft().fontRenderer.drawSplitString("To set up seperate lists for sending and recieving, activate this list below", posX + 5, posY + 3, sizeX, 0x404040);
        }
        else if (getElementCount() == 0)
        {
            Minecraft.getMinecraft().fontRenderer.drawString("No entries found", posX + 5, posY + 3, 0x404040);
        }
        
        GL11.glColor3f(1f, 1f, 1f);
    }
    
    @Override
    protected void entrySelected(int entry, int mouseX, int mouseY)
    {
        if (mouseX >= posX + 5 && mouseX <= posX + sizeX - 38) // Selected glyph entry
        {            
            if (gui instanceof IElementHandler)
            {
                ((IElementHandler) gui).onElementChanged(this, new int[] { (sendList ? 0 : 1), 0, entry });
            }
        }
        else if (mouseX >= posX + sizeX - 36 && mouseX <= posX + sizeX - 22) // Selected mode button
        {
            if (gui instanceof IElementHandler)
            {
                ((IElementHandler) gui).onElementChanged(this, new int[] { (sendList ? 0 : 1), 1, entry });
            }
        }
        else if (mouseX >= posX + sizeX - 20 && mouseX <= posX + sizeX - 6) // Selected remove button
        {
            if (gui instanceof IElementHandler)
            {
                ((IElementHandler) gui).onElementChanged(this, new int[] { (sendList ? 0 : 1), 2, entry });
            }
        }
    }
    
    @Override
    protected int getElementCount()
    {
        return (sendList ? biometric.sendingEntityTypes : biometric.recievingEntityTypes).size();
    }
}
