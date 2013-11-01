package uk.co.shadeddimensions.ep3.client.gui.elements;

import net.minecraft.client.gui.Gui;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.ep3.client.gui.GuiEnhancedPortals;

public class GuiFakeItemSlot extends Gui
{
    int x, y;
    public ItemStack stack;
    GuiEnhancedPortals parent;
    
    public GuiFakeItemSlot(int X, int Y, ItemStack S, GuiEnhancedPortals gui)
    {
        x = X;
        y = Y;
        parent = gui;
        stack = S;
    }
    
    public void drawBackground(int mX, int mY)
    {
        int X = x + parent.getGuiLeft(), Y = y + parent.getGuiTop();
        
        GL11.glColor4f(1f, 1f, 1f, 1f);
        parent.getMinecraft().renderEngine.bindTexture(new ResourceLocation("enhancedportals", "textures/gui/inventorySlots.png"));
        drawTexturedModalRect(X - 1, Y - 1, 0, 0, 18, 18);
        
        if (mX >= X && mX <= X + 16 && mY >= Y && mY <= Y + 16)
        {
            parent.drawRectangle(X, Y, 16, 16, 0x88FFFFFF, false);
        }
        
        if (stack != null)
        {
            parent.getItemRenderer().renderItemAndEffectIntoGUI(parent.getFontRenderer(), parent.getMinecraft().renderEngine, stack, X, Y);
        }
    }
    
    public boolean mouseClicked(int mX, int mY, int mouseButton, ItemStack s)
    {
        int X = x + parent.getGuiLeft(), Y = y + parent.getGuiTop();
        
        if (mX >= X && mX <= X + 16 && mY >= Y && mY <= Y + 16)
        {
            if (!isValidItem(s))
            {
                return false;
            }
            
            stack = s;            
            return true;
        }
        
        return false;
    }
    
    public boolean isValidItem(ItemStack s)
    {
        return s == null || s.getItem() instanceof ItemBlock;
    }
}
