package uk.co.shadeddimensions.ep3.client.gui.elements;

import java.util.ArrayList;

import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.ep3.client.gui.GuiDiallingDevice;
import uk.co.shadeddimensions.ep3.client.gui.GuiEnhancedPortals;
import uk.co.shadeddimensions.ep3.network.ClientProxy;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileDiallingDevice;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileDiallingDevice.GlyphElement;
import uk.co.shadeddimensions.ep3.util.GuiPayload;

public class GuiGlyphPage extends Gui
{
    public int selectedGlyph;
    int x, y, w, h, nudge;
    GuiEnhancedPortals parent;
    TileDiallingDevice dialler;
    
    public GuiGlyphPage(int posX, int posY, int width, int height, GuiEnhancedPortals gui, TileDiallingDevice dial)
    {
        x = posX;
        y = posY;
        w = width;
        h = height;
        parent = gui;
        nudge = 0;
        selectedGlyph = -1;
        dialler = dial;
    }
    
    public void drawBackground(int mX, int mY)
    {
        if (dialler.glyphList.isEmpty())
        {
            parent.getFontRenderer().drawString("You have no stored identifiers.", x + parent.getGuiLeft() + 5, y + parent.getGuiTop() + 5, 0xDD0000);
            return;
        }
        
        parent.getMinecraft().renderEngine.bindTexture(new ResourceLocation("enhancedportals", "textures/gui/buttons.png"));
        for (int i = 0; i < 5; i++)
        {
            int glyph = nudge + i;
            
            if (glyph >= dialler.glyphList.size())
            {
                break;
            }
            
            boolean isSelected = selectedGlyph == glyph;
            
            if (isSelected)
            {
                GL11.glColor3f(0.5f, 0.75f, 1f);
            }
            
            drawTexturedModalRect(parent.getGuiLeft() + x, parent.getGuiTop() + y + (i * 16), 0, isMouseOver(i, mX, mY) || isSelected ? 110 : 95, 200, 15);
            
            GL11.glColor3f(1f, 1f, 1f);
            drawTexturedModalRect(parent.getGuiLeft() + x + 201, parent.getGuiTop() + y + (i * 16), 200, isMouseOverSmall(i, mX, mY) ? 140 : 125, 15, 15);
        }
        
        drawTexturedModalRect(parent.getGuiLeft() + x + 227, parent.getGuiTop() + y, 200, nudge > 0 ? 65 : 50, 15, 15);
        drawTexturedModalRect(parent.getGuiLeft() + x + 227, parent.getGuiTop() + y + (4 * 16), 200, dialler.glyphList.size() > 5 + nudge ? 20 : 5, 15, 15);
        
        for (int i = 0; i < 5; i++)
        {
            int glyph = nudge + i;
            
            if (glyph >= dialler.glyphList.size())
            {
                break;
            }
            
            parent.getFontRenderer().drawStringWithShadow(dialler.glyphList.get(glyph).name, parent.getGuiLeft() + x + 5, parent.getGuiTop() + y + 3 + (i * 16), 0xDDDDDD);
        }
    }
    
    public boolean isMouseOver(int id, int mX, int mY)
    {
        int X = x + parent.getGuiLeft(), Y = y + (id * 16) + parent.getGuiTop();        
        return mX >= X && mX <= X + 200 && mY >= Y && mY <= Y + 15;
    }
    
    public boolean isMouseOverSmall(int id, int mX, int mY)
    {
        int X = x + parent.getGuiLeft() + 201, Y = y + (id * 16) + parent.getGuiTop();        
        return mX >= X && mX <= X + 15 && mY >= Y && mY <= Y + 15;
    }
    
    public boolean isMouseOverSmall(int xPos, int yPos, int mX, int mY)
    {
        int X = xPos + parent.getGuiLeft(), Y = yPos + parent.getGuiTop();        
        return mX >= X && mX <= X + 15 && mY >= Y && mY <= Y + 15;
    }
    
    public void mouseClicked(int mX, int mY, int mouseButton)
    {
        for (int i = 0; i < 5; i++)
        {
            int glyph = nudge + i;
            
            if (isMouseOver(i, mX, mY) && mouseButton == 0)
            {
                if (selectedGlyph != glyph && glyph < dialler.glyphList.size())
                {
                    // TODO: Play sound
                    selectedGlyph = glyph;
                    
                    if (parent instanceof GuiDiallingDevice)
                    {
                        ((GuiDiallingDevice) parent).selectionChanged(glyph);
                    }
                }
                
                return;
            }
            else if (isMouseOverSmall(i, mX, mY) && mouseButton == 0)
            {
                if (i >= dialler.glyphList.size())
                {
                    return;
                }
                
                GuiPayload p = new GuiPayload();
                p.data.setInteger("DeleteGlyph", i);
                ClientProxy.sendGuiPacket(p);
                selectedGlyph = -1;
                
                if (parent instanceof GuiDiallingDevice)
                {
                    ((GuiDiallingDevice) parent).selectionChanged(glyph);
                }
            }
            else if (isMouseOverSmall(x + 227, y, mX, mY) && mouseButton == 0 && nudge > 0)
            {
                nudge--;
            }
            else if (isMouseOverSmall(x + 227, y + (4 * 16), mX, mY) && mouseButton == 0 && dialler.glyphList.size() > 5 + nudge)
            {
                nudge++;
            }
        }
    }

    public ArrayList<GlyphElement> getList()
    {
        return dialler.glyphList;
    }
}
