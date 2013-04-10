package alz.mods.enhancedportals.portals;

import alz.mods.enhancedportals.reference.Localizations;
import alz.mods.enhancedportals.reference.Strings;

public enum PortalUpgrade
{
    MULTIPLE_PORTALS, DIMENSIONAL, ADVANCED_DIMENSIONAL, MODIFIER_CAMO, COMPUTER, UNKNOWN;

    public static String getLocalizedName(int id)
    {
        return Localizations.getLocalizedString(getUnlocalizedName(id));
    }
    
    public String getLocalizedName()
    {
        return Localizations.getLocalizedString(getUnlocalizedName());
    }

    public static String getUnlocalizedName(int id)
    {
        switch (id)
        {
            case 0:
                return Strings.PortalModifierUpgrade_MultiplePortals_Name;
            case 1:
                return Strings.PortalModifierUpgrade_Dimensional_Name;
            case 2:
                return Strings.PortalModifierUpgrade_AdvancedDimensional_Name;
            case 3:
                return Strings.PortalModifierUpgrade_ModifierCamo_Name;
            case 4:
                return Strings.PortalModifierUpgrade_Computer_Name;
        }

        return "";
    }
    
    public String getUnlocalizedName()
    {
        switch (ordinal())
        {
            case 0:
                return Strings.PortalModifierUpgrade_MultiplePortals_Name;
            case 1:
                return Strings.PortalModifierUpgrade_Dimensional_Name;
            case 2:
                return Strings.PortalModifierUpgrade_AdvancedDimensional_Name;
            case 3:
                return Strings.PortalModifierUpgrade_ModifierCamo_Name;
            case 4:
                return Strings.PortalModifierUpgrade_Computer_Name;
        }

        return "";
    }
    
    public static PortalUpgrade getPortalUpgrade(int id)
    {
        if (id < 0 || id >= PortalUpgrade.values().length)
        {
            return PortalUpgrade.UNKNOWN;
        }
        
        return PortalUpgrade.values()[id];
    }
}
