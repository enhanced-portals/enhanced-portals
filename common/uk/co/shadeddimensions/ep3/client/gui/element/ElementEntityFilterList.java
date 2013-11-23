package uk.co.shadeddimensions.ep3.client.gui.element;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.ep3.client.gui.IElementHandler;
import uk.co.shadeddimensions.ep3.lib.Localization;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileBiometricIdentifier;
import uk.co.shadeddimensions.ep3.util.EntityData;
import uk.co.shadeddimensions.ep3.util.GuiUtils;
import cofh.gui.GuiBase;

public class ElementEntityFilterList extends ElementDialDeviceScrollList
{
    ArrayList<String> tooltip;
    TileBiometricIdentifier biometric;
    boolean sendList;

    public ElementEntityFilterList(GuiBase gui, ResourceLocation texture, TileBiometricIdentifier bio, int posX, int posY, int width, int height, boolean isSendList)
    {
        super(gui, texture, null, posX, posY, width, height);
        biometric = bio;
        sendList = isSendList;
        tooltip = new ArrayList<String>();
    }

    @Override
    protected void drawOverlays()
    {
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        gui.drawTexturedModalRect(posX, posY - getEntrySize(), posX - gui.getGuiLeft() + (sendList ? 0 : 106), posY - gui.getGuiTop() - getEntrySize(), sizeX, getEntrySize());
        gui.drawTexturedModalRect(posX, posY + sizeY, posX - gui.getGuiLeft() + (sendList ? 0 : 106), posY - gui.getGuiTop() + sizeY, sizeX, getEntrySize());
    }

    @Override
    public void draw()
    {
        tooltip = new ArrayList<String>();
        super.draw();
    }

    @Override
    protected void drawElement(int i, int x, int y, boolean isSelected, int mouseX, int mouseY)
    {
        boolean yMouse = mouseY >= y && mouseY <= y + getEntrySize();
        boolean mouseOverMain = yMouse && mouseX >= x + 5 && mouseX <= x + sizeX - 38, mouseOverMode = yMouse && mouseX >= x + sizeX - 36 && mouseX <= x + sizeX - 22, mouseOverRemove = yMouse && mouseX >= x + sizeX - 20 && mouseX <= x + sizeX - 6;
        float colour = isSelected ? 0.7f : 1f;
        int mainButtonWidth = sizeX - 37, halfButtonWidth = mainButtonWidth / 2;
        EntityData data = (sendList ? biometric.sendingEntityTypes : biometric.recievingEntityTypes).get(i);
        String s = data.shouldCheckClass() ? EntityData.getClassDisplayName(data) : data.EntityDisplayName + (data.shouldCheckNameAndClass() ? " (" + EntityData.getClassDisplayName(data) + ")" : "");

        if (s.length() > 20)
        {
            s = s.substring(0, 17) + "...";
        }

        GL11.glColor3f(colour, colour, colour);
        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("enhancedportals", "textures/gui/buttons.png"));
        gui.drawTexturedModalRect(x + 5, y, 0, isSelected || mouseOverMain ? 110 : 95, halfButtonWidth, 15);
        gui.drawTexturedModalRect(x + halfButtonWidth, y, 200 - halfButtonWidth - 1, isSelected || mouseOverMain ? 110 : 95, halfButtonWidth + 1, 15);

        GL11.glColor3f(1f, 1f, 1f);
        gui.drawTexturedModalRect(x + sizeX - 20, y, 45, mouseOverRemove ? 170 : 155, 15, 15);
        gui.drawTexturedModalRect(x + sizeX - 36, y, 30, mouseOverMode ? 170 : 155, 15, 15);

        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(s, x + 10, y + 3, data.isInverted ? 0xFF0000 : 0x00FF00);
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("" + data.checkType, x + sizeX - 31, y + 3, 0xFFFFFF);
        GL11.glColor3f(1f, 1f, 1f);

        if (mouseOverMain)
        {
            tooltip.add(data.isInverted ? EnumChatFormatting.RED + Localization.getGuiString("disallowing") : EnumChatFormatting.GREEN + Localization.getGuiString("allowing"));
            tooltip.add(data.shouldCheckClass() ? Localization.getGuiString("anythingOfTheType") : Localization.getGuiString("anythingCalled"));
            tooltip.add(EnumChatFormatting.GRAY + " " + (data.shouldCheckClass() ? EntityData.getClassDisplayName(data) : data.EntityDisplayName));

            if (data.shouldCheckNameAndClass())
            {
                tooltip.add(Localization.getGuiString("withTheTypeOf"));
                tooltip.add(EnumChatFormatting.GRAY + " " + EntityData.getClassDisplayName(data));
            }
        }
        else if (mouseOverMode)
        {
            if (data.checkType == 0)
            {
                tooltip.add(Localization.getGuiString("matchName"));
            }
            else if (data.checkType == 1)
            {
                tooltip.add(Localization.getGuiString("matchType"));
            }
            else
            {
                tooltip.add(Localization.getGuiString("matchTypeAndName"));
            }
        }
    }

    @Override
    protected void drawForeground()
    {
        if (!sendList && !biometric.hasSeperateLists)
        {
            Minecraft.getMinecraft().fontRenderer.drawString(Localization.getGuiString("listNotEnabled"), posX + 10, posY + 7, 0xAAAAAA);
        }
        else
        {
            super.drawForeground();
        }
    }

    public void drawHoverText()
    {
        GuiUtils.getInstance().drawHoveringText(tooltip, gui.getMouseX(), gui.getMouseY());
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glColor3f(1f, 1f, 1f);
    }

    @Override
    protected void entrySelected(int entry, int mouseX, int mouseY)
    {
        if (mouseX >= posX + 5 && mouseX <= posX + sizeX - 38) // Selected glyph entry
        {
            if (gui instanceof IElementHandler)
            {
                ((IElementHandler) gui).onElementChanged(this, new int[] { sendList ? 0 : 1, 0, entry });
            }
        }
        else if (mouseX >= posX + sizeX - 36 && mouseX <= posX + sizeX - 22) // Selected mode button
        {
            if (gui instanceof IElementHandler)
            {
                ((IElementHandler) gui).onElementChanged(this, new int[] { sendList ? 0 : 1, 1, entry });
            }
        }
        else if (mouseX >= posX + sizeX - 20 && mouseX <= posX + sizeX - 6) // Selected remove button
        {
            if (gui instanceof IElementHandler)
            {
                ((IElementHandler) gui).onElementChanged(this, new int[] { sendList ? 0 : 1, 2, entry });
            }
        }
    }

    @Override
    protected int getElementCount()
    {
        return (sendList ? biometric.sendingEntityTypes : biometric.recievingEntityTypes).size();
    }
}
