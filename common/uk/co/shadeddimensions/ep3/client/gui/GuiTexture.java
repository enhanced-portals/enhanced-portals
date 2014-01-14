package uk.co.shadeddimensions.ep3.client.gui;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import uk.co.shadeddimensions.ep3.client.gui.elements.ElementGlyph;
import uk.co.shadeddimensions.ep3.client.gui.elements.ElementIconToggleButton;
import uk.co.shadeddimensions.ep3.container.ContainerTexture;
import uk.co.shadeddimensions.ep3.lib.GUIs;
import uk.co.shadeddimensions.ep3.lib.Localization;
import uk.co.shadeddimensions.ep3.network.ClientProxy;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileDiallingDevice;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;
import uk.co.shadeddimensions.ep3.util.GuiPayload;
import uk.co.shadeddimensions.ep3.util.PortalTextureManager;
import uk.co.shadeddimensions.library.gui.GuiBase;
import uk.co.shadeddimensions.library.gui.button.GuiBetterSlider;
import uk.co.shadeddimensions.library.gui.button.GuiRGBSlider;
import uk.co.shadeddimensions.library.gui.element.ElementBase;
import uk.co.shadeddimensions.library.gui.element.ElementFakeItemSlot;
import uk.co.shadeddimensions.library.gui.element.ElementScrollBar;
import uk.co.shadeddimensions.library.gui.element.ElementScrollPanelOverlay;
import uk.co.shadeddimensions.library.gui.element.ElementText;
import uk.co.shadeddimensions.library.gui.tab.TabToggleButton;

public class GuiTexture extends GuiBase
{
    /*class ColourTab extends TabBase
    {
        public ColourTab(GuiBase gui)
        {
            super(gui);
            backgroundColor = 0x5396da;
            maxHeight += 90;
        }

        @Override
        public void draw()
        {
            drawBackground();
            drawIcon(ItemPaintbrush.texture, posX + 2, posY + 4, 1);

            if (isFullyOpened())
            {
                fontRenderer.drawStringWithShadow(Localization.getGuiString("colour"), posX + 24, posY + 7, 0xe1c92f);
            }

            redSlider.drawButton = greenSlider.drawButton = blueSlider.drawButton = colourSaveButton.drawButton = colourResetButton.drawButton = isFullyOpened();
        }

        @Override
        public boolean handleMouseClicked(int x, int y, int mouseButton)
        {
            if (isFullyOpened() && mouseButton == 0)
            {
                return true;
            }

            return false;
        }
    }*/

    TilePortalController controller;
    TileDiallingDevice dial;
    int screenState;
    boolean editController;
    GuiButton colourResetButton, colourSaveButton, mainCancelButton, mainSaveButton;
    GuiRGBSlider redSlider, greenSlider, blueSlider;

    //ColourTab colourTab;

    ElementFakeItemSlot fakeItem;
    ElementScrollPanelOverlay frameList, portalList, particleList;

    public GuiTexture(TileDiallingDevice t, EntityPlayer p, int startScreen, boolean editControl)
    {
        super(new ContainerTexture(t.getPortalController(), p), new ResourceLocation("enhancedportals", "textures/gui/colourInterface.png"));
        ySize += 10;
        controller = t.getPortalController();
        screenState = startScreen;
        editController = editControl;
        dial = t;
    }

    public GuiTexture(TilePortalController t, EntityPlayer p, int startScreen, boolean editControl)
    {
        super(new ContainerTexture(t, p), new ResourceLocation("enhancedportals", "textures/gui/colourInterface.png"));
        ySize += 10;
        controller = t;
        screenState = startScreen;
        editController = editControl;
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == colourSaveButton.id)
        {
            int hex = Integer.parseInt(String.format("%02x%02x%02x", redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue()), 16);

            if (screenState == 0)
            {
                getPortalTextureManager().setFrameColour(hex);
            }
            else if (screenState == 1)
            {
                getPortalTextureManager().setPortalColour(hex);
            }
            else if (screenState == 2)
            {
                getPortalTextureManager().setParticleColour(hex);
            }

            if (editController)
            {
                GuiPayload payload = new GuiPayload();
                payload.data.setInteger((screenState == 0 ? "frame" : screenState == 1 ? "portal" : "particle") + "Colour", hex);
                ClientProxy.sendGuiPacket(payload);
            }
        }
        else if (button.id == colourResetButton.id)
        {
            int colour = 0xffffff;

            if (screenState == 0)
            {
                getPortalTextureManager().setFrameColour(colour);
            }
            else if (screenState == 1)
            {
                getPortalTextureManager().setPortalColour(colour);
            }
            else if (screenState == 2)
            {
                colour = 0x0077D8;
                getPortalTextureManager().setParticleColour(colour);
            }

            Color c = new Color(colour);
            redSlider.sliderValue = c.getRed() / 255f;
            greenSlider.sliderValue = c.getGreen() / 255f;
            blueSlider.sliderValue = c.getBlue() / 255f;

            if (editController)
            {
                GuiPayload payload = new GuiPayload();
                payload.data.setInteger((screenState == 0 ? "frame" : screenState == 1 ? "portal" : "particle") + "Colour", Integer.parseInt(String.format("%02x%02x%02x", redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue()), 16));
                ClientProxy.sendGuiPacket(payload);
            }
        }
        else if (!editController)
        {
            if (button.id == mainCancelButton.id)
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
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y)
    {
        super.drawGuiContainerBackgroundLayer(f, x, y);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y)
    {
        fontRenderer.drawString(Localization.getGuiString("customIcon"), 8, 6, 0x404040);
        fontRenderer.drawString(Localization.getGuiString("facade"), xSize - 28 - fontRenderer.getStringWidth(Localization.getGuiString("facade")), 79, 0x404040);
        super.drawGuiContainerForegroundLayer(x, y);
    }

