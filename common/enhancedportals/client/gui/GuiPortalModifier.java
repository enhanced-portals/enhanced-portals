package enhancedportals.client.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.common.network.PacketDispatcher;
import enhancedcore.gui.GuiItemStackButton;
import enhancedportals.EnhancedPortals;
import enhancedportals.container.ContainerPortalModifier;
import enhancedportals.lib.GuiIds;
import enhancedportals.lib.Localization;
import enhancedportals.lib.Reference;
import enhancedportals.lib.Strings;
import enhancedportals.lib.Textures;
import enhancedportals.network.packet.PacketEnhancedPortals;
import enhancedportals.network.packet.PacketGui;
import enhancedportals.network.packet.PacketPortalModifierUpdate;
import enhancedportals.network.packet.PacketPortalModifierUpgrade;
import enhancedportals.portal.PortalTexture;
import enhancedportals.portal.upgrades.Upgrade;
import enhancedportals.portal.upgrades.modifier.UpgradeDialDevice;
import enhancedportals.tileentity.TileEntityPortalModifier;

public class GuiPortalModifier extends GuiEnhancedPortalsScreen
{
    TileEntityPortalModifier portalModifier;
    public boolean           hasInteractedWith, isActive;
    GuiButton                okayButton;

    public GuiPortalModifier(InventoryPlayer player, TileEntityPortalModifier modifier)
    {
        super(new ContainerPortalModifier(player, modifier), null);
        portalModifier = modifier;
        hasInteractedWith = false;
        isActive = portalModifier.isActive();

        extendedSlots.add(new GuiTextureSlot(xSize - 24, 15, Textures.getItemStackFromTexture(portalModifier.texture), this));
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 100)
        {
            mc.thePlayer.closeScreen();
        }
        else if (button.id == 10)
        {
            portalModifier.redstoneSetting = 0;
            ((GuiItemStackButton) buttonList.get(2)).isActive = false;
            ((GuiItemStackButton) buttonList.get(3)).displayString = "0";
            ((GuiItemStackButton) buttonList.get(3)).isActive = false;
            hasInteractedWith = true;
            ((GuiItemStackButton) button).isActive = true;
        }
        else if (button.id == 11)
        {
            portalModifier.redstoneSetting = 1;
            ((GuiItemStackButton) buttonList.get(1)).isActive = false;
            ((GuiItemStackButton) buttonList.get(3)).displayString = "0";
            ((GuiItemStackButton) buttonList.get(3)).isActive = false;
            hasInteractedWith = true;
            ((GuiItemStackButton) button).isActive = true;
        }
        else if (button.id == 12)
        {
            int num = 0;

            if (button.displayString != null && button.displayString != "")
            {
                num = Integer.parseInt(button.displayString);
            }

            if (num + 1 < 16)
            {
                button.displayString = "" + (num + 1);
                num++;
            }
            else
            {
                button.displayString = "1";
                num = 1;
            }

            portalModifier.redstoneSetting = (byte) (2 + num);
            ((GuiItemStackButton) buttonList.get(1)).isActive = false;
            ((GuiItemStackButton) buttonList.get(2)).isActive = false;
            hasInteractedWith = true;
            ((GuiItemStackButton) button).isActive = true;
        }
        else if (button.id == 13)
        {
            int num = 0;

            if (button.displayString != null && button.displayString != "")
            {
                num = Integer.parseInt(button.displayString);
            }

            if (num + 1 < 5)
            {
                button.displayString = "" + (num + 1);
                num++;
            }
            else
            {
                button.displayString = "1";
                num = 1;
            }

            portalModifier.thickness = (byte) (num - 1);
            ((GuiItemStackButton) button).hoverText.set(1, EnumChatFormatting.GRAY + (portalModifier.thickness == 0 ? Strings.Normal.toString() : portalModifier.thickness == 1 ? Strings.Thick.toString() : portalModifier.thickness == 2 ? Strings.Thicker.toString() : Strings.FullBlock.toString()));
            hasInteractedWith = true;
        }
        else if (button.id == 50)
        {
            GuiItemStackButton guiButton = (GuiItemStackButton) button;

            for (Upgrade u : Upgrade.getAllUpgrades())
            {
                if (u.getDisplayItemStack().isItemEqual(guiButton.itemStack))
                {
                    if (portalModifier.upgradeHandler.removeUpgrade(u.getUpgradeID(), portalModifier))
                    {
                        buttonList.remove(button);
                        hasInteractedWith = true;
                    }
                }
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        super.drawGuiContainerBackgroundLayer(f, i, j);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(Reference.GUI_LOCATION + "portalModifier.png");
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2 - 3;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

        fontRenderer.drawString(Localization.localizeString("tile.portalModifier.name"), guiLeft + xSize / 2 - fontRenderer.getStringWidth(Localization.localizeString("tile.portalModifier.name")) / 2, guiTop + -15, 0xFFFFFF);
        fontRenderer.drawString(Strings.Upgrades.toString(), guiLeft + 8, guiTop + 3, 0xFF444444);
        fontRenderer.drawString(Strings.Modifications.toString(), guiLeft + xSize - 8 - fontRenderer.getStringWidth(Strings.Modifications.toString()), guiTop + 3, 0xFF444444);
        fontRenderer.drawString(portalModifier.upgradeHandler.hasUpgrade(new UpgradeDialDevice()) ? Strings.UniqueIdentifier.toString() : Strings.Network.toString(), guiLeft + 8, guiTop + 35, 0xFF444444);

        boolean draw = portalModifier.isRemotelyControlled() ? portalModifier.dialDeviceNetwork.equals("") : portalModifier.modifierNetwork.equals("");

        if (draw)
        {
            String str = portalModifier.isRemotelyControlled() ? Strings.ClickToSetIdentifier.toString() : Strings.ClickToSetNetwork.toString();

            drawRect(guiLeft + 7, guiTop + 46, guiLeft + xSize - 7, guiTop + 64, 0x55000000);
            fontRenderer.drawStringWithShadow(str, guiLeft + xSize / 2 - fontRenderer.getStringWidth(str) / 2, guiTop + 51, 0xFF00FF00);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        super.drawGuiContainerForegroundLayer(par1, par2);
    }

    @Override
    public void drawScreen(int x, int y, float par3)
    {
        if (!isActive)
        {
            super.drawScreen(x, y, par3);

            String network = portalModifier.isRemotelyControlled() ? portalModifier.dialDeviceNetwork : portalModifier.modifierNetwork;

            if (!network.equals(""))
            {
                String[] split = network.split(Reference.glyphSeperator);

                for (int i = 0; i < split.length; i++)
                {
                    for (int j = 0; j < Reference.glyphItems.size(); j++)
                    {
                        if (Reference.glyphItems.get(j).getItemName().replace("item.", "").equalsIgnoreCase(split[i]))
                        {
                            itemRenderer.renderItemIntoGUI(fontRenderer, mc.renderEngine, Reference.glyphItems.get(j), guiLeft + 8 + i * 18, guiTop + 47);
                        }
                    }
                }
            }

            if (isPointInRegion(7, 46, 162, 18, x, y))
            {
                List<String> str = new ArrayList<String>();
                str.add(portalModifier.upgradeHandler.hasUpgrade(new UpgradeDialDevice()) ? Strings.UniqueIdentifier.toString() : Strings.Network.toString());
                str.add(EnumChatFormatting.GRAY + (portalModifier.upgradeHandler.hasUpgrade(new UpgradeDialDevice()) ? Strings.ClickToSetIdentifier.toString() : Strings.ClickToSetNetwork.toString()));
                drawText(str, x, y);
            }
        }
        else
        {
            drawDefaultBackground();
            fontRenderer.drawString(Strings.ModifierActive.toString(), width / 2 - fontRenderer.getStringWidth(Strings.ModifierActive.toString()) / 2, guiTop, 0xFFFFFF);
            okayButton.drawButton(mc, 0, 0);
        }
    }

    @SuppressWarnings("rawtypes")
    public void drawText(List<String> list, int x, int y)
    {
        if (!list.isEmpty())
        {
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            int k = 0;
            Iterator iterator = list.iterator();

            while (iterator.hasNext())
            {
                String s = (String) iterator.next();
                int l = fontRenderer.getStringWidth(s);

                if (l > k)
                {
                    k = l;
                }
            }

            int i1 = x + 12;
            int j1 = y - 12;
            int k1 = 8;

            if (list.size() > 1)
            {
                k1 += 2 + (list.size() - 1) * 10;
            }

            if (i1 + k > width)
            {
                i1 -= 28 + k;
            }

            if (j1 + k1 + 6 > height)
            {
                j1 = height - k1 - 6;
            }

            zLevel = 300.0F;
            itemRenderer.zLevel = 300.0F;
            int l1 = -267386864;
            drawGradientRect(i1 - 3, j1 - 4, i1 + k + 3, j1 - 3, l1, l1);
            drawGradientRect(i1 - 3, j1 + k1 + 3, i1 + k + 3, j1 + k1 + 4, l1, l1);
            drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 + k1 + 3, l1, l1);
            drawGradientRect(i1 - 4, j1 - 3, i1 - 3, j1 + k1 + 3, l1, l1);
            drawGradientRect(i1 + k + 3, j1 - 3, i1 + k + 4, j1 + k1 + 3, l1, l1);
            int i2 = 1347420415;
            int j2 = (i2 & 16711422) >> 1 | i2 & -16777216;
            drawGradientRect(i1 - 3, j1 - 3 + 1, i1 - 3 + 1, j1 + k1 + 3 - 1, i2, j2);
            drawGradientRect(i1 + k + 2, j1 - 3 + 1, i1 + k + 3, j1 + k1 + 3 - 1, i2, j2);
            drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 - 3 + 1, i2, i2);
            drawGradientRect(i1 - 3, j1 + k1 + 2, i1 + k + 3, j1 + k1 + 3, j2, j2);

            for (int k2 = 0; k2 < list.size(); ++k2)
            {
                String s1 = list.get(k2);
                fontRenderer.drawStringWithShadow(s1, i1, j1, -1);

                if (k2 == 0)
                {
                    j1 += 2;
                }

                j1 += 10;
            }

            zLevel = 0.0F;
            itemRenderer.zLevel = 0.0F;
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            RenderHelper.enableStandardItemLighting();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        }
    }

