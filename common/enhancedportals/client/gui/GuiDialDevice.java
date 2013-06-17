package enhancedportals.client.gui;

import java.text.DecimalFormat;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.util.EnumChatFormatting;

import org.lwjgl.input.Mouse;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import enhancedcore.util.MathHelper;
import enhancedportals.lib.GuiIds;
import enhancedportals.lib.Localization;
import enhancedportals.lib.Settings;
import enhancedportals.lib.Strings;
import enhancedportals.network.packet.PacketDialDeviceUpdate;
import enhancedportals.network.packet.PacketDialEntry;
import enhancedportals.network.packet.PacketDialRequest;
import enhancedportals.network.packet.PacketEnhancedPortals;
import enhancedportals.network.packet.PacketGui;
import enhancedportals.portal.network.DialDeviceNetworkObject;
import enhancedportals.tileentity.TileEntityDialDevice;

public class GuiDialDevice extends GuiScreen
{
    TileEntityDialDevice dialDevice;
    int guiTop = 0, guiLeft = 0, xSize = 176, ySize = 166;
    boolean rightClick;
    GuiDialDeviceNetworkList networkList;
    public RenderItem itemRenderer = new RenderItem();

    public GuiDialDevice(TileEntityDialDevice dialdevice)
    {
        dialDevice = dialdevice;
        rightClick = false;
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 1)
        {
            PacketDispatcher.sendPacketToServer(PacketEnhancedPortals.makePacket(new PacketGui(dialDevice, GuiIds.DialDeviceAdd)));
        }
        else if (button.id == 2)
        {
            if (getSelected() >= 0 && !getSize().isEmpty() && getSelected() < getSize().size())
            {
                PacketDispatcher.sendPacketToServer(PacketEnhancedPortals.makePacket(new PacketDialRequest(dialDevice, getSelected() + "")));
                FMLClientHandler.instance().getClient().thePlayer.closeScreen();
            }
        }
        else if (button.id == 3)
        {
            if (getSelected() >= 0 && !getSize().isEmpty() && getSelected() < getSize().size())
            {
                DialDeviceNetworkObject obj = (DialDeviceNetworkObject) getSize().get(getSelected());
                PacketDispatcher.sendPacketToServer(PacketEnhancedPortals.makePacket(new PacketDialEntry(dialDevice, (byte) 1, obj)));
                getSize().remove(getSelected());
            }
        }
        else if (button.id == 4)
        {
            if (isShiftKeyDown() && isCtrlKeyDown())
            {
                dialDevice.tickTimer += rightClick ? -100 : 100;
            }
            else if (isShiftKeyDown())
            {
                dialDevice.tickTimer += rightClick ? -10 : 10;
            }
            else if (isCtrlKeyDown())
            {
                dialDevice.tickTimer += rightClick ? -20 : 20;
            }
            else
            {
                dialDevice.tickTimer += rightClick ? -1 : 1;
            }

            dialDevice.tickTimer = MathHelper.clampInt(dialDevice.tickTimer, 19, 60 * 20 + 1);

            ((GuiButton) buttonList.get(3)).displayString = dialDevice.tickTimer == 1201 || dialDevice.tickTimer == 19 ? Strings.DontShutdownAuto.toString() : EnumChatFormatting.GOLD + (dialDevice.tickTimer + "") + " " + EnumChatFormatting.WHITE + "ticks (" + EnumChatFormatting.GOLD + new DecimalFormat("#.##").format((float) dialDevice.tickTimer / 20) + "" + EnumChatFormatting.WHITE + " seconds)";
            PacketDispatcher.sendPacketToServer(PacketEnhancedPortals.makePacket(new PacketDialDeviceUpdate(dialDevice)));
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

        // Not the best place for this but it will do.        
        GuiButton button = (GuiButton) buttonList.get(3);

        if (Mouse.isButtonDown(1) && button.xPosition <= x && button.yPosition <= y && button.xPosition + 240 >= x && button.yPosition + 20 >= y && !rightClick && Settings.canUse(FMLClientHandler.instance().getClient().thePlayer))
        {
            rightClick = true;
            actionPerformed(button);
            mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);

        }
        else if (rightClick && !Mouse.isButtonDown(1) && Settings.canUse(FMLClientHandler.instance().getClient().thePlayer))
        {
            rightClick = false;
        }
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

        buttonList.add(new GuiButton(1, guiLeft - 30, height - 30, 70, 20, Strings.Add.toString()));
        buttonList.add(new GuiButton(2, guiLeft + 45, height - 30, 90, 20, dialDevice.active ? Strings.Terminate.toString() : Strings.Dial.toString()));
        buttonList.add(new GuiButton(3, guiLeft + 140, height - 30, 70, 20, Strings.Remove.toString()));
        buttonList.add(new GuiButton(4, guiLeft - 30, height - 55, 240, 20, dialDevice.tickTimer == 1201 || dialDevice.tickTimer == 19 ? Strings.DontShutdownAuto.toString() : EnumChatFormatting.GOLD + (dialDevice.tickTimer + "") + " " + EnumChatFormatting.WHITE + "ticks (" + EnumChatFormatting.GOLD + new DecimalFormat("#.##").format((float) dialDevice.tickTimer / 20) + "" + EnumChatFormatting.WHITE + " seconds)"));
        ((GuiButton) buttonList.get(2)).enabled = !dialDevice.active;

        if (!Settings.canUse(FMLClientHandler.instance().getClient().thePlayer))
        {
            ((GuiButton) buttonList.get(0)).drawButton = false;
            ((GuiButton) buttonList.get(2)).drawButton = false;
            ((GuiButton) buttonList.get(3)).drawButton = false;
        }
    }

    @Override
    protected void keyTyped(char par1, int par2)
    {
        super.keyTyped(par1, par2);

        if (par2 == mc.gameSettings.keyBindInventory.keyCode)
        {
            mc.thePlayer.closeScreen();
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

        if (dialDevice.destinationList.isEmpty())
        {
            ((GuiButton) buttonList.get(1)).enabled = false;
            ((GuiButton) buttonList.get(2)).enabled = false;
        }
        else
        {
            if (dialDevice.selectedDestination < 0)
            {
                onElementSelected(0);
            }

            ((GuiButton) buttonList.get(1)).displayString = dialDevice.active ? Strings.Terminate.toString() : Strings.Dial.toString();
            ((GuiButton) buttonList.get(1)).enabled = true;
            ((GuiButton) buttonList.get(2)).enabled = !dialDevice.active;
        }
    }
}
