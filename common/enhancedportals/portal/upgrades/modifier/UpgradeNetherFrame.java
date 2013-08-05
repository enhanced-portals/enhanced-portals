package enhancedportals.portal.upgrades.modifier;

import java.util.ArrayList;
import java.util.List;

import uk.co.shadeddimensions.enhancedportals.EnhancedPortals_deprecated;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import enhancedportals.lib.Localization;
import enhancedportals.lib.Settings;
import enhancedportals.lib.Strings;
import enhancedportals.portal.upgrades.Upgrade;

public class UpgradeNetherFrame extends Upgrade
{
    public UpgradeNetherFrame()
    {
        super();
    }

    @Override
    public ItemStack getDisplayItemStack()
    {
        return new ItemStack(Block.blockNetherQuartz, 1, 2);
    }

    @Override
    public ItemStack getItemStack()
    {
        return new ItemStack(EnhancedPortals_deprecated.proxy.itemPortalModifierUpgrade, 1, getUpgradeID());
    }

    @Override
    public String getName()
    {
        return "netherFrame";
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

        if (Settings.NetherFrameUpgrade.isEmpty())
        {
            list.add(EnumChatFormatting.DARK_AQUA + Localization.localizeString(Block.glowStone.getUnlocalizedName() + ".name"));
            list.add(EnumChatFormatting.DARK_AQUA + Localization.localizeString(Block.netherBrick.getUnlocalizedName() + ".name"));
            list.add(EnumChatFormatting.DARK_AQUA + Localization.localizeString(Block.blockNetherQuartz.getUnlocalizedName() + ".default.name"));
        }
        else
        {
            for (int id : Settings.NetherFrameUpgrade)
            {
                list.add(EnumChatFormatting.DARK_AQUA + new ItemStack(id, 1, 0).getDisplayName());
            }
        }

        if (includeTitle)
        {
            list.add(Strings.RemoveUpgrade.toString());
        }

        return list;
    }

    @Override
    public boolean onActivated(net.minecraft.tileentity.TileEntity tileEntity)
    {
        return true;
    }

    @Override
    public boolean onDeactivated(net.minecraft.tileentity.TileEntity tileEntity)
    {
        return true;
    }
}
