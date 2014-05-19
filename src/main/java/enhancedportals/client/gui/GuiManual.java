package enhancedportals.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import enhancedportals.EnhancedPortals;
import enhancedportals.inventory.ContainerManual;
import enhancedportals.network.ClientProxy;

public class GuiManual extends BaseGui
{
    public static final int CONTAINER_SIZE = 180, CONTAINER_WIDTH = 279;
    static ResourceLocation textureB = new ResourceLocation("enhancedportals", "textures/gui/manualB.png");

    public GuiManual(EntityPlayer p)
    {
        super(new ContainerManual(p.inventory), CONTAINER_SIZE);
        xSize = CONTAINER_WIDTH;
        setHidePlayerInventory();
        texture = new ResourceLocation("enhancedportals", "textures/gui/manualA.png");
    }
    
    @Override
    protected void drawBackgroundTexture()
    {
        mc.renderEngine.bindTexture(texture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, 140, ySize);
        mc.renderEngine.bindTexture(textureB);
        drawTexturedModalRect(guiLeft + 140, guiTop, 0, 0, 139, ySize);
        
        drawTexturedModalRect(guiLeft + CONTAINER_WIDTH - 23, guiTop + CONTAINER_SIZE + 3, 0, 233, 18, 10);
        drawTexturedModalRect(guiLeft + 5, guiTop + CONTAINER_SIZE + 3, 0, 246, 18, 10);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        getFontRenderer().drawSplitString(EnhancedPortals.localize("gui.manual." + ClientProxy.manualPage + ".title"), 19, 12, 110, 0x404040);
        
        if (true) // Temporary
        {
            getFontRenderer().drawSplitString(EnhancedPortals.localize("gui.manual." + ClientProxy.manualPage + ".page.1"), 19, 32, 110, 0x777777);
            getFontRenderer().drawSplitString(EnhancedPortals.localize("gui.manual." + ClientProxy.manualPage + ".page.2"), 152, 12, 110, 0x777777);
        }
        
        super.drawGuiContainerForegroundLayer(par1, par2);
    }
}
