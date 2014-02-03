package uk.co.shadeddimensions.ep3.client.gui;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidContainerRegistry;
import uk.co.shadeddimensions.ep3.client.gui.elements.ElementIconToggleButton;
import uk.co.shadeddimensions.ep3.client.gui.elements.ElementParticleToggleButton;
import uk.co.shadeddimensions.ep3.container.ContainerTexture;
import uk.co.shadeddimensions.ep3.item.ItemPaintbrush;
import uk.co.shadeddimensions.ep3.lib.GUIs;
import uk.co.shadeddimensions.ep3.lib.Localization;
import uk.co.shadeddimensions.ep3.network.ClientProxy;
import uk.co.shadeddimensions.ep3.network.ClientProxy.ParticleSet;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileDiallingDevice;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;
import uk.co.shadeddimensions.ep3.util.GuiPayload;
import uk.co.shadeddimensions.library.gui.GuiBaseContainer;
import uk.co.shadeddimensions.library.gui.button.GuiBetterSlider;
import uk.co.shadeddimensions.library.gui.button.GuiRGBSlider;
import uk.co.shadeddimensions.library.gui.element.ElementBase;
import uk.co.shadeddimensions.library.gui.element.ElementClickBlocker;
import uk.co.shadeddimensions.library.gui.element.ElementFakeItemSlot;
import uk.co.shadeddimensions.library.gui.element.ElementScrollBar;
import uk.co.shadeddimensions.library.gui.element.ElementScrollPanelOverlay;
import uk.co.shadeddimensions.library.gui.element.ElementText;
import uk.co.shadeddimensions.library.gui.tab.TabBase;
import uk.co.shadeddimensions.library.gui.tab.TabToggleButton;

public class GuiTextureDialler extends GuiBaseContainer
{
    class ColourTab extends TabBase
    {
        public ColourTab(GuiBaseContainer gui)
        {
            super(gui);
            backgroundColor = 0x5396da;
            maxHeight += 90;
            name = Localization.getGuiString("colour");
            icon = ItemPaintbrush.texture;
            
            addElement(new ElementClickBlocker(gui, 3, 21, maxWidth - 7, maxHeight - 25));
        }

        @Override
        public void draw()
        {
            super.draw();
            redSlider.drawButton = greenSlider.drawButton = blueSlider.drawButton = colourSaveButton.drawButton = colourResetButton.drawButton = isFullyOpened();
        }
    }

    TilePortalController controller;
    TileDiallingDevice dial;
    int screenState;
    GuiButton colourResetButton, colourSaveButton, mainCancelButton, mainSaveButton;
    GuiRGBSlider redSlider, greenSlider, blueSlider;

    ElementFakeItemSlot fakeItem;
    ElementScrollPanelOverlay frameList, portalList, particleList;

