package uk.co.shadeddimensions.enhancedportals.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.enhancedportals.container.ContainerPortalFrameControllerPortalTexture;
import uk.co.shadeddimensions.enhancedportals.gui.slider.GuiBetterSlider;
import uk.co.shadeddimensions.enhancedportals.gui.slider.GuiRGBSlider;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameController;

public class GuiPortalFrameControllerPortalTexture extends GuiEnhancedPortals
{
    TilePortalFrameController controller;
    int r = 255, g = 255, b = 255;

    public GuiPortalFrameControllerPortalTexture(TilePortalFrameController tile, InventoryPlayer inventory)
    {
        super(new ContainerPortalFrameControllerPortalTexture(tile, inventory), tile);

        controller = tile;
        ySize += 10;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        GL11.glColor4f(1f, 1f, 1f, 1f);
        mc.renderEngine.func_110577_a(new ResourceLocation("enhancedportals", "textures/gui/frameControllerPortalTexture.png"));
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        drawBorderedRectangle(93, 50, 16, 16, 0xFFffffff, 0xFF000000, true);
        drawColouredItemStack(94, 51, controller.portalTexture.TextureColour, controller.portalTexture.getItemStack(), true);
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();

        int R = ((GuiRGBSlider) buttonList.get(0)).getValue(), G = ((GuiRGBSlider) buttonList.get(1)).getValue(), B = ((GuiRGBSlider) buttonList.get(2)).getValue();

        if (r != R || g != G || b != B)
        {
            r = R;
            g = G;
            b = B;

            controller.portalTexture.TextureColour = Integer.parseInt(String.format("%02x%02x%02x", r, g, b), 16);
        }
    }

    @Override
    protected void mouseClicked(int par1, int par2, int mouseButton)
    {
        super.mouseClicked(par1, par2, mouseButton);
    }

    @Override
    protected void mouseMovedOrUp(int par1, int par2, int par3)
    {
        super.mouseMovedOrUp(par1, par2, par3);

        if (par3 >= 0)
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
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        super.drawGuiContainerForegroundLayer(par1, par2);

        fontRenderer.drawStringWithShadow("Portal Texture", xSize / 2 - fontRenderer.getStringWidth("Portal Texture") / 2, -13, 0xFFFFFF);
        fontRenderer.drawString("Colour Multiplier", 8, 8, 0x404040);
        fontRenderer.drawString("Texture", 117, 55, 0x404040);
        fontRenderer.drawString("Inventory", 8, 80, 0x404040);
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {

    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();

        buttonList.add(new GuiRGBSlider(0, guiLeft + 8, guiTop + 25, "Red", 1f));
        buttonList.add(new GuiRGBSlider(1, guiLeft + xSize - 8 - 75, guiTop + 25, "Green", 1f));
        buttonList.add(new GuiRGBSlider(2, guiLeft + 8, guiTop + 49, "Blue", 1f));
    }
}
