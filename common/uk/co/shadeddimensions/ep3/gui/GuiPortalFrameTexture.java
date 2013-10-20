package uk.co.shadeddimensions.ep3.gui;

import java.awt.Color;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.ep3.container.ContainerPortalFrameTexture;
import uk.co.shadeddimensions.ep3.gui.button.GuiBetterButton;
import uk.co.shadeddimensions.ep3.gui.button.StandardButtonTextureSets;
import uk.co.shadeddimensions.ep3.gui.elements.GuiIconList;
import uk.co.shadeddimensions.ep3.lib.Reference;
import uk.co.shadeddimensions.ep3.network.ClientProxy;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;
import uk.co.shadeddimensions.ep3.util.GuiPayload;

public class GuiPortalFrameTexture extends GuiColourInterface
{
    TilePortalController controller;
    GuiIconList scrollList;
    int id;
    
    GuiBetterButton backButton, forwardButton;

    public GuiPortalFrameTexture(EntityPlayer player, TilePortalController control)
    {
        super(new ContainerPortalFrameTexture(player, control), control, new Color(control.frameColour));
        controller = control;
        scrollList = new GuiIconList(guiLeft + 6, guiTop + 6, 164, 56, this, false);
        id = -1;
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == saveButton.id)
        {
            String hex = String.format("%02x%02x%02x", redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue());
            
            GuiPayload payload = new GuiPayload();
            payload.data.setInteger("frameColour", Integer.parseInt(hex, 16));
            ClientProxy.sendGuiPacket(payload);
        }
        else if (button.id == resetButton.id)
        {
            redSlider.sliderValue = 1f;
            greenSlider.sliderValue = 1f;
            blueSlider.sliderValue = 1f;
            
            String hex = String.format("%02x%02x%02x", redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue());
            
            GuiPayload payload = new GuiPayload();
            payload.data.setInteger("frameColour", Integer.parseInt(hex, 16));
            payload.data.setInteger("resetSlot", 0);            
            ClientProxy.sendGuiPacket(payload);
        }
        else if (button.id == backButton.id)
        {
            if (scrollList.page >= 1)
            {
                scrollList.page--;
                forwardButton.enabled = true;
            }
            
            if (scrollList.page == 0)
            {
                button.enabled = false;
            }
        }
        else if (button.id == forwardButton.id)
        {
            if (scrollList.page < (scrollList.isPortalTexture ? ClientProxy.customPortalTextures.size() : ClientProxy.customPortalFrameTextures.size()) / 27)
            {
                scrollList.page++;
                backButton.enabled = true;
            }
            
            if (scrollList.page >= ((scrollList.isPortalTexture ? ClientProxy.customPortalTextures.size() : ClientProxy.customPortalFrameTextures.size()) / 27))
            {
                button.enabled = false;
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();
        
        scrollList.selectedIcon = id = controller.customFrameTexture;
        backButton = new GuiBetterButton(1, guiLeft + xSize - 38, guiTop + 64, 16, StandardButtonTextureSets.BACK_BUTTON, "");
        forwardButton = new GuiBetterButton(2, guiLeft + xSize - 22, guiTop + 64, 16, StandardButtonTextureSets.FORWARD_BUTTON, "");
        
        buttonList.add(backButton);
        buttonList.add(forwardButton);
        
        backButton.enabled = scrollList.page > 0;
        forwardButton.enabled = scrollList.page < ((scrollList.isPortalTexture ? ClientProxy.customPortalTextures.size() : ClientProxy.customPortalFrameTextures.size()) / 27);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        super.drawGuiContainerBackgroundLayer(f, i, j);
        scrollList.drawBackground();
        
        drawTabFlipped(guiLeft - 27, guiTop + 10, 27, 30, 1f, 1f, 1f);
        
        GL11.glColor4f(1f, 1f, 1f, 1f);
        mc.renderEngine.bindTexture(new ResourceLocation("enhancedportals", "textures/gui/inventorySlots.png"));
        drawTexturedModalRect(guiLeft - 21, guiTop + 16, 0, 0, 18, 18);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        super.drawGuiContainerForegroundLayer(par1, par2);

        fontRenderer.drawStringWithShadow(StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".portalFrameTexture"), xSize / 2 - fontRenderer.getStringWidth(StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".portalFrameTexture")) / 2, -13, 0xFFFFFF);
        fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, 70, 0x404040);
    }
    
    @Override
    protected void mouseClicked(int par1, int par2, int mouseButton)
    {
        super.mouseClicked(par1, par2, mouseButton);
        scrollList.mouseClicked(par1, par2, mouseButton);
        
        if (scrollList.isActive && scrollList.selectedIcon != id)
        {
            id = scrollList.selectedIcon;
            
            GuiPayload payload = new GuiPayload();
            payload.data.setInteger("customFrameTexture", id);
            ClientProxy.sendGuiPacket(payload);
        }
    }
}
