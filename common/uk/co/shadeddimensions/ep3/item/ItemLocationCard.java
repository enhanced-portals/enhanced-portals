package uk.co.shadeddimensions.ep3.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import uk.co.shadeddimensions.ep3.item.base.ItemEnhancedPortals;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.tileentity.TileStabilizer;
import uk.co.shadeddimensions.ep3.tileentity.TileStabilizerMain;
import uk.co.shadeddimensions.ep3.util.WorldCoordinates;

public class ItemLocationCard extends ItemEnhancedPortals
{
    Icon texture;

    public ItemLocationCard(int id, String name)
    {
        super(id, true);
        setUnlocalizedName(name);
    }

    public static boolean hasDBSLocation(ItemStack s)
    {
        return s.hasTagCompound();
    }

    public static WorldCoordinates getDBSLocation(ItemStack s)
    {
        if (hasDBSLocation(s))
        {
            NBTTagCompound t = s.getTagCompound();            
            return new WorldCoordinates(t.getInteger("X"), t.getInteger("Y"), t.getInteger("Z"), t.getInteger("D"));
        }

        return null;
    }

    public static void setDBSLocation(ItemStack s, WorldCoordinates w)
    {
        NBTTagCompound t = new NBTTagCompound();
        t.setInteger("X", w.posX);
        t.setInteger("Y", w.posY);
        t.setInteger("Z", w.posZ);
        t.setInteger("D", w.dimension);

        s.setTagCompound(t);
    }

    public static void clearDBSLocation(ItemStack s)
    {
        s.setTagCompound(null);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
    {
        WorldCoordinates w = getDBSLocation(stack);

        if (w != null)
        {
            list.add(EnumChatFormatting.GRAY + DimensionManager.getProvider(w.dimension).getDimensionName());
            list.add(EnumChatFormatting.DARK_GRAY + "X: " + w.posX);
            list.add(EnumChatFormatting.DARK_GRAY + "Y: " + w.posY);
            list.add(EnumChatFormatting.DARK_GRAY + "Z: " + w.posZ);
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if (player.isSneaking() && hasDBSLocation(stack))
        {
            clearDBSLocation(stack);
            return stack;
        }
        else
        {
            MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world,player, true);
            
            if (movingobjectposition == null)
            {
                return stack;
            }
            else
            {
                if (movingobjectposition.typeOfHit == EnumMovingObjectType.TILE)
                {
                    TileEntity tile = world.getBlockTileEntity(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ);
                    
                    if (tile != null && tile instanceof TileStabilizer)
                    {
                        tile = ((TileStabilizer) tile).getMainBlock();
                    }
                    
                    if (tile != null && tile instanceof TileStabilizerMain)
                    {
                        ItemStack s = new ItemStack(CommonProxy.itemLocationCard, 1);
                        setDBSLocation(s, ((TileStabilizerMain) tile).getWorldCoordinates());
                        
                       if (--stack.stackSize <= 0)
                       {
                           return s;
                       }
                       
                       if (!player.inventory.addItemStackToInventory(s))
                       {
                           player.dropPlayerItem(s);
                       }
                       
                       return stack;
                    }
                }
            }
        }

        return stack;
    }

    @Override
    public Icon getIconFromDamage(int par1)
    {
        return texture;
    }

    @Override
    public void registerIcons(IconRegister register)
    {
        texture = register.registerIcon("enhancedportals:locationCard");
    }
}
