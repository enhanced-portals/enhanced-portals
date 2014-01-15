package uk.co.shadeddimensions.ep3.item;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import uk.co.shadeddimensions.ep3.item.base.ItemPortalTool;
import buildcraft.api.tools.IToolWrench;
import cofh.api.block.IDismantleable;

public class ItemWrench extends ItemPortalTool implements IToolWrench
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
    public boolean canWrench(EntityPlayer player, int x, int y, int z)
    {
        return true;
    }

    @Override
    public void wrenchUsed(EntityPlayer player, int x, int y, int z)
    {
        
    }
    
    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote && player.isSneaking())
        {
            Block block = Block.blocksList[world.getBlockId(x, y, z)];
            
            if (block instanceof IDismantleable)
            {
                if (((IDismantleable) block).canDismantle(player, world, x, y, z))
                {
                    ((IDismantleable) block).dismantleBlock(player, world, x, y, z, false);
                    return true;
                }
            }
        }
        
        return false;
    }
}
