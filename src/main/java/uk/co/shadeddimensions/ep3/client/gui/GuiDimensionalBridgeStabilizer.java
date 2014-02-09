package uk.co.shadeddimensions.ep3.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.ep3.container.ContainerDimensionalBridgeStabilizer;
import uk.co.shadeddimensions.ep3.lib.Localization;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.network.PacketHandlerClient;
import uk.co.shadeddimensions.ep3.tileentity.TileStabilizerMain;
import uk.co.shadeddimensions.ep3.util.GeneralUtils;
import uk.co.shadeddimensions.library.gui.GuiBaseContainer;
import uk.co.shadeddimensions.library.gui.element.ElementRedstoneFlux;
import uk.co.shadeddimensions.library.gui.tab.TabRedstoneFlux;

public class GuiDimensionalBridgeStabilizer extends GuiBaseContainer
{
    class TabRedstoneFluxInfo extends TabRedstoneFlux
    {
        public TabRedstoneFluxInfo(GuiBaseContainer gui, ElementRedstoneFlux f)
        {
            super(gui, f);
            maxHeight += 35;
        }

        @Override
        public void draw()
        {
            super.draw();

            if (isFullyOpened() && elementFlux != null)
            {
                int instability = DBS.powerState == 0 ? DBS.instability : DBS.powerState == 1 ? 20 : DBS.powerState == 2 ? 50 : 70;
                int powerCost = DBS.intActiveConnections * CommonProxy.REDSTONE_FLUX_COST * CommonProxy.redstoneFluxPowerMultiplier;
                powerCost -= (int) (powerCost * (instability / 100f));

                gui.getFontRenderer().drawStringWithShadow("Energy Usage:", posX + 10, posY + 70, 0xAAAAAA);
                gui.getFontRenderer().drawString(powerCost + " RF/s", posX + 17, posY + 83, 0x000000);
                gui.getFontRenderer().drawString(powerCost / 20 + " RF/t", posX + 17, posY + 94, 0x000000);
            }
        }
    }

    TileStabilizerMain DBS;

    public GuiDimensionalBridgeStabilizer(TileStabilizerMain stabilizer, EntityPlayer player)
    {
        super(new ContainerDimensionalBridgeStabilizer(stabilizer, player), new ResourceLocation("enhancedportals", "textures/gui/dimensionalBridgeStabilizer.png"));
        DBS = stabilizer;
        ySize = 176;
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 0)
        {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setBoolean("button", false);
            PacketHandlerClient.sendGuiPacket(tag);
        }
    }

    @Override
    public void addElements()
    {
        if (GeneralUtils.hasEnergyCost())
        {
            addElement(new ElementRedstoneFlux(this, xSize - 23, 10, DBS.getEnergyStored(null), DBS.getMaxEnergyStored(null)));
        }
    }

    @Override
    public void addTabs()
    {
        if (GeneralUtils.hasEnergyCost())
        {
            addTab(new TabRedstoneFluxInfo(this, (ElementRedstoneFlux) elements.get(0)));
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        super.drawGuiContainerForegroundLayer(par1, par2);
        GL11.glDisable(GL11.GL_LIGHTING);
        
        fontRenderer.drawStringWithShadow(Localization.getGuiString("dimensionalBridgeStabilizer"), xSize / 2 - fontRenderer.getStringWidth(Localization.getGuiString("dimensionalBridgeStabilizer")) / 2, -13, 0xFFFFFF);
        fontRenderer.drawString(Localization.getGuiString("information"), 8, 8, 0x404040);
        
        fontRenderer.drawString(Localization.getGuiString("activePortals"), 12, 18, 0x777777);
        String s1 = "" + DBS.intActiveConnections * 2;
        fontRenderer.drawString(s1, xSize - 27 - fontRenderer.getStringWidth(s1), 18, 0x404040);
        
        if (GeneralUtils.hasEnergyCost())
        {
            int instability = DBS.powerState == 0 ? DBS.instability : DBS.powerState == 1 ? 20 : DBS.powerState == 2 ? 50 : 70;        
            fontRenderer.drawString(Localization.getGuiString("instability"), 12, 28, 0x777777);
    
            String s2 = instability + "%";        
            fontRenderer.drawString(s2, xSize - 27 - fontRenderer.getStringWidth(s2), 28, instability == 0 ? 0x00DD00 : instability == 20 ? 0xDD6644 : instability == 50 ? 0xDD4422 : 0xFF0000);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();
        
        if (GeneralUtils.hasEnergyCost())
        {
            buttonList.add(new GuiButton(0, guiLeft + 7, guiTop + 56, 140, 20, CommonProxy.redstoneFluxPowerMultiplier == 0 ? Localization.getGuiString("powerModeFree") : Localization.getGuiString("powerModeNormal")));
        }
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
        
        if (GeneralUtils.hasEnergyCost())
        {
            ((ElementRedstoneFlux) elements.get(0)).setMaximum(DBS.getMaxEnergyStored(null)).setProgress(DBS.getEnergyStored(null));
            ((GuiButton) buttonList.get(0)).displayString = DBS.powerState == 0 ? Localization.getGuiString("powerModeNormal") : DBS.powerState == 1 ? Localization.getGuiString("powerModeRisky") : DBS.powerState == 2 ? Localization.getGuiString("powerModeUnstable") : Localization.getGuiString("powerModeUnpredictable");
        }
    }
}
