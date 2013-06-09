package enhancedportals.portal.upgrades;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.tileentity.TileEntity;

public class UpgradeHandler
{
    private List<Upgrade> upgradeList;
    private int           maximumUpgrades;

    public UpgradeHandler()
    {
        upgradeList = new ArrayList<Upgrade>();
        maximumUpgrades = 5;
    }

    public UpgradeHandler(int maxUpgrades)
    {
        upgradeList = new ArrayList<Upgrade>();
        maximumUpgrades = maxUpgrades;
    }

    public boolean addUpgrade(byte ID, TileEntity tileEntity)
    {
        return addUpgrade(Upgrade.getUpgrade(ID), tileEntity);
    }

    public boolean addUpgrade(Upgrade upgrade, TileEntity tileEntity)
    {
        if (!hasUpgrade(upgrade) && !isUpgradeLimitReached())
        {
            if (tileEntity != null)
            {
                if (upgrade.onActivated(tileEntity))
                {
                    upgradeList.add(upgrade);
                }
            }
            else
            {
                upgradeList.add(upgrade);
            }

            return true;
        }

        return false;
    }

    public boolean addUpgradeNoActivate(byte ID, TileEntity tileEntity)
    {
        Upgrade upgrade = Upgrade.getUpgrade(ID);

        if (!hasUpgrade(upgrade) && !isUpgradeLimitReached())
        {
            upgradeList.add(upgrade);

            return true;
        }

        return false;
    }

    public void addUpgradesFromByteArray(byte[] upgrades, TileEntity tileEntity)
    {
        if (upgrades != null)
        {
            for (byte b : upgrades)
            {
                addUpgrade(b, tileEntity);
            }
        }
    }

    public byte[] getInstalledUpgrades()
    {
        byte[] installedUpgrades = new byte[upgradeList.size()];

        for (int i = 0; i < installedUpgrades.length; i++)
        {
            installedUpgrades[i] = upgradeList.get(i).getUpgradeID();
        }

        return installedUpgrades;
    }

    public int getMaximumUpgrades()
    {
        return maximumUpgrades;
    }

    public Upgrade getUpgrade(int i)
    {
        return upgradeList.get(i);
    }

    public List<Upgrade> getUpgrades()
    {
        return upgradeList;
    }

    public boolean hasUpgrade(byte ID)
    {
        return hasUpgrade(Upgrade.getUpgrade(ID));
    }

    public boolean hasUpgrade(Upgrade upgrade)
    {
        for (Upgrade u : upgradeList)
        {
            if (u.getUpgradeID() == upgrade.getUpgradeID())
            {
                return true;
            }
        }

        return false;
    }

    public boolean isUpgradeLimitReached()
    {
        return maximumUpgrades == upgradeList.size();
    }

    public boolean removeUpgrade(byte ID, TileEntity tileEntity)
    {
        return removeUpgrade(Upgrade.getUpgrade(ID), tileEntity);
    }

    public boolean removeUpgrade(Upgrade upgrade, TileEntity tileEntity)
    {
        if (!hasUpgrade(upgrade))
        {
            return false;
        }

        if (tileEntity != null)
        {
            if (upgrade.onDeactivated(tileEntity))
            {
                for (int i = 0; i < upgradeList.size(); i++)
                {
                    if (upgradeList.get(i).getUpgradeID() == upgrade.getUpgradeID())
                    {
                        upgradeList.remove(i);
                    }
                }
            }
        }
        else
        {
            for (int i = 0; i < upgradeList.size(); i++)
            {
                if (upgradeList.get(i).getUpgradeID() == upgrade.getUpgradeID())
                {
                    upgradeList.remove(i);
                }
            }
        }

        return !hasUpgrade(upgrade);
    }
}
