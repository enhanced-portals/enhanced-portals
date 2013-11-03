package uk.co.shadeddimensions.ep3.client.gui;

import java.awt.Color;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import uk.co.shadeddimensions.ep3.client.gui.base.GuiColourInterface;
import uk.co.shadeddimensions.ep3.client.gui.button.GuiBetterButton;
import uk.co.shadeddimensions.ep3.client.gui.button.StandardButtonTextureSets;
import uk.co.shadeddimensions.ep3.client.gui.elements.GuiFakeItemSlot;
import uk.co.shadeddimensions.ep3.client.gui.elements.GuiIconList;
import uk.co.shadeddimensions.ep3.client.gui.elements.GuiParticleList;
import uk.co.shadeddimensions.ep3.container.ContainerTexture;
import uk.co.shadeddimensions.ep3.lib.GUIs;
import uk.co.shadeddimensions.ep3.lib.Reference;
import uk.co.shadeddimensions.ep3.network.ClientProxy;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;
import uk.co.shadeddimensions.ep3.util.GuiPayload;

public class GuiTexture extends GuiColourInterface
{
    TilePortalController controller;
    int screenState, tabScroll, tabWidth;
    String[] tabText;
    boolean editController;
    
    GuiIconList frameList, portalList;
    GuiParticleList particleList;
    GuiBetterButton backButton, forwardButton, mainCancelButton, mainSaveButton;
    GuiFakeItemSlot frameItem, portalItem;
    
    public GuiTexture(TilePortalController t, EntityPlayer p, int startScreen, boolean editControl)
    {
        super(new ContainerTexture(t, p), t);
        controller = t;
        tabText = new String[] { "Frame", "Portal", "Particles" };
        screenState = startScreen;
        tabScroll = tabWidth = getMinecraft().fontRenderer.getStringWidth(tabText[screenState]) + 10;        
        frameList = new GuiIconList(guiLeft + 6, guiTop + 6, 164, 56, this, false);
        portalList = new GuiIconList(guiLeft + 6, guiTop + 6, 164, 56, this, true);
        particleList = new GuiParticleList(guiLeft + 6, guiTop + 6, 164, 56, this);
        frameItem = new GuiFakeItemSlot(152, 64, null, this);
        portalItem = new GuiFakeItemSlot(152, 64, null, this);
        editController = editControl;
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        super.drawGuiContainerBackgroundLayer(f, i, j);
        
        for (int k = 0; k < 3; k++)
        {
            boolean isActive = screenState == k;
            float colour = isActive ? 1f : 0.6f;
            int offset = isActive ? tabScroll : 0;
            
            drawTabFlipped(guiLeft - 25 - offset, guiTop + 10 + (27 * k), 25 + offset, 26, colour, colour, colour);
            
            if (isActive && tabScroll >= tabWidth)
            {
                fontRenderer.drawString(tabText[k], guiLeft + 2 - offset, guiTop + 19 + (27 * k), 0x000000);
            }
        }
                
        itemRenderer.renderItemAndEffectIntoGUI(getFontRenderer(), getMinecraft().renderEngine, new ItemStack(CommonProxy.blockFrame), guiLeft - 19 - (screenState == 0 ? tabScroll : 0), guiTop + 15);
        itemRenderer.renderItemAndEffectIntoGUI(getFontRenderer(), getMinecraft().renderEngine, new ItemStack(CommonProxy.blockPortal), guiLeft - 19 - (screenState == 1 ? tabScroll : 0), guiTop + 42);
        drawParticle(guiLeft - 19 - (screenState == 2 ? tabScroll : 0), guiTop + 69, 0x0077D8, 7, false);
        
        if (screenState == 0)
        {
            frameList.drawBackground();
            frameItem.drawBackground(i, j);
        }
        else if (screenState == 1)
        {
            portalList.drawBackground();
            portalItem.drawBackground(i, j);
        }
        else if (screenState == 2)
        {
            particleList.drawBackground();
        }
        
        if (tabScroll < tabWidth)
        {
            tabScroll += 2;
        }
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        super.drawGuiContainerForegroundLayer(par1, par2);
        fontRenderer.drawStringWithShadow(StatCollector.translateToLocal("gui." + Reference.SHORT_ID + "." + (screenState == 0 ? "portalFrameTexture" : screenState == 1 ? "portalTexture" : "particleTexture")), xSize / 2 - fontRenderer.getStringWidth(StatCollector.translateToLocal("gui." + Reference.SHORT_ID + "." + (screenState == 0 ? "portalFrameTexture" : screenState == 1 ? "portalTexture" : "particleTexture"))) / 2, -13, 0xFFFFFF);
        fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, 70, 0x404040);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();
        
