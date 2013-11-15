package uk.co.shadeddimensions.ep3.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import uk.co.shadeddimensions.ep3.network.CommonProxy;

public class ItemScanner extends ItemBlock
{
    public ItemScanner(int par1)
    {
        super(par1);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
    {
        list.add("Multiblock Structure");
    }

    @Override
    public Icon getIconFromDamage(int par1)
    {
        return CommonProxy.blockScanner.getIcon(0, par1);
    }

    @Override
    public int getMetadata(int par1)
    {
        return par1;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List list)
    {
        for (int i = 0; i < 2; i++)
        {
            list.add(new ItemStack(CommonProxy.blockScanner.blockID, 1, i));
        }
    }
    
    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        int meta = stack.getItemDamage();
        String name = "unknown";

        if (meta == 0)
        {
            name = "controller";
        }
        else if (meta == 1)
        {
            name = "frame";
        }

        return super.getUnlocalizedName() + "." + name;
    }
}
