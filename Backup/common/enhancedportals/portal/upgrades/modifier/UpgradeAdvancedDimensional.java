package enhancedportals.portal.upgrades.modifier;

import java.util.ArrayList;
import java.util.List;

import uk.co.shadeddimensions.enhancedportals.EnhancedPortals_deprecated;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import enhancedportals.lib.Localization;
import enhancedportals.lib.Strings;
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
        return new ItemStack(EnhancedPortals_deprecated.proxy.blockDummyPortal, 1, 14);
    }

    @Override
    public ItemStack getItemStack()
    {
        return new ItemStack(EnhancedPortals_deprecated.proxy.itemPortalModifierUpgrade, 1, getUpgradeID());
    }

    @Override
    public String getName()
    {
        return "advancedDimension";
    }

    @Override
    public List<String> getText(boolean includeTitle)
    {
        List<String> list = new ArrayList<String>();

        if (includeTitle)
        {
            list.add(EnumChatFormatting.AQUA + Localization.localizeString("item." + Localization.PortalModifierUpgrade_Name + "." + getName() + ".name"));
        }

        list.add(EnumChatFormatting.GRAY + Localization.localizeString("upgrade." + getName() + ".text"));

        if (includeTitle)
        {
            list.add(Strings.RemoveUpgrade.toString());
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
