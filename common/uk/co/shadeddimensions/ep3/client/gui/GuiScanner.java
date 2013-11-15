package uk.co.shadeddimensions.ep3.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import uk.co.shadeddimensions.ep3.container.ContainerScanner;
import uk.co.shadeddimensions.ep3.tileentity.TileScanner;
import cofh.gui.GuiBase;

public class GuiScanner extends GuiBase
{
    public GuiScanner(TileScanner scanner, EntityPlayer player)
    {
        super(new ContainerScanner(scanner, player), new ResourceLocation("enhancedportals", "textures/gui/scanner.png"));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y)
    {
        super.drawGuiContainerForegroundLayer(x, y);
        
        fontRenderer.drawString("Scanner", 7, 7, 0x404040);
        fontRenderer.drawString("Inventory", 7, 70, 0x404040);
    }
}
