package enhancedportals.portal;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import cpw.mods.fml.common.network.PacketDispatcher;
import enhancedportals.EnhancedPortals;
import enhancedportals.client.gui.GuiPortalModifier;
import enhancedportals.client.gui.GuiUpgradeButton;
import enhancedportals.item.ItemPortalModifierUpgrade;
import enhancedportals.lib.Localization;
import enhancedportals.network.packet.PacketUpgrade;
import enhancedportals.tileentity.TileEntityPortalModifier;

public class Upgrade
{
    List<GuiUpgradeButton> elementList;
    public GuiPortalModifier parent;
    TileEntityPortalModifier modifier;

    public Upgrade(GuiPortalModifier parentGui, TileEntityPortalModifier portalModifier)
    {
        parent = parentGui;
        modifier = portalModifier;
        elementList = new ArrayList<GuiUpgradeButton>();

        for (int i = 0; i < modifier.upgrades.length; i++)
        {
            if (modifier.upgrades[i])
            {
                boolean active = true;

                if (i == 0)
                {
                    active = modifier.getParticles();
                }
                else if (i == 1)
                {
                    active = modifier.getSounds();
                }

                elementList.add(new GuiUpgradeButton(i, this, active));
            }
        }
    }

    public void updateUpgradesList()
    {
        elementList.clear();
        
        for (int i = 0; i < modifier.upgrades.length; i++)
        {
            if (modifier.upgrades[i])
            {
                boolean active = true;

                if (i == 0)
                {
                    active = modifier.getParticles();
                }
                else if (i == 1)
                {
                    active = modifier.getSounds();
                }

                elementList.add(new GuiUpgradeButton(i, this, active));
            }
        }
    }
    
    public void drawElements(int mouseX, int mouseY, FontRenderer fontRenderer, RenderItem itemRenderer, RenderEngine renderEngine)
    {
        for (int i = elementList.size() - 1; i >= 0; i--)
        {
            elementList.get(i).drawButton(parent.getGuiLeft() + 8 + i * 18, parent.getGuiTop() + 15, mouseX, mouseY, fontRenderer, itemRenderer, renderEngine);
        }
    }

    public List<String> getHoverText(int ID)
    {
        List<String> theList = new ArrayList<String>();
        theList.add(EnumChatFormatting.AQUA + Localization.localizeString("item." + Localization.PortalModifierUpgrade_Name + "." + ItemPortalModifierUpgrade.names[ID] + ".name"));

        if (ID == 0)
        {
            theList.add(EnumChatFormatting.GRAY + (modifier.getParticles() ? Localization.localizeString("upgrade.particles.active") : Localization.localizeString("upgrade.particles.inactive")));
        }
        else if (ID == 1)
        {
            theList.add(EnumChatFormatting.GRAY + (modifier.getSounds() ? Localization.localizeString("upgrade.sounds.active") : Localization.localizeString("upgrade.sounds.inactive")));
        }
        else if (ID == 2)
        {
            theList.add(EnumChatFormatting.GRAY + Localization.localizeString("upgrade.portalModifier.dimensional.text"));
        }
        else if (ID == 3)
        {
            theList.add(EnumChatFormatting.GRAY + Localization.localizeString("upgrade.advancedDimensional.text"));
            theList.add(EnumChatFormatting.GRAY + Localization.localizeString("upgrade.advancedDimensional.textB"));
            theList.add(EnumChatFormatting.GRAY + Localization.localizeString("upgrade.advancedDimensional.textC"));
        }
        else if (ID == 4)
        {
            theList.add(EnumChatFormatting.GRAY + Localization.localizeString("upgrade.computer.text"));
        }
        else if (ID == 5)
        {
            theList.add(EnumChatFormatting.GRAY + Localization.localizeString("upgrade.blocks.text"));
            theList.add(EnumChatFormatting.GRAY + Localization.localizeString("upgrade.blocks.textA"));
            theList.add(EnumChatFormatting.DARK_AQUA + Localization.localizeString(Block.glowStone.getUnlocalizedName() + ".name"));
            theList.add(EnumChatFormatting.DARK_AQUA + Localization.localizeString(Block.netherBrick.getUnlocalizedName() + ".name"));
            theList.add(EnumChatFormatting.DARK_AQUA + Localization.localizeString(Block.blockNetherQuartz.getUnlocalizedName() + ".name"));
        }
        else if (ID == 6)
        {
            theList.add(EnumChatFormatting.GRAY + Localization.localizeString("upgrade.blocks.text"));
            theList.add(EnumChatFormatting.GRAY + Localization.localizeString("upgrade.blocks.textA"));
            theList.add(EnumChatFormatting.DARK_AQUA + Localization.localizeString(Block.blockIron.getUnlocalizedName() + ".name"));
            theList.add(EnumChatFormatting.DARK_AQUA + Localization.localizeString(Block.blockGold.getUnlocalizedName() + ".name"));
            theList.add(EnumChatFormatting.DARK_AQUA + Localization.localizeString(Block.blockDiamond.getUnlocalizedName() + ".name"));
        }

        theList.add(EnumChatFormatting.DARK_GRAY + Localization.localizeString("upgrade.remove"));

        return theList;
    }

    public ItemStack getItemStack(int ID)
    {
        ItemStack stack = null;

        switch (ID)
        {
            case 0:
                return new ItemStack(Item.blazePowder);
            case 1:
                return new ItemStack(Block.jukebox);
            case 2:
                return new ItemStack(EnhancedPortals.proxy.blockDummyPortal, 1, 0);
            case 3:
                return new ItemStack(EnhancedPortals.proxy.blockDummyPortal, 1, 14);
            case 4:
                return new ItemStack(Item.blazePowder);
            case 5:
                return new ItemStack(Block.blockNetherQuartz, 1, 2);
            case 6:
                return new ItemStack(Block.blockDiamond);
        }

        return stack;
    }

    public void mouseClicked(int mouseX, int mouseY, int buttonClicked)
    {
        for (int i = 0; i < elementList.size(); i++)
        {
            elementList.get(i).mouseClicked(parent.getGuiLeft() + 8 + i * 18, parent.getGuiTop() + 15, mouseX, mouseY, buttonClicked);
        }
    }

    public void onElementClicked(GuiUpgradeButton element, int button)
    {
        if (button == 0)
        {
            if (element.ID == 0)
            {
                modifier.setParticles(!modifier.getParticles());
                element.active = modifier.getParticles();
            }
            else if (element.ID == 1)
            {
                modifier.setSounds(!modifier.getSounds());
                element.active = modifier.getSounds();
            }

            parent.hasInteractedWith = true;
        }
        else if (button == 1 && GuiScreen.isShiftKeyDown())
        {
            modifier.upgrades[element.ID] = false;
            elementList.remove(element);
            PacketDispatcher.sendPacketToServer(new PacketUpgrade(modifier, element.ID, false).getPacket());
        }
    }
}
