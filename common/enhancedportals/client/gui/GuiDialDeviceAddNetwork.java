package enhancedportals.client.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import enhancedportals.container.ContainerDialDeviceAddNetwork;
import enhancedportals.lib.BlockIds;
import enhancedportals.lib.Localization;
import enhancedportals.lib.Reference;
import enhancedportals.lib.Textures;
import enhancedportals.portal.PortalTexture;
import enhancedportals.tileentity.TileEntityDialDevice;

public class GuiDialDeviceAddNetwork extends GuiEnhancedPortalsScreen
{
    TileEntityDialDevice  dialDevice;
    GuiTextField          nameField;

    byte                  thickness;
    boolean               sounds, particles, popUpState;
    String                texture;
    String                network, name;

    List<GuiGlyphElement> elementList   = new ArrayList<GuiGlyphElement>();
    List<GuiGlyphElement> stackList     = new ArrayList<GuiGlyphElement>();
    int                   elementCount  = 0;
    String                elementString = "";

    public GuiDialDeviceAddNetwork(InventoryPlayer inventory, TileEntityDialDevice dialdevice)
    {
        super(new ContainerDialDeviceAddNetwork(inventory), null);

        dialDevice = dialdevice;
        texture = "";
        name = "Name";
        thickness = 0;
        sounds = true;
        particles = true;
        popUpState = false;

        for (int i = 0; i < 9; i++)
        {
            elementList.add(new GuiGlyphElement(guiLeft + 8 + i * 18, guiTop + 15, Reference.glyphItems.get(i).getItemName().replace("item.", ""), Reference.glyphItems.get(i), this));
        }

        for (int i = 9; i < 18; i++)
        {
            elementList.add(new GuiGlyphElement(guiLeft + 8 + (i - 9) * 18, guiTop + 33, Reference.glyphItems.get(i).getItemName().replace("item.", ""), Reference.glyphItems.get(i), this));
        }

        for (int i = 18; i < 27; i++)
        {
            elementList.add(new GuiGlyphElement(guiLeft + 8 + (i - 18) * 18, guiTop + 51, Reference.glyphItems.get(i).getItemName().replace("item.", ""), Reference.glyphItems.get(i), this));
        }

    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 1)
        {
            if (network == null || network.equals(""))
            {
                return;
            }

            // TODO PacketDispatcher.sendPacketToServer(new PacketNetworkData(dialDevice, name, network, texture, thickness, particles, sounds).getPacket());
            // TODO PacketDispatcher.sendPacketToServer(new PacketGui(true, false, GuiIds.DialDevice, dialDevice).getPacket());
        }
        else if (button.id == 2)
        {
            // TODO PacketDispatcher.sendPacketToServer(new PacketGui(true, false, GuiIds.DialDevice, dialDevice).getPacket());
        }
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        drawBackground(0);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(Reference.GUI_LOCATION + "dialDeviceInventory.png");
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

        int padding = 5, width = 10, colourA = 0xFFFF0000, colourB = 0xFF00FF00;
        ItemStack itemstack = Textures.getItemStackFromTexture(texture);

        if (thickness == 1)
        {
            padding = 4;
            width = 8;
        }
        else if (thickness == 2)
        {
            padding = 2;
            width = 4;
        }
        else if (thickness == 3)
        {
            padding = 0;
            width = 0;
        }