    public GuiTextureDialler(TileDiallingDevice d, EntityPlayer p)
    {
        super(new ContainerTexture(d.getPortalController(), p), new ResourceLocation("enhancedportals", "textures/gui/colourInterface.png"));
        ySize += 10;
        controller = d.getPortalController();
        dial = d;
        screenState = 0;
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == colourSaveButton.id)
        {
            int hex = Integer.parseInt(String.format("%02x%02x%02x", redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue()), 16);

            if (screenState == 0)
            {
                ClientProxy.dialEntryTexture.setFrameColour(hex);
            }
            else if (screenState == 1)
            {
                ClientProxy.dialEntryTexture.setPortalColour(hex);
            }
            else if (screenState == 2)
            {
                ClientProxy.dialEntryTexture.setParticleColour(hex);
            }
        }
        else if (button.id == colourResetButton.id)
        {
            int colour = 0xffffff;

            if (screenState == 0)
            {
                ClientProxy.dialEntryTexture.setFrameColour(colour);
            }
            else if (screenState == 1)
            {
                ClientProxy.dialEntryTexture.setPortalColour(colour);
            }
            else if (screenState == 2)
            {
                colour = 0x0077D8;
                ClientProxy.dialEntryTexture.setParticleColour(colour);
            }

            Color c = new Color(colour);
            redSlider.sliderValue = c.getRed() / 255f;
            greenSlider.sliderValue = c.getGreen() / 255f;
            blueSlider.sliderValue = c.getBlue() / 255f;
        }
        else if (button.id == mainCancelButton.id)
        {
            CommonProxy.openGui(Minecraft.getMinecraft().thePlayer, GUIs.DiallingDevice, dial);
        }
        else if (button.id == mainSaveButton.id)
        {
            if (ClientProxy.editingDialEntry == -1)
            {
                return;
            }

            GuiPayload payload = new GuiPayload();
            payload.data.setInteger("SetDialTexture", ClientProxy.editingDialEntry);
            ClientProxy.dialEntryTexture.writeToNBT(payload.data, "TextureData");
            ClientProxy.sendGuiPacket(payload);
            ClientProxy.editingDialEntry = -1;
            CommonProxy.openGui(Minecraft.getMinecraft().thePlayer, GUIs.DiallingDevice, dial);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y)
    {
        fontRenderer.drawString(Localization.getGuiString("customIcon"), 8, 6, 0x404040);
        fontRenderer.drawString(Localization.getGuiString("facade"), xSize - 28 - fontRenderer.getStringWidth(Localization.getGuiString("facade")), 79, 0x404040);
        super.drawGuiContainerForegroundLayer(x, y);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();

        Color c = new Color(screenState == 0 ? ClientProxy.dialEntryTexture.getFrameColour() : screenState == 1 ? ClientProxy.dialEntryTexture.getPortalColour() : ClientProxy.dialEntryTexture.getParticleColour());
        redSlider = new GuiRGBSlider(100, guiLeft + xSize + 5, guiTop + 27, Localization.getGuiString("red"), c.getRed() / 255f);
        greenSlider = new GuiRGBSlider(101, guiLeft + xSize + 5, guiTop + 48, Localization.getGuiString("green"), c.getGreen() / 255f);
        blueSlider = new GuiRGBSlider(102, guiLeft + xSize + 5, guiTop + 69, Localization.getGuiString("blue"), c.getBlue() / 255f);

        buttonList.add(redSlider);
        buttonList.add(greenSlider);
        buttonList.add(blueSlider);

        colourSaveButton = new GuiButton(110, guiLeft + xSize + 5, guiTop + 90, 51, 20, Localization.getGuiString("save"));
        colourResetButton = new GuiButton(111, guiLeft + xSize + 68, guiTop + 90, 51, 20, Localization.getGuiString("reset"));

        buttonList.add(colourSaveButton);
        buttonList.add(colourResetButton);

        mainCancelButton = new GuiButton(10, guiLeft, guiTop + ySize + 3, 75, 20, Localization.getGuiString("cancel"));
        mainSaveButton = new GuiButton(11, guiLeft + xSize - 75, guiTop + ySize + 3, 75, 20, Localization.getGuiString("save"));

        buttonList.add(mainCancelButton);
        buttonList.add(mainSaveButton);

        redSlider.drawButton = greenSlider.drawButton = blueSlider.drawButton = colourSaveButton.drawButton = colourResetButton.drawButton = tabs.get(tabs.size() - 1).isFullyOpened();
        setScreenState(screenState);
    }

