package uk.co.shadeddimensions.ep3.client.gui.New.element;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.ep3.network.ClientProxy;
import uk.co.shadeddimensions.ep3.network.ClientProxy.ParticleSet;
import cofh.gui.GuiBase;

public class ElementParticleScrollList extends ElementIconScrollList
{
    final int TICK_REFRESH = 100;
    int tickCounter, spriteIndex = 0;

    public ElementParticleScrollList(GuiBase gui, ResourceLocation t, int posX, int posY, int width, int height, ArrayList<ParticleSet> theList)
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
        int type = ClientProxy.particleSets.get(i).type;
        Gui.drawRect(x - 1, y - 1, x + getEntrySize() + 1, y + getEntrySize() + 1, type == 0 ? 0x22FF0000 : type == 1 ? 0x2200FF00 : 0x220000FF);

        if (isSelected)
        {
            Gui.drawRect(x - 1, y - 1, x + getEntrySize() + 1, y + getEntrySize() + 1, 0x33FFFFFF);
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
            ParticleSet element = (ParticleSet) list.get(id);

            if (element.frames.length <= spriteIndex)
            {
                return element.frames[spriteIndex % element.frames.length];
            }
            else
            {
                return element.frames[spriteIndex];
            }
        }

        return 0;
    }
}
