package uk.co.shadeddimensions.enhancedportals.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.enhancedportals.container.ContainerPortalFrameController;
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
                fontRenderer.drawString(String.format("Frames: %s", controller.getAttachedFrames() - controller.getAttachedFrameRedstone()), x + 5, y + 25, textColour);
                fontRenderer.drawString(String.format("RS Controllers: %s", controller.getAttachedFrameRedstone()), x + 5, y + 35, textColour);
                fontRenderer.drawString(String.format("Portals: %s", controller.getAttachedPortals()), x + 5, y + 45, textColour);
            }

            itemRenderer.renderItemIntoGUI(fontRenderer, mc.renderEngine, new ItemStack(CommonProxy.blockFrame, 1, 1), x + 3, y + 4);
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
    
    static GuiGlyphElement[] elementList = new GuiGlyphElement[27];
    static boolean isChanging = false, expanding = false, expanded = false;
    static final int MIN_SIZE = 122, MAX_SIZE = 215;
    static int currentSize = MIN_SIZE;

    static
    {        
        for (int i = 0; i < elementList.length; i++)
        {
            int x = (i % 9) * 18;
            int y = (i / 9) * 18;
            
            elementList[i] = new GuiGlyphElement(new ItemStack(i + 1, 1, 0), true, x, y);
        }
    }
    
    public GuiPortalFrameController(EntityPlayer play, TilePortalFrameController tile)
    {
        super(new ContainerPortalFrameController(tile), tile);

        controller = tile;
        player = play;
        ySize = MIN_SIZE;
    }
    
    private void toggleState()
    {
        isChanging = true;
        
        if (expanded)
        {
            expanded = false;
            updateButtons();
        }
    }
    
    private void updateButtons()
    {
        ((GuiButton) buttonList.get(0)).drawButton = expanded;
        ((GuiButton) buttonList.get(1)).drawButton = expanded;
    }
    
    @Override
    public void updateScreen()
    {
        super.updateScreen();
        
        if (isShiftKeyDown())
        {
            ((GuiButton) buttonList.get(0)).displayString = EnumChatFormatting.AQUA + "Clear";
            ((GuiButton) buttonList.get(1)).displayString = (isCtrlKeyDown() ? EnumChatFormatting.GOLD : EnumChatFormatting.AQUA) + "Random";
        }
        else
        {
            ((GuiButton) buttonList.get(0)).displayString = "Cancel";
            ((GuiButton) buttonList.get(1)).displayString = "Save";
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        GL11.glColor4f(1f, 1f, 1f, 1f);
        mc.renderEngine.func_110577_a(new ResourceLocation("enhancedportals", "textures/gui/frameController.png"));
        
        drawTexturedModalRect(guiLeft, guiTop + 40, 0, 235 - currentSize, xSize, currentSize - 114);        
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, 40); // Draw in the static top
        
        drawItemSlotBackground(6, 20, xSize - 12, 18);
        
        if (expanded)
        {
            drawItemSlotBackground(6, 55, xSize - 12, 56);
            
            for (int k = 0; k < elementList.length; k++)
            {
                elementList[k].draw(fontRenderer, mc.renderEngine, itemRenderer, guiLeft + 8, guiTop + 57);
            }
        }
        
        // Logic for updating background -- can't be in update because that doesn't get called as frequently..        
        if (isChanging)
        {
            if (expanding)
            {
                if (currentSize < MAX_SIZE)
                {
                    currentSize += 2;
                }
                else
                {
                    expanding = false;
                    expanded = true;
                    updateButtons();
                    isChanging = false;
                }
            }
            else
            {
                if (currentSize > MIN_SIZE)
                {
                    currentSize -= 2;
                }
                else
                {
                    expanding = true;
                    isChanging = false;
                }
            }
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        super.drawGuiContainerForegroundLayer(par1, par2);

        fontRenderer.drawStringWithShadow("Portal Controller", xSize / 2 - fontRenderer.getStringWidth("Portal Controller") / 2, -13, 0xFFFFFF);
        fontRenderer.drawString("Unique Identifier", 8, 8, 0x404040);
        
        if (expanded)
        {
            fontRenderer.drawString("Glyphs", 8, 43, 0x404040);
        }
        else if (!expanded && !isChanging)
        {
            if (par1 >= guiLeft + 10 && par1 <= guiLeft + xSize - 10)
            {
                if (par2 >= guiTop + 20 && par2 <= guiTop + 36)
                {
                    List<String> list = new ArrayList<String>();
                    list.add("Click to modify");
                    
                    drawHoveringText(list, par1 - guiLeft, par2 - guiTop, fontRenderer);
                }
            }
        }        
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();
        
        buttonList.add(new GuiButton(0, guiLeft + 10, guiTop + 116, ((xSize - 20) / 2) - 5, 20, "Cancel"));
        buttonList.add(new GuiButton(1, guiLeft + (xSize / 2) + 6, guiTop + 116, ((xSize - 20) / 2) - 5, 20, "Save"));
        
        updateButtons();
    }
    
    @Override
    protected void initLedgers(IInventory inventory)
    {
        ledgerManager.add(new InformationLedger());
    }
    
    @Override
    protected void mouseClicked(int par1, int par2, int mouseButton)
    {
        super.mouseClicked(par1, par2, mouseButton);

        if (par1 >= guiLeft + 10 && par1 <= guiLeft + xSize - 10)
        {
            if (par2 >= guiTop + 20 && par2 <= guiTop + 36)
            {
                if (!expanded && !isChanging)
                {
                    toggleState();
                }
            }
        }
    }
    
    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (isShiftKeyDown())
        {
            if (button.id == 0)
            {
                // clear
            }
            else if (button.id == 1)
            {
                boolean forceMaximum = isCtrlKeyDown();
                
                // random
            }
        }
        else
        {
            if (button.id == 0)
            {
                // reset changes
                
                toggleState();
            }
            else if (button.id == 1)
            {
                // save changes
                
                toggleState();
            }
        }
    }
}
