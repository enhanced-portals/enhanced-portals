package enhancedportals.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import enhancedportals.EnhancedPortals;
import enhancedportals.inventory.ContainerModuleManipulator;
import enhancedportals.tileentity.TileModuleManipulator;

public class GuiModuleManipulator extends BaseGui
{
    public static final int CONTAINER_SIZE = 53;
    TileModuleManipulator module;

    public GuiModuleManipulator(TileModuleManipulator m, EntityPlayer p)
    {
        super(new ContainerModuleManipulator(m, p.inventory), CONTAINER_SIZE);
        module = m;
        name = "gui.moduleManipulator";
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        super.drawGuiContainerForegroundLayer(par1, par2);
        getFontRenderer().drawString(EnhancedPortals.localize("gui.modules"), 8, containerSize - 35, 0x404040);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        super.drawGuiContainerBackgroundLayer(f, i, j);
        
        mc.renderEngine.bindTexture(playerInventoryTexture);
        drawTexturedModalRect(guiLeft + 7, guiTop + containerSize - 25, 7, 7, 162, 18);
    }
}
