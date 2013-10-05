package uk.co.shadeddimensions.ep3.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.ep3.container.ContainerModuleManipulator;
import uk.co.shadeddimensions.ep3.lib.Reference;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileModuleManipulator;

public class GuiModuleManipulator extends GuiEnhancedPortals
{
    TileModuleManipulator module;

    public GuiModuleManipulator(EntityPlayer player, TileModuleManipulator t)
    {
        super(new ContainerModuleManipulator(player, t), t);
        module = t;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        GL11.glColor4f(1f, 1f, 1f, 1F);
        getMinecraft().renderEngine.bindTexture(new ResourceLocation("enhancedportals", "textures/gui/moduleManipulator.png"));
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
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
