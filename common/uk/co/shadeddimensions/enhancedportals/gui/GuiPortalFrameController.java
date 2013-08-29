package uk.co.shadeddimensions.enhancedportals.gui;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.enhancedportals.network.CommonProxy;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameController;

public class GuiPortalFrameController extends GuiEnhancedPortals
{
    class InformationLedger extends Ledger
    {
        int headerColour = 0xe1c92f;
        int textColour = 0x000000;
                
        public InformationLedger()
        {
            overlayColor = 0x9b2a7e;
            maxHeight = 60;
        }
        
        @Override
        public void draw(int x, int y)
        {
            drawBackground(x, y);
            
            if (isFullyOpened())
            {
                fontRenderer.drawString("Control Info.", x + 25, y + 9, headerColour);
                fontRenderer.drawString(String.format("Frames: %s", (controller.getAttachedFrames() - controller.getAttachedFrameRedstone())), x + 5, y + 25, textColour);
                fontRenderer.drawString(String.format("Redstone Frames: %s", controller.getAttachedFrameRedstone()), x + 5, y + 35, textColour);
                fontRenderer.drawString(String.format("Portals: %s", controller.getAttachedPortals()), x + 5, y + 45, textColour);
            }
            
            itemRenderer.renderItemIntoGUI(fontRenderer, mc.renderEngine, new ItemStack(CommonProxy.blockFrame), x + 3, y + 4);
        }

        @Override
        public ArrayList<String> getTooltip()
        {
            ArrayList<String> strList = new ArrayList<String>();
            
            if (!isFullyOpened())
            {
                strList.add("Control Information");
            }
            
            return strList;
        }        
    }
    
    TilePortalFrameController controller;
    EntityPlayer player;
    
    public GuiPortalFrameController(EntityPlayer play, TilePortalFrameController tile)
    {
        super(new ContainerPortalFrameController(), tile);
        
        controller = tile;
        player = play;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        GL11.glColor4f(1f, 1f, 1f, 1f);
        mc.renderEngine.func_110577_a(new ResourceLocation("enhancedportals", "textures/gui/frameController.png"));
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        
        int xOffset = 18, yOffset = 25;
        
        for (int x = 0; x < 4; x++)
        {
            drawRect(guiLeft + xOffset - 1 + (x * 20), guiTop + yOffset - 1, guiLeft + xOffset + 17 + (x * 20), guiTop + yOffset + 17, 0xff000000);
            drawRect(guiLeft + xOffset + (x * 20), guiTop + yOffset, guiLeft + xOffset + 16 + (x * 20), guiTop + yOffset + 16, 0xffffffff);
        }
        
        for (int x = 0; x < 2; x++)
        {
            drawRect(guiLeft + 100 + xOffset - 1 + (x * 20), guiTop + yOffset - 1, guiLeft + 100 + xOffset + 17 + (x * 20), guiTop + yOffset + 17, 0xff000000);
            drawRect(guiLeft + 100 + xOffset + (x * 20), guiTop + yOffset, guiLeft + 100 + xOffset + 16 + (x * 20), guiTop + yOffset + 16, 0xffffffff);
        }
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        super.drawGuiContainerForegroundLayer(par1, par2);
        
        fontRenderer.drawStringWithShadow("Portal Controller", xSize / 2 - fontRenderer.getStringWidth("Portal Controller") / 2, -13, 0xFFFFFF);        
        fontRenderer.drawString("Textures", 8, 8, 0x404040);
        fontRenderer.drawString("Networking", 8, 50, 0x404040);
    }
    
    @Override
    public void initGui()
    {
        super.initGui();
    }
    
    @Override
    protected void initLedgers(TileEntity tileEntity)
    {
        ledgerManager.add(new InformationLedger());
    }
}
