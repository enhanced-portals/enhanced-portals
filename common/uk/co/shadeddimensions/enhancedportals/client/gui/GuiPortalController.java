package uk.co.shadeddimensions.enhancedportals.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.enhancedportals.container.ContainerPortalController;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalController;

public class GuiPortalController extends GuiContainer
{
    EntityPlayer player;
    TilePortalController tile;

    public GuiPortalController(EntityPlayer player, TilePortalController frame)
    {
        super(new ContainerPortalController(player.inventory, frame));
        this.player = player;
        tile = frame;
    }

    @Override
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        super.actionPerformed(par1GuiButton);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    public void initGui()
    {
        super.initGui();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        GL11.glColor4f(1f, 1f, 1f, 1f);
        mc.renderEngine.func_110577_a(new ResourceLocation("enhancedportals", "textures/gui/frameController.png"));
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        
        drawRectangle(8, 20, 18, 18, 0xFF000000);
        drawRectangle(9, 21, 16, 16, 0xFFFFFFFF);
        
        drawRectangle(30, 20, 18, 18, 0xFF000000);
        drawRectangle(31, 21, 16, 16, 0xFFFFFFFF);
        
        drawRectangle(8, 62, 18, 18, 0xFF000000);
        drawRectangle(9, 63, 16, 16, 0xFFFFFFFF);
        
        drawRectangle(30, 62, 18, 18, 0xFF000000);
        drawRectangle(31, 63, 16, 16, 0xFFFFFFFF);
    }
    
    private void drawRectangle(int x, int y, int w, int h, int c)
    {
        drawRect(guiLeft + x, guiTop + y, guiLeft + x + w, guiTop + y + h, c);
    }

    @Override
    public void drawScreen(int par1, int par2, float par3)
    {
        super.drawScreen(par1, par2, par3);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        fontRenderer.drawStringWithShadow("Portal Controller", xSize / 2 - fontRenderer.getStringWidth("Portal Controller") / 2, -13, 0xFFFFFF);

        // Main Titles
        fontRenderer.drawString("Network", 8, 90, 0x404040);
        
        // Sub Titles
        fontRenderer.drawString("Portal", 8, 8, 0x404040);
        fontRenderer.drawString("Particles", 8, 50, 0x404040);
        
        // Portal
        fontRenderer.drawString("Obsidian", 55, 26, 0x404040);
        
        // Particles
        fontRenderer.drawString("Hearts", 55, 67, 0x404040);
        
        // Icons
        itemRenderer.renderItemIntoGUI(fontRenderer, mc.renderEngine, new ItemStack(49, 0, 1), 31, 21);
    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3)
    {
        super.mouseClicked(par1, par2, par3);
    }

    protected int getLeft()
    {
        return guiLeft;
    }

    protected int getTop()
    {
        return guiTop;
    }

    protected int getYSize()
    {
        return ySize;
    }
}
