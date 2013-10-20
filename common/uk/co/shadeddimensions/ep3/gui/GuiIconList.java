package uk.co.shadeddimensions.ep3.gui;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.ep3.network.ClientProxy;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.Icon;

public class GuiIconList extends Gui
{
    int posX, posY, width, height;
    GuiEnhancedPortals parent;
    boolean isPortalTexture;
    public int selectedIcon, page;
    public boolean isActive;
    
    public GuiIconList(int x, int y, int w, int h, GuiEnhancedPortals gui, boolean portalTexture)
    {
        posX = x;
        posY = y;
        width = w;
        height = h;
        parent = gui;
        isPortalTexture = portalTexture;
        selectedIcon = -1;
        isActive = true;
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
            
            if (selectedIcon == ic)
            {
                parent.drawRectangle(x - 1, y - 1, 18, 18, 0x88FFFFFF, false);
            }

            if (isPortalTexture ? ClientProxy.customPortalTextures.size() <= ic : ClientProxy.customPortalFrameTextures.size() <= ic)
            {
                break;
            }
            
            if (ic < 0)
            {
                continue;
            }
            
            drawTexturedModelRectFromIcon(x, y, (Icon)(isPortalTexture ? ClientProxy.customPortalTextures.get(ic) : ClientProxy.customPortalFrameTextures.get(ic)), 16, 16);
        }
        
        if (isPortalTexture ? ClientProxy.customPortalTextures.isEmpty() : ClientProxy.customPortalFrameTextures.isEmpty())
        {
            parent.getFontRenderer().drawSplitString("No custom icons found.", parent.getGuiLeft() + posX + 5, parent.getGuiTop() + posY + 5, width - 10, 0xFFFFFFFF);
        }
    }
        
    public void mouseClicked(int x, int y, int button)
    {
        if (isActive)
        {
            if (x >= parent.getGuiLeft() + posX && x <= parent.getGuiLeft() + posX + width && y >= parent.getGuiTop() + posY && y <= parent.getGuiTop() + posY + height)
            {
                if (button == 1)
                {
                    selectedIcon = -1;
                }
                else if (button == 0)
                {
                    for (int i = 0; i < 27; i++)
                    {
                        int x2 = parent.getGuiLeft() + posX + 2 + (i % 9) * 18;
                        int y2 = parent.getGuiTop() + posY + 2 + (i / 9) * 18;
                        
                        if (x >= x2 && x <= x2 + 18 && y >= y2 && y <= y2 + 18)
                        {
                            if (isPortalTexture ? ClientProxy.customPortalTextures.size() <= i : ClientProxy.customPortalFrameTextures.size() <= i)
                            {
                                return;
                            }
                            
                            if (selectedIcon == (page * 27) + i)
                            {
                                selectedIcon = -1;
                            }
                            else
                            {
                                selectedIcon = (page * 27) + i;
                            }
                            
                            return;
                        }
                    }
                }
            }
        }
    }
}
