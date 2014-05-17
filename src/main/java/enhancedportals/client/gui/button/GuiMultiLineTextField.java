package enhancedportals.client.gui.button;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

public class GuiMultiLineTextField extends GuiTextField
{

    public GuiMultiLineTextField(FontRenderer fontRenderer, int posX, int posY, int width, int height)
    {
        super(fontRenderer, posX, posY, width, height);
        setEnableBackgroundDrawing(false);
        setMaxStringLength(Integer.MAX_VALUE);
    }

}
