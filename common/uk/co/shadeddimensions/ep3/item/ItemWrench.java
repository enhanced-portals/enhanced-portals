package uk.co.shadeddimensions.ep3.item;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import uk.co.shadeddimensions.ep3.item.base.ItemPortalTool;
import uk.co.shadeddimensions.ep3.lib.GUIs;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.tileentity.TileStabilizer;

public class ItemWrench extends ItemPortalTool
{
    Icon texture;
    
    public ItemWrench(int id, String name)
    {
        super(id, true, name);
    }
    
    @Override
    public Icon getIconFromDamage(int par1)
    {
        return texture;
    }
    
    @Override
    public void registerIcons(IconRegister register)
    {
        texture = register.registerIcon("enhancedportals:wrench");
    }
    
    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int par7, float par8, float par9, float par10)
    {
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        
        if (tile != null && tile instanceof TileStabilizer)
        {
            TileStabilizer dbs = (TileStabilizer) tile;
            
            if (dbs.hasConfigured)
            {
                CommonProxy.openGui(player, GUIs.DimensionalBridgeStabilizer, dbs.getMainBlock());
                return true;
            }
        }
        
        return super.onItemUse(stack, player, world, x, y, z, par7, par8, par9, par10);
    }
}
