package uk.co.shadeddimensions.library.gui.element;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import uk.co.shadeddimensions.library.gui.GuiBaseContainer;
import uk.co.shadeddimensions.library.gui.IGuiBase;
import uk.co.shadeddimensions.library.util.GuiUtils;
import cpw.mods.fml.client.FMLClientHandler;

/**
 * Base class for a modular GUI element. Has self-contained rendering methods and a link back to the {@link GuiBaseContainer} it is a part of.
 * 
 * @author King Lemming, Alz454
 */
public abstract class ElementBase
{
    public static final SoundManager elementSoundManager = FMLClientHandler.instance().getClient().sndManager;
    public static final FontRenderer elementFontRenderer = FMLClientHandler.instance().getClient().fontRenderer;

    protected IGuiBase gui;
    protected ResourceLocation texture = new ResourceLocation("alzlib", "textures/gui/elements.png");

    protected int posX, posY, relativePosX, relativePosY, sizeX, sizeY, texW = 256, texH = 256;

    protected String name;

    protected boolean visible = true, disabled = false;
    protected ArrayList<String> hoverText;

    public ElementBase(IGuiBase gui, int posX, int posY)
    {
        this.gui = gui;
        relativePosX = posX;
        relativePosY = posY;
        this.posX = gui.getGuiLeft() + posX;
        this.posY = gui.getGuiTop() + posY;
    }

    public ElementBase(IGuiBase gui, int posX, int posY, int sizeX, int sizeY)
    {
        this(gui, posX, posY);
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public void addTooltip(List<String> list)
    {

    }

    public abstract void draw();

    public void draw(int x, int y)
    {
        posX = x;
        posY = y;
        draw();
    }

    public void drawTexturedModalRect(int x, int y, int u, int v, int width, int height)
    {
        GuiUtils.drawSizedTexturedModalRect(gui, x, y, u, v, width, height, texW, texH);
    }

    public int getHeight()
    {
        return sizeY;
    }

    public String getName()
    {
        return name;
    }

    public int getRelativeX()
    {
        return relativePosX;
    }

    public int getRelativeY()
    {
        return relativePosY;
    }

    public int getWidth()
    {
        return sizeX;
    }

    public boolean handleMouseClicked(int x, int y, int mouseButton)
    {
        return false;
    }

    public boolean intersectsWith(int mouseX, int mouseY)
    {
        mouseX += gui.getGuiLeft();
        mouseY += gui.getGuiTop();

        if (mouseX >= posX && mouseX < posX + sizeX && mouseY >= posY && mouseY < posY + sizeY)
        {
            return true;
        }

        return false;
    }

    public boolean isDisabled()
    {
        return disabled;
    }

    public boolean isVisible()
    {
        return visible;
    }

    public ElementBase setDisabled(boolean disabled)
    {
        this.disabled = disabled;

        return this;
    }

    public ElementBase setName(String name)
    {
        this.name = name;

        return this;
    }

    public ElementBase setPosition(int posX, int posY)
    {
        this.posX = gui.getGuiLeft() + posX;
        this.posY = gui.getGuiTop() + posY;
        relativePosX = posX;
        relativePosY = posY;

        return this;
    }

    public ElementBase setSize(int sizeX, int sizeY)
    {
        this.sizeX = sizeX;
        this.sizeY = sizeY;

        return this;
    }

    public ElementBase setTexture(String texture, int texW, int texH)
    {
        this.texture = new ResourceLocation(texture);
        this.texW = texW;
        this.texH = texH;

        return this;
    }

    public ElementBase setVisible(boolean visible)
    {
        this.visible = visible;

        return this;
    }

    public void update()
    {

    }
}
