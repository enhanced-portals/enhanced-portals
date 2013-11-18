package uk.co.shadeddimensions.ep3.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.ep3.container.ContainerDimensionalBridgeStabilizer;
import uk.co.shadeddimensions.ep3.network.ClientProxy;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.tileentity.TileStabilizerMain;
import uk.co.shadeddimensions.ep3.util.GuiPayload;
import cofh.gui.GuiBase;
import cofh.gui.element.ElementEnergyStored;

public class GuiDimensionalBridgeStabilizer extends GuiBase
{
    TileStabilizerMain DBS;
    
    public GuiDimensionalBridgeStabilizer(TileStabilizerMain stabilizer, EntityPlayer player)
    {
        super(new ContainerDimensionalBridgeStabilizer(stabilizer, player), new ResourceLocation("enhancedportals", "textures/gui/dimensionalBridgeStabilizer.png"));
        DBS = stabilizer;
        ySize = 176;
        drawInventory = false;
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        super.drawGuiContainerForegroundLayer(par1, par2);        
        GL11.glDisable(GL11.GL_LIGHTING);
        
        int instability = DBS.powerState == 0 ? DBS.instability : DBS.powerState == 1 ? 20 : DBS.powerState == 2 ? 50 : 70;
        fontRenderer.drawStringWithShadow(StatCollector.translateToLocal(CommonProxy.blockStabilizer.getUnlocalizedName() + ".name"), xSize / 2 - fontRenderer.getStringWidth(StatCollector.translateToLocal(CommonProxy.blockStabilizer.getUnlocalizedName() + ".name")) / 2, -13, 0xFFFFFF);
        fontRenderer.drawString("Information", 7, 7, 0x404040);
        fontRenderer.drawString("Active Portals:", 7, 17, 0x777777);
        fontRenderer.drawString("RF/s:", 7, 27, 0x777777);
        fontRenderer.drawString("Instability:", 7, 37, 0x777777);
        fontRenderer.drawString("Inventory", 7, 81, 0x404040);
        
        int powerCost = (DBS.intActiveConnections * CommonProxy.REDSTONE_FLUX_COST) * CommonProxy.redstoneFluxPowerMultiplier;
        powerCost -= (int)(powerCost * (instability / 100f));
        
        String s1 = "" + (DBS.intActiveConnections * 2), s2 = powerCost + " RF", s3 = instability + "%";
        fontRenderer.drawString(s1, xSize - 27 - fontRenderer.getStringWidth(s1), 17, 0x404040);
        fontRenderer.drawString(s2, xSize - 27 - fontRenderer.getStringWidth(s2), 27, 0x404040);
        fontRenderer.drawString(s3, xSize - 27 - fontRenderer.getStringWidth(s3), 37, instability == 0 ? 0x00DD00 : instability == 20 ? 0xDD6644 : instability == 50 ? 0xDD4422 : 0xFF0000);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();
        
        addElement(new ElementEnergyStored(this, xSize - 24, 10, DBS.getEnergyStorage()));
        buttonList.add(new GuiButton(0, guiLeft + 7, guiTop + 56, 140, 20, CommonProxy.redstoneFluxPowerMultiplier == 0 ? "Mode: Free" : "Mode: Normal"));
        
        if (CommonProxy.redstoneFluxPowerMultiplier == 0)
        {
            ((GuiButton) buttonList.get(0)).enabled = false;
        }
    }
    
    @Override
    public void updateScreen()
    {
        super.updateScreen();
        
        ((GuiButton) buttonList.get(0)).displayString = "Mode: " + (DBS.powerState == 0 ? "Normal" : DBS.powerState == 1 ? "Risky" : DBS.powerState == 2 ? "Unstable" : "Unpredictable");
    }
    
    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 0)
        {
            GuiPayload payload = new GuiPayload();
            payload.data.setBoolean("button", false);
            ClientProxy.sendGuiPacket(payload);
        }
    }
}
