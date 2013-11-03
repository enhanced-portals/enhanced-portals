package uk.co.shadeddimensions.ep3.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.ep3.client.gui.base.GuiEnhancedPortals;
import uk.co.shadeddimensions.ep3.container.ContainerDimensionalBridgeStabilizer;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.tileentity.TileStabilizer;

public class GuiDimensionalBridgeStabilizer extends GuiEnhancedPortals
{
    TileStabilizer DBS;
    
    public GuiDimensionalBridgeStabilizer(TileStabilizer stabilizer, EntityPlayer player)
    {
        super(new ContainerDimensionalBridgeStabilizer(stabilizer, player), stabilizer);
        DBS = stabilizer;
        ySize = 31;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        super.drawGuiContainerBackgroundLayer(f, i, j);
        
        GL11.glColor4f(1f, 1f, 1f, 1f);
        mc.renderEngine.bindTexture(new ResourceLocation("enhancedportals", "textures/gui/dimensionalBridgeStabilizer.png"));
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);        
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        super.drawGuiContainerForegroundLayer(par1, par2);
        
        fontRenderer.drawStringWithShadow(StatCollector.translateToLocal(CommonProxy.blockStabilizer.getUnlocalizedName() + ".name"), xSize / 2 - fontRenderer.getStringWidth(StatCollector.translateToLocal(CommonProxy.blockStabilizer.getUnlocalizedName() + ".name")) / 2, -13, 0xFFFFFF);
        fontRenderer.drawString("Active Portals:", 7, 7, 0x777777);
        fontRenderer.drawString("Power Usage:", 7, 17, 0x777777);
        
        String s1 = "" + DBS.intActiveConnections, s2 = "" + (DBS.intActiveConnections * 100);
        fontRenderer.drawString(s1, xSize - 7 - fontRenderer.getStringWidth(s1), 7, 0x404040);
        fontRenderer.drawString(s2, xSize - 7 - fontRenderer.getStringWidth(s2), 17, 0x404040);
    }
    
    @Override
    public void initGui()
    {
        super.initGui();
    }
}
