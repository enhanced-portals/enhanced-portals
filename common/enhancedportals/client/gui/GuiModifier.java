package enhancedportals.client.gui;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;

import org.lwjgl.opengl.GL11;

import uk.co.shadeddimensions.enhancedportals.EnhancedPortals_deprecated;
import cpw.mods.fml.common.network.PacketDispatcher;
import enhancedcore.gui.EnhancedCoreContainer;
import enhancedcore.gui.GuiEnhancedCore;
import enhancedcore.gui.GuiItemStackButton;
import enhancedcore.util.Properties;
import enhancedportals.lib.GuiIds;
import enhancedportals.lib.Reference;
import enhancedportals.lib.Strings;
import enhancedportals.network.packet.PacketEnhancedPortals;
import enhancedportals.network.packet.PacketGui;
import enhancedportals.network.packet.PacketRedstoneControl;
import enhancedportals.network.packet.PacketThickness;
import enhancedportals.network.packet.PacketUpgrade;
import enhancedportals.portal.upgrades.Upgrade;
import enhancedportals.tileentity.TileEntityPortalModifier;

public class GuiModifier extends GuiEnhancedCore
{
    private class ModifierTips extends TipLedger
    {
        @Override
        protected String[] getTipsList()
        {
            return new String[] { "You can camouflage the portal with any block or fluid", "You can control your portals with Redstone or ComputerCraft", "The portal modifier does not have to be inside a portal frame" };
        }
    }

    private class ModifierRedstone extends RedstoneLedger
    {
        TileEntityPortalModifier modifier;
        
        public ModifierRedstone(TileEntityPortalModifier modifier)
        {
            this.modifier = modifier;
            setSelected(modifier.redstoneSetting);
        }
        
        @Override
        protected void selectedChanged(int i)
        {
            modifier.redstoneSetting = (byte) i;
            PacketDispatcher.sendPacketToServer(PacketEnhancedPortals.makePacket(new PacketRedstoneControl(modifier)));
        }
        
        @Override
        public boolean handleMouseClicked(int x, int y, int mouseButton)
        {
            if (!isOpen())
            {
                setSelected(modifier.redstoneSetting);
            }
            
            return super.handleMouseClicked(x, y, mouseButton);
        }
    }
    
    TileEntityPortalModifier modifier;

