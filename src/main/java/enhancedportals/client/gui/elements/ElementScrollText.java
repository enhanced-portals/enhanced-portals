package enhancedportals.client.gui.elements;

import java.util.List;

import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import truetyper.FontHelper;
import enhancedportals.client.gui.BaseGui;
import enhancedportals.network.ClientProxy;
import enhancedportals.utility.GeneralUtils;

public class ElementScrollText extends BaseElement
{
    int textX, textY, textW, textH, scrollAmount, linesPerPage = 10, fontColour = 0x000000;
    List lines;
    float currentScroll = 0f;
    boolean isScrolling = false, wasClicking = false;
    
    public ElementScrollText(BaseGui gui, int x, int y, int w, int h, String text)
    {
        super(gui, x, y, w, h);
        textX = posX + 2;
        textY = posY + 2;
        textW = sizeX - 2 - 17;
        textH = sizeY - 2;
        lines = GeneralUtils.listFormattedStringToWidth(text, textW);
        texture = new ResourceLocation("enhancedportals", "textures/gui/dialling_device.png");
        linesPerPage = (int) (textH / 8);
    }
    
    @Override
    protected void drawBackground()
    {
        parent.drawRect(posX, posY, posX + sizeX, posY + sizeY, 0xAA0000FF);
    }
    
    protected void handleMouse()
    {
        boolean isMouseDown = Mouse.isButtonDown(0), ignoreScroll = false;
        int mouseX = parent.getMouseX() + parent.getGuiLeft(), mouseY = parent.getMouseY() + parent.getGuiTop();
        int scrollbarX = posX + sizeX - 13, scrollbarY = posY + 1, scrollbarX2 = scrollbarX + 11, scrollbarY2 = scrollbarY + sizeY - 3;

        if (!wasClicking && isMouseDown && mouseX >= scrollbarX && mouseX <= scrollbarX2 && mouseY >= scrollbarY && mouseY <= scrollbarY2)
        {
            isScrolling = true;
        }

        if (!isMouseDown)
        {
            isScrolling = false;
        }

        wasClicking = isMouseDown;

        if (!isScrolling && !isMouseDown && intersectsWith(mouseX - parent.getGuiLeft(), mouseY - parent.getGuiTop()))
        {
            int wheel = Mouse.getDWheel();

            if (wheel < 0)
            {
                currentScroll += 0.1;
                isScrolling = ignoreScroll = true;
            }
            else if (wheel > 0)
            {
                currentScroll -= 0.1;
                isScrolling = ignoreScroll = true;
            }
        }

        if (isScrolling)
        {
            if (!ignoreScroll)
            {
                currentScroll = ((mouseY - scrollbarY) - 7.5F) / ((scrollbarY2 - scrollbarY) - 15f);
            }

            if (currentScroll < 0f)
            {
                currentScroll = 0f;
            }
            else if (currentScroll > 1f)
            {
                currentScroll = 1f;
            }

            int items = lines.size() - linesPerPage + 1;
            scrollAmount = (int)((currentScroll * items) + 0.5D);

            if (scrollAmount < 0)
            {
                scrollAmount = 0;
            }

            int max = lines.size() - linesPerPage;

            if (scrollAmount > max)
            {
                scrollAmount = max;
            }
        }
    }
    @Override
    public void addTooltip(List<String> list)
    {

    }

    @Override
    protected void drawContent()
    {
        boolean canScroll = false;
        
        if (lines.size() >= linesPerPage + 1)
        {
            canScroll = true;
            handleMouse();
        }
        
        int l = posY + 1, k = l + sizeY - 1;
        GL11.glColor3f(1f, 1f, 1f);
        parent.getMinecraft().getTextureManager().bindTexture(texture);
        drawTexturedModalRect(posX + sizeX - 13, posY + 1 + (int)((float)(k - l - 16) * this.currentScroll), 244, 226 + (canScroll ? 0 : 15), 12, 15);

        for (int i = 0; i < linesPerPage; i++)
        {
            if (scrollAmount + i >= lines.size())
            {
                break;
            }
            
            FontHelper.drawString((String) lines.get(scrollAmount + i), textX, textY + (i * 8), ClientProxy.bookFont, 1f, 1f, 0f, new float[] { 0f, 0f, 0f, 1f });
        }
    }

    @Override
    public void update()
    {

    }
}
