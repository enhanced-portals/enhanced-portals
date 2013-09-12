package uk.co.shadeddimensions.enhancedportals.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.enhancedportals.container.ContainerPortalFrameTexture;
import uk.co.shadeddimensions.enhancedportals.gui.slider.GuiBetterSlider;
import uk.co.shadeddimensions.enhancedportals.gui.slider.GuiRGBSlider;
import uk.co.shadeddimensions.enhancedportals.network.CommonProxy;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameController;

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
        
        fontRenderer.drawString("Texture", xSize - fontRenderer.getStringWidth("Texture") - 33, 13, 0x404040);
        fontRenderer.drawString("Inventory", 8, 70, 0x404040);
        
        GL11.glColor3f(((GuiRGBSlider) buttonList.get(0)).sliderValue, ((GuiRGBSlider) buttonList.get(1)).sliderValue, ((GuiRGBSlider) buttonList.get(2)).sliderValue);
        itemRenderer.renderWithColor = false;
        itemRenderer.renderItemIntoGUI(fontRenderer, mc.renderEngine, controller.getStackInSlot(0) == null ? new ItemStack(CommonProxy.blockFrame, 1) : controller.getStackInSlot(0), xSize + 5, 19);
        itemRenderer.renderWithColor = true;
        GL11.glColor3f(1f, 1f, 1f);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();
                
        buttonList.add(new GuiRGBSlider(0, guiLeft + 8, guiTop + 8, "Red", 1.0f));
        buttonList.add(new GuiRGBSlider(1, guiLeft + 8, guiTop + 32, "Green", 1.0f));
        buttonList.add(new GuiRGBSlider(2, guiLeft + xSize - 75 - 8, guiTop + 32, "Blue", 1.0f));
        buttonList.add(new GuiButton(10, guiLeft + xSize + 5, guiTop + 40, 53, 20, "Save"));
    }
    
    @Override
    protected void actionPerformed(GuiButton button)
    {
        
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        GL11.glColor4f(1f, 1f, 1f, 1f);
        mc.renderEngine.func_110577_a(new ResourceLocation("enhancedportals", "textures/gui/frameRedstoneController.png"));
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        
        GL11.glColor4f(0.4f, 0.4f, 1f, 1f);
        mc.renderEngine.func_110577_a(new ResourceLocation("enhancedportals", "textures/gui/ledger.png"));
        drawTexturedModalRect(guiLeft + xSize, guiTop + 10, 0, 0, 60, 52);
        drawTexturedModalRect(guiLeft + xSize + 60, guiTop + 10, 256 - 4, 0, 4, 37);
        drawTexturedModalRect(guiLeft + xSize + 60, guiTop + 47, 256 - 4, 256 - 19, 4, 19);
        drawTexturedModalRect(guiLeft + xSize, guiTop + 62, 0, 256 - 4, 60, 4);
        
        GL11.glColor4f(1f, 1f, 1f, 1f);
        mc.renderEngine.func_110577_a(new ResourceLocation("enhancedportals", "textures/gui/inventorySlots.png"));
        drawTexturedModalRect(guiLeft + 7, guiTop + 83, 0, 0, 162, 54);
        drawTexturedModalRect(guiLeft + 7, guiTop + 141, 0, 0, 162, 18);
        drawTexturedModalRect(guiLeft + xSize - 27, guiTop + 8, 0, 0, 18, 18);
        drawTexturedModalRect(guiLeft + xSize + 4, guiTop + 18, 0, 0, 18, 18);
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