    public GuiModifier(EnhancedCoreContainer container, IInventory inventory)
    {
        super(container, inventory);

        modifier = (TileEntityPortalModifier) inventory;
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 1)
        {
            GuiItemStackButton btn = (GuiItemStackButton) button;
            int num = Integer.parseInt(btn.displayString) + 1;

            if (num >= 4)
            {
                num = 0;
            }

            modifier.thickness = (byte) num;
            PacketDispatcher.sendPacketToServer(PacketEnhancedPortals.makePacket(new PacketThickness(modifier)));
        }
        else if (button.id >= 100)
        {
            int size = modifier.upgradeHandler.getUpgrades().size();
            Upgrade upgrade = modifier.upgradeHandler.getUpgrade(MathHelper.clamp_int(button.id - 100, 0, size > 0 ? size - 1 : size));

            if (upgrade != null)
            {
                PacketDispatcher.sendPacketToServer(PacketEnhancedPortals.makePacket(new PacketUpgrade(modifier, (byte) 1, upgrade.getUpgradeID())));
                buttonList.remove(button);
                modifier.upgradeHandler.removeUpgrade(upgrade, modifier);
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        Properties.bindTexture(mc.renderEngine, "ep2", "textures/gui/portalModifier.png");

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        drawTexturedModalRect((width - xSize) / 2, (height - ySize) / 2, 0, 0, xSize, ySize);

        if (modifier.getStackInSlot(0) == null)
        {
            // Render default portal if no item in slot
            itemRenderer.renderItemIntoGUI(fontRenderer, mc.renderEngine, new ItemStack(Block.portal), guiLeft + 152, guiTop + 18);
        }

        boolean draw = modifier.isRemotelyControlled() ? modifier.dialDeviceNetwork.equals("") : modifier.modifierNetwork.equals("");

        if (draw)
        {
            // Draw error/message
            String str = modifier.isRemotelyControlled() ? Strings.ClickToSetIdentifier.toString() : Strings.ClickToSetNetwork.toString();

            if (!EnhancedPortals_deprecated.proxy.isIdentifierTaken)
            {
                fontRenderer.drawStringWithShadow(str, guiLeft + xSize / 2 - fontRenderer.getStringWidth(str) / 2, guiTop + 54, 0xFF00FF00);
            }
            else
            {
                fontRenderer.drawStringWithShadow(Strings.IdentifierInUse.toString(), guiLeft + xSize / 2 - fontRenderer.getStringWidth(Strings.IdentifierInUse.toString()) / 2, guiTop + 54, 0xEE0000);
            }
        }
        else
        {
            // Render network icons
            String network = modifier.isRemotelyControlled() ? modifier.dialDeviceNetwork : modifier.modifierNetwork;

            if (!network.equals(""))
            {
                String[] split = network.split(Reference.glyphSeperator);
                int theSize = guiLeft + xSize / 2 - split.length * 18 / 2 + 1;

                for (int i1 = 0; i1 < split.length; i1++)
                {
                    for (int j1 = 0; j1 < Reference.glyphItems.size(); j1++)
                    {
                        if (Reference.glyphItems.get(j1).getItemName().replace("item.", "").equalsIgnoreCase(split[i1]))
                        {
                            ItemStack stack = Reference.glyphItems.get(j1);
                            Icon icon = stack.getIconIndex();

                            Properties.bindTexture(mc.renderEngine, stack.getItem().getSpriteNumber());
                            itemRenderer.renderIcon(theSize + i1 * 18, guiTop + 50, icon, 16, 16);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        fontRenderer.drawStringWithShadow("Portal Modifier", xSize / 2 - fontRenderer.getStringWidth("Portal Modifier") / 2, -12, 0xFFFFFFFF);

        fontRenderer.drawString("Upgrades", 8, 6, 0xFF444444);
        fontRenderer.drawString("Modifications", xSize - 6 - fontRenderer.getStringWidth("Modifications"), 6, 0xFF444444);
        fontRenderer.drawString("Network", 8, 38, 0xFF444444);
        fontRenderer.drawString("Inventory", 8, 70, 0xFF444444);

        super.drawGuiContainerForegroundLayer(par1, par2);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();

        ArrayList<String> strList = new ArrayList<String>();
        strList.add(EnumChatFormatting.WHITE + "Thickness");
        strList.add(EnumChatFormatting.GRAY + "Normal");

        buttonList.add(new GuiItemStackButton(1, guiLeft + 134, guiTop + 18, new ItemStack(Block.portal, 1), true, strList, "" + modifier.thickness, true));

        for (int i = 0; i < modifier.upgradeHandler.getInstalledUpgrades().length; i++)
        {
            Upgrade u = modifier.upgradeHandler.getUpgrade(i);
            buttonList.add(new GuiItemStackButton(100 + i, guiLeft + 8 + 18 * i, guiTop + 18, u.getDisplayItemStack(), true, u.getText(true), true));
        }
    }

    @Override
    protected void initLedgers(IInventory inventory)
    {
        super.initLedgers(inventory);

        ledgerManager.add(new ModifierTips());
        ledgerManager.add(new ModifierRedstone((TileEntityPortalModifier) inventory));
    }

    @Override
    protected void mouseClicked(int x, int y, int mouseButton)
    {
        super.mouseClicked(x, y, mouseButton);

        if (x >= guiLeft + 8 && x <= width - guiLeft - 10 && y >= guiTop + 49 && y <= guiTop + 65 && mouseButton == 0)
        {
            mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
            PacketDispatcher.sendPacketToServer(PacketEnhancedPortals.makePacket(new PacketGui(modifier, GuiIds.PortalModifierNetwork)));
        }
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();

        ((GuiItemStackButton) buttonList.get(0)).displayString = "" + modifier.thickness;
        ((GuiItemStackButton) buttonList.get(0)).hoverText.set(1, EnumChatFormatting.GRAY + (modifier.thickness == 0 ? "Normal" : modifier.thickness == 1 ? "Thick" : modifier.thickness == 2 ? "Thicker" : "Full Block"));
    }
}
