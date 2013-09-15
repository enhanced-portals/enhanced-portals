package uk.co.shadeddimensions.enhancedportals.gui;

import java.awt.Color;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.enhancedportals.container.ContainerPortalFrameTexture;
import uk.co.shadeddimensions.enhancedportals.gui.slider.GuiBetterSlider;
import uk.co.shadeddimensions.enhancedportals.gui.slider.GuiRGBSlider;
import uk.co.shadeddimensions.enhancedportals.network.CommonProxy;
import uk.co.shadeddimensions.enhancedportals.network.packet.MainPacket;
import uk.co.shadeddimensions.enhancedportals.network.packet.PacketGuiInteger;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameController;
import cpw.mods.fml.common.network.PacketDispatcher;

public class GuiPortalFrameTexture extends GuiEnhancedPortals
{
    TilePortalFrameController controller;

    public GuiPortalFrameTexture(EntityPlayer player, TilePortalFrameController control)
    {
        super(new ContainerPortalFrameTexture(player, control), control);
        controller = control;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        super.drawGuiContainerForegroundLayer(par1, par2);

        fontRenderer.drawStringWithShadow("Portal Frame Texture", xSize / 2 - fontRenderer.getStringWidth("Portal Frame Texture") / 2, -13, 0xFFFFFF);

        fontRenderer.drawString("Texture", 8, 8, 0x404040);
        fontRenderer.drawString("Inventory", 8, 70, 0x404040);

        fontRenderer.drawString("Colour", xSize + 6, 18, 0xe1c92f);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();

        Color c = new Color(controller.FrameColour);
        buttonList.add(new GuiRGBSlider(0, guiLeft + xSize + 5, guiTop + 32, "Red", c.getRed() / 255f));
        buttonList.add(new GuiRGBSlider(1, guiLeft + xSize + 5, guiTop + 56, "Green", c.getGreen() / 255f));
        buttonList.add(new GuiRGBSlider(2, guiLeft + xSize + 5, guiTop + 80, "Blue", c.getBlue() / 255f));

        buttonList.add(new GuiButton(10, guiLeft + xSize - 75 - 7, guiTop + 45, 75, 20, "Save"));
        buttonList.add(new GuiButton(11, guiLeft + 8, guiTop + 45, 75, 20, "Reset"));
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 10 || button.id == 11)
        {
            if (button.id == 11)
            {
                ((GuiRGBSlider) buttonList.get(0)).sliderValue = 1f;
                ((GuiRGBSlider) buttonList.get(1)).sliderValue = 1f;
                ((GuiRGBSlider) buttonList.get(2)).sliderValue = 1f;
            }

            String hex = String.format("%02x%02x%02x", ((GuiRGBSlider) buttonList.get(0)).getValue(), ((GuiRGBSlider) buttonList.get(1)).getValue(), ((GuiRGBSlider) buttonList.get(2)).getValue());
            PacketDispatcher.sendPacketToServer(MainPacket.makePacket(new PacketGuiInteger(0, Integer.parseInt(hex, 16))));
            PacketDispatcher.sendPacketToServer(MainPacket.makePacket(new PacketGuiInteger(4, 0)));
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        GL11.glColor4f(1f, 1f, 1f, 1f);
        mc.renderEngine.bindTexture(new ResourceLocation("enhancedportals", "textures/gui/frameRedstoneController.png"));
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        drawTab(guiLeft + xSize, guiTop + 10, 87, 97, 0.4f, 0.4f, 1f);

        GL11.glColor4f(1f, 1f, 1f, 1f);
        mc.renderEngine.bindTexture(new ResourceLocation("enhancedportals", "textures/gui/inventorySlots.png"));
        drawTexturedModalRect(guiLeft + 7, guiTop + 83, 0, 0, 162, 54);
        drawTexturedModalRect(guiLeft + 7, guiTop + 141, 0, 0, 162, 18);
        drawTexturedModalRect(guiLeft + xSize / 2 - 20, guiTop + 20, 0, 0, 18, 18);
        drawTexturedModalRect(guiLeft + xSize / 2 - 0, guiTop + 20, 0, 0, 18, 18);

        GL11.glColor3f(((GuiRGBSlider) buttonList.get(0)).sliderValue, ((GuiRGBSlider) buttonList.get(1)).sliderValue, ((GuiRGBSlider) buttonList.get(2)).sliderValue);
        itemRenderer.renderWithColor = false;
        itemRenderer.renderItemIntoGUI(fontRenderer, mc.renderEngine, controller.getStackInSlot(0) == null ? new ItemStack(CommonProxy.blockFrame, 1) : controller.getStackInSlot(0), guiLeft + xSize / 2 + 1, guiTop + 21);
        itemRenderer.renderWithColor = true;
        GL11.glColor3f(1f, 1f, 1f);
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