        frameList.selectedIcon = (editController ? controller.activeTextureData : ClientProxy.dialEntryTexture).getCustomFrameTexture();
        portalList.selectedIcon = (editController ? controller.activeTextureData : ClientProxy.dialEntryTexture).getCustomPortalTexture();
        particleList.selected = (editController ? controller.activeTextureData : ClientProxy.dialEntryTexture).getParticleType();
        
        backButton = new GuiBetterButton(1, guiLeft + xSize - 62, guiTop + 64, 16, StandardButtonTextureSets.BACK_BUTTON, "");
        forwardButton = new GuiBetterButton(2, guiLeft + xSize - 45, guiTop + 64, 16, StandardButtonTextureSets.FORWARD_BUTTON, "");
        buttonList.add(backButton);
        buttonList.add(forwardButton);
        
        backButton.enabled = screenState == 0 ? frameList.page > 0 : screenState == 1 ? portalList.page > 0 : particleList.page > 0;
        forwardButton.enabled = (screenState == 0 ? frameList.page : screenState == 1 ? portalList.page : particleList.page) < ((screenState == 0 ? ClientProxy.customFrameTextures.size() : screenState == 1 ? ClientProxy.customPortalTextures.size() : ClientProxy.particleSets.size()) / 27);
   
        Color c = new Color(screenState == 0 ? (editController ? controller.activeTextureData : ClientProxy.dialEntryTexture).getFrameColour() : screenState == 1 ?(editController ? controller.activeTextureData : ClientProxy.dialEntryTexture).getPortalColour() : (editController ? controller.activeTextureData : ClientProxy.dialEntryTexture).getParticleColour());
        redSlider.sliderValue = c.getRed() / 255f;
        greenSlider.sliderValue = c.getGreen() / 255f;
        blueSlider.sliderValue = c.getBlue() / 255f;
        
        frameItem.stack = (editController ? controller.activeTextureData : ClientProxy.dialEntryTexture).getFrameItem();
        portalItem.stack = (editController ? controller.activeTextureData : ClientProxy.dialEntryTexture).getPortalItem();
        
