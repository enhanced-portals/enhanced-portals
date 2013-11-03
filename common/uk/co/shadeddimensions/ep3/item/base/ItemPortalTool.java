package uk.co.shadeddimensions.ep3.item.base;

import net.minecraft.world.World;

public class ItemPortalTool extends ItemEnhancedPortals
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
