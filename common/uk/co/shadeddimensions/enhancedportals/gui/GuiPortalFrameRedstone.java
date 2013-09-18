package uk.co.shadeddimensions.enhancedportals.gui;

import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;
import uk.co.shadeddimensions.enhancedportals.EnhancedPortals;
import uk.co.shadeddimensions.enhancedportals.container.ContainerPortalFrameRedstone;
import uk.co.shadeddimensions.enhancedportals.network.packet.MainPacket;
import uk.co.shadeddimensions.enhancedportals.network.packet.PacketGuiButtonPressed;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameRedstone;
import cpw.mods.fml.common.network.PacketDispatcher;

public class GuiPortalFrameRedstone extends GuiResizable
{
    TilePortalFrameRedstone redstone;
    EntityPlayer player;
    String expandedText, oldText;

    public GuiPortalFrameRedstone(EntityPlayer play, TilePortalFrameRedstone tile)
    {
        super(new ContainerPortalFrameRedstone(tile), tile, 176, 58);

        redstone = tile;
        player = play;
        expandedText = oldText = "";

        ySize += 40;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        super.drawGuiContainerBackgroundLayer(f, i, j);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        super.drawGuiContainerForegroundLayer(par1, par2);

        fontRenderer.drawStringWithShadow(StatCollector.translateToLocal("tile.ep2.portalFrame.redstone.name"), xSize / 2 - fontRenderer.getStringWidth(StatCollector.translateToLocal("tile.ep2.portalFrame.redstone.name")) / 2, -13, 0xFFFFFF);

        if (MAX_HEIGHT > 58 && !isChanging && EnhancedPortals.config.getBoolean("showExtendedRedstoneInformation"))
        {
            fontRenderer.drawSplitString(expandedText, 8, 55, xSize - 16, 0x404040);
        }
    }

    @SuppressWarnings("rawtypes")
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
        
        if (EnhancedPortals.config.getBoolean("showExtendedRedstoneInformation"))
        {
            if (!expandedText.equals(oldText))
            {
                oldText = expandedText;
                List list = getMinecraft().fontRenderer.listFormattedStringToWidth(expandedText, xSize - 16);
                int tmpHeight = 58 + list.size() * getMinecraft().fontRenderer.FONT_HEIGHT + 5;

                if (MAX_HEIGHT != tmpHeight)
                {
                    MAX_HEIGHT = tmpHeight;

                    if (CURRENT_HEIGHT > MAX_HEIGHT)
                    {
                        MIN_HEIGHT = MAX_HEIGHT;
                        expanding = false;
                    }
                    else
                    {
                        expanding = true;
                    }

                    if (!isChanging)
                    {
                        isChanging = true;
                    }
                }
            }
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
