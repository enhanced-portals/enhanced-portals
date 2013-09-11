package uk.co.shadeddimensions.enhancedportals.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

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
        mc.renderEngine.func_110577_a(new ResourceLocation("enhancedportals", "textures/gui/frameRedstoneController.png"));
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        super.drawGuiContainerForegroundLayer(par1, par2);

        fontRenderer.drawStringWithShadow("Redstone Controller", xSize / 2 - fontRenderer.getStringWidth("Redstone Controller") / 2, -13, 0xFFFFFF);
        fontRenderer.drawSplitString(expandedText, 8, 55, xSize - 16, 0x404040);
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();

        String stateText = "";
        boolean flag = redstone.output;

        switch (redstone.getState())
        // TODO: Localization
        {
            default:
                expandedText = "";
                break;

            case 0:
                stateText = flag ? "On Portal Created" : "Create Portal on Signal";
                expandedText = flag ? "Pulse a redstone signal when a portal gets created. Pulse will last for 20 ticks (1 second), with a strength of 15." : "Creates a portal while recieving a redstone signal. Portal will be removed when the signal is interrupted.";
                break;

            case 1:
                stateText = flag ? "On Portal Removed" : "Create Portal Without Signal";
                expandedText = flag ? "Pulse a redstone signal when a portal gets removed. Pulse will last for 20 ticks (1 second), with a strength of 15." : "Creates a portal when no redstone signal is being recieved. Portal will be removed when a redstone signal is applied.";
                break;

            case 2:
                stateText = flag ? "Portal Active" : "Create Portal on Pulse";
                expandedText = flag ? "Emit a constant redstone signal while the portal is active with a strength of 15." : "Creates a portal when recieving a redstone pulse. Portal will not be removed.";
                break;

            case 3:
                stateText = flag ? "Portal Inactive" : "Remove Portal on Pulse";
                expandedText = flag ? "Emit a constant redstone signal while the portal is not active with a strength of 15." : "Removes a portal when recieving a redstone pulse. Portal will not be re-created.";
                break;

            case 4:
                stateText = "Entity Touch";
                expandedText = "Pulse a redstone signal when an entity touches any side of this block. Pulse will last for 20 ticks (1 second), with a strength of 15.";
                break;
        }

        ((GuiButton) buttonList.get(0)).displayString = redstone.output ? "Outputting" : "Recieving";
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
