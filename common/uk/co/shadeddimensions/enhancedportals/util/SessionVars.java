/**
 * Derived from BuildCraft released under the MMPL https://github.com/BuildCraft/BuildCraft http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package uk.co.shadeddimensions.enhancedportals.util;

public class SessionVars
{
    private static Class<?> openedLedger;

    public static Class<?> getOpenedLedger()
    {
        return openedLedger;
    }

    public static void setOpenedLedger(Class<?> ledgerClass)
    {
        openedLedger = ledgerClass;
    }
}
