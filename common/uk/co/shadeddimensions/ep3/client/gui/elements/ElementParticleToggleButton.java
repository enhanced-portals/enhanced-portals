package uk.co.shadeddimensions.ep3.client.gui.elements;

import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.ep3.network.ClientProxy.ParticleSet;
import uk.co.shadeddimensions.library.gui.GuiBase;
import uk.co.shadeddimensions.library.gui.element.ElementButton;

public class ElementParticleToggleButton extends ElementButton
{
    ParticleSet set;
    boolean selected;
    int num, drawCounter;

    public ElementParticleToggleButton(GuiBase parent, int x, int y, String id, ParticleSet p)
    {
        super(parent, x, y, 0, id, null, null);
        this.set = p;
        this.sizeY = 18;
        this.sizeX = 18;
    }
    
    public void setSelected(boolean b)
    {
        selected = b;
    }
    
    @Override
    public void draw()
    {
        if (selected)
        {
            Gui.drawRect(posX, posY, posX + sizeX, posY + sizeY, 0xFFACACAC);
        }
        
        drawCounter++;
        
        if (drawCounter > 100)
        {
            num++;
            drawCounter = 0;
        }
        
        int textureIndex = set.frames[num % set.frames.length];
        GL11.glColor3f(1f, 1f, 1f);
        gui.getTextureManager().bindTexture(new ResourceLocation("textures/particle/particles.png"));
        drawTexturedModalRect(posX + 1, posY + 1, textureIndex % 16 * 16, textureIndex / 16 * 16, 16, 16);
    }
}
