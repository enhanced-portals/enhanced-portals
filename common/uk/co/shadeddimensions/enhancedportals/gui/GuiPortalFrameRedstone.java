package uk.co.shadeddimensions.enhancedportals.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.enhancedportals.container.ContainerPortalFrameRedstone;
import uk.co.shadeddimensions.enhancedportals.network.packet.MainPacket;
import uk.co.shadeddimensions.enhancedportals.network.packet.PacketGuiButtonPressed;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameRedstone;
import cpw.mods.fml.common.network.PacketDispatcher;

public class GuiPortalFrameRedstone extends GuiEnhancedPortals
{
    TilePortalFrameRedstone redstone;
    EntityPlayer player;
    String expandedText;

    public GuiPortalFrameRedstone(EntityPlayer play, TilePortalFrameRedstone tile)
    {
        super(new ContainerPortalFrameRedstone(tile), tile);

        redstone = tile;
        player = play;
        expandedText = "";
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        GL11.glColor4f(1f, 1f, 1f, 1f);
        mc.renderEngine.bindTexture(new ResourceLocation("enhancedportals", "textures/gui/frameRedstoneController.png"));
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        super.drawGuiContainerForegroundLayer(par1, par2);

        fontRenderer.drawStringWithShadow(StatCollector.translateToLocal("tile.ep2.portalFrame.redstone.name"), xSize / 2 - fontRenderer.getStringWidth(StatCollector.translateToLocal("tile.ep2.portalFrame.redstone.name")) / 2, -13, 0xFFFFFF);
        fontRenderer.drawSplitString(expandedText, 8, 55, xSize - 16, 0x404040);
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();

        String stateText = "";
        boolean flag = redstone.output;

        switch (redstone.getState())
        {
            default:
                expandedText = "";
                break;

            case 0:
                stateText = StatCollector.translateToLocal("gui.ep2.redstone." + (flag ? "output.portalCreated" : "input.portalOnSignal"));
                expandedText = StatCollector.translateToLocal("gui.ep2.redstone." + (flag ? "output.portalCreated.desc" : "input.portalOnSignal.desc"));
                break;

            case 1:
                stateText = StatCollector.translateToLocal("gui.ep2.redstone." + (flag ? "output.portalRemoved" : "input.portalNoSignal"));
                expandedText = StatCollector.translateToLocal("gui.ep2.redstone." + (flag ? "output.portalRemoved.desc" : "input.portalNoSignal.desc"));
                break;

            case 2:
                stateText = StatCollector.translateToLocal("gui.ep2.redstone." + (flag ? "output.portalActive" : "input.portalOnPulse"));
                expandedText = StatCollector.translateToLocal("gui.ep2.redstone." + (flag ? "output.portalActive.desc" : "input.portalOnPulse.desc"));
                break;

            case 3:
                stateText = StatCollector.translateToLocal("gui.ep2.redstone." + (flag ? "output.portalInactive" : "input.noPortalOnPulse"));
                expandedText = StatCollector.translateToLocal("gui.ep2.redstone." + (flag ? "output.portalInactive.desc" : "input.noPortalOnPulse.desc"));
                break;

            case 4:
                stateText = StatCollector.translateToLocal("gui.ep2.redstone.output.entityTouch");
                expandedText = StatCollector.translateToLocal("gui.ep2.redstone.output.entityTouch.desc");
                break;
        }

        ((GuiButton) buttonList.get(0)).displayString = redstone.output ? StatCollector.translateToLocal("gui.ep2.button.output") : StatCollector.translateToLocal("gui.ep2.button.input");
        ((GuiButton) buttonList.get(1)).displayString = stateText;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();

        buttonList.add(new GuiButton(0, guiLeft + 8, guiTop + 8, xSize - 16, 20, ""));
        buttonList.add(new GuiButton(1, guiLeft + 8, guiTop + 30, xSize - 16, 20, ""));
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        PacketDispatcher.sendPacketToServer(MainPacket.makePacket(new PacketGuiButtonPressed(button.id)));
    }
}
