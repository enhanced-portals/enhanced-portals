package uk.co.shadeddimensions.enhancedportals.gui;

import java.awt.Color;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.enhancedportals.container.ContainerPortalFrameControllerFrameTexture;
import uk.co.shadeddimensions.enhancedportals.gui.slider.GuiBetterSlider;
import uk.co.shadeddimensions.enhancedportals.gui.slider.GuiRGBSlider;
import uk.co.shadeddimensions.enhancedportals.network.packet.MainPacket;
import uk.co.shadeddimensions.enhancedportals.network.packet.PacketPortalFrameControllerFrameTextureData;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameController;
import cpw.mods.fml.common.network.PacketDispatcher;

public class GuiPortalFrameControllerFrameTexture extends GuiEnhancedPortals
{
    TilePortalFrameController controller;
    int r = 255, g = 255, b = 255, lastUpdate = 20;
    boolean unsavedChanges = false;

    public GuiPortalFrameControllerFrameTexture(TilePortalFrameController tile, InventoryPlayer inventory)
    {
        super(new ContainerPortalFrameControllerFrameTexture(tile, inventory), tile);

        controller = tile;
        ySize += 10;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        GL11.glColor4f(1f, 1f, 1f, 1f);
        mc.renderEngine.func_110577_a(new ResourceLocation("enhancedportals", "textures/gui/frameControllerFrameTexture.png"));
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        drawBorderedRectangle(93, 50, 16, 16, 0xFFffffff, 0xFF000000, true);
        drawColouredItemStack(94, 51, controller.frameTexture.TextureColour, controller.frameTexture.getItemStack(), true);
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();

        int R = ((GuiRGBSlider) buttonList.get(0)).getValue(),
            G = ((GuiRGBSlider) buttonList.get(1)).getValue(),
            B = ((GuiRGBSlider) buttonList.get(2)).getValue();

        if (r != R || g != G || b != B)
        {
            r = R;
            g = G;
            b = B;

            controller.frameTexture.TextureColour = Integer.parseInt(String.format("%02x%02x%02x", r, g, b), 16);
            updateServer();
        }
        else if (unsavedChanges && lastUpdate >= 20)
        {
            updateServer();
        }        
        
        lastUpdate++;
    }
    
    @Override
    public void onGuiClosed()
    {
        if (unsavedChanges)
        {
            lastUpdate = 25;
            updateServer();
        }
        
        super.onGuiClosed();
    }

    @Override
    protected void mouseClicked(int xPos, int yPos, int mouseButton)
    {
        if (isShiftKeyDown() && getSlotAtPosition(xPos, yPos) != null && mouseButton == 0)
        {
            ItemStack stack = getSlotAtPosition(xPos, yPos).getStack();
            
            if (stack != null && stack.getItemSpriteNumber() == 0)
            {
                int blockID = stack.getItem().itemID, blockMeta = stack.getItemDamage();
                
                if (Block.isNormalCube(blockID))
                {
                    controller.frameTexture.Texture = "B:" + blockID + ":" + blockMeta;
                    updateServer();
                    return;
                }
            }
        }
        
        super.mouseClicked(xPos, yPos, mouseButton);
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

        fontRenderer.drawStringWithShadow("Frame Texture", xSize / 2 - fontRenderer.getStringWidth("Frame Texture") / 2, -13, 0xFFFFFF);
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
        
        Color c = new Color(controller.frameTexture.TextureColour);
        buttonList.add(new GuiRGBSlider(0, guiLeft + 8, guiTop + 25, "Red", c.getRed() / 255));
        buttonList.add(new GuiRGBSlider(1, guiLeft + xSize - 8 - 75, guiTop + 25, "Green", c.getGreen() / 255));
        buttonList.add(new GuiRGBSlider(2, guiLeft + 8, guiTop + 49, "Blue", c.getBlue() / 255));
    }
    
    private void updateServer()
    {
        if (lastUpdate > 20)
        {
            unsavedChanges = false;
            lastUpdate = 0;
            System.out.println("Sent packet to server!");
            PacketDispatcher.sendPacketToServer(MainPacket.makePacket(new PacketPortalFrameControllerFrameTextureData(controller)));
        }
        else
        {
            unsavedChanges = true;
        }
    }
}
