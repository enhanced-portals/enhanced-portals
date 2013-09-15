package uk.co.shadeddimensions.enhancedportals.gui;

import java.awt.Color;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.enhancedportals.client.particle.PortalFX;
import uk.co.shadeddimensions.enhancedportals.container.ContainerPortalTexture;
import uk.co.shadeddimensions.enhancedportals.gui.slider.GuiBetterSlider;
import uk.co.shadeddimensions.enhancedportals.gui.slider.GuiRGBSlider;
import uk.co.shadeddimensions.enhancedportals.network.packet.MainPacket;
import uk.co.shadeddimensions.enhancedportals.network.packet.PacketGuiInteger;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameController;
import cpw.mods.fml.common.network.PacketDispatcher;

public class GuiPortalTexture extends GuiEnhancedPortals
{
    TilePortalFrameController controller;
    int pType;

    public GuiPortalTexture(EntityPlayer player, TilePortalFrameController control)
    {
        super(new ContainerPortalTexture(player, control), control);
        controller = control;
        pType = control.ParticleType;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        GL11.glColor4f(1f, 1f, 1f, 1f);
        mc.renderEngine.bindTexture(new ResourceLocation("enhancedportals", "textures/gui/frameRedstoneController.png"));
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        drawTab(guiLeft + xSize, guiTop + 10, 87, 97, 0.4f, 0.4f, 1f);
        drawTabFlipped(guiLeft - 87, guiTop + 10, 87, 97, 0.4f, 0.4f, 1f);

        GL11.glColor4f(1f, 1f, 1f, 1f);
        mc.renderEngine.bindTexture(new ResourceLocation("enhancedportals", "textures/gui/inventorySlots.png"));
        drawTexturedModalRect(guiLeft + 7, guiTop + 83, 0, 0, 162, 54);
        drawTexturedModalRect(guiLeft + 7, guiTop + 141, 0, 0, 162, 18);
        drawTexturedModalRect(guiLeft + 37, guiTop + 22, 0, 0, 18, 18);
        drawTexturedModalRect(guiLeft + xSize - 48, guiTop + 22, 0, 0, 18, 18);
        drawTexturedModalRect(guiLeft + xSize - 28, guiTop + 22, 0, 0, 18, 18);

        GL11.glColor3f(((GuiRGBSlider) buttonList.get(0)).sliderValue, ((GuiRGBSlider) buttonList.get(1)).sliderValue, ((GuiRGBSlider) buttonList.get(2)).sliderValue);
        itemRenderer.renderWithColor = false;
        itemRenderer.renderItemIntoGUI(fontRenderer, mc.renderEngine, controller.getStackInSlot(1) == null ? new ItemStack(Block.portal, 1) : controller.getStackInSlot(1), guiLeft + xSize - 27, guiTop + 23);
        itemRenderer.renderWithColor = true;
        GL11.glColor3f(1f, 1f, 1f);

        drawParticle(38, 23, ((GuiRGBSlider) buttonList.get(3)).getValue(), ((GuiRGBSlider) buttonList.get(4)).getValue(), ((GuiRGBSlider) buttonList.get(5)).getValue(), 255, PortalFX.getStaticParticleIndex(controller.ParticleType), true);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        super.drawGuiContainerForegroundLayer(par1, par2);

        fontRenderer.drawStringWithShadow("Portal Texture", xSize / 2 - fontRenderer.getStringWidth("Portal Texture") / 2, -13, 0xFFFFFF);

        fontRenderer.drawString("Texture", 8, 8, 0x404040);
        fontRenderer.drawString("Inventory", 8, 70, 0x404040);

        fontRenderer.drawString("Portal Colour", xSize + 6, 18, 0xe1c92f);
        fontRenderer.drawString("Particle Colour", -80, 18, 0xe1c92f);
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

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();

        Color c = new Color(controller.PortalColour);
        buttonList.add(new GuiRGBSlider(0, guiLeft + xSize + 5, guiTop + 32, "Red", c.getRed() / 255f));
        buttonList.add(new GuiRGBSlider(1, guiLeft + xSize + 5, guiTop + 56, "Green", c.getGreen() / 255f));
        buttonList.add(new GuiRGBSlider(2, guiLeft + xSize + 5, guiTop + 80, "Blue", c.getBlue() / 255f));

        c = new Color(controller.ParticleColour);
        buttonList.add(new GuiRGBSlider(3, guiLeft - 79, guiTop + 32, "Red", c.getRed() / 255f));
        buttonList.add(new GuiRGBSlider(4, guiLeft - 79, guiTop + 56, "Green", c.getGreen() / 255f));
        buttonList.add(new GuiRGBSlider(5, guiLeft - 79, guiTop + 80, "Blue", c.getBlue() / 255f));

        buttonList.add(new GuiButton(10, guiLeft + xSize - 75 - 7, guiTop + 45, 75, 20, "Save"));
        buttonList.add(new GuiButton(11, guiLeft + 8, guiTop + 45, 75, 20, "Reset"));

        buttonList.add(new GuiButton(12, guiLeft + 8, guiTop + 21, 16, 20, "<"));
        buttonList.add(new GuiButton(13, guiLeft + 66, guiTop + 21, 16, 20, ">"));
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

                Color c = new Color(0xB336A1);
                ((GuiRGBSlider) buttonList.get(3)).sliderValue = c.getRed() / 255f;
                ((GuiRGBSlider) buttonList.get(4)).sliderValue = c.getGreen() / 255f;
                ((GuiRGBSlider) buttonList.get(5)).sliderValue = c.getBlue() / 255f;

                PacketDispatcher.sendPacketToServer(MainPacket.makePacket(new PacketGuiInteger(3, 0)));
            }

            String portalColourHex = String.format("%02x%02x%02x", ((GuiRGBSlider) buttonList.get(0)).getValue(), ((GuiRGBSlider) buttonList.get(1)).getValue(), ((GuiRGBSlider) buttonList.get(2)).getValue());
            String particleColourHex = String.format("%02x%02x%02x", ((GuiRGBSlider) buttonList.get(3)).getValue(), ((GuiRGBSlider) buttonList.get(4)).getValue(), ((GuiRGBSlider) buttonList.get(5)).getValue());

            PacketDispatcher.sendPacketToServer(MainPacket.makePacket(new PacketGuiInteger(1, Integer.parseInt(portalColourHex, 16))));
            PacketDispatcher.sendPacketToServer(MainPacket.makePacket(new PacketGuiInteger(2, Integer.parseInt(particleColourHex, 16))));
            PacketDispatcher.sendPacketToServer(MainPacket.makePacket(new PacketGuiInteger(4, 1)));
        }
        else if (button.id == 12 || button.id == 13)
        {
            boolean changed = false;

            if (controller.ParticleType > 0 && button.id == 12)
            {
                controller.ParticleType--;
                changed = true;
            }
            else if (controller.ParticleType < 13 && button.id == 13)
            {
                controller.ParticleType++;
                changed = true;
            }
            else if (controller.ParticleType == 0 && button.id == 12)
            {
                controller.ParticleType = 13;
                changed = true;
            }
            else if (controller.ParticleType == 13 && button.id == 13)
            {
                controller.ParticleType = 0;
                changed = true;
            }

            if (changed)
            {
                PacketDispatcher.sendPacketToServer(MainPacket.makePacket(new PacketGuiInteger(3, controller.ParticleType)));
            }
        }
    }
}
