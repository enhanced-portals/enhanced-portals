package enhancedportals.client.gui.elements;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import enhancedportals.client.gui.BaseGui;
import enhancedportals.portal.GlyphIdentifier;

public class ElementGlyphSelector extends BaseElement
{
    public static final ResourceLocation glyphs = new ResourceLocation("enhancedportals", "textures/gui/glyphs.png");
    ArrayList<Integer> selectedGlyphs = new ArrayList<Integer>();
    int[] glyphCount = new int[27];

    public ElementGlyphSelector(BaseGui gui, int x, int y)
    {
        super(gui, x, y, 162, 54);
        texture = new ResourceLocation("enhancedportals", "textures/gui/player_inventory.png");
    }

    @Override
    protected void drawBackground()
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        parent.getMinecraft().renderEngine.bindTexture(texture);
        parent.drawTexturedModalRect(posX, posY, 7, 7, sizeX, sizeY);
    }

    @Override
    public boolean handleMouseClicked(int x, int y, int mouseButton)
    {
        if (disabled)
        {
            return false;
        }

        for (int i = 0; i < 27; i++)
        {
            int X = posX - parent.getGuiLeft() + ((i % 9) * 18), Y = posY - parent.getGuiTop() + ((i / 9) * 18);

            if (x >= X && x < X + 18 && y >= Y && y < Y + 18)
            {
                if (mouseButton == 0)
                {
                    if (selectedGlyphs.size() == 9)
                    {
                        return true;
                    }

                    addGlyph(i);
                }
                else
                {
                    if (glyphCount[i] == 0)
                    {
                        return true;
                    }
                    else
                    {
                        for (int j = selectedGlyphs.size() - 1; j >= 0; j--)
                        {
                            if (selectedGlyphs.get(j) == i)
                            {
                                removeGlyphAt(j);
                                break;
                            }
                        }
                    }
                }

                // TODO parent.getMinecraft().sndManager.playSoundFX("random.click", 1.0F, 1.0F);
                return true;
            }
        }

        return false;
    }

    public void removeGlyphAt(int i)
    {
        glyphCount[selectedGlyphs.get(i)]--;
        selectedGlyphs.remove(i);
    }

    public void reset()
    {
        selectedGlyphs.clear();
        glyphCount = new int[27];
    }

    public void setIdentifierTo(GlyphIdentifier identifier)
    {
        reset();

        if (identifier != null && !identifier.isEmpty())
        {
            selectedGlyphs = new ArrayList<Integer>(identifier.getGlyphs());

            for (int i : selectedGlyphs)
            {
                glyphCount[i]++;
            }
        }
    }

    @Override
    public void update()
    {

    }

    public void addGlyph(int i)
    {
        if (selectedGlyphs.size() < 9)
        {
            selectedGlyphs.add(i);
            glyphCount[i]++;
        }
    }

    public GlyphIdentifier getGlyphIdentifier()
    {
        return new GlyphIdentifier(selectedGlyphs);
    }

    @Override
    protected void drawContent()
    {
        parent.getMinecraft().renderEngine.bindTexture(glyphs);

        for (int i = 0; i < 27; i++)
        {
            int X = i % 9 * 18, Y = i / 9 * 18;

            if (parent.getMouseX() + parent.getGuiLeft() >= posX + X && parent.getMouseX() + parent.getGuiLeft() < posX + X + 18 && parent.getMouseY() + parent.getGuiTop() >= posY + Y && parent.getMouseY() + parent.getGuiTop() < posY + Y + 18)
            {
                parent.drawRect(posX + X + 1, posY + Y + 1, posX + X + 17, posY + Y + 17, 0x66FFFFFF);
            }
            
            if (glyphCount[i] > 0)
            {
                parent.drawRect(posX + X + 1, posY + Y + 1, posX + X + 17, posY + Y + 17, 0x3300FF00);
                GL11.glColor3f(1f, 1f, 1f);
            }

            drawTexturedModalRect(posX + X, posY + Y, X, Y, 18, 18);
        }

        for (int i = 0; i < glyphCount.length; i++)
        {
            String s = "" + (glyphCount[i] > 0 ? glyphCount[i] : "");
            parent.getFontRenderer().drawStringWithShadow(s, posX + i % 9 * 18 + 17 - parent.getFontRenderer().getStringWidth(s), posY + i / 9 * 18 + 9, 0xFFFFFF);
        }
    }

    @Override
    public void addTooltip(List<String> list)
    {

    }
}
