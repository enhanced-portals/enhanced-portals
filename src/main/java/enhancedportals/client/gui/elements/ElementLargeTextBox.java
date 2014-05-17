package enhancedportals.client.gui.elements;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea;

import org.lwjgl.opengl.GL11;

import enhancedportals.client.gui.BaseGui;

public class ElementLargeTextBox extends BaseElement
{
    float vertCurrentScroll = 0f, horizCurrentScroll = 0f;
    int vertScrollAmount = 0, horizScrollAmount = 0;
    boolean isScrolling = false, wasClicking = false;
    
    public ElementLargeTextBox(BaseGui gui, int x, int y, int w, int h)
    {
        super(gui, x, y, w, h);
        texture = gui.getTexture();
    }

    @Override
    public void addTooltip(List<String> list)
    {

    }

    @Override
    protected void drawBackground()
    {

    }
    
    @Override
    public boolean keyPressed(int keyCode, char key)
    {
        //if (isFocused)
        //{
            // TODO
        //    return true;
        //}
        
        return super.keyPressed(keyCode, key);
    }
    
    @Override
    public boolean handleMouseClicked(int x, int y, int mouseButton)
    {
        // TODO
        return true;
    }
    
    @Override
    protected void drawContent()
    {
        boolean canScrollHoriz = true, canScrollVert = false;

        // TODO
        
        int l = posY + 1, k = l + sizeY - 1, m = posX + 1, n = m + sizeX - 1;
        GL11.glColor3f(1f, 1f, 1f);
        parent.getMinecraft().getTextureManager().bindTexture(texture);
        drawTexturedModalRect(posX + sizeX - 13, posY + 1 + (int)((float)(k - l - 16) * this.vertCurrentScroll), 244, 226 + (canScrollVert ? 0 : 15), 12, 15);
        drawTexturedModalRect(posX + 1 + (int)((float)(n - m - 16) * horizCurrentScroll), posY + sizeY - 13, 229, 232 + (canScrollHoriz ? 0 : 12), 15, 12);
    }

    @Override
    public void update()
    {
        //cursorCounter++;
    }
}
