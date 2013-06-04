package enhancedportals.client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.util.EnumChatFormatting;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.network.PacketDispatcher;
import enhancedportals.lib.GuiIds;
import enhancedportals.lib.Localization;
import enhancedportals.lib.Reference;
import enhancedportals.lib.Strings;
import enhancedportals.network.packet.PacketEnhancedPortals;
import enhancedportals.network.packet.PacketGui;
import enhancedportals.network.packet.PacketPortalModifierUpdate;
import enhancedportals.portal.upgrades.modifier.UpgradeDialDevice;
import enhancedportals.tileentity.TileEntityPortalModifier;

public class GuiPortalModifierNetwork extends GuiNetwork
{
    List<GuiGlyphElement>    elementList   = new ArrayList<GuiGlyphElement>();
    List<GuiGlyphElement>    stackList     = new ArrayList<GuiGlyphElement>();
    int                      elementCount  = 0;
    int                      guiTop        = 0, guiLeft = 0, xSize = 176, ySize = 166;
    String                   elementString = "";
    RenderItem               itemRenderer  = new RenderItem();

    TileEntityPortalModifier portalModifier;

    public GuiPortalModifierNetwork(TileEntityPortalModifier modifier)
    {
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
            PacketDispatcher.sendPacketToServer(PacketEnhancedPortals.makePacket(new PacketGui(portalModifier, GuiIds.PortalModifier)));
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
            PacketDispatcher.sendPacketToServer(PacketEnhancedPortals.makePacket(new PacketPortalModifierUpdate(portalModifier)));
            PacketDispatcher.sendPacketToServer(PacketEnhancedPortals.makePacket(new PacketGui(portalModifier, GuiIds.PortalModifier)));
        }
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
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
        String stri = portalModifier.upgradeHandler.hasUpgrade(new UpgradeDialDevice()) ? Strings.IdentifierSelection.toString() : Strings.NetworkSelection.toString();
        fontRenderer.drawString(stri, guiLeft + xSize / 2 - fontRenderer.getStringWidth(stri) / 2, guiTop - 15, 0xFFCCCCCC);

        super.drawScreen(x, y, par3);

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        fontRenderer.drawString(Strings.Glyphs.toString(), guiLeft + 7, guiTop + 3, 0xFF444444);
        fontRenderer.drawString(portalModifier.upgradeHandler.hasUpgrade(new UpgradeDialDevice()) ? Strings.UniqueIdentifier.toString() : Strings.Network.toString(), guiLeft + 7, guiTop + 71, 0xFF444444);
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

            ((GuiButton) buttonList.get(1)).displayString = Strings.Clear.toString();
        }
        else
        {
            ((GuiButton) buttonList.get(1)).displayString = (isCtrlKeyDown() ? EnumChatFormatting.GOLD : "") + Strings.Random.toString();
        }
    }

    @Override
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

        buttonList.add(new GuiButton(1, guiLeft + 7, guiTop + 108, 50, 20, Strings.Cancel.toString()));
        buttonList.add(new GuiButton(2, guiLeft + 63, guiTop + 108, 50, 20, Strings.Random.toString()));
        buttonList.add(new GuiButton(3, guiLeft + 119, guiTop + 108, 50, 20, Strings.Accept.toString()));
    }

    @Override
    protected void keyTyped(char par1, int par2)
    {
        if (par2 == 1)
        {
            PacketDispatcher.sendPacketToServer(PacketEnhancedPortals.makePacket(new PacketGui(portalModifier, GuiIds.PortalModifier)));
            return;
        }

        super.keyTyped(par1, par2);
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
