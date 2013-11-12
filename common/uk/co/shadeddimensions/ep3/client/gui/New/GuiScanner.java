package uk.co.shadeddimensions.ep3.client.gui.New;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import uk.co.shadeddimensions.ep3.client.gui.New.element.ElementGlyphSelector;
import uk.co.shadeddimensions.ep3.container.ContainerScanner;
import uk.co.shadeddimensions.ep3.tileentity.TileScanner;
import cofh.gui.GuiBase;

public class GuiScanner extends GuiBase
{
    public GuiScanner(TileScanner scanner, EntityPlayer player)
    {
        super(new ContainerScanner(scanner, player), new ResourceLocation("enhancedportals", "textures/gui/colourInterface.png"));
        ySize += 10;
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
        
    }
    
    @Override
    public void initGui()
    {
        super.initGui();
        
        addElement(new ElementGlyphSelector(this, 7, 93));
    }
}
