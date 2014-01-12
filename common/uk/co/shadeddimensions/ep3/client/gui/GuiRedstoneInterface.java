package uk.co.shadeddimensions.ep3.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import uk.co.shadeddimensions.ep3.lib.Localization;
import uk.co.shadeddimensions.ep3.network.ClientProxy;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileRedstoneInterface;
import uk.co.shadeddimensions.ep3.util.GuiPayload;
import uk.co.shadeddimensions.library.gui.GuiBase;

public class GuiRedstoneInterface extends GuiBase
{
    TileRedstoneInterface redstone;

    public GuiRedstoneInterface(TileRedstoneInterface tile)
    {
        super(new ResourceLocation("enhancedportals", "textures/gui/redstoneInterface.png"));
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
        drawCenteredString(fontRenderer, Localization.getGuiString("redstoneInterface"), xSize / 2, -13, 0xFFFFFF);
        
        super.drawGuiContainerForegroundLayer(par1, par2);
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
    public void updateScreen()
    {
        super.updateScreen();

        String stateText = "";
        boolean flag = redstone.isOutput;

        switch (redstone.state)
        {
            case 0:
                stateText = flag ? Localization.getGuiString("portalCreated") : Localization.getGuiString("createPortalOnSignal");
                break;

            case 1:
                stateText = flag ? Localization.getGuiString("portalRemoved") : Localization.getGuiString("removePortalOnSignal");
                break;

            case 2:
                stateText = flag ? Localization.getGuiString("portalActive") : Localization.getGuiString("createPortalOnPulse");
                break;

            case 3:
                stateText = flag ? Localization.getGuiString("portalInactive") : Localization.getGuiString("removePortalOnPulse");
                break;

            case 4:
                stateText = flag ? Localization.getGuiString("entityTeleport") : Localization.getGuiString("dialStoredIdentifier");
                break;

            case 5:
                stateText = flag ? Localization.getGuiString("playerTeleport") : Localization.getGuiString("dialStoredIdentifier2");
                break;

            case 6:
                stateText = flag ? Localization.getGuiString("animalTeleport") : Localization.getGuiString("dialRandomIdentifier");
                break;

            case 7:
                stateText = flag ? Localization.getGuiString("monsterTeleport") : Localization.getGuiString("dialRandomIdentifier2");
                break;
        }

        ((GuiButton) buttonList.get(0)).displayString = redstone.isOutput ? Localization.getGuiString("output") : Localization.getGuiString("input");
        ((GuiButton) buttonList.get(1)).displayString = stateText;
    }
}
