package enhancedportals.client.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.common.network.PacketDispatcher;
import enhancedportals.lib.GuiIds;
import enhancedportals.lib.Localization;
import enhancedportals.lib.Reference;
import enhancedportals.network.packet.PacketGui;
import enhancedportals.network.packet.PacketNetworkUpdate;
import enhancedportals.tileentity.TileEntityPortalModifier;

public class GuiPortalModifierNetwork extends GuiScreen
{
    List<GuiGlyphElement> elementList = new ArrayList<GuiGlyphElement>();
    List<GuiGlyphElement> stackList = new ArrayList<GuiGlyphElement>();
    int elementCount = 0;
    int guiTop = 0, guiLeft = 0, xSize = 176, ySize = 166;
    String elementString = "";
    RenderItem itemRenderer = new RenderItem();

    TileEntityPortalModifier portalModifier;

    public GuiPortalModifierNetwork(TileEntityPortalModifier modifier)
    {
        // Row 1
        for (int i = 0; i < 9; i++)
        {
            elementList.add(new GuiGlyphElement(guiLeft + 8 + i * 18, guiTop + 15, Reference.glyphValues.get(i), Reference.glyphItems.get(i), this));
        }

        for (int i = 9; i < 18; i++)
        {
            elementList.add(new GuiGlyphElement(guiLeft + 8 + (i - 9) * 18, guiTop + 33, Reference.glyphValues.get(i), Reference.glyphItems.get(i), this));
        }

        for (int i = 18; i < 27; i++)
        {
            elementList.add(new GuiGlyphElement(guiLeft + 8 + (i - 18) * 18, guiTop + 51, Reference.glyphValues.get(i), Reference.glyphItems.get(i), this));
        }

        portalModifier = modifier;

        if (portalModifier.network != "")
        {
            String[] split = portalModifier.network.split(Reference.glyphSeperator);

            for (String element2 : split)
            {
                for (int j = 0; j < Reference.glyphValues.size(); j++)
                {
                    if (Reference.glyphValues.get(j).equalsIgnoreCase(element2))
                    {
                        GuiGlyphElement element = elementList.get(j);

                        stackList.add(new GuiGlyphElement(0, 0, Reference.glyphValues.get(j), Reference.glyphItems.get(j), this, true));
                        element.stackSize++;
                        elementCount++;
                    }
                }
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        super.actionPerformed(button);
        Random random = new Random();

        if (button.id == 1)
        {
            PacketDispatcher.sendPacketToServer(new PacketGui(true, false, GuiIds.PortalModifier, portalModifier).getPacket());
        }
        else if (button.id == 2)
        {
            if (isShiftKeyDown())
            {
                stackList.clear();
                elementCount = 0;

                for (int i = 0; i < elementList.size(); i++)
                {
                    elementList.get(i).stackSize = 0;
                }
            }
            else
            {
                stackList.clear();
                elementCount = 0;

                for (int i = 0; i < elementList.size(); i++)
                {
                    elementList.get(i).stackSize = 0;
                }

                for (int i = 0; i < (isCtrlKeyDown() ? 9 : random.nextInt(8) + 1); i++)
                {
                    GuiGlyphElement element = elementList.get(random.nextInt(elementList.size()));

                    stackList.add(new GuiGlyphElement(0, 0, element.value, element.itemStack, this, true));
                    element.stackSize++;

                    elementCount++;

                }
            }
        }
        else if (button.id == 3)
        {
            String str = "";

            for (int i = 0; i < stackList.size(); i++)
            {
                str = str + Reference.glyphSeperator + stackList.get(i).value;
            }

            if (str.length() > 0)
            {
                str = str.substring(Reference.glyphSeperator.length());
            }

            portalModifier.network = str;
            PacketDispatcher.sendPacketToServer(new PacketNetworkUpdate(portalModifier).getPacket());
            PacketDispatcher.sendPacketToServer(new PacketGui(true, false, GuiIds.PortalModifier, portalModifier).getPacket());
        }
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @SuppressWarnings("rawtypes")
    protected void drawHoveringText(List par1List, int par2, int par3, FontRenderer font)
    {
        if (!par1List.isEmpty())
        {
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            int k = 0;
            Iterator iterator = par1List.iterator();

            while (iterator.hasNext())
            {
                String s = (String) iterator.next();
                int l = font.getStringWidth(s);

                if (l > k)
                {
                    k = l;
                }
            }

            int i1 = par2 + 12;
            int j1 = par3 - 12;
            int k1 = 8;

            if (par1List.size() > 1)
            {
                k1 += 2 + (par1List.size() - 1) * 10;
            }

            if (i1 + k > width)
            {
                i1 -= 28 + k;
            }

            if (j1 + k1 + 6 > height)
            {
                j1 = height - k1 - 6;
            }

            zLevel = 300.0F;
            itemRenderer.zLevel = 300.0F;
            int l1 = -267386864;
            drawGradientRect(i1 - 3, j1 - 4, i1 + k + 3, j1 - 3, l1, l1);
            drawGradientRect(i1 - 3, j1 + k1 + 3, i1 + k + 3, j1 + k1 + 4, l1, l1);
            drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 + k1 + 3, l1, l1);
            drawGradientRect(i1 - 4, j1 - 3, i1 - 3, j1 + k1 + 3, l1, l1);
            drawGradientRect(i1 + k + 3, j1 - 3, i1 + k + 4, j1 + k1 + 3, l1, l1);
            int i2 = 1347420415;
            int j2 = (i2 & 16711422) >> 1 | i2 & -16777216;
            drawGradientRect(i1 - 3, j1 - 3 + 1, i1 - 3 + 1, j1 + k1 + 3 - 1, i2, j2);
            drawGradientRect(i1 + k + 2, j1 - 3 + 1, i1 + k + 3, j1 + k1 + 3 - 1, i2, j2);
            drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 - 3 + 1, i2, i2);
            drawGradientRect(i1 - 3, j1 + k1 + 2, i1 + k + 3, j1 + k1 + 3, j2, j2);

            for (int k2 = 0; k2 < par1List.size(); ++k2)
            {
                String s1 = (String) par1List.get(k2);
                font.drawStringWithShadow(s1, i1, j1, -1);

                if (k2 == 0)
                {
                    j1 += 2;
                }

                j1 += 10;
            }

            zLevel = 0.0F;
            itemRenderer.zLevel = 0.0F;
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            RenderHelper.enableStandardItemLighting();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void drawItemStackTooltip(ItemStack par1ItemStack, int par2, int par3)
    {
        List list = par1ItemStack.getTooltip(mc.thePlayer, mc.gameSettings.advancedItemTooltips);

        for (int k = 0; k < list.size(); ++k)
        {
            if (k == 0)
            {
                list.set(k, "\u00a7" + Integer.toHexString(par1ItemStack.getRarity().rarityColor) + (String) list.get(k));
            }
            else
            {
                list.set(k, EnumChatFormatting.GRAY + (String) list.get(k));
            }
        }

        FontRenderer font = par1ItemStack.getItem().getFontRenderer(par1ItemStack);
        drawHoveringText(list, par2, par3, font == null ? fontRenderer : font);
    }

    @Override
    public void drawScreen(int x, int y, float par3)
    {
        drawDefaultBackground();

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(Reference.GUI_LOCATION + "portalModifierNetwork.png");
        int x2 = (width - xSize) / 2;
        int y2 = (height - ySize) / 2 - 3;
        drawTexturedModalRect(x2, y2, 0, 0, 166 + 22, 176);
        fontRenderer.drawString(Localization.localizeString("gui.selectNetwork.title"), guiLeft + xSize / 2 - fontRenderer.getStringWidth(Localization.localizeString("tile.selectNetwork.title")) / 2, guiTop - 15, 0xFFCCCCCC);

        super.drawScreen(x, y, par3);

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        fontRenderer.drawString(Localization.localizeString("gui.glyphs.title"), guiLeft + 7, guiTop + 3, 0xFF444444);
        fontRenderer.drawString(Localization.localizeString("gui.network.title"), guiLeft + 7, guiTop + 71, 0xFF444444);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        for (int i = stackList.size() - 1; i >= 0; i--)
        {
            stackList.get(i).drawElement(guiLeft + 8 + i * 18, guiTop + 83, x, y, fontRenderer, itemRenderer, mc.renderEngine);
        }

        for (int i = 8; i >= 0; i--)
        {
            elementList.get(i).drawElement(guiLeft, guiTop, x, y, fontRenderer, itemRenderer, mc.renderEngine);
        }

        for (int i = 17; i >= 9; i--)
        {
            elementList.get(i).drawElement(guiLeft, guiTop, x, y, fontRenderer, itemRenderer, mc.renderEngine);
        }

        for (int i = elementList.size() - 1; i >= 18; i--)
        {
            elementList.get(i).drawElement(guiLeft, guiTop, x, y, fontRenderer, itemRenderer, mc.renderEngine);
        }

        if (isShiftKeyDown())
        {
            String str = "";

            for (int i = 0; i < stackList.size(); i++)
            {
                str = str + Reference.glyphSeperator + stackList.get(i).value;
            }

            if (str.length() > 0)
            {
                str = str.substring(Reference.glyphSeperator.length());
            }

            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            fontRenderer.drawString(EnumChatFormatting.GRAY + str, guiLeft + xSize / 2 - fontRenderer.getStringWidth(str) / 2, guiTop + ySize - 20, 0xFFFFFFFF);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);

            ((GuiButton) buttonList.get(1)).displayString = Localization.localizeString("gui.clear");
        }
        else
        {
            ((GuiButton) buttonList.get(1)).displayString = Localization.localizeString("gui.random");
        }
    }

    public void elementClicked(GuiGlyphElement element, int button)
    {
        if (!element.isStack)
        {
            if (button == 0)
            {
                if (elementCount < 9)
                {
                    stackList.add(new GuiGlyphElement(0, 0, element.value, element.itemStack, this, true));
                    element.stackSize++;

                    elementCount++;
                }
            }
            else if (button == 1)
            {
                if (elementCount > 0)
                {
                    for (int i = stackList.size() - 1; i >= 0; i--)
                    {
                        if (stackList.get(i).itemStack.isItemEqual(element.itemStack))
                        {
                            stackList.remove(i);

                            if (element.stackSize >= 1)
                            {
                                element.stackSize--;
                            }

                            elementCount--;
                            return;
                        }
                    }
                }
            }
        }
        else
        {
            if (button == 1)
            {
                stackList.remove(element);

                for (int i = elementList.size() - 1; i >= 0; i--)
                {
                    if (elementList.get(i).itemStack.isItemEqual(element.itemStack))
                    {
                        if (elementList.get(i).stackSize >= 1)
                        {
                            elementList.get(i).stackSize--;
                        }

                        elementCount--;
                        return;
                    }
                }

                elementCount--;
                return;
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();

        guiLeft = (width - xSize) / 2;
        guiTop = (height - ySize) / 2;

        buttonList.add(new GuiButton(1, guiLeft + 7, guiTop + 108, 50, 20, Localization.localizeString("gui.cancel")));
        buttonList.add(new GuiButton(2, guiLeft + 63, guiTop + 108, 50, 20, Localization.localizeString("gui.random")));
        buttonList.add(new GuiButton(3, guiLeft + 119, guiTop + 108, 50, 20, Localization.localizeString("gui.accept")));
    }

    @Override
    protected void mouseClicked(int x, int y, int buttonClicked)
    {
        super.mouseClicked(x, y, buttonClicked);

        for (int i = 0; i < elementList.size(); i++)
        {
            elementList.get(i).handleMouseClick(guiLeft, guiTop, x, y, buttonClicked);
        }

        for (int i = 0; i < stackList.size(); i++)
        {
            stackList.get(i).handleMouseClick(guiLeft + 8 + i * 18, guiTop + 83, x, y, buttonClicked);
        }
    }
}