    private PortalTextureManager getPortalTextureManager()
    {
        return editController ? controller.activeTextureData : ClientProxy.dialEntryTexture;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();

        /* fakeItem = new ElementFakeItemSlot(this, 151, 74);
         fakeItem.setVisible(screenState != 2);
         fakeItem.setItem(screenState == 0 ? getTextureManager().getFrameItem() : getTextureManager().getPortalItem());

         frameList = new ElementIconScrollList(this, texture, 6, 18, 180, 54, ClientProxy.customFrameTextures);
         frameList.setVisible(screenState == 0);
         frameList.setSelected(getTextureManager().getCustomFrameTexture());

         portalList = new ElementIconScrollList(this, texture, 6, 18, 180, 54, ClientProxy.customPortalTextures);
         portalList.setVisible(screenState == 1);
         portalList.setSelected(getTextureManager().getCustomPortalTexture());

         particleList = new ElementParticleScrollList(this, texture, 6, 18, 180, 54, ClientProxy.particleSets);
         particleList.setVisible(screenState == 2);
         particleList.setSelected(getTextureManager().getParticleType());

         frameTab = new ElementFakeTab(this, -22, 5, Localization.getGuiString("frame"), new ItemStack(CommonProxy.blockFrame), screenState == 0);
         portalTab = new ElementFakeTab(this, -22, 26, Localization.getGuiString("portal"), new ItemStack(CommonProxy.blockPortal), screenState == 1);
         particleTab = new ElementFakeTab(this, -22, 47, Localization.getGuiString("particle"), new ItemStack(Item.blazePowder), screenState == 2);

         addElement(frameList);
         addElement(portalList);
         addElement(particleList);
         addElement(fakeItem);
         addElement(frameTab);
         addElement(portalTab);
         addElement(particleTab);

         colourTab = new ColourTab(this);
         addTab(colourTab);

         // Sliders
         Color c = new Color(screenState == 0 ? getTextureManager().getFrameColour() : screenState == 1 ? getTextureManager().getPortalColour() : getTextureManager().getParticleColour());
         redSlider = new GuiRGBSlider(100, guiLeft + xSize + 5, guiTop + 27, Localization.getGuiString("red"), c.getRed() / 255f);
         greenSlider = new GuiRGBSlider(101, guiLeft + xSize + 5, guiTop + 48, Localization.getGuiString("green"), c.getGreen() / 255f);
         blueSlider = new GuiRGBSlider(102, guiLeft + xSize + 5, guiTop + 69, Localization.getGuiString("blue"), c.getBlue() / 255f);

         buttonList.add(redSlider);
         buttonList.add(greenSlider);
         buttonList.add(blueSlider);*/

        // Buttons
        //colourSaveButton = new GuiButton(110, guiLeft + xSize + 5, guiTop + 90, 51, 20, Localization.getGuiString("save"));
        //colourResetButton = new GuiButton(111, guiLeft + xSize + 68, guiTop + 90, 51, 20, Localization.getGuiString("reset"));

        //buttonList.add(colourSaveButton);
        //buttonList.add(colourResetButton);

        if (!editController)
        {
            mainCancelButton = new GuiButton(10, guiLeft, guiTop + ySize + 3, 75, 20, Localization.getGuiString("cancel"));
            mainSaveButton = new GuiButton(11, guiLeft + xSize - 75, guiTop + ySize + 3, 75, 20, Localization.getGuiString("save"));

            buttonList.add(mainCancelButton);
            buttonList.add(mainSaveButton);
        }

        /*redSlider.drawButton = greenSlider.drawButton = blueSlider.drawButton = colourSaveButton.drawButton = colourResetButton.drawButton = colourTab.isFullyOpened();*/
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

        frameList.addElement(new ElementText(this, 5, 2, "Frame", null));

        // add portal elements
        int x = 8, y = 0, count = 0;

        for (Icon i : ClientProxy.customPortalTextures)
        {
            ElementIconToggleButton button = new ElementIconToggleButton(this, x, y, "P" + count, i);
            button.setSelected(getPortalTextureManager().hasCustomPortalTexture() && getPortalTextureManager().getCustomPortalTexture() == count);

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

        particleList.addElement(new ElementText(this, 5, 2, "Random", "Particle icon will be randomly chosen from their set", 0xFFFFFF, true));

        for (int i = 0; i < 6; i++)
        {
            particleList.addElement(new ElementGlyph(this, 5 + i * 18, 12, i));
        }

        particleList.addElement(new ElementText(this, 5, 32, "Sequential", "Particles will go through their sequence once", 0xFFFFFF, true));

        for (int i = 0; i < 6; i++)
        {
            particleList.addElement(new ElementGlyph(this, 5 + i * 18, 42, i));
        }

        particleList.addElement(new ElementText(this, 5, 62, "Loop", "Particles will loop through their sequence", 0xFFFFFF, true));

        for (int i = 0; i < 6; i++)
        {
            particleList.addElement(new ElementGlyph(this, 5 + i * 18, 72, i));
        }

        particleList.addElement(new ElementText(this, 5, 92, "Static", "Particles will not change icon", 0xFFFFFF, true));

        for (int i = 0; i < 6; i++)
        {
            particleList.addElement(new ElementGlyph(this, 5 + i * 18, 102, i));
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
                        b.setSelected(true);

                        GuiPayload payload = new GuiPayload();
                        payload.data.setInteger("customPortalTexture", Integer.parseInt(buttonName.replace("P", "")));
                        ClientProxy.sendGuiPacket(payload);
                    }
                    else
                    {
                        b.setSelected(false);
                    }
                }
            }
        }
    }

    /*@Override
    public void onElementChanged(ElementBase element, Object data)
    {
        if (element instanceof ElementIconScrollList)
        {
            GuiPayload payload = null;

            if (screenState == 0)
            {
                getTextureManager().setCustomFrameTexture(Integer.parseInt(data.toString()));

                payload = new GuiPayload();
                payload.data.setInteger("customFrameTexture", Integer.parseInt(data.toString()));
            }
            else if (screenState == 1)
            {
                getTextureManager().setCustomPortalTexture(Integer.parseInt(data.toString()));

                payload = new GuiPayload();
                payload.data.setInteger("customPortalTexture", Integer.parseInt(data.toString()));
            }
            else if (screenState == 2)
            {
                getTextureManager().setParticleType(Integer.parseInt(data.toString()));

                payload = new GuiPayload();
                payload.data.setInteger("particleType", Integer.parseInt(data.toString()));
            }

            if (payload != null)
            {
                ClientProxy.sendGuiPacket(payload);
            }
        }
        else if (element instanceof ElementFakeItemSlot)
        {
            GuiPayload payload = null;

            if (screenState == 0)
            {
                ItemStack s = (ItemStack) data;
                payload = new GuiPayload();

                getTextureManager().setFrameItem(s);

                if (s == null)
                {
                    payload.data.setInteger("frameItemID", 0);
                    payload.data.setInteger("frameItemMeta", 0);
                }
                else
                {
                    payload.data.setInteger("frameItemID", s.itemID);
                    payload.data.setInteger("frameItemMeta", s.getItemDamage());
                }
            }
            else if (screenState == 1)
            {
                ItemStack s = (ItemStack) data;
                payload = new GuiPayload();

                getTextureManager().setPortalItem(s);

                if (s == null)
                {
                    payload.data.setInteger("portalItemID", 0);
                    payload.data.setInteger("portalItemMeta", 0);
                }
                else
                {
                    payload.data.setInteger("portalItemID", s.itemID);
                    payload.data.setInteger("portalItemMeta", s.getItemDamage());
                }
            }

            if (payload != null)
            {
                ClientProxy.sendGuiPacket(payload);
            }
        }
        else if (element instanceof ElementFakeTab)
        {
            if (element.equals(frameTab))
            {
                setScreenState(0);
            }
            else if (element.equals(portalTab))
            {
                setScreenState(1);
            }
            if (element.equals(particleTab))
            {
                setScreenState(2);
            }
        }
    }*/

    private void setScreenState(int state)
    {
        screenState = state;

        tabs.get(screenState).setFullyOpen();

        fakeItem.setDisabled(screenState == 2);
        frameList.setVisible(screenState == 0);
        portalList.setVisible(screenState == 1);
        particleList.setVisible(screenState == 2);

        fakeItem.setItem(screenState == 0 ? getPortalTextureManager().getFrameItem() : getPortalTextureManager().getPortalItem());

        //Color c = new Color(screenState == 0 ? getPortalTextureManager().getFrameColour() : screenState == 1 ? getPortalTextureManager().getPortalColour() : getPortalTextureManager().getParticleColour());
        //redSlider.sliderValue = c.getRed() / 255f;
        //greenSlider.sliderValue = c.getGreen() / 255f;
        //blueSlider.sliderValue = c.getBlue() / 255f;
    }
}
