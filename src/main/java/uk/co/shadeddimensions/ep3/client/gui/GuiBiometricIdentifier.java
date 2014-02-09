package uk.co.shadeddimensions.ep3.client.gui;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import uk.co.shadeddimensions.ep3.container.ContainerBiometricIdentifier;
import uk.co.shadeddimensions.ep3.item.ItemWrench;
import uk.co.shadeddimensions.ep3.lib.Localization;
import uk.co.shadeddimensions.ep3.network.PacketHandlerClient;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileBiometricIdentifier;
import uk.co.shadeddimensions.ep3.util.EntityData;
import uk.co.shadeddimensions.library.gui.GuiBaseContainer;
import uk.co.shadeddimensions.library.gui.element.ElementButton;
import uk.co.shadeddimensions.library.gui.element.ElementButtonIcon;
import uk.co.shadeddimensions.library.gui.element.ElementScrollBar;
import uk.co.shadeddimensions.library.gui.element.ElementScrollPanelOverlay;

public class GuiBiometricIdentifier extends GuiBaseContainer
{
    TileBiometricIdentifier biometric;
    ElementScrollPanelOverlay sendList;
    ElementButton finalButton;
    long lastUpdateTime;

    public GuiBiometricIdentifier(TileBiometricIdentifier tile, EntityPlayer player)
    {
        super(new ContainerBiometricIdentifier(tile, player), new ResourceLocation("enhancedportals", "textures/gui/biometric.png"));
        xSize = 256;
        ySize = 230;
        biometric = tile;
        drawInventory = false;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        fontRenderer.drawString(Localization.getGuiString("biometricIdentifier"), xSize / 2 - fontRenderer.getStringWidth(Localization.getGuiString("biometricIdentifier")) / 2, 6, 0x404040);
        fontRenderer.drawString(Localization.getGuiString("idCard"), 27, ySize - 20, 0x404040);
        fontRenderer.drawString(biometric.isActive ? Localization.getGuiString("active") : Localization.getGuiString("inactive"), xSize - fontRenderer.getStringWidth(biometric.isActive ? Localization.getGuiString("active") : Localization.getGuiString("inactive")) - 5, 6, biometric.isActive ? 0x00AA00 : 0xAA0000);

        super.drawGuiContainerForegroundLayer(par1, par2);
    }

    @Override
    public void addElements()
    {
        sendList = new ElementScrollPanelOverlay(this, 3, 21, xSize - 16, 121, texture, 20).setSides(true, true, false, false);
        addElement(sendList);
        addElement(new ElementScrollBar(this, sendList.getWidth() + 3, 21, 10, 122, sendList));

        finalButton = new ElementButton(this, 7, 147, 75, "mainToggle", biometric.defaultPermissions ? EnumChatFormatting.GREEN + Localization.getGuiString("allow") : EnumChatFormatting.RED + Localization.getGuiString("disallow"), Localization.getGuiString("anythingNotOnList"));
        addElement(finalButton);
        initializeList();
        lastUpdateTime = biometric.lastUpdateTime;
    }

    void initializeList()
    {
        sendList.clear();

        int i = 0;
        for (EntityData data : biometric.entityList)
        {
            ArrayList<String> entityList = new ArrayList<String>();
            entityList.add(data.disallow ? EnumChatFormatting.RED + Localization.getGuiString("disallowing") : EnumChatFormatting.GREEN + Localization.getGuiString("allowing"));
            entityList.add(Localization.getGuiString(data.shouldCheckClass() ? "anythingOfTheType" : "anythingCalled"));
            entityList.add(EnumChatFormatting.GRAY + " " + (data.shouldCheckClass() ? EntityData.getClassDisplayName(data) : data.EntityDisplayName));

            if (data.shouldCheckNameAndClass())
            {
                entityList.add(Localization.getGuiString("withTheTypeOf"));
                entityList.add(EnumChatFormatting.GRAY + " " + EntityData.getClassDisplayName(data));
            }

            ArrayList<String> modeList = new ArrayList<String>();
            modeList.add(Localization.getGuiString(data.checkType == 0 ? "matchName" : data.checkType == 1 ? "matchType" : "matchTypeAndName"));

            sendList.addElement(new ElementButton(this, 2, i * 21, 200, "E" + i, (data.disallow ? EnumChatFormatting.RED : EnumChatFormatting.GREEN) + (data.shouldCheckClass() ? EntityData.getClassDisplayName(data) : data.shouldCheckNameAndClass() ? data.EntityDisplayName + " (" + EntityData.getClassDisplayName(data) + ")" : data.EntityDisplayName), entityList));
            sendList.addElement(new ElementButton(this, 203, i * 21, 15, "M" + i, data.checkType + "", modeList));
            sendList.addElement(new ElementButtonIcon(this, 218, i * 21, "R" + i, "Remove", ItemWrench.instance.getIconFromDamage(0)));
            i++;
        }
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();

        if (lastUpdateTime != biometric.lastUpdateTime)
        {
            initializeList();
            lastUpdateTime = biometric.lastUpdateTime;
        }

        finalButton.setText(biometric.defaultPermissions ? EnumChatFormatting.GREEN + Localization.getGuiString("allow") : EnumChatFormatting.RED + Localization.getGuiString("disallow"));
    }

    @Override
    public void handleElementButtonClick(String buttonName, int mouseButton)
    {
        NBTTagCompound tag = new NBTTagCompound();

        if (buttonName.equals("mainToggle"))
        {
            tag.setBoolean("default", false);
        }
        else if (buttonName.startsWith("E"))
        {
            tag.setInteger("invert", Integer.parseInt(buttonName.replace("E", "")));
        }
        else if (buttonName.startsWith("M"))
        {
            tag.setInteger("type", Integer.parseInt(buttonName.replace("M", "")));
        }
        else if (buttonName.startsWith("R"))
        {
            tag.setInteger("remove", Integer.parseInt(buttonName.replace("R", "")));
        }

        PacketHandlerClient.sendGuiPacket(tag);
    }
}
