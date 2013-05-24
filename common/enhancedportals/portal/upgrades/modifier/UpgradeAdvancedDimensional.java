package enhancedportals.portal.upgrades.modifier;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import enhancedportals.EnhancedPortals;
import enhancedportals.lib.Localization;
import enhancedportals.portal.upgrades.Upgrade;

public class UpgradeAdvancedDimensional extends Upgrade
{
    public UpgradeAdvancedDimensional()
    {
        super();
    }

    @Override
    public ItemStack getDisplayItemStack()
    {
        return new ItemStack(EnhancedPortals.proxy.blockDummyPortal, 1, 14);
    }

    @Override
    public ItemStack getItemStack()
    {
        return new ItemStack(EnhancedPortals.proxy.portalModifierUpgrade, 1, getUpgradeID());
    }

    @Override
    public List<String> getText(boolean includeTitle)
    {
        List<String> list = new ArrayList<String>();

        if (includeTitle)
        {
            list.add(EnumChatFormatting.AQUA + Localization.localizeString("item." + Localization.PortalModifierUpgrade_Name + ".advancedDimension.name"));
        }

        list.add(EnumChatFormatting.GRAY + Localization.localizeString("upgrade.advancedDimensional.text"));
        list.add(EnumChatFormatting.GRAY + Localization.localizeString("upgrade.advancedDimensional.textB"));
        list.add(EnumChatFormatting.GRAY + Localization.localizeString("upgrade.advancedDimensional.textC"));

        if (includeTitle)
        {
            list.add(EnumChatFormatting.DARK_GRAY + "gui.upgrade.remove");
        }

        return list;
    }

    @Override
    public boolean onActivated(TileEntity tileEntity)
    {
        return true;
    }

    @Override
    public boolean onDeactivated(TileEntity tileEntity)
    {
        return true;
    }
}
