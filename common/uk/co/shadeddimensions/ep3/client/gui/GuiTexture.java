package uk.co.shadeddimensions.ep3.client.gui;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import uk.co.shadeddimensions.ep3.client.gui.element.ElementFakeItemSlot;
import uk.co.shadeddimensions.ep3.client.gui.element.ElementFakeTab;
import uk.co.shadeddimensions.ep3.client.gui.element.ElementIconScrollList;
import uk.co.shadeddimensions.ep3.client.gui.element.ElementParticleScrollList;
import uk.co.shadeddimensions.ep3.client.gui.slider.GuiBetterSlider;
import uk.co.shadeddimensions.ep3.client.gui.slider.GuiRGBSlider;
import uk.co.shadeddimensions.ep3.container.ContainerTexture;
import uk.co.shadeddimensions.ep3.item.ItemPaintbrush;
import uk.co.shadeddimensions.ep3.lib.GUIs;
import uk.co.shadeddimensions.ep3.lib.Reference;
import uk.co.shadeddimensions.ep3.network.ClientProxy;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;
import uk.co.shadeddimensions.ep3.util.GuiPayload;
import uk.co.shadeddimensions.ep3.util.PortalTextureManager;
import cofh.gui.GuiBase;
import cofh.gui.element.ElementBase;
import cofh.gui.element.TabBase;

public class GuiTexture extends GuiBase implements IElementHandler
{    
    class ColourTab extends TabBase
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
                fontRenderer.drawStringWithShadow("Colour", posX + 24, posY + 7, 0xe1c92f);
            }

            redSlider.drawButton = greenSlider.drawButton = blueSlider.drawButton = colourSaveButton.drawButton = colourResetButton.drawButton = isFullyOpened();
        }
        
        @Override
        public boolean handleMouseClicked(int x, int y, int mouseButton)
        {
            if (y > posY - 10)
            {
                return true;
            }
            
            return false;
        }

        @Override
        public String getTooltip()
        {
            return null;
        }
    }
    
    TilePortalController controller;
    int screenState;
    boolean editController;
    GuiButton colourResetButton, colourSaveButton, mainCancelButton, mainSaveButton;
    GuiRGBSlider redSlider, greenSlider, blueSlider;
    ColourTab colourTab;
    
    ElementIconScrollList frameList, portalList, particleList;
    ElementFakeItemSlot fakeItem;
    ElementFakeTab frameTab, portalTab, particleTab;

    public GuiTexture(TilePortalController t, EntityPlayer p, int startScreen, boolean editControl)
    {
        super(new ContainerTexture(t, p), new ResourceLocation("enhancedportals", "textures/gui/colourInterface.png"));
        ySize += 10;
        controller = t;
        screenState = startScreen;
        editController = editControl;
    }

    @SuppressWarnings("unchecked")
    public void initGui()
    {
        super.initGui();

        fakeItem = new ElementFakeItemSlot(this, 151, 74);
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
        
        frameTab = new ElementFakeTab(this, -22, 5, "Frame", new ItemStack(CommonProxy.blockFrame), screenState == 0);
        portalTab = new ElementFakeTab(this, -22, 26, "Portal", new ItemStack(CommonProxy.blockPortal), screenState == 1);
        particleTab = new ElementFakeTab(this, -22, 47, "Particle", new ItemStack(Item.blazePowder), screenState == 2);
        
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
        redSlider = new GuiRGBSlider(100, guiLeft + xSize + 5, guiTop + 27, StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".colour.red"), c.getRed() / 255f);
        greenSlider = new GuiRGBSlider(101, guiLeft + xSize + 5, guiTop + 48, StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".colour.green"), c.getGreen() / 255f);
        blueSlider = new GuiRGBSlider(102, guiLeft + xSize + 5, guiTop + 69, StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".colour.blue"), c.getBlue() / 255f);
        
        buttonList.add(redSlider);
        buttonList.add(greenSlider);
        buttonList.add(blueSlider);
        
        // Buttons
        colourSaveButton = new GuiButton(110, guiLeft + xSize + 5, guiTop + 90, 51, 20, StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".button.save"));
        colourResetButton = new GuiButton(111, guiLeft + xSize + 68, guiTop + 90, 51, 20, StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".button.reset"));
        
        buttonList.add(colourSaveButton);
        buttonList.add(colourResetButton);
        
        if (!editController)
        {
            mainCancelButton = new GuiButton(10, guiLeft, guiTop + ySize + 3, 75, 20, "Cancel");
            mainSaveButton = new GuiButton(11, guiLeft + xSize - 75, guiTop + ySize + 3, 75, 20, "Save");

            buttonList.add(mainCancelButton);
            buttonList.add(mainSaveButton);
        }
        
        redSlider.drawButton = greenSlider.drawButton = blueSlider.drawButton = colourSaveButton.drawButton = colourResetButton.drawButton = colourTab.isFullyOpened();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y)
    {
        super.drawGuiContainerBackgroundLayer(f, x, y);
    }

    protected void drawGuiContainerForegroundLayer(int x, int y)
    {
        fontRenderer.drawString("Custom Icon", 8, 6, 0x404040);
        fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, 82, 0x404040);

        if (screenState != 2)
        {
            fontRenderer.drawString("Facade", xSize - 28 - fontRenderer.getStringWidth("Facade"), 79, 0x404040);
        }
        
        super.drawGuiContainerForegroundLayer(x, y);
    }

    private PortalTextureManager getTextureManager()
    {
        return editController ? controller.activeTextureData : ClientProxy.dialEntryTexture;
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
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == colourSaveButton.id)
        {
            int hex = Integer.parseInt(String.format("%02x%02x%02x", redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue()), 16);

            if (screenState == 0)
            {
                getTextureManager().setFrameColour(hex);
            }
            else if (screenState == 1)
            {
                getTextureManager().setPortalColour(hex);
            }
            else if (screenState == 2)
            {
                getTextureManager().setParticleColour(hex);
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
                getTextureManager().setFrameColour(colour);
            }
            else if (screenState == 1)
            {
                getTextureManager().setPortalColour(colour);
            }
            else if (screenState == 2)
            {
                colour = 0x0077D8;
                getTextureManager().setParticleColour(colour);
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
                ClientProxy.openGui(Minecraft.getMinecraft().thePlayer, GUIs.DiallingDevice, controller);
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
                ClientProxy.openGui(Minecraft.getMinecraft().thePlayer, GUIs.DiallingDevice, controller);
            }
        }
    }
    
    @Override
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
    }
    
    private void setScreenState(int state)
    {
        screenState = state;
        
        frameTab.setActive(state == 0);
        portalTab.setActive(state == 1);
        particleTab.setActive(state == 2);
        fakeItem.setVisible(screenState != 2);
        frameList.setVisible(screenState == 0);
        portalList.setVisible(screenState == 1);
        particleList.setVisible(screenState == 2);
        
        fakeItem.setItem(screenState == 0 ? getTextureManager().getFrameItem() : getTextureManager().getPortalItem());
        
        Color c = new Color(screenState == 0 ? getTextureManager().getFrameColour() : screenState == 1 ? getTextureManager().getPortalColour() : getTextureManager().getParticleColour());
        redSlider.sliderValue = c.getRed() / 255f;
        greenSlider.sliderValue = c.getGreen() / 255f;
        blueSlider.sliderValue = c.getBlue() / 255f;
    }
}
