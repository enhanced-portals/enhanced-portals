package uk.co.shadeddimensions.ep3.item;

import uk.co.shadeddimensions.ep3.api.IPortalTool;

public abstract class ItemPortalTool extends ItemEnhancedPortals implements IPortalTool
{
    public ItemPortalTool(int id, boolean tab, String name)
    {
        super(id, tab);
        setUnlocalizedName(name);
        maxStackSize = 1;
        setMaxDamage(0);
    }
}
