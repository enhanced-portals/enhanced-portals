package uk.co.shadeddimensions.ep3.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import uk.co.shadeddimensions.ep3.container.ContainerModuleManipulator;
import uk.co.shadeddimensions.ep3.lib.Reference;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileModuleManipulator;
import cofh.gui.GuiBase;

public class GuiModuleManipulator extends GuiBase
{
    TileModuleManipulator module;

    public GuiModuleManipulator(TileModuleManipulator t, EntityPlayer player)
    {
        super(new ContainerModuleManipulator(t, player), new ResourceLocation("enhancedportals", "textures/gui/moduleManipulator.png"));
        module = t;
        drawInventory = false;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        super.drawGuiContainerForegroundLayer(par1, par2);

        fontRenderer.drawStringWithShadow(StatCollector.translateToLocal("tile." + Reference.SHORT_ID + ".portalFrame.upgrade.name"), xSize / 2 - fontRenderer.getStringWidth(StatCollector.translateToLocal("tile." + Reference.SHORT_ID + ".portalFrame.upgrade.name")) / 2, -13, 0xFFFFFF);

        fontRenderer.drawString(StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".modules"), 7, 8, 0x404040);
        fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 7, 70, 0x404040);
    }
}
