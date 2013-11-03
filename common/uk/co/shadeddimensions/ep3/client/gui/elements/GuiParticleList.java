package uk.co.shadeddimensions.ep3.client.gui.elements;

import net.minecraft.client.renderer.texture.TextureMap;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.ep3.client.gui.base.GuiEnhancedPortals;
import uk.co.shadeddimensions.ep3.network.ClientProxy;

public class GuiParticleList
{
    int posX, posY, width, height;
    GuiEnhancedPortals parent;
    public int selected, page;
    int spriteCounter, spriteCounterFine;
    
    public GuiParticleList(int x, int y, int w, int h, GuiEnhancedPortals gui)
    {
        posX = x;
        posY = y;
        width = w;
        height = h;
        parent = gui;
        selected = 0;
        page = spriteCounter = spriteCounterFine = 0;
    }
    
    public void drawBackground()
    {
        parent.drawRectangle(posX, posY, width, height, 0xFF444444, true);
        parent.drawRectangle(posX + 1, posY + 1, width - 2, height - 2, 0xFF777777, true);
        
        GL11.glColor3f(1f, 1f, 1f);
        parent.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        
        for (int i = 0; i < 27; i++)
        {
            int ic = (page * 27) + i;
            int x = parent.getGuiLeft() + posX + 2 + (i % 9) * 18;
            int y = parent.getGuiTop() + posY + 2 + (i / 9) * 18;
            
            if (selected == ic)
            {
                parent.drawRectangle(x - 1, y - 1, 18, 18, 0x88FFFFFF, false);
            }

            if (ClientProxy.particleSets.size() <= ic)
            {
                break;
            }
            
            if (ic < 0)
            {
                continue;
            }
            
            parent.drawParticle(x, y, 0xFFFFFFFF, getParticleSprite(ic), false);
        }
        
        if (spriteCounterFine >= 200)
        {
            spriteCounter++;
            spriteCounterFine = 0;
            
            if (spriteCounter >= 1000)
            {
                spriteCounter = 0;
            }
        }
        
        spriteCounterFine++;
    }
    
    protected int getParticleSprite(int id)
    {
        if (ClientProxy.particleSets.size() > id)
        {
            if (ClientProxy.particleSets.get(id).length <= spriteCounter)
            {
                return ClientProxy.particleSets.get(id)[spriteCounter % ClientProxy.particleSets.get(id).length];
            }
            else
            {
                return ClientProxy.particleSets.get(id)[spriteCounter];
            }
        }
        
        return 0;
    }
    
    public void mouseClicked(int x, int y, int button)
    {
        if (x >= parent.getGuiLeft() + posX && x <= parent.getGuiLeft() + posX + width && y >= parent.getGuiTop() + posY && y <= parent.getGuiTop() + posY + height)
        {
            if (button == 1)
            {
                selected = 0;
            }
            else if (button == 0)
            {
                for (int i = 0; i < 27; i++)
                {
                    int x2 = parent.getGuiLeft() + posX + 2 + (i % 9) * 18;
                    int y2 = parent.getGuiTop() + posY + 2 + (i / 9) * 18;
                    
                    if (x >= x2 && x <= x2 + 18 && y >= y2 && y <= y2 + 18)
                    {
                        if (ClientProxy.particleSets.size() <= i)
                        {
                            return;
                        }
                        
                        if (selected == (page * 27) + i)
                        {
                            selected = 0;
                        }
                        else
                        {
                            selected = (page * 27) + i;
                        }
                        
                        return;
                    }
                }
            }
        }
    }
}
