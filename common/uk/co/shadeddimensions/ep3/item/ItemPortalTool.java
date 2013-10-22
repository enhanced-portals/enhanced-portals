package uk.co.shadeddimensions.ep3.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import uk.co.shadeddimensions.ep3.api.IPortalTool;
import uk.co.shadeddimensions.ep3.tileentity.TilePortalPart;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;

public class ItemPortalTool extends ItemEnhancedPortals implements IPortalTool
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
    
    @Override
    public boolean openGui(TilePortalPart portalPart, ItemStack stack, boolean isSneaking, EntityPlayer player)
    {
        TilePortalController controller = portalPart.getPortalController();

        if (controller != null && !controller.hasConfigured)
        {
            return false;
        }
        
        return true;
    }
}
