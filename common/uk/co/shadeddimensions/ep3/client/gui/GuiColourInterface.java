package uk.co.shadeddimensions.ep3.client.gui;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import uk.co.shadeddimensions.ep3.client.gui.slider.GuiBetterSlider;
import uk.co.shadeddimensions.ep3.client.gui.slider.GuiRGBSlider;
import uk.co.shadeddimensions.ep3.container.ContainerEnhancedPortals;
import uk.co.shadeddimensions.ep3.item.ItemPaintbrush;
import uk.co.shadeddimensions.ep3.lib.Reference;

public class GuiColourInterface extends GuiEnhancedPortals
{
    class ColourLedger extends Ledger
    {
        int headerColour = 0xe1c92f;
        int subheaderColour = 0xaaafb8;
        int textColour = 0x000000;
        
        public ColourLedger()
        {
            overlayColor = 0x5396da;
            maxHeight = 123;
        }
        
        @Override
        public void draw(int x, int y)
        {
            drawBackground(x, y);
            
            getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
            drawIcon(ItemPaintbrush.texture, x + 2, y + 4);
            
            if (isFullyOpened())
            {
                drawString(fontRenderer, StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".colour"), x + 25, y + 8, headerColour);
            }
            
            redSlider.drawButton = greenSlider.drawButton = blueSlider.drawButton = saveButton.drawButton = resetButton.drawButton = isFullyOpened();
        }
        
        @Override
        public boolean handleMouseClicked(int x, int y, int mouseButton)
        {
            if (isFullyOpened() && y >= 65)
            {
                return true;
            }
            
            return super.handleMouseClicked(x, y, mouseButton);
        }

        @Override
        public ArrayList<String> getTooltip()
        {
            ArrayList<String> strList = new ArrayList<String>();
            
            if (!isOpen())
            {
                strList.add("Colour");
                strList.add(EnumChatFormatting.RED + "Red: " + redSlider.getValue());
                strList.add(EnumChatFormatting.GREEN + "Green: " + greenSlider.getValue());
                strList.add(EnumChatFormatting.BLUE + "Blue: " + blueSlider.getValue());
            }
            
            return strList;
        }
    }

    protected ColourLedger ledger;
    protected GuiRGBSlider redSlider, greenSlider, blueSlider;
    protected GuiButton saveButton, resetButton;
    protected Color c;
    
    public GuiColourInterface(ContainerEnhancedPortals container, IInventory inventory)
    {
        super(container, inventory);
        c = new Color(0xFFFFFF);
    }
    
    public GuiColourInterface(ContainerEnhancedPortals container, IInventory inventory, Color color)
    {
        super(container, inventory);
        c = color;
    }
    
    @Override
    protected void initLedgers(IInventory inventory)
    {
        ledger = new ColourLedger();
        ledgerManager.add(ledger);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        super.drawGuiContainerBackgroundLayer(f, i, j);
        
        mc.renderEngine.bindTexture(new ResourceLocation("enhancedportals", "textures/gui/colourInterface.png"));
        GL11.glColor4f(1f, 1f, 1f, 1f);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();
        
        redSlider = new GuiRGBSlider(100, guiLeft + xSize + 5, guiTop + 33, StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".colour.red"), c.getRed() / 255f);
        greenSlider = new GuiRGBSlider(101, guiLeft + xSize + 5, guiTop + 57, StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".colour.green"), c.getGreen() / 255f);
        blueSlider = new GuiRGBSlider(102, guiLeft + xSize + 5, guiTop + 81, StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".colour.blue"), c.getBlue() / 255f);
        
        saveButton = new GuiButton(110, guiLeft + xSize + 5, guiTop + 105, 51, 20, StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".button.save"));
        resetButton = new GuiButton(111, guiLeft + xSize + 68, guiTop + 105, 51, 20, StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".button.reset"));
        
        buttonList.add(redSlider);
        buttonList.add(greenSlider);
        buttonList.add(blueSlider);
        buttonList.add(saveButton);
        buttonList.add(resetButton);
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