    public void drawTexturedRect(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        float f = 0.062F;
        float f1 = 0.062F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(par1 + 0, par2 + par6, zLevel, (par3 + 0) * f, (par4 + par6) * f1);
        tessellator.addVertexWithUV(par1 + par5, par2 + par6, zLevel, (par3 + par5) * f, (par4 + par6) * f1);
        tessellator.addVertexWithUV(par1 + par5, par2 + 0, zLevel, (par3 + par5) * f, (par4 + 0) * f1);
        tessellator.addVertexWithUV(par1 + 0, par2 + 0, zLevel, (par3 + 0) * f, (par4 + 0) * f1);
        tessellator.draw();
    }

    @Override
    public void extendedSlotChanged(GuiExtendedItemSlot slot)
    {
        ItemStack stack = slot.getItemStack();

        if (stack.itemID == EnhancedPortals.proxy.blockDummyPortal.blockID)
        {
            stack = new ItemStack(Item.dyePowder, 1, stack.getItemDamage());
        }

        PortalTexture text = Textures.getTextureFromItemStack(stack);

        if (text != null)
        {
            portalModifier.texture = text.getID();
        }
        else
        {
            portalModifier.texture = "C:5";
        }

        hasInteractedWith = true;
    }

    public int getGuiLeft()
    {
        return guiLeft;
    }

