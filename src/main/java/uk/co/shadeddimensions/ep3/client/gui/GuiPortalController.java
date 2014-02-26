package uk.co.shadeddimensions.ep3.client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import uk.co.shadeddimensions.ep3.block.BlockFrame;
import uk.co.shadeddimensions.ep3.block.BlockPortal;
import uk.co.shadeddimensions.ep3.client.gui.elements.ElementGlyphIdentifier;
import uk.co.shadeddimensions.ep3.client.gui.elements.ElementGlyphSelector;
import uk.co.shadeddimensions.ep3.lib.Localization;
import uk.co.shadeddimensions.ep3.network.PacketHandlerClient;
import uk.co.shadeddimensions.ep3.portal.GlyphIdentifier;
import uk.co.shadeddimensions.ep3.tileentity.portal.TileController;
import uk.co.shadeddimensions.library.gui.GuiBase;
import uk.co.shadeddimensions.library.gui.element.ElementItemIconWithCount;
import uk.co.shadeddimensions.library.gui.element.ElementItemStackPanel;
import uk.co.shadeddimensions.library.util.GuiUtils;

public class GuiPortalController extends GuiBase
{
    TileController controller;
    GuiButton resetButton, saveButton, publicButton;
    boolean overlayActive;
    String warningMessage;
    int warningTimer;

    ElementGlyphSelector selector;
    ElementGlyphIdentifier identifier;
    ElementItemStackPanel portalComponents;

    public GuiPortalController(TileController tile)
    {
        super(new ResourceLocation("enhancedportals", "textures/gui/portalController.png"));
        ySize = 144;
        controller = tile;
        overlayActive = false;
    }