        if (itemstack != null)
        {
            itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, itemstack, guiLeft + xSize - 24, guiTop + 40);
            GL11.glDisable(GL11.GL_LIGHTING);
        }

        drawRect(guiLeft + 8, guiTop + 40, guiLeft + 24, guiTop + 56, particles ? colourB : colourA);
        drawRect(guiLeft + 8 + 18, guiTop + 40, guiLeft + 24 + 18, guiTop + 56, sounds ? colourB : colourA);
        drawRect(guiLeft + 134 + padding, guiTop + 40, guiLeft + 16 - width + 134 + padding, guiTop + 40 + 16, 0xFF555555);

        itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, new ItemStack(Item.blazePowder), guiLeft + 8, guiTop + 40);
        GL11.glDisable(GL11.GL_LIGHTING);
        itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, new ItemStack(Block.jukebox), guiLeft + 8 + 18, guiTop + 40);
        GL11.glDisable(GL11.GL_LIGHTING);

        if (stackList != null)
        {
            for (int i2 = 0; i2 < stackList.size(); i2++)
            {
                int x2 = guiLeft + 8, y2 = guiTop + 62;

                if (stackList.get(i2).itemStack == null)
                {
                    continue;
                }

                itemRenderer.renderItemIntoGUI(fontRenderer, mc.renderEngine, stackList.get(i2).itemStack, x2 + i2 * 18, y2);
            }
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        fontRenderer.drawString(Localization.localizeString("tile.dialDevice.name"), xSize / 2 - fontRenderer.getStringWidth(Localization.localizeString("tile.dialDevice.name")) / 2, -20, 0xFFCCCCCC);

        if (!popUpState)
        {
            if (stackList.isEmpty())
            {
                fontRenderer.drawStringWithShadow(Localization.localizeString("gui.network.info"), xSize / 2 - fontRenderer.getStringWidth(Localization.localizeString("gui.network.info")) / 2, 66, 0xFF00FF00);
            }

            fontRenderer.drawString(Localization.localizeString("Modifications"), xSize / 2 - fontRenderer.getStringWidth(Localization.localizeString("Modifications")) / 2, 44, 0xFF444444);
        }
    }

    @SuppressWarnings("rawtypes")
    private void drawHText(List par1List, int par2, int par3, FontRenderer font)
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
    public void drawItemTooltip(ItemStack itemStack, int par2, int par3)
    {
        List list = itemStack.getTooltip(mc.thePlayer, mc.gameSettings.advancedItemTooltips);

        for (int k = 0; k < list.size(); ++k)
        {
            if (k == 0)
            {
                list.set(k, "\u00a7" + Integer.toHexString(itemStack.getRarity().rarityColor) + (String) list.get(k));
            }
            else
            {
                list.set(k, EnumChatFormatting.GRAY + (String) list.get(k));
            }
        }

        FontRenderer font = itemStack.getItem().getFontRenderer(itemStack);
        drawHText(list, par2, par3, font == null ? fontRenderer : font);
    }

    @Override
    public void drawScreen(int par1, int par2, float par3)
    {
        ((GuiButton) buttonList.get(2)).drawButton = popUpState;
        ((GuiButton) buttonList.get(3)).drawButton = popUpState;

        if (popUpState)
        {
            drawBackground(0);

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            mc.renderEngine.bindTexture(Reference.GUI_LOCATION + "portalModifierNetwork.png");
            int x = (width - xSize) / 2;
            int y = (height - ySize) / 2;
            drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

            fontRenderer.drawString(Localization.localizeString("tile.dialDevice.name"), guiLeft + xSize / 2 - fontRenderer.getStringWidth(Localization.localizeString("tile.dialDevice.name")) / 2, 20, 0xFFCCCCCC);
            fontRenderer.drawString(Localization.localizeString("gui.glyphs.title"), guiLeft + 7, guiTop + 6, 0xFF444444);
            fontRenderer.drawString(Localization.localizeString("gui.network.title"), guiLeft + 7, guiTop + 74, 0xFF444444);

            ((GuiButton) buttonList.get(2)).drawButton(mc, par1, par2);
            ((GuiButton) buttonList.get(3)).drawButton(mc, par1, par2);

            x = par1;
            y = par2;

            for (int i = stackList.size() - 1; i >= 0; i--)
            {
                stackList.get(i).drawElement(guiLeft + 8 + i * 18, guiTop + 86, x, y, fontRenderer, itemRenderer, mc.renderEngine);
            }

            for (int i = 8; i >= 0; i--)
            {
                elementList.get(i).drawElement(guiLeft, guiTop + 3, x, y, fontRenderer, itemRenderer, mc.renderEngine);
            }

            for (int i = 17; i >= 9; i--)
            {
                elementList.get(i).drawElement(guiLeft, guiTop + 3, x, y, fontRenderer, itemRenderer, mc.renderEngine);
            }

            for (int i = elementList.size() - 1; i >= 18; i--)
            {
                elementList.get(i).drawElement(guiLeft, guiTop + 3, x, y, fontRenderer, itemRenderer, mc.renderEngine);
            }
        }
        else
        {
            super.drawScreen(par1, par2, par3);
            nameField.drawTextBox();
            List<String> theList = new ArrayList<String>();

            if (isPointInRegion(8, 40, 16, 16, par1, par2))
            {
                theList.add("Particles");
                theList.add(EnumChatFormatting.GRAY + (particles ? "Active" : "Inactive"));
                theList.add(EnumChatFormatting.DARK_GRAY + "The modifier must have");
                theList.add(EnumChatFormatting.DARK_GRAY + "the upgrade installed");
                drawHText(theList, par1, par2, fontRenderer);
            }
            else if (isPointInRegion(8 + 18, 40, 16, 16, par1, par2))
            {
                theList.clear();
                theList.add("Sounds");
                theList.add(EnumChatFormatting.GRAY + (sounds ? "Active" : "Inactive"));
                theList.add(EnumChatFormatting.DARK_GRAY + "The modifier must have");
                theList.add(EnumChatFormatting.DARK_GRAY + "the upgrade installed");
                drawHText(theList, par1, par2, fontRenderer);
            }
            else if (isPointInRegion(134, 40, 16, 16, par1, par2))
            {
                theList.clear();
                theList.add("Thickness");
                theList.add(EnumChatFormatting.GRAY + (thickness == 0 ? "Normal" : thickness == 1 ? "Thick" : thickness == 2 ? "Thicker" : thickness == 3 ? "Full Block" : "Unknown"));
                drawHText(theList, par1, par2, fontRenderer);
            }
            else if (isPointInRegion(134 + 18, 40, 16, 16, par1, par2))
            {
                theList.clear();
                theList.add("Facade");

                if (Textures.getItemStackFromTexture(texture) != null)
                {
                    ItemStack stack = Textures.getItemStackFromTexture(texture);

                    if (stack.itemID == BlockIds.DummyPortal)
                    {
                        theList.add(EnumChatFormatting.GRAY + Localization.localizeString("gui.portalColour." + ItemDye.dyeColorNames[stack.getItemDamage()]));
                    }
                    else
                    {
                        theList.add(EnumChatFormatting.GRAY + Localization.localizeString(stack.getItemName() + ".name"));
                    }
                }
                else
                {
                    theList.add(EnumChatFormatting.GRAY + "Unknown");
                }

                theList.add(EnumChatFormatting.DARK_GRAY + "Shift-click on a block to change");
                drawHText(theList, par1, par2, fontRenderer);
            }
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

        buttonList.add(new GuiButton(1, guiLeft + xSize - 82, guiTop + ySize + 5, 75, 20, "Save"));
        buttonList.add(new GuiButton(2, guiLeft + 7, guiTop + ySize + 5, 75, 20, "Cancel"));

        buttonList.add(new GuiButton(3, guiLeft + 7 + 3, guiTop + ySize - 55, 75, 20, "Cancel"));
        buttonList.add(new GuiButton(4, guiLeft + 87 + 3, guiTop + ySize - 55, 75, 20, "Save"));

        nameField = new GuiTextField(fontRenderer, guiLeft, guiTop, xSize, 16);

        if (name == null)
        {
            nameField.setText("Name");
        }
        else
        {
            nameField.setText(name);
        }
    }

    @Override
    protected void keyTyped(char par1, int par2)
    {
        if (!popUpState)
        {
            if (par1 != ';')
            {
                if (nameField.textboxKeyTyped(par1, par2))
                {
                    name = nameField.getText();
                    return;
                }
            }
        }
        else
        {
            if (par2 == 1 || par2 == mc.gameSettings.keyBindInventory.keyCode)
            {
                popUpState = false;
                return;
            }
        }

        super.keyTyped(par1, par2);
    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3)
    {
        if (!popUpState)
        {
            if (isShiftKeyDown() && getSlotAtPosition(par1, par2) != null)
            {
                PortalTexture Text = Textures.getTextureFromItemStack(getSlotAtPosition(par1, par2).decrStackSize(0));

                if (Text != null)
                {
                    String text = Text.getID();

                    if (texture.equals(text))
                    {
                        text = Textures.getTextureFromItemStack(getSlotAtPosition(par1, par2).decrStackSize(0), texture).getID();
                    }

                    texture = text;
                }
            }

            super.mouseClicked(par1, par2, par3);
            nameField.mouseClicked(par1, par2, par3);

            if (isPointInRegion(8, 40, 16, 16, par1, par2))
            {
                particles = !particles;
            }
            else if (isPointInRegion(8 + 18, 40, 16, 16, par1, par2))
            {
                sounds = !sounds;
            }
            else if (isPointInRegion(134, 40, 16, 16, par1, par2))
            {
                thickness++;

                if (thickness > 3)
                {
                    thickness = 0;
                }
            }
            else if (isPointInRegion(7, 40 + 21, xSize - 15, 17, par1, par2))
            {
                popUpState = true;
            }
            else if (isPointInRegion(134 + 18, 40, 16, 16, par1, par2))
            {
                if (par3 == 1)
                {
                    texture = Textures.getTexture("").getID();
                }
            }
        }
        else
        {
            if (isPointInRegion(10, ySize - 55, 75, ySize - 35, par1, par2))
            {
                popUpState = false;
            }
            else if (isPointInRegion(10 + 80, ySize - 55, 75, ySize - 35, par1, par2))
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

                network = str;
                popUpState = false;
            }

            for (int i = 0; i < elementList.size(); i++)
            {
                elementList.get(i).handleMouseClick(guiLeft, guiTop, par1, par2, par3);
            }

            for (int i = 0; i < stackList.size(); i++)
            {
                stackList.get(i).handleMouseClick(guiLeft + 8 + i * 18, guiTop + 83, par1, par2, par3);
            }
        }
    }

    @Override
    public void updateScreen()
    {
        if (!popUpState)
        {
            super.updateScreen();

            nameField.updateCursorCounter();
        }
    }
}
