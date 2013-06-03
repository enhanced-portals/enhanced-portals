package enhancedportals.portal.upgrades.modifier;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import enhancedportals.EnhancedPortals;
import enhancedportals.lib.Localization;
import enhancedportals.portal.upgrades.Upgrade;

public class UpgradeResourceFrame extends Upgrade
{
    public UpgradeResourceFrame()
    {
        super();
    }

    @Override
    public ItemStack getDisplayItemStack()
    {
        return new ItemStack(Block.blockDiamond);
    }

    @Override
    public ItemStack getItemStack()
    {
        return new ItemStack(EnhancedPortals.proxy.portalModifierUpgrade, 1, getUpgradeID());
    }

    @Override
    public String getName()
    {
        return "resourceFrame";
    }

    @Override
    public List<String> getText(boolean includeTitle)
    {
        List<String> list = new ArrayList<String>();

        if (includeTitle)
        {
            list.add(EnumChatFormatting.AQUA + Localization.localizeString("item." + Localization.PortalModifierUpgrade_Name + "." + getName() + ".name"));
        }

        list.add(EnumChatFormatting.GRAY + Localization.localizeString("upgrade.blocks.text"));
        list.add(EnumChatFormatting.GRAY + Localization.localizeString("upgrade.blocks.textA"));
        list.add(EnumChatFormatting.DARK_AQUA + Localization.localizeString(Block.blockIron.getUnlocalizedName() + ".name"));
        list.add(EnumChatFormatting.DARK_AQUA + Localization.localizeString(Block.blockGold.getUnlocalizedName() + ".name"));
        list.add(EnumChatFormatting.DARK_AQUA + Localization.localizeString(Block.blockDiamond.getUnlocalizedName() + ".name"));

        if (includeTitle)
        {
            list.add(EnumChatFormatting.DARK_GRAY + "gui.upgrade.remove");
        }

        return list;
    }

    @Override
    public boolean onActivated(net.minecraft.tileentity.TileEntity tileEntity)
    {
        return true;
    }

    @Override
    public boolean onDeactivated(TileEntity tileEntity)
    {
        return true;
    }
}
