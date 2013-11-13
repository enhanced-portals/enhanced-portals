package uk.co.shadeddimensions.ep3.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.ep3.container.ContainerDimensionalBridgeStabilizer;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.tileentity.TileStabilizerMain;
import cofh.gui.GuiBase;
import cofh.gui.element.ElementEnergyStored;

public class GuiDimensionalBridgeStabilizer extends GuiBase
{
    TileStabilizerMain DBS;
    
    public GuiDimensionalBridgeStabilizer(TileStabilizerMain stabilizer, EntityPlayer player)
    {
        super(new ContainerDimensionalBridgeStabilizer(stabilizer, player), new ResourceLocation("enhancedportals", "textures/gui/dimensionalBridgeStabilizer.png"));
        DBS = stabilizer;
        ySize = 67;
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        super.drawGuiContainerForegroundLayer(par1, par2);        
        GL11.glDisable(GL11.GL_LIGHTING);
        
        fontRenderer.drawStringWithShadow(StatCollector.translateToLocal(CommonProxy.blockStabilizer.getUnlocalizedName() + ".name"), xSize / 2 - fontRenderer.getStringWidth(StatCollector.translateToLocal(CommonProxy.blockStabilizer.getUnlocalizedName() + ".name")) / 2, -13, 0xFFFFFF);
        fontRenderer.drawString("Information", 7, 7, 0x404040);
        fontRenderer.drawString("Active Portals:", 7, 17, 0x777777);
        fontRenderer.drawString("RF/s:", 7, 27, 0x777777);
        
        String s1 = "" + (DBS.intActiveConnections * 2), s2 = ((DBS.intActiveConnections * CommonProxy.REDSTONE_FLUX_COST) * CommonProxy.redstoneFluxPowerMultiplier) + " RF";
        fontRenderer.drawString(s1, xSize - 27 - fontRenderer.getStringWidth(s1), 17, 0x404040);
        fontRenderer.drawString(s2, xSize - 27 - fontRenderer.getStringWidth(s2), 27, 0x404040);
        
        fontRenderer.drawStringWithShadow("Instability: " + DBS.instability, 10, ySize + 10, DBS.instability < 20 ? 0x00FF00 : DBS.instability < 50 ? 0xFFFF00 : 0xFF0000);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();
        
        addElement(new ElementEnergyStored(this, xSize - 23, ((ySize / 2) - 21), DBS.getIEnergyStorage()));
        buttonList.add(new GuiButton(0, guiLeft + 7, guiTop + 40, 143, 20, CommonProxy.redstoneFluxPowerMultiplier == 0 ? "Power Mode: Free" : "Power Mode: Normal"));
        
        if (CommonProxy.redstoneFluxPowerMultiplier == 0)
        {
            ((GuiButton) buttonList.get(0)).enabled = false;
        }
    }
}
