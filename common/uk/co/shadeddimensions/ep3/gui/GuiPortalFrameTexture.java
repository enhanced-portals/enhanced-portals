package uk.co.shadeddimensions.ep3.gui;

import java.awt.Color;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.ep3.container.ContainerPortalFrameTexture;
import uk.co.shadeddimensions.ep3.gui.slider.GuiBetterSlider;
import uk.co.shadeddimensions.ep3.lib.Reference;
import uk.co.shadeddimensions.ep3.network.ClientProxy;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;
import uk.co.shadeddimensions.ep3.util.GuiPayload;

public class GuiPortalFrameTexture extends GuiColourInterface
{
    TilePortalController controller;

    public GuiPortalFrameTexture(EntityPlayer player, TilePortalController control)
    {
        super(new ContainerPortalFrameTexture(player, control), control, new Color(control.frameColour));
        controller = control;
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
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        super.drawGuiContainerBackgroundLayer(f, i, j);

        GL11.glColor4f(1f, 1f, 1f, 1f);
        mc.renderEngine.bindTexture(new ResourceLocation("enhancedportals", "textures/gui/inventorySlots.png"));
        drawTexturedModalRect(guiLeft + 7, guiTop + 83, 0, 0, 162, 54);
        drawTexturedModalRect(guiLeft + 7, guiTop + 141, 0, 0, 162, 18);
        drawTexturedModalRect(guiLeft + xSize / 2 - 20, guiTop + 20, 0, 0, 18, 18);
        drawTexturedModalRect(guiLeft + xSize / 2 - 0, guiTop + 20, 0, 0, 18, 18);

        GL11.glColor3f(redSlider.sliderValue, greenSlider.sliderValue, blueSlider.sliderValue);
        itemRenderer.renderWithColor = false;
        itemRenderer.renderItemAndEffectIntoGUI(fontRenderer, mc.renderEngine, controller.getStackInSlot(0) == null ? new ItemStack(CommonProxy.blockFrame, 1) : controller.getStackInSlot(0), guiLeft + xSize / 2 + 1, guiTop + 21);
        itemRenderer.renderWithColor = true;
        GL11.glColor3f(1f, 1f, 1f);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        super.drawGuiContainerForegroundLayer(par1, par2);

        fontRenderer.drawStringWithShadow(StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".portalFrameTexture"), xSize / 2 - fontRenderer.getStringWidth(StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".portalFrameTexture")) / 2, -13, 0xFFFFFF);

        fontRenderer.drawString(StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".texture"), 8, 8, 0x404040);
        fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, 70, 0x404040);
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
}
