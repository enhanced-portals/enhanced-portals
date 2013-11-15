package uk.co.shadeddimensions.ep3.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.ep3.client.gui.element.ElementDialDeviceScrollList;
import uk.co.shadeddimensions.ep3.client.gui.element.ElementEntityFilterList;
import uk.co.shadeddimensions.ep3.container.ContainerBiometricIdentifier;
import uk.co.shadeddimensions.ep3.lib.Reference;
import uk.co.shadeddimensions.ep3.network.ClientProxy;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileBiometricIdentifier;
import uk.co.shadeddimensions.ep3.util.GuiPayload;
import cofh.gui.GuiBase;
import cofh.gui.element.ElementBase;

public class GuiBiometricIdentifier extends GuiBase implements IElementHandler
{
    TileBiometricIdentifier biometric;
    GuiButton leftButton, rightButton, activateRecieveList;
    ElementEntityFilterList sendList, recieveList;

    public GuiBiometricIdentifier(TileBiometricIdentifier tile, EntityPlayer player)
    {
        super(new ContainerBiometricIdentifier(tile, player));
        xSize = 300;
        ySize = 200;
        biometric = tile;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        updateElements();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(new ResourceLocation("enhancedportals", "textures/gui/biometric.png"));
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, 256, ySize);
        mc.renderEngine.bindTexture(new ResourceLocation("enhancedportals", "textures/gui/biometric_2.png"));
        drawTexturedModalRect(guiLeft + 150, guiTop, 0, 0, 150, ySize);

        drawElements();
        drawTabs();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        super.drawGuiContainerForegroundLayer(par1, par2);

        fontRenderer.drawStringWithShadow(StatCollector.translateToLocal("tile." + Reference.SHORT_ID + ".portalFrame.biometric.name"), xSize / 2 - fontRenderer.getStringWidth(StatCollector.translateToLocal("tile." + Reference.SHORT_ID + ".portalFrame.biometric.name")) / 2, -13, 0xFFFFFF);
        fontRenderer.drawString("Sending", 7, 6, 0x404040);
        fontRenderer.drawString("Recieving", xSize - 7 - fontRenderer.getStringWidth("Recieving"), 6, 0x404040);
        fontRenderer.drawString("ID Card", 28, ySize - 20, 0x404040);
        fontRenderer.drawString("ID Card", 273 - fontRenderer.getStringWidth("ID Card"), ySize - 20, 0x404040);
        fontRenderer.drawString(biometric.isActive ? "Active" : "Inactive", xSize / 2 - fontRenderer.getStringWidth(biometric.isActive ? "Active" : "Inactive") / 2, 6, biometric.isActive ? 0x00AA00 : 0xAA0000);
    }
    
    @Override
    public void updateScreen()
    {
        super.updateScreen();
        
        leftButton.displayString = biometric.notFoundSend ? "Allow" : "Disallow";
        rightButton.displayString = biometric.notFoundRecieve ? "Allow" : "Disallow";
        rightButton.enabled = biometric.hasSeperateLists;
        activateRecieveList.displayString = biometric.hasSeperateLists ? "Deactivate" : "Activate";
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();
        
        leftButton = new GuiButton(0, guiLeft + 7, guiTop + 117, 60, 20, biometric.notFoundSend ? "Allow" : "Disallow");
        rightButton = new GuiButton(1, guiLeft + xSize - 67, guiTop + 117, 60, 20, biometric.notFoundRecieve ? "Allow" : "Disallow");
        activateRecieveList = new GuiButton(2, guiLeft + xSize - 67, guiTop + 138, 60, 20, biometric.hasSeperateLists ? "Deactivate" : "Activate");
        rightButton.enabled = biometric.hasSeperateLists;

        buttonList.add(leftButton);
        buttonList.add(rightButton);
        buttonList.add(activateRecieveList);
        
        sendList = new ElementEntityFilterList(this, new ResourceLocation("enhancedportals", "textures/gui/biometric.png"), biometric, 7, 17, 120, 95, true);
        recieveList = new ElementEntityFilterList(this, new ResourceLocation("enhancedportals", "textures/gui/biometric_2.png"), biometric, xSize - 127, 17, 120, 95, false);
        addElement(sendList);
        addElement(recieveList);
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 0)
        {
            GuiPayload payload = new GuiPayload();
            payload.data.setBoolean("list", true);
            payload.data.setBoolean("default", false);
            ClientProxy.sendGuiPacket(payload);
        }
        else if (button.id == 1)
        {
            GuiPayload payload = new GuiPayload();
            payload.data.setBoolean("list", false);
            payload.data.setBoolean("default", false);
            ClientProxy.sendGuiPacket(payload);
        }
        else if (button.id == 2)
        {
            GuiPayload payload = new GuiPayload();
            payload.data.setBoolean("toggleSeperateLists", false);
            ClientProxy.sendGuiPacket(payload);
        }
    }

    @Override
    public void onElementChanged(ElementBase element, Object data)
    {
        if (element instanceof ElementDialDeviceScrollList)
        {
            int[] obj = (int[]) data;
            int list = obj[0], type = obj[1], entry = obj[2];
            
            if (type == 0 && entry >= 0 && entry < (list == 0 ? biometric.sendingEntityTypes : biometric.recievingEntityTypes).size())
            {
                // invert
                GuiPayload p = new GuiPayload();
                p.data.setBoolean("list", list == 0);
                p.data.setInteger("invert", entry);
                ClientProxy.sendGuiPacket(p);
            }
            else if (type == 1 && entry >= 0 && entry < (list == 0 ? biometric.sendingEntityTypes : biometric.recievingEntityTypes).size())
            {
                // mode
                GuiPayload p = new GuiPayload();
                p.data.setBoolean("list", list == 0);
                p.data.setInteger("type", entry);
                ClientProxy.sendGuiPacket(p);
            }
            else if (type == 2 && entry >= 0 && entry < (list == 0 ? biometric.sendingEntityTypes : biometric.recievingEntityTypes).size())
            {
                // remove                
                GuiPayload p = new GuiPayload();
                p.data.setBoolean("list", list == 0);
                p.data.setInteger("remove", entry);
                ClientProxy.sendGuiPacket(p);
            }
            
            Minecraft.getMinecraft().sndManager.playSoundFX("random.click", 1.0F, 1.0F);
        }
    }
}
