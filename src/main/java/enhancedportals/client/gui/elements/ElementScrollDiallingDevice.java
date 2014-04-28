package enhancedportals.client.gui.elements;

import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.util.ResourceLocation;
import uk.co.shadeddimensions.ep3.portal.GlyphElement;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileDiallingDevice;
import enhancedportals.client.gui.GuiDiallingDevice;

public class ElementScrollDiallingDevice extends BaseElement
{
    static final ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");
    TileDiallingDevice dial;
    int scrollIndex = 0;

    public ElementScrollDiallingDevice(GuiDiallingDevice gui, TileDiallingDevice d, int x, int y)
    {
        super(gui, x, y, gui.getSizeX() - 14, gui.getSizeY() - 57);
        dial = d;
    }

    @Override
    public void addTooltip(List<String> list)
    {
        
    }

    @Override
    public boolean handleMouseClicked(int x, int y, int mouseButton)
    {
        int offsetX = 5, offsetY = 5, buttonHalf = 100, buttonSmallHalf = 10, buttonSpacing = 2, entryHeight = 22;
        
        for (int i = scrollIndex; i < scrollIndex + 5; i++)
        {
            if (i >= dial.glyphList.size())
            {
                break;
            }
            
            GlyphElement e = dial.glyphList.get(i);
            int entryOffset = i * entryHeight, mouseX = parent.getMouseX() + parent.getGuiLeft(), mouseY = parent.getMouseY() + parent.getGuiTop();
            boolean mouseOverEntry = mouseY >= posY + offsetY + entryOffset && mouseY <= posY + offsetY + entryOffset + 20, mouseOverMain = mouseOverEntry && mouseX >= posX + offsetX && mouseX < posX + offsetX + buttonHalf + buttonHalf, mouseOverSmall = mouseOverEntry && mouseX >= posX + offsetX + buttonHalf + buttonHalf + buttonSpacing && mouseX < posX + offsetX + buttonHalf + buttonHalf + buttonSpacing + buttonSmallHalf + buttonSmallHalf, delete = parent.isShiftKeyDown();
            
            if (mouseOverMain)
            {
                ((GuiDiallingDevice) parent).onEntrySelected(i);
            }
            else if (mouseOverSmall)
            {
                if (parent.isShiftKeyDown())
                {
                    ((GuiDiallingDevice) parent).onEntryDeleted(i);
                }
                else
                {
                    ((GuiDiallingDevice) parent).onEntryEdited(i);
                }
            }
        }
        
        return true;
    }
    
    @Override
    protected void drawContent()
    {
        parent.drawRect(posX, posY, posX + sizeX, posY + sizeY, 0xFF000000);
        parent.drawRect(posX + 1, posY + 1, posX + sizeX - 1, posY + sizeY - 1, 0xAAFFFFFF);
        
        parent.drawRect(posX + sizeX - 10, posY + 1, posX + sizeX - 1, posY + sizeY - 1, 0x44000000);
        parent.drawRect(posX + sizeX - 10, posY + 1, posX + sizeX - 1, posY + 10, 0x66000000);
        parent.drawRect(posX + sizeX - 10, posY + sizeY - 10, posX + sizeX - 1, posY + sizeY - 1, 0x66000000);
        
        int offsetX = 5, offsetY = 5, buttonHalf = 100, buttonSmallHalf = 10, buttonSpacing = 2, entryHeight = 22;
        
        for (int i = scrollIndex; i < scrollIndex + 5; i++)
        {
            if (i >= dial.glyphList.size())
            {
                break;
            }
            
            GlyphElement e = dial.glyphList.get(i);
            int entryOffset = i * entryHeight, mouseX = parent.getMouseX() + parent.getGuiLeft(), mouseY = parent.getMouseY() + parent.getGuiTop(), fontColour = 0xFFFFFF;
            boolean mouseOverEntry = mouseY >= posY + offsetY + entryOffset && mouseY <= posY + offsetY + entryOffset + 20, mouseOverMain = mouseOverEntry && mouseX >= posX + offsetX && mouseX < posX + offsetX + buttonHalf + buttonHalf, mouseOverSmall = mouseOverEntry && mouseX >= posX + offsetX + buttonHalf + buttonHalf + buttonSpacing && mouseX < posX + offsetX + buttonHalf + buttonHalf + buttonSpacing + buttonSmallHalf + buttonSmallHalf, delete = parent.isShiftKeyDown();
            
            parent.getTextureManager().bindTexture(buttonTextures);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            
            parent.drawTexturedModalRect(posX + offsetX, posY + entryOffset + offsetY, 0, 66 + (mouseOverMain ? 20 : 0), buttonHalf, 20);
            parent.drawTexturedModalRect(posX + offsetX + buttonHalf, posY + entryOffset + offsetY, 200 - buttonHalf, 66 + (mouseOverMain ? 20 : 0), buttonHalf, 20);
            
            if (delete)
            {
                GL11.glColor4f(1.0F, 0.6F, 0.6F, 1.0F);
            }
            
            parent.drawTexturedModalRect(posX + offsetX + (buttonHalf * 2) + buttonSpacing, posY + entryOffset + offsetY, 0, 66 + (mouseOverSmall ? 20 : 0), buttonSmallHalf, 20);
            parent.drawTexturedModalRect(posX + offsetX + (buttonHalf * 2) + buttonSpacing + buttonSmallHalf, posY + entryOffset + offsetY, 200 - buttonSmallHalf, 66 + (mouseOverSmall ? 20 : 0), buttonSmallHalf, 20);
            
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            parent.getFontRenderer().drawStringWithShadow(e.name, posX + offsetX + 5, posY + offsetY + 6 + entryOffset, fontColour);
        }
    }

    @Override
    public void update()
    {

    }
}
