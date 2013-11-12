package uk.co.shadeddimensions.ep3.client.gui.New.element;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.ep3.network.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import cofh.gui.GuiBase;

public class ElementParticleScrollList extends ElementIconScrollList
{
    final int TICK_REFRESH = 100;
    int tickCounter, spriteIndex = 0;
    
    public ElementParticleScrollList(GuiBase gui, ResourceLocation t, int posX, int posY, int width, int height, ArrayList<int[]> theList)
    {
        super(gui, t, posX, posY, width, height, null);
        list = theList;
        DEFAULT_SELECTION = 0;
    }
    
    @Override
    public void draw()
    {
        super.draw();
        
        tickCounter++;
        
        if (tickCounter > 100)
        {
            spriteIndex++;
            tickCounter = 0;
        }
    }
    
    @Override
    protected void drawElement(int i, int x, int y, boolean isSelected, int mouseX, int mouseY)
    {
        if (isSelected)
        {
            Gui.drawRect(x - 1, y - 1, x + getEntrySize() + 1, y + getEntrySize() + 1, 0x88FFFFFF);
        }
        
        int textureIndex = getParticleSprite(i);
        
        GL11.glColor3f(1f, 1f, 1f);
        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("textures/particle/particles.png"));
        drawTexturedModalRect(x, y, textureIndex % 16 * 16, textureIndex / 16 * 16, 16, 16);
    }
    
    protected int getParticleSprite(int id)
    {
        if (list.size() > id)
        {
            int[] element = (int[]) list.get(id);
            
            if (element.length <= spriteIndex)
            {
                return element[spriteIndex % ClientProxy.particleSets.get(id).length];
            }
            else
            {
                return element[spriteIndex];
            }
        }
        
        return 0;
    }
}