    public void setWarningMessage(int type)
    {
        if (type == 0)
        {
            selector.setIdentifierTo(null);
            warningMessage = Localization.getGuiString("uidInUse");
            warningTimer = 100;
        }
    }
    
    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (isShiftKeyDown())
        {
            if (button.id == resetButton.id) // Clear
            {
                selector.reset();
            }
            else if (button.id == saveButton.id) // Random
            {
                Random random = new Random();
                GlyphIdentifier iden = new GlyphIdentifier();

                for (int i = 0; i < (isCtrlKeyDown() ? 9 : random.nextInt(8) + 1); i++)
                {
                    iden.addGlyph(random.nextInt(27));
                }

                selector.setIdentifierTo(iden);
            }
        }
        else
        {
            if (button.id == resetButton.id) // Reset Changes
            {
                selector.setIdentifierTo(controller.getIdentifierUnique());
                toggleState();
            }
            else if (button.id == saveButton.id) // Save Changes
            {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setString("uid", selector.getGlyphIdentifier().getGlyphString());
                PacketHandlerClient.sendGuiPacket(tag);
                toggleState();
            }
            else if (button.id == publicButton.id)
            {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setBoolean("public", true);
                PacketHandlerClient.sendGuiPacket(tag);
            }
        }
    }

    @Override
    public void addElements()
    {
        selector = new ElementGlyphSelector(this, 7, 57);
        identifier = new ElementGlyphIdentifier(this, 7, 20, selector);
        portalComponents = new ElementItemStackPanel(this, 10, 78, 158, 56);
        portalComponents.addElement(new ElementItemIconWithCount(this, 0, 0, new ItemStack(BlockPortal.instance, controller.getPortalCount())));
        portalComponents.addElement(new ElementItemIconWithCount(this, 0, 0, new ItemStack(BlockFrame.instance, controller.getFrameCount(), 0)));
        portalComponents.addElement(new ElementItemIconWithCount(this, 0, 0, new ItemStack(BlockFrame.instance, 1, 1)));

        if (controller.getRedstoneInterfaceCount() > 0)
        {
            portalComponents.addElement(new ElementItemIconWithCount(this, 0, 0, new ItemStack(BlockFrame.instance, controller.getRedstoneInterfaceCount(), BlockFrame.REDSTONE_INTERFACE)));
        }

        if (controller.getNetworkInterfaceCount() > 0)
        {
            portalComponents.addElement(new ElementItemIconWithCount(this, 0, 0, new ItemStack(BlockFrame.instance, controller.getNetworkInterfaceCount(), BlockFrame.NETWORK_INTERFACE)));
        }
        else if (controller.getDiallingDeviceCount() > 0)
        {
            portalComponents.addElement(new ElementItemIconWithCount(this, 0, 0, new ItemStack(BlockFrame.instance, controller.getDiallingDeviceCount(), BlockFrame.DIALLING_DEVICE)));
        }

        if (controller.getHasBiometricIdentifier())
        {
            portalComponents.addElement(new ElementItemIconWithCount(this, 0, 0, new ItemStack(BlockFrame.instance, 1, BlockFrame.BIOMETRIC_IDENTIFIER)));
        }

        if (controller.getHasModuleManipulator())
        {
            portalComponents.addElement(new ElementItemIconWithCount(this, 0, 0, new ItemStack(BlockFrame.instance, 1, BlockFrame.MODULE_MANIPULATOR)));
        }

        if (controller.getTransferEnergyCount() > 0)
        {
            portalComponents.addElement(new ElementItemIconWithCount(this, 0, 0, new ItemStack(BlockFrame.instance, controller.getTransferEnergyCount(), BlockFrame.TRANSFER_ENERGY)));
        }
        
        if (controller.getTransferFluidCount() > 0)
        {
            portalComponents.addElement(new ElementItemIconWithCount(this, 0, 0, new ItemStack(BlockFrame.instance, controller.getTransferFluidCount(), BlockFrame.TRANSFER_FLUID)));
        }
        
        if (controller.getTransferItemCount() > 0)
        {
            portalComponents.addElement(new ElementItemIconWithCount(this, 0, 0, new ItemStack(BlockFrame.instance, controller.getTransferItemCount(), BlockFrame.TRANSFER_ITEM)));
        }
        
        identifier.setDisabled(!overlayActive);
        portalComponents.setVisible(!overlayActive);
        selector.setVisible(overlayActive);
        selector.setIdentifierTo(controller.getIdentifierUnique());

        addElement(identifier);
        addElement(portalComponents);
        addElement(selector);
    }

    @Override
    public void drawBackgroundTexture()
    {
        super.drawBackgroundTexture();

        if (overlayActive)
        {
            drawTexturedModalRect(guiLeft, guiTop + ySize - 106, 0, ySize, xSize, 106);
        }
    }

    @Override
    public void drawGuiForegroundLayer(int x, int y)
    {
        drawCenteredString(fontRenderer, Localization.getGuiString("portalController"), xSize / 2, -13, 0xFFFFFF);
        fontRenderer.drawString(Localization.getGuiString("uniqueIdentifier"), 8, 8, 0x404040);
       
        if (!overlayActive)
        {
            getFontRenderer().drawString(Localization.getGuiString("portalComponents"), 8, 65, 0x404040);
            
            if (x >= guiLeft + 7 && x <= guiLeft + xSize - 8)
            {
                if (y >= guiTop + 20 && y <= guiTop + 37)
                {
                    List<String> list = new ArrayList<String>();
                    list.add(Localization.getGuiString("clickToModify"));
                    
                    GuiUtils.drawTooltipHoveringText(this, list, x - guiLeft, y - guiTop);
                }
            }
        }
        else
        {
            getFontRenderer().drawString(Localization.getGuiString("glyphs"), 8, 44, 0x404040);
        }
        
        if (warningTimer > 0)
        {
            fontRenderer.drawString(warningMessage, xSize / 2 - fontRenderer.getStringWidth(warningMessage) / 2, 125, 0xFF0000);
        }

        super.drawGuiForegroundLayer(x, y);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();

        resetButton = new GuiButton(0, guiLeft + 10, guiTop + 117, (xSize - 20) / 2 - 5, 20, Localization.getGuiString("cancel"));
        saveButton = new GuiButton(1, guiLeft + xSize / 2 + 6, guiTop + 117, (xSize - 20) / 2 - 5, 20, Localization.getGuiString("save"));
        publicButton = new GuiButton(10, guiLeft + 10, guiTop + 41, xSize - 20, 20, Localization.getGuiString(controller.isPublic() ? "public" : "private"));
        
        buttonList.add(resetButton);
        buttonList.add(saveButton);
        buttonList.add(publicButton);

        resetButton.drawButton = saveButton.drawButton = overlayActive;
        publicButton.drawButton = !overlayActive;
    }

    @Override
    protected void mouseClicked(int x, int y, int mouseButton)
    {
        if (!overlayActive)
        {
            if (x >= guiLeft + 7 && x <= guiLeft + xSize - 8)
            {
                if (y >= guiTop + 20 && y <= guiTop + 37)
                {
                    toggleState();
                    mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
                    return;
                }
            }
        }

        super.mouseClicked(x, y, mouseButton);
    }

    private void toggleState()
    {
        overlayActive = !overlayActive;
        portalComponents.setVisible(!overlayActive);
        identifier.setDisabled(!overlayActive);
        selector.setVisible(overlayActive);
        resetButton.drawButton = saveButton.drawButton = overlayActive;
        publicButton.drawButton = !overlayActive;
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();

        if (overlayActive)
        {
            if (isShiftKeyDown())
            {
                resetButton.displayString = EnumChatFormatting.AQUA + Localization.getGuiString("clear");
                saveButton.displayString = (isCtrlKeyDown() ? EnumChatFormatting.GOLD : EnumChatFormatting.AQUA) + Localization.getGuiString("random");
            }
            else
            {
                resetButton.displayString = Localization.getGuiString("cancel");
                saveButton.displayString = Localization.getGuiString("save");
            }
            
            if (warningTimer > 0)
            {
                warningTimer = 0;
            }
        }
        else
        {
            publicButton.displayString = Localization.getGuiString(controller.isPublic() ? "public" : "private");
        }
        
        if (warningTimer > 0)
        {
            warningTimer--;
        }
    }
}