    public int getGuiTop()
    {
        return guiTop;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();

        okayButton = new GuiButton(100, width / 2 - 50, height / 2 - 10, 100, 20, Strings.Accept.toString());
        buttonList.add(okayButton);
        okayButton.drawButton = isActive;

        List<String> strList = new ArrayList<String>();
        strList.add(Strings.RedstoneControl.toString());
        strList.add(EnumChatFormatting.GRAY + Strings.Normal.toString());
        buttonList.add(new GuiItemStackButton(10, guiLeft + xSize + 4, guiTop + 4, new ItemStack(Block.torchRedstoneActive), portalModifier.redstoneSetting == 0, strList, false, !portalModifier.upgradeHandler.hasUpgrade(new UpgradeDialDevice())));

        strList = new ArrayList<String>();
        strList.add(Strings.RedstoneControl.toString());
        strList.add(EnumChatFormatting.GRAY + Strings.Inverted.toString());
        buttonList.add(new GuiItemStackButton(11, guiLeft + xSize + 4, guiTop + 24, new ItemStack(Block.torchRedstoneIdle), portalModifier.redstoneSetting == 1, strList, false, !portalModifier.upgradeHandler.hasUpgrade(new UpgradeDialDevice())));

        strList = new ArrayList<String>();
        strList.add(Strings.RedstoneControl.toString());
        strList.add(EnumChatFormatting.GRAY + Strings.Precise.toString());
        buttonList.add(new GuiItemStackButton(12, guiLeft + xSize + 4, guiTop + 44, new ItemStack(Item.redstone), portalModifier.redstoneSetting > 1, strList, "" + (portalModifier.redstoneSetting > 2 ? portalModifier.redstoneSetting - 2 : 0), !portalModifier.upgradeHandler.hasUpgrade(new UpgradeDialDevice())));

        strList = new ArrayList<String>();
        strList.add(Strings.Thickness.toString());
        strList.add("");
        buttonList.add(new GuiItemStackButton(13, guiLeft + xSize - 42, guiTop + 15, new ItemStack(EnhancedPortals.proxy.blockNetherPortal, 1, 2), true, strList, true));
        ((GuiItemStackButton) buttonList.get(4)).displayString = "" + (portalModifier.thickness + 1);
        ((GuiItemStackButton) buttonList.get(4)).hoverText.set(1, EnumChatFormatting.GRAY + (portalModifier.thickness == 0 ? Strings.Normal.toString() : portalModifier.thickness == 1 ? Strings.Thick.toString() : portalModifier.thickness == 2 ? Strings.Thicker.toString() : Strings.FullBlock.toString()));

        for (int i = portalModifier.upgradeHandler.getUpgrades().size() - 1; i >= 0; i--)
        {
            buttonList.add(new GuiItemStackButton(50, guiLeft + 8 + i * 18, guiTop + 15, portalModifier.upgradeHandler.getUpgrade(i).getDisplayItemStack(), false, portalModifier.upgradeHandler.getUpgrade(i).getText(true), true));
        }
    }

