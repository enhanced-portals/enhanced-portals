package uk.co.shadeddimensions.ep3.client.gui;

import java.awt.Color;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.StatCollector;
import uk.co.shadeddimensions.ep3.client.gui.elements.GuiParticleList;
import uk.co.shadeddimensions.ep3.container.ContainerParticleTexture;
import uk.co.shadeddimensions.ep3.lib.Reference;
import uk.co.shadeddimensions.ep3.network.ClientProxy;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;
import uk.co.shadeddimensions.ep3.util.GuiPayload;

public class GuiParticleTexture extends GuiColourInterface
{
    TilePortalController controller;
    GuiParticleList particleList;
    int id;
    
    public GuiParticleTexture(TilePortalController control)
    {
        super(new ContainerParticleTexture(control), control, new Color(control.activeTextureData.getParticleColour()));
        controller = control;
        particleList = new GuiParticleList(guiLeft + 6, guiTop + 6, 164, 154, this);
        id = 0;
    }
    
    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == saveButton.id)
        {
            String hex = String.format("%02x%02x%02x", redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue());
            
            GuiPayload payload = new GuiPayload();
            payload.data.setInteger("particleColour", Integer.parseInt(hex, 16));
            ClientProxy.sendGuiPacket(payload);
        }
        else if (button.id == resetButton.id)
        {
            redSlider.sliderValue = 0f;
            greenSlider.sliderValue = 119 / 255f;
            blueSlider.sliderValue = 216 / 255f;
            
            String hex = String.format("%02x%02x%02x", redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue());
            
            GuiPayload payload = new GuiPayload();
            payload.data.setInteger("particleColour", Integer.parseInt(hex, 16));
            ClientProxy.sendGuiPacket(payload);
        }
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        super.drawGuiContainerBackgroundLayer(f, i, j);
        
        particleList.drawBackground();
    }
        
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        super.drawGuiContainerForegroundLayer(par1, par2);
        
        fontRenderer.drawStringWithShadow(StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".particleTexture"), xSize / 2 - fontRenderer.getStringWidth(StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".particleTexture")) / 2, -13, 0xFFFFFF);
    }
    
    @Override
    public void initGui()
    {
        super.initGui();
        
        particleList.selected = id = controller.activeTextureData.getParticleType();
    }
    
    @Override
    protected void mouseClicked(int par1, int par2, int mouseButton)
    {
        super.mouseClicked(par1, par2, mouseButton);
        particleList.mouseClicked(par1, par2, mouseButton);
        
        if (particleList.selected != id)
        {
            id = particleList.selected;
            
            GuiPayload payload = new GuiPayload();
            payload.data.setInteger("particleType", id);
            ClientProxy.sendGuiPacket(payload);
        }
    }
}
