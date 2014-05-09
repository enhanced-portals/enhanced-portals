package enhancedportals.client.gui.elements;

import java.util.List;

import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import enhancedportals.client.gui.GuiTextureParticle;
import enhancedportals.network.ClientProxy;
import enhancedportals.network.ClientProxy.ParticleSet;

public class ElementScrollParticles extends BaseElement
{
    float currentScroll = 0f;
    boolean isScrolling = false, wasClicking = false;
    int scrollAmount = 0, particleFrame, particleCounter;

    public ElementScrollParticles(GuiTextureParticle gui, int x, int y, ResourceLocation t)
    {
        super(gui, x, y, 176, 54);
        texture = t;
    }

    @Override
    public void addTooltip(List<String> list)
    {

    }

    @Override
    public boolean handleMouseClicked(int x, int y, int mouseButton)
    {
        if (mouseButton == 1)
        {
            ((GuiTextureParticle) parent).particleSelected(0);
            return true;
        }
        
        x += parent.getGuiLeft();
        y += parent.getGuiTop();
        
        for (int i = 0; i < 27; i++)
        {
            if (scrollAmount + i >= ClientProxy.particleSets.size())
            {
                break;
            }

            int x2 = posX + (i % 9 * 18) + 1, y2 = posY + (i / 9 * 18) + 1;

            if (x >= x2 && x < x2 + 16 && y >= y2 && y < y2 + 16)
            {
                ((GuiTextureParticle) parent).particleSelected(scrollAmount + i);
                break;
            }
        }

        return true;
    }

    @Override
    protected void drawBackground()
    {

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

            int items = ClientProxy.customFrameTextures.size() - 27 + 1;
            scrollAmount = (int)((currentScroll * items) + 0.5D);

            if (scrollAmount < 0)
            {
                scrollAmount = 0;
            }

            int max = ClientProxy.customFrameTextures.size() - 27;

            if (scrollAmount > max)
            {
                scrollAmount = max;
            }
        }
    }

    @Override
    protected void drawContent()
    {
        boolean canScroll = false;

        if (ClientProxy.particleSets.size() > 27)
        {
            canScroll = true;
            handleMouse();
        }

        int l = posY + 1, k = l + sizeY - 1, selectedIcon = ((GuiTextureParticle) parent).getPTM().getParticleType();
        GL11.glColor3f(1f, 1f, 1f);
        parent.getTextureManager().bindTexture(texture);
        drawTexturedModalRect(posX + sizeX - 13, posY + 1 + (int)((float)(k - l - 16) * this.currentScroll), 244, 226 + (canScroll ? 0 : 15), 12, 15);
        parent.getTextureManager().bindTexture(new ResourceLocation("textures/particle/particles.png"));

        for (int i = 0; i < 27; i++)
        {
            if (scrollAmount + i >= ClientProxy.particleSets.size())
            {
                break;
            }

            int x = posX + (i % 9 * 18) + 1, y = posY + (i / 9 * 18) + 1;
            
            if (scrollAmount + i == selectedIcon)
            {
                parent.drawRect(x - 1, y - 1, x + 17, y, 0xFF00FF00);
                parent.drawRect(x - 1, y - 1, x, y + 17, 0xFF00FF00);
                parent.drawRect(x - 1, y + 16, x + 17, y + 17, 0xFF00FF00);
                parent.drawRect(x + 16, y - 1, x + 17, y + 17, 0xFF00FF00);
            }
            
            GL11.glColor3f(1f, 1f, 1f);
            ParticleSet particles = ClientProxy.particleSets.get(scrollAmount + i);
            int particle = particles.frames[particleFrame % particles.frames.length];
            drawTexturedModalRect(x, y, particle % 16 * 16, particle / 16 * 16, 16, 16);
        }
    }

    @Override
    public void update()
    {
        particleCounter++;
        
        if (particleCounter > 75)
        {
            particleCounter = 0;
            particleFrame++;
        }
    }
}
