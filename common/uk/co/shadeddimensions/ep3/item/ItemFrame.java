package uk.co.shadeddimensions.ep3.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import uk.co.shadeddimensions.ep3.block.BlockFrame;
import uk.co.shadeddimensions.ep3.lib.Localization;
import uk.co.shadeddimensions.ep3.network.CommonProxy;

public class ItemFrame extends ItemBlockWithMetadata
{
    public static String[] unlocalizedName = new String[] { "frame", "controller", "redstone", "networkInterface", "dialDevice", "biometric", "upgrade" };

    public ItemFrame(int par1)
    {
        super(par1, CommonProxy.blockFrame);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
    {
        int damage = stack.getItemDamage();

        if (damage > 0)
        {
            list.add(Localization.getBlockString("portalFramePart"));
        }
    }

    @Override
    public Icon getIconFromDamageForRenderPass(int meta, int pass)
    {
        if (pass == 0)
        {
            return getIconFromDamage(meta);
        }

        return BlockFrame.overlayIcons[meta];
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List list)
    {
        for (int i = 0; i < BlockFrame.FRAME_TYPES; i++)
        {
            list.add(new ItemStack(CommonProxy.blockFrame.blockID, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName() + "." + unlocalizedName[stack.getItemDamage()];
    }

    @Override
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }
}
