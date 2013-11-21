package uk.co.shadeddimensions.ep3.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import uk.co.shadeddimensions.ep3.container.ContainerRedstoneInterface;
import uk.co.shadeddimensions.ep3.lib.Localization;
import uk.co.shadeddimensions.ep3.lib.Reference;
import uk.co.shadeddimensions.ep3.network.ClientProxy;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileRedstoneInterface;
import uk.co.shadeddimensions.ep3.util.GuiPayload;
import cofh.gui.GuiBase;

public class GuiRedstoneInterface extends GuiBase
{
    TileRedstoneInterface redstone;

    public GuiRedstoneInterface(TileRedstoneInterface tile, EntityPlayer play)
    {
        super(new ContainerRedstoneInterface(tile, play), new ResourceLocation("enhancedportals", "textures/gui/redstoneInterface.png"));
        redstone = tile;
        ySize = 58;
        drawInventory = false;
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        GuiPayload payload = new GuiPayload();
        payload.data.setInteger("id", button.id);
        ClientProxy.sendGuiPacket(payload);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        super.drawGuiContainerForegroundLayer(par1, par2);
        drawCenteredString(fontRenderer, Localization.getGuiString("redstoneIdentifier"), xSize / 2, -13, 0xFFFFFF);
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
    public void updateScreen() // TODO: Rewrite when RI has been rewritten
    {
        super.updateScreen();

        String stateText = "";
        boolean flag = redstone.output;

        switch (redstone.getState())
        {
            case 0:
                stateText = StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".redstone." + (flag ? "output.portalCreated" : "input.portalOnSignal"));
                break;

            case 1:
                stateText = StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".redstone." + (flag ? "output.portalRemoved" : "input.portalNoSignal"));
                break;

            case 2:
                stateText = StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".redstone." + (flag ? "output.portalActive" : "input.portalOnPulse"));
                break;

            case 3:
                stateText = StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".redstone." + (flag ? "output.portalInactive" : "input.noPortalOnPulse"));
                break;

            case 4:
                stateText = StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".redstone.output.entityTouch");
                break;
        }

        ((GuiButton) buttonList.get(0)).displayString = redstone.output ? StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".button.output") : StatCollector.translateToLocal("gui." + Reference.SHORT_ID + ".button.input");
        ((GuiButton) buttonList.get(1)).displayString = stateText;
    }
}