    @Override
    public void addElements()
    {
        particleList = new ElementScrollPanelOverlay(this, 0, 18, xSize - 16, 54, texture, 18);
        frameList = new ElementScrollPanelOverlay(this, 0, 18, xSize - 16, 54, texture, 18);
        portalList = new ElementScrollPanelOverlay(this, 0, 18, xSize - 16, 54, texture, 18);

        particleList.setSides(true, true, false, false);
        frameList.setSides(true, true, false, false);
        portalList.setSides(true, true, false, false);

        // add frame elements

        int x = 8, y = 0, count = 0;

        for (Icon i : ClientProxy.customFrameTextures)
        {
            ElementIconToggleButton button = new ElementIconToggleButton(this, x, y, "F" + count, i);
            button.setSelected(ClientProxy.dialEntryTexture.hasCustomFrameTexture() && ClientProxy.dialEntryTexture.getCustomFrameTexture() == count);

            count++;
            x += button.getWidth();

            if (x >= frameList.getWidth() - 20)
            {
                x = 8;
                y += button.getHeight();
            }

            frameList.addElement(button);
        }

        if (count == 0)
        {
            frameList.addElement(new ElementText(this, 10, 6, "No custom icons found", null));
        }

        // add portal elements
        x = 8;
        y = 0;
        count = 0;

        for (Icon i : ClientProxy.customPortalTextures)
        {
            ElementIconToggleButton button = new ElementIconToggleButton(this, x, y, "P" + count, i);
            button.setSelected(ClientProxy.dialEntryTexture.hasCustomPortalTexture() && ClientProxy.dialEntryTexture.getCustomPortalTexture() == count);

            count++;
            x += button.getWidth();

            if (x >= portalList.getWidth() - 20)
            {
                x = 8;
                y += button.getHeight();
            }

            portalList.addElement(button);
        }

        // add particle elements

        x = 8;
        y = 0;
        count = 0;
        int type = -1;

        for (ParticleSet set : ClientProxy.particleSets)
        {
            if (set.type != type)
            {
                // add a new 'category'
                x = 8;
                y += type != -1 ? 24 : 2;

                type = set.type;
                particleList.addElement(new ElementText(this, x, y, Localization.getGuiString("particleType." + type + ".name"), Localization.getGuiString("particleType." + type + ".info"), 0xFFFFFF, true));

                x = 8;
                y += 10;
            }

            ElementParticleToggleButton button = new ElementParticleToggleButton(this, x, y, "X" + count, set);
            button.setSelected(ClientProxy.dialEntryTexture.getParticleType() == count);

            count++;
            x += button.getWidth();

            if (x >= particleList.getWidth() - 20)
            {
                x = 8;
                y += button.getHeight();
            }

            particleList.addElement(button);
        }

        // end

        fakeItem = new ElementFakeItemSlot(this, 151, 74, null);

        addElement(frameList);
        addElement(portalList);
        addElement(particleList);
        addElement(fakeItem);

        addElement(new ElementScrollBar(this, xSize - 15, 18, 10, 55, frameList));
        addElement(new ElementScrollBar(this, xSize - 15, 18, 10, 55, portalList));
        addElement(new ElementScrollBar(this, xSize - 15, 18, 10, 55, particleList));
    }

    @Override
    public void addTabs()
    {
        addTab(new TabToggleButton(this, "frame", Localization.getGuiString("frame"), new ItemStack(CommonProxy.blockFrame, 1, 0)));
        addTab(new TabToggleButton(this, "portal", Localization.getGuiString("portal"), new ItemStack(CommonProxy.blockPortal)));
        addTab(new TabToggleButton(this, "particle", Localization.getGuiString("particle"), new ItemStack(Item.blazePowder)));
        addTab(new ColourTab(this));
    }

    @Override
    protected void mouseMovedOrUp(int par1, int par2, int par3)
    {
        super.mouseMovedOrUp(par1, par2, par3);

        if (par3 == 0)
        {
            for (Object o : buttonList)
            {
                if (o instanceof GuiBetterSlider)
                {
                    GuiBetterSlider slider = (GuiBetterSlider) o;
                    slider.mouseReleased(par1, par2);
                }
            }
        }
    }

    @Override
    public void handleElementFakeSlotItemChange(ElementFakeItemSlot slot)
    {
        ItemStack s = slot.getStack();

        if (screenState == 0)
        {
            ClientProxy.dialEntryTexture.setFrameItem(s);
        }
        else if (screenState == 1)
        {
            ClientProxy.dialEntryTexture.setPortalItem(s);
        }
    }

