package enhancedportals.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import enhancedportals.EnhancedPortals;
import enhancedportals.client.gui.elements.ElementRedstoneFlux;
import enhancedportals.client.gui.elements.ElementScrollDiallingDevice;
import enhancedportals.client.gui.tabs.TabRedstoneFlux;
import enhancedportals.client.gui.tabs.TabTip;
import enhancedportals.inventory.ContainerDimensionalBridgeStabilizer;
import enhancedportals.network.packet.PacketGuiData;
import enhancedportals.tile.TileStabilizerMain;
import enhancedportals.utility.GeneralUtils;

public class GuiDimensionalBridgeStabilizer extends BaseGui
{
    public static final int CONTAINER_SIZE = 90, CONTAINER_SIZE_SMALL = 44;
    TileStabilizerMain stabilizer;

    public GuiDimensionalBridgeStabilizer(TileStabilizerMain s, EntityPlayer p)
    {
        super(new ContainerDimensionalBridgeStabilizer(s, p.inventory), GeneralUtils.hasEnergyCost() ? CONTAINER_SIZE : CONTAINER_SIZE_SMALL);
        stabilizer = s;
        name = "gui.dimensionalBridgeStabilizer";
        setCombinedInventory();
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
    	// Triggered when the + and - buttons are pushed for Instability.
        if((button.id == 0)||(button.id == 1)){
        	String key=(button.id==0?"increase":"decrease")+"_powerState";
            NBTTagCompound tag = new NBTTagCompound();
            tag.setBoolean(key, false);
            // Sent to ContainerDBS.java.
            EnhancedPortals.packetPipeline.sendToServer(new PacketGuiData(tag));
        }
    }
    
    @Override
    public void initGui()
    {
        super.initGui();
        
        if (GeneralUtils.hasEnergyCost())
        {
        	GuiButton add_risk=new GuiButton(0,guiLeft+65,guiTop+containerSize-53,10,10,"+");
        	GuiButton minus_risk=new GuiButton(1,guiLeft+77,guiTop+containerSize-53,10,10,"-");
            buttonList.add(add_risk);
            buttonList.add(minus_risk);
            addElement(new ElementRedstoneFlux(this, xSize - 23, 18, stabilizer.getEnergyStorage()));
            //addElement(new ElementScrollStabilizer(this, stabilizer, 7, 28));
            addTab(new TabRedstoneFlux(this, stabilizer));
        }
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        super.drawGuiContainerBackgroundLayer(f, i, j);
        
        mc.renderEngine.bindTexture(playerInventoryTexture);
        drawTexturedModalRect(guiLeft + xSize - 25, guiTop + containerSize - 26, 7, 7, 18, 18);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        super.drawGuiContainerForegroundLayer(par1, par2);
        
        String s1 = "" + stabilizer.intActiveConnections * 2;
        getFontRenderer().drawString(EnhancedPortals.localize("gui.information"), 8, 18, 0x404040);
        getFontRenderer().drawString(EnhancedPortals.localize("gui.activePortals"), 12, 28, 0x777777);
        getFontRenderer().drawString(s1, xSize - 27 - getFontRenderer().getStringWidth(s1), 28, 0x404040);

        if (GeneralUtils.hasEnergyCost())
        {
            int instability = stabilizer.powerState == 0 ? stabilizer.instability : stabilizer.powerState == 1 ? 20 : stabilizer.powerState == 2 ? 50 : 70;
            String s2 = instability + "%";
            
            getFontRenderer().drawString(EnhancedPortals.localize("gui.instability"), 12, 38, 0x777777);
            getFontRenderer().drawString(s2, xSize - 27 - getFontRenderer().getStringWidth(s2), 38, instability == 0 ? 0x00BB00 : instability == 20 ? 0xDD6644 : instability == 50 ? 0xDD4422 : 0xFF0000);
        }
    }
    
    @Override
    public void updateScreen()
    {
        super.updateScreen();
        /*
        if (GeneralUtils.hasEnergyCost()){
        	((GuiButton) buttonList.get(0)).displayString=getPowerStateLocString();
        }*/
    }
}
