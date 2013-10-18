package uk.co.shadeddimensions.ep3.item;

import net.minecraft.world.World;
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
    
    @Override
    public boolean shouldPassSneakingClickToBlock(World par2World, int par4, int par5, int par6)
    {
        return true;
    }
}
