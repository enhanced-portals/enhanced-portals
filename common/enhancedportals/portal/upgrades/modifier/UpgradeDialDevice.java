package enhancedportals.portal.upgrades.modifier;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import enhancedcore.world.WorldLocation;
import enhancedportals.EnhancedPortals;
import enhancedportals.lib.Localization;
import enhancedportals.lib.Strings;
import enhancedportals.portal.upgrades.Upgrade;
import enhancedportals.tileentity.TileEntityPortalModifier;

public class UpgradeDialDevice extends Upgrade
{
    public UpgradeDialDevice()
    {
        super();
    }

    @Override
    public ItemStack getDisplayItemStack()
    {
        return new ItemStack(EnhancedPortals.proxy.blockPortalModifier, 1, 0);
    }

    @Override
    public ItemStack getItemStack()
    {
        return new ItemStack(EnhancedPortals.proxy.portalModifierUpgrade, 1, getUpgradeID());
    }

    @Override
    public String getName()
    {
        return "dialDevice";
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
        TileEntityPortalModifier modifier = (TileEntityPortalModifier) tileEntity;
        WorldLocation loc = new WorldLocation(modifier.xCoord, modifier.yCoord, modifier.zCoord, modifier.worldObj);

        if (!tileEntity.worldObj.isRemote && EnhancedPortals.proxy.ModifierNetwork.isInNetwork(modifier.network, loc))
        {
            EnhancedPortals.proxy.ModifierNetwork.removeFromNetwork(modifier.network, loc);
        }

        modifier.network = "";
        return true;
    }

    @Override
    public boolean onDeactivated(TileEntity tileEntity)
    {
        TileEntityPortalModifier modifier = (TileEntityPortalModifier) tileEntity;

        // TODO : Remove from DHD network

        modifier.network = "";
        return true;
    }
}
