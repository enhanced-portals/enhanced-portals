package uk.co.shadeddimensions.ep3.item.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Icon;
import uk.co.shadeddimensions.ep3.block.BlockDecoration;
import uk.co.shadeddimensions.ep3.block.BlockFrame;
import uk.co.shadeddimensions.ep3.block.BlockStabilizer;
import uk.co.shadeddimensions.ep3.lib.Localization;

public class ItemDecoration extends ItemBlock
{
    public ItemDecoration(int id)
    {
        super(id);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
    {
        list.add(EnumChatFormatting.DARK_GRAY + Localization.getBlockString("decorativeBlock"));
    }

    @Override
    public Icon getIconFromDamage(int meta)
    {
        return meta == 0 ? BlockFrame.instance.getIcon(0, 0) : meta == 1 ? BlockStabilizer.instance.getIcon(0, 1) : null;
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
        for (int i = 0; i < BlockDecoration.BLOCK_TYPES; i++)
        {
            list.add(new ItemStack(itemID, 1, i));
        }
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
            name = "stabilizer";
        }

        return super.getUnlocalizedName() + "." + name;
    }
}