    @Override
    protected void mouseClicked(int x, int y, int buttonClicked)
    {
        super.mouseClicked(x, y, buttonClicked);

        if (isPointInRegion(7, 46, 162, 18, x, y))
        {
            PacketDispatcher.sendPacketToServer(PacketEnhancedPortals.makePacket(new PacketGui(portalModifier, GuiIds.PortalModifierNetwork)));
        }
        else if (isShiftKeyDown() && getSlotAtPosition(x, y) != null)
        {
            PortalTexture Text = Textures.getTextureFromItemStack(getSlotAtPosition(x, y).decrStackSize(0));

            if (Text != null)
            {
                extendedSlots.get(0).setSlot(getSlotAtPosition(x, y).decrStackSize(0));
            }
        }
    }

    @Override
    public void onGuiClosed()
    {
        super.onGuiClosed();

        if (hasInteractedWith)
        {
            PacketDispatcher.sendPacketToServer(PacketEnhancedPortals.makePacket(new PacketPortalModifierUpdate(portalModifier)));
            PacketDispatcher.sendPacketToServer(PacketEnhancedPortals.makePacket(new PacketPortalModifierUpgrade(portalModifier)));

            portalModifier.worldObj.markBlockForRenderUpdate(portalModifier.xCoord, portalModifier.yCoord, portalModifier.zCoord);
        }
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();

        if (portalModifier.upgradeHandler.hasUpgrade(new UpgradeDialDevice()))
        {
            if (((GuiItemStackButton) buttonList.get(1)).enabled)
            {
                ((GuiItemStackButton) buttonList.get(1)).enabled = ((GuiItemStackButton) buttonList.get(2)).enabled = ((GuiItemStackButton) buttonList.get(3)).enabled = false;
            }
        }
        else
        {
            if (!((GuiItemStackButton) buttonList.get(1)).enabled)
            {
                ((GuiItemStackButton) buttonList.get(1)).enabled = ((GuiItemStackButton) buttonList.get(2)).enabled = ((GuiItemStackButton) buttonList.get(3)).enabled = true;
            }
        }
    }
}
