package uk.co.shadeddimensions.ep3.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.ep3.client.gui.base.GuiEnhancedPortals;
import uk.co.shadeddimensions.ep3.client.gui.button.GuiBetterButton;
import uk.co.shadeddimensions.ep3.client.gui.scroll.GuiEntityList;
import uk.co.shadeddimensions.ep3.client.gui.tooltips.ToolTip;
import uk.co.shadeddimensions.ep3.client.gui.tooltips.ToolTipLine;
import uk.co.shadeddimensions.ep3.container.ContainerBiometricIdentifier;
import uk.co.shadeddimensions.ep3.lib.Reference;
import uk.co.shadeddimensions.ep3.network.ClientProxy;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileBiometricIdentifier;
import uk.co.shadeddimensions.ep3.util.GuiPayload;

public class GuiBiometricIdentifier extends GuiEnhancedPortals
{
    TileBiometricIdentifier biometric;
    GuiEntityList sendingList, recievingList;
    GuiBetterButton leftButton, rightButton;
    ToolTip allowTip, disallowTip;

    public GuiBiometricIdentifier(TileBiometricIdentifier tile, EntityPlayer player)
    {
        super(new ContainerBiometricIdentifier(tile, player), tile);
        xSize = 300;
        ySize = 200;
        biometric = tile;
        sendingList = new GuiEntityList(this, 7, 17, 120, 95, tile, true, new ResourceLocation("enhancedportals", "textures/gui/biometric.png"));
        recievingList = new GuiEntityList(this, xSize - 127, 17, 120, 95, tile, false, new ResourceLocation("enhancedportals", "textures/gui/biometric_2.png"));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        super.drawGuiContainerBackgroundLayer(f, i, j);
        GL11.glColor4f(1f, 1f, 1f, 1f);
        mc.renderEngine.bindTexture(new ResourceLocation("enhancedportals", "textures/gui/biometric.png"));
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, 256, ySize);
        mc.renderEngine.bindTexture(new ResourceLocation("enhancedportals", "textures/gui/biometric_2.png"));
        drawTexturedModalRect(guiLeft + 150, guiTop, 0, 0, 150, ySize);

        sendingList.drawBackground(i, j);
        recievingList.drawBackground(i, j);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        super.drawGuiContainerForegroundLayer(par1, par2);
        sendingList.drawForeground(par1, par2);
        recievingList.drawForeground(par1, par2);

        fontRenderer.drawStringWithShadow(StatCollector.translateToLocal("tile." + Reference.SHORT_ID + ".portalFrame.biometric.name"), xSize / 2 - fontRenderer.getStringWidth(StatCollector.translateToLocal("tile." + Reference.SHORT_ID + ".portalFrame.biometric.name")) / 2, -13, 0xFFFFFF);
        fontRenderer.drawString("Sending", 7, 6, 0x404040);
        fontRenderer.drawString("Recieving", xSize - 7 - fontRenderer.getStringWidth("Recieving"), 6, 0x404040);
        fontRenderer.drawString("ID Card", 28, ySize - 20, 0x404040);
        fontRenderer.drawString("ID Card", 273 - fontRenderer.getStringWidth("ID Card"), ySize - 20, 0x404040);
    }
    
    @Override
    public void updateScreen()
    {
        super.updateScreen();
        
        leftButton.displayString = biometric.notFoundSend ? "Allow" : "Disallow";
        leftButton.setToolTip(biometric.notFoundSend ? allowTip : disallowTip);
        rightButton.displayString = biometric.notFoundRecieve ? "Allow" : "Disallow";
        rightButton.setToolTip(biometric.notFoundRecieve ? allowTip : disallowTip);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();
        sendingList.init();
        recievingList.init();
        
        disallowTip = new ToolTip();
        disallowTip.add(new ToolTipLine(EnumChatFormatting.RED + "Not Allowing", -1));
        disallowTip.add(new ToolTipLine(EnumChatFormatting.WHITE + "Any entity that is not", -1));
        disallowTip.add(new ToolTipLine(EnumChatFormatting.WHITE + "on the above list", -1));
        
        allowTip = new ToolTip();
        allowTip.add(new ToolTipLine(EnumChatFormatting.GREEN + "Allowing", -1));
        allowTip.add(new ToolTipLine(EnumChatFormatting.WHITE + "Any entity that is not", -1));
        allowTip.add(new ToolTipLine(EnumChatFormatting.WHITE + "on the above list", -1));

        leftButton = new GuiBetterButton(0, guiLeft + 7, guiTop + 117, 60, biometric.notFoundSend ? "Allow" : "Disallow");
        rightButton = new GuiBetterButton(1, guiLeft + xSize - 67, guiTop + 117, 60, biometric.notFoundRecieve ? "Allow" : "Disallow");
        
        leftButton.setToolTip(biometric.notFoundSend ? allowTip : disallowTip);
        rightButton.setToolTip(biometric.notFoundRecieve ? allowTip : disallowTip);

        buttonList.add(leftButton);
        buttonList.add(rightButton);
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
    }
}
