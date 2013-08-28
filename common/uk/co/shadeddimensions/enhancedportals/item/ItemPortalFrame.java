package uk.co.shadeddimensions.enhancedportals.item;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemPortalFrame extends ItemBlock
{
    public ItemPortalFrame(int par1)
    {
        super(par1);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int par1)
    {
        return par1;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        int meta = stack.getItemDamage();
        String name = "unknown";

        if (meta == 0)
        {
            name = "frame";
        }
        else if (meta == 1)
        {
            name = "controller";
        }
        else if (meta == 2)
        {
            name = "redstone";
        }

        return super.getUnlocalizedName() + "." + name;
    }
}
