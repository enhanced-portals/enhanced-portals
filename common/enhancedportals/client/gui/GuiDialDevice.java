package enhancedportals.client.gui;

import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.entity.RenderItem;
import cpw.mods.fml.client.FMLClientHandler;
import enhancedportals.lib.Localization;
import enhancedportals.tileentity.TileEntityDialDevice;

public class GuiDialDevice extends GuiScreen
{
    TileEntityDialDevice     dialDevice;
    int                      guiTop       = 0, guiLeft = 0, xSize = 176, ySize = 166;
    GuiDialDeviceNetworkList networkList;
    public RenderItem        itemRenderer = new RenderItem();

    public GuiDialDevice(TileEntityDialDevice dialdevice)
    {
        dialDevice = dialdevice;
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 1)
        {
            // TODO PacketDispatcher.sendPacketToServer(new PacketGui(true, false, GuiIds.DialDeviceAdd, dialDevice).getPacket());
        }
        else if (button.id == 2)
        {
            if (getSelected() >= 0 && !getSize().isEmpty() && getSelected() < getSize().size())
            {
                getSize().get(getSelected());

                // TODO PacketDispatcher.sendPacketToServer(new PacketDialRequest(dialDevice.xCoord, dialDevice.yCoord, dialDevice.zCoord, dialDevice.worldObj.provider.dimensionId, obj.network, obj.texture, obj.thickness, obj.sounds, obj.particles).getPacket());
                FMLClientHandler.instance().getClient().thePlayer.closeScreen();
            }
        }
        else if (button.id == 3)
        {
            if (getSelected() >= 0 && !getSize().isEmpty() && getSelected() < getSize().size())
            {
                getSize().get(getSelected());

                // TODO PacketDispatcher.sendPacketToServer(new PacketNetworkData(dialDevice, getSelected(), obj.displayName, obj.network).getPacket());
                getSize().remove(getSelected());
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    public void drawScreen(int x, int y, float par3)
    {
        networkList.drawScreen(x, y, par3);
        fontRenderer.drawString(Localization.localizeString("tile.dialDevice.name"), guiLeft + xSize / 2 - fontRenderer.getStringWidth(Localization.localizeString("tile.dialDevice.name")) / 2, 15, 0xFFCCCCCC);
        super.drawScreen(x, y, par3); // Draw buttons        
    }

    public GuiButton getDeleteButton()
    {
        return (GuiButton) buttonList.get(2);
    }

    public GuiButton getDialButton()
    {
        return (GuiButton) buttonList.get(1);
    }

    public int getSelected()
    {
        return dialDevice.selectedDestination;
    }

    @SuppressWarnings("rawtypes")
    public List getSize()
    {
        return dialDevice.destinationList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();

        networkList = new GuiDialDeviceNetworkList(this);
        networkList.registerScrollButtons(buttonList, 4, 5);

        guiLeft = (width - xSize) / 2;
        guiTop = (height - ySize) / 2;

        buttonList.add(new GuiButton(1, guiLeft - 30, height - 40, 70, 20, "Add"));
        buttonList.add(new GuiButton(2, guiLeft + 45, height - 40, 90, 20, "Dial"));
        buttonList.add(new GuiButton(3, guiLeft + 140, height - 40, 70, 20, "Remove"));

        if (getSelected() < 0 || dialDevice.active)
        {
            ((GuiButton) buttonList.get(1)).enabled = false;
            ((GuiButton) buttonList.get(2)).enabled = false;
        }
    }

    public void onElementSelected(int par1)
    {
        dialDevice.selectedDestination = par1;
    }

    public void select(int par1) // Double click
    {
        actionPerformed(getDialButton());
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
    }
}