    @Override
    public void handleElementButtonClick(String buttonName, int mouseButton)
    {
        if (buttonName.equals("frame"))
        {
            setScreenState(0);
        }
        else if (buttonName.equals("portal"))
        {
            setScreenState(1);
        }
        else if (buttonName.equals("particle"))
        {
            setScreenState(2);
        }
        else if (buttonName.startsWith("P"))
        {
            for (ElementBase element : portalList.getElements())
            {
                if (element instanceof ElementIconToggleButton)
                {
                    ElementIconToggleButton b = (ElementIconToggleButton) element;

                    if (b.getID().equals(buttonName))
                    {
                        if (mouseButton == 0)
                        {
                            fakeItem.setDisabled(true);
                            b.setSelected(true);
                            ClientProxy.dialEntryTexture.setCustomPortalTexture(Integer.parseInt(buttonName.replace("P", "")));
                        }
                        else
                        {
                            fakeItem.setDisabled(false);
                            b.setSelected(false);
                            ClientProxy.dialEntryTexture.setCustomPortalTexture(-1);
                        }
                    }
                    else
                    {
                        b.setSelected(false);
                    }
                }
            }
        }
        else if (buttonName.startsWith("F"))
        {
            for (ElementBase element : frameList.getElements())
            {
                if (element instanceof ElementIconToggleButton)
                {
                    ElementIconToggleButton b = (ElementIconToggleButton) element;

                    if (b.getID().equals(buttonName))
                    {
                        if (mouseButton == 0)
                        {
                            fakeItem.setDisabled(true);
                            b.setSelected(true);
                            ClientProxy.dialEntryTexture.setCustomFrameTexture(Integer.parseInt(buttonName.replace("F", "")));
                        }
                        else
                        {
                            fakeItem.setDisabled(false);
                            b.setSelected(false);
                            ClientProxy.dialEntryTexture.setCustomFrameTexture(-1);
                        }
                    }
                    else
                    {
                        b.setSelected(false);
                    }
                }
            }
        }
        else if (buttonName.startsWith("X"))
        {
            for (ElementBase element : particleList.getElements())
            {
                if (element instanceof ElementParticleToggleButton)
                {
                    ElementParticleToggleButton b = (ElementParticleToggleButton) element;

                    if (b.getID().equals(buttonName))
                    {
                        if (mouseButton == 0)
                        {
                            b.setSelected(true);
                            ClientProxy.dialEntryTexture.setParticleType(Integer.parseInt(buttonName.replace("X", "")));
                        }
                        else
                        {
                            b.setSelected(false);
                            ClientProxy.dialEntryTexture.setParticleType(0);
                            ((ElementParticleToggleButton) particleList.getElements().get(1)).setSelected(true);
                        }
                    }
                    else
                    {
                        b.setSelected(false);
                    }
                }
            }
        }
    }

    @Override
    public boolean isItemStackAllowedInFakeSlot(ElementFakeItemSlot slot, ItemStack stack)
    {
        return stack == null || stack.getItem() instanceof ItemBlock || FluidContainerRegistry.isFilledContainer(stack);
    }

    private void setScreenState(int state)
    {
        screenState = state;

        tabs.get(screenState).setFullyOpen();
        frameList.setVisible(screenState == 0);
        portalList.setVisible(screenState == 1);
        particleList.setVisible(screenState == 2);

        fakeItem.setItem(screenState == 0 ? ClientProxy.dialEntryTexture.getFrameItem() : ClientProxy.dialEntryTexture.getPortalItem());
        fakeItem.setDisabled(screenState == 2 || screenState == 0 && ClientProxy.dialEntryTexture.hasCustomFrameTexture() || screenState == 1 && ClientProxy.dialEntryTexture.hasCustomPortalTexture());

        Color c = new Color(screenState == 0 ? ClientProxy.dialEntryTexture.getFrameColour() : screenState == 1 ? ClientProxy.dialEntryTexture.getPortalColour() : ClientProxy.dialEntryTexture.getParticleColour());
        redSlider.sliderValue = c.getRed() / 255f;
        greenSlider.sliderValue = c.getGreen() / 255f;
        blueSlider.sliderValue = c.getBlue() / 255f;
    }
}
