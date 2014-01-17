package uk.co.shadeddimensions.ep3.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import uk.co.shadeddimensions.ep3.util.WorldCoordinates;

public class ItemLocationCard extends ItemEnhancedPortals
{
    public static void clearDBSLocation(ItemStack s)
    {
        s.setTagCompound(null);
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

    public static boolean hasDBSLocation(ItemStack s)
    {
        return s.hasTagCompound();
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

    Icon texture;

    public ItemLocationCard(int id, String name)
    {
        super(id, true);
        setUnlocalizedName(name);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
    {
        WorldCoordinates w = getDBSLocation(stack);

        if (w != null)
        {
            list.add("Location set");
        }
    }

    @Override
    public Icon getIconFromDamage(int par1)
    {
        return texture;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if (player.isSneaking() && hasDBSLocation(stack))
        {
            clearDBSLocation(stack);
            return stack;
        }

        return stack;
    }

    @Override
    public void registerIcons(IconRegister register)
    {
        texture = register.registerIcon("enhancedportals:locationCard");
    }
}
