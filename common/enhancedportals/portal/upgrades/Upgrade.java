package enhancedportals.portal.upgrades;

import java.util.List;
import java.util.logging.Level;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import enhancedportals.lib.Reference;
import enhancedportals.portal.upgrades.modifier.UpgradeAdvancedDimensional;
import enhancedportals.portal.upgrades.modifier.UpgradeCamouflage;
import enhancedportals.portal.upgrades.modifier.UpgradeDimensional;
import enhancedportals.portal.upgrades.modifier.UpgradeNetherFrame;
import enhancedportals.portal.upgrades.modifier.UpgradeParticles;
import enhancedportals.portal.upgrades.modifier.UpgradeResourceFrame;
import enhancedportals.portal.upgrades.modifier.UpgradeSounds;

public abstract class Upgrade
{
    enum Type
    {
        PARTICLES(UpgradeParticles.class), SOUNDS(UpgradeSounds.class), DIMENSIONAL(UpgradeDimensional.class), ADVANCED_DIMENSIONAL(UpgradeAdvancedDimensional.class), COMPUTER(UpgradeParticles.class), NETHER_FRAME(UpgradeNetherFrame.class), RESOURCE_FRAME(UpgradeResourceFrame.class), CAMOUFLAGE(UpgradeCamouflage.class);

        private Class<? extends Upgrade> upgradeType;

        private Type(Class<? extends Upgrade> type)
        {
            upgradeType = type;
        }

        Upgrade make()
        {
            try
            {
                return upgradeType.newInstance();
            }
            catch (Exception e)
            {
                Reference.log.log(Level.SEVERE, "Something went wrong when constructing the upgrade. Cannot continue.");
                throw new RuntimeException(e);
            }
        }
    }

    public static Upgrade getUpgrade(int ID)
    {
        if (ID >= 0 && ID < Type.values().length)
        {
            return Type.values()[ID].make();
        }

        return null;
    }

    private Type type;

    public Upgrade()
    {
        for (Type t : Type.values())
        {
            if (t.upgradeType == getClass())
            {
                type = t;
                break;
            }
        }

        if (type == null)
        {

            throw new RuntimeException("Can not create an upgrade of an unregistered type.");
        }
    }

    public ItemStack getDisplayItemStack()
    {
        return null;
    }

    public ItemStack getItemStack()
    {
        return null;
    }

    public Upgrade getNewInstance()
    {
        return type.make();
    }

    public List<String> getText(boolean includeTitle)
    {
        return null;
    }

    public byte getUpgradeID()
    {
        return (byte) type.ordinal();
    }

    public boolean onActivated(TileEntity tileEntity)
    {
        return false;
    }

    public boolean onDeactivated(TileEntity tileEntity)
    {
        return false;
    }

    public static Upgrade[] getAllUpgrades()
    {
        Upgrade[] upgrades = new Upgrade[Type.values().length];
        
        for (int i = 0; i < upgrades.length; i++)
        {
            upgrades[i] = getUpgrade(i);
        }
        
        return upgrades;
    }
}
