package uk.co.shadeddimensions.ep3.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import uk.co.shadeddimensions.ep3.container.ContainerModuleManipulator;
import uk.co.shadeddimensions.ep3.lib.Localization;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileModuleManipulator;
import uk.co.shadeddimensions.library.gui.GuiBaseContainer;

public class GuiModuleManipulator extends GuiBaseContainer
{
    TileModuleManipulator module;

    public GuiModuleManipulator(TileModuleManipulator t, EntityPlayer player)
    {
        super(new ContainerModuleManipulator(t, player), new ResourceLocation("enhancedportals", "textures/gui/moduleManipulator.png"));
        module = t;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        super.drawGuiContainerForegroundLayer(par1, par2);

        fontRenderer.drawStringWithShadow(Localization.getGuiString("moduleManipulator"), xSize / 2 - fontRenderer.getStringWidth(Localization.getGuiString("moduleManipulator")) / 2, -13, 0xFFFFFF);
        fontRenderer.drawString(Localization.getGuiString("modules"), 8, 8, 0x404040);
    }
}