        if (!editController)
        {
            mainCancelButton = new GuiBetterButton(10, guiLeft, guiTop + ySize + 7, 75, "Cancel");
            mainSaveButton = new GuiBetterButton(11, guiLeft + xSize - 75, guiTop + ySize + 7, 75, "Save");
            
            buttonList.add(mainCancelButton);
            buttonList.add(mainSaveButton);
        }
    }
    
    @Override
    protected void actionPerformed(GuiButton button)
    {
        super.actionPerformed(button);
        
        if (button.id == saveButton.id)
        {
            int hex = Integer.parseInt(String.format("%02x%02x%02x", redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue()), 16);
            
            if (screenState == 0)
            {
                (editController ? controller.activeTextureData : ClientProxy.dialEntryTexture).setFrameColour(hex);
            }
            else if (screenState == 1)
            {
                (editController ? controller.activeTextureData : ClientProxy.dialEntryTexture).setPortalColour(hex);
            }
            else if (screenState == 2)
            {
                (editController ? controller.activeTextureData : ClientProxy.dialEntryTexture).setParticleColour(hex);
            }
            
            if (editController)
            {
                GuiPayload payload = new GuiPayload();
                payload.data.setInteger((screenState == 0 ? "frame" : screenState == 1 ? "portal" : "particle") + "Colour", hex);
                ClientProxy.sendGuiPacket(payload);
            }
        }
        else if (button.id == resetButton.id)
        {
            int colour = 0xffffff;
            
            if (screenState == 0)
            {
                (editController ? controller.activeTextureData : ClientProxy.dialEntryTexture).setFrameColour(0xffffff);
            }
            else if (screenState == 1)
            {
                (editController ? controller.activeTextureData : ClientProxy.dialEntryTexture).setPortalColour(0xffffff);
            }
            else if (screenState == 2)
            {
                (editController ? controller.activeTextureData : ClientProxy.dialEntryTexture).setParticleColour(0x0077D8);
                colour = 0x0077D8;
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
        else if (button.id == backButton.id)
        {
            if (screenState == 0)
            {
                if (frameList.page >= 1)
                {
                    frameList.page--;
                    forwardButton.enabled = true;
                }
                
                if (frameList.page == 0)
                {
                    button.enabled = false;
                }
            }
            else if (screenState == 1)
            {
                if (portalList.page >= 1)
                {
                    portalList.page--;
                    forwardButton.enabled = true;
                }
                
                if (portalList.page == 0)
                {
                    button.enabled = false;
                }
            }
            else if (screenState == 2)
            {
                if (particleList.page >= 1)
                {
                    particleList.page--;
                    forwardButton.enabled = true;
                }
                
                if (particleList.page == 0)
                {
                    button.enabled = false;
                }
            }
        }
        else if (button.id == forwardButton.id)
        {
            if (screenState == 0)
            {
                if (frameList.page < ClientProxy.customFrameTextures.size() / 27)
                {
                    frameList.page++;
                    backButton.enabled = true;
                }
                
                if (frameList.page >= ClientProxy.customFrameTextures.size() / 27)
                {
                    button.enabled = false;
                }
            }
            else if (screenState == 1)
            {
                if (portalList.page < ClientProxy.customPortalTextures.size() / 27)
                {
                    portalList.page++;
                    backButton.enabled = true;
                }
                
                if (portalList.page >= ClientProxy.customPortalTextures.size() / 27)
                {
                    button.enabled = false;
                }
            }
            else if (screenState == 2)
            {
                if (particleList.page < ClientProxy.particleSets.size() / 27)
                {
                    particleList.page++;
                    backButton.enabled = true;
                }
                
                if (particleList.page >= ClientProxy.particleSets.size() / 27)
                {
                    button.enabled = false;
                }
            }
        }
        else if (!editController)
        {
            if (button.id == mainCancelButton.id)
            {
                ClientProxy.openGui(getMinecraft().thePlayer, GUIs.DiallingDevice, controller);
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
                ClientProxy.openGui(getMinecraft().thePlayer, GUIs.DiallingDevice, controller);
            }
        }
    }
    
    @Override
    protected void mouseClicked(int mX, int mY, int mouseButton)
    {
        if (mX >= guiLeft - 25 && mX <= guiLeft)
        {
            for (int i = 0; i < 3; i++)
            {
                if (mY >= guiTop + 10 + (27 * i) && mY <= guiTop + 10 + (27 * i) + 26 && screenState != i)
                {
                    screenState = i;
                    tabScroll = 0;
                    tabWidth = fontRenderer.getStringWidth(tabText[screenState]) + 10;
                    backButton.enabled = screenState == 0 ? frameList.page > 0 : screenState == 1 ? portalList.page > 0 : particleList.page > 0;
                    forwardButton.enabled = (screenState == 0 ? frameList.page : screenState == 1 ? portalList.page : particleList.page) < ((screenState == 0 ? ClientProxy.customFrameTextures.size() : screenState == 1 ? ClientProxy.customPortalTextures.size() : ClientProxy.particleSets.size()) / 27);
                    
                    Color c = new Color(screenState == 0 ? (editController ? controller.activeTextureData : ClientProxy.dialEntryTexture).getFrameColour() : screenState == 1 ? (editController ? controller.activeTextureData : ClientProxy.dialEntryTexture).getPortalColour() : (editController ? controller.activeTextureData : ClientProxy.dialEntryTexture).getParticleColour());
                    redSlider.sliderValue = c.getRed() / 255f;
                    greenSlider.sliderValue = c.getGreen() / 255f;
                    blueSlider.sliderValue = c.getBlue() / 255f;
                    
                    return;
                }
            }
        }
        
        if (screenState == 0)
        {
            frameList.mouseClicked(mX, mY, mouseButton);
            
            if (frameList.selectedIcon != (editController ? controller.activeTextureData : ClientProxy.dialEntryTexture).getCustomFrameTexture())
            {
                (editController ? controller.activeTextureData : ClientProxy.dialEntryTexture).setCustomFrameTexture(frameList.selectedIcon);
                
                if (editController)
                {
                    GuiPayload payload = new GuiPayload();
                    payload.data.setInteger("customFrameTexture", frameList.selectedIcon);
                    ClientProxy.sendGuiPacket(payload);
                }
            }
            else if (frameItem.mouseClicked(mX, mY, mouseButton, getMinecraft().thePlayer.inventory.getItemStack()))
            {
                (editController ? controller.activeTextureData : ClientProxy.dialEntryTexture).setFrameItem(getMinecraft().thePlayer.inventory.getItemStack());
                
                if (editController)
                {
                    ItemStack i = controller.activeTextureData.getFrameItem();
                    
                    GuiPayload payload = new GuiPayload();
                    payload.data.setInteger("frameItemID", i != null ? i.itemID : 0);
                    payload.data.setInteger("frameItemMeta", i != null ? i.getItemDamage() : 0);
                    ClientProxy.sendGuiPacket(payload);
                }
                return;
            }
        }
        else if (screenState == 1)
        {
            portalList.mouseClicked(mX, mY, mouseButton);
            
            if (portalList.selectedIcon != (editController ? controller.activeTextureData : ClientProxy.dialEntryTexture).getCustomPortalTexture())
            {
                (editController ? controller.activeTextureData : ClientProxy.dialEntryTexture).setCustomPortalTexture(portalList.selectedIcon);
                
                if (editController)
                {
                    GuiPayload payload = new GuiPayload();
                    payload.data.setInteger("customPortalTexture", portalList.selectedIcon);
                    ClientProxy.sendGuiPacket(payload);
                }
            }
            else if (portalItem.mouseClicked(mX, mY, mouseButton, getMinecraft().thePlayer.inventory.getItemStack()))
            {
                (editController ? controller.activeTextureData : ClientProxy.dialEntryTexture).setPortalItem(getMinecraft().thePlayer.inventory.getItemStack());
                
                if (editController)
                {
                    ItemStack i = controller.activeTextureData.getPortalItem();
                    
                    GuiPayload payload = new GuiPayload();
                    payload.data.setInteger("portalItemID", i != null ? i.itemID : 0);
                    payload.data.setInteger("portalItemMeta", i != null ? i.getItemDamage() : 0);
                    ClientProxy.sendGuiPacket(payload);
                }
                return;
            }
        }
        else if (screenState == 2)
        {
            particleList.mouseClicked(mX, mY, mouseButton);
            
            if (particleList.selected != (editController ? controller.activeTextureData : ClientProxy.dialEntryTexture).getParticleType())
            {
                (editController ? controller.activeTextureData : ClientProxy.dialEntryTexture).setParticleType(particleList.selected);
                
                if (editController)
                {
                    GuiPayload payload = new GuiPayload();
                    payload.data.setInteger("particleType", particleList.selected);
                    ClientProxy.sendGuiPacket(payload);
                }
            }
        }
        
        super.mouseClicked(mX, mY, mouseButton);
    }
}
