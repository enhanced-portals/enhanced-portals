package uk.co.shadeddimensions.ep3.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import uk.co.shadeddimensions.ep3.client.gui.element.ElementIconScrollList;
import uk.co.shadeddimensions.ep3.container.ContainerScanner;
import uk.co.shadeddimensions.ep3.network.ClientProxy;
import uk.co.shadeddimensions.ep3.tileentity.TileScanner;
import cofh.gui.GuiBase;
import cofh.gui.element.ElementBase;
import cofh.gui.element.TabBase;

public class GuiScanner extends GuiBase implements IElementHandler
{
    class Tab extends TabBase
    {
        public Tab(GuiBase gui)
        {
            super(gui);
        }

        @Override
        public void draw()
        {
            drawBackground();
        }

        @Override
        public String getTooltip()
        {
            return null;
        }
    }
    
    class LeftTab extends TabBase
    {
        public LeftTab(GuiBase gui)
        {
            super(gui, 0);
            maxHeight += 50;
        }

        @Override
        public void draw()
        {
            drawBackground();
        }

        @Override
        public boolean handleMouseClicked(int x, int y, int mouseButton)
        {
            if (mouseButton != 0)
            {
                //((GuiScanner)gui).toggleState();
                return true;
            }
            
            return super.handleMouseClicked(x, y, mouseButton);
        }
        
        @Override
        public String getTooltip()
        {
            return null;
        }
    }

    public GuiScanner(TileScanner scanner, EntityPlayer player)
    {
        super(new ContainerScanner(scanner, player), new ResourceLocation("enhancedportals", "textures/gui/colourInterface.png"));
        //super(new ContainerScanner(scanner, player), xSizeDefault, ySizeDefault, xSizeDefault, ySizeDefault - 100, false);
        ySize += 5;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y)
    {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y)
    {
        super.drawGuiContainerForegroundLayer(x, y);
        
        fontRenderer.drawString("Custom Texture", 8, 6, 0x404040);
        fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, 77, 0x404040);
    }
    
    @Override
    public void initGui()
    {
        super.initGui();
        addTab(new Tab(this));
        addTab(new LeftTab(this));
        addElement(new ElementIconScrollList(this, texture, 6, 18, 180, 54, ClientProxy.customPortalTextures));
    }

    @Override
    public void onElementChanged(ElementBase element, Object data)
    {
        // TODO Auto-generated method stub
        
    }
}
