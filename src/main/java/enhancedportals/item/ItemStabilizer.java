package enhancedportals.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import enhancedportals.EnhancedPortals;
import enhancedportals.block.BlockStabilizer;

public class ItemStabilizer extends ItemBlock
{
    public ItemStabilizer(Block b)
    {
        super(b);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
    {
        list.add(EnhancedPortals.localize("block.multiblockStructure"));
        list.add(EnumChatFormatting.DARK_GRAY + EnhancedPortals.localize("block.dbsSize"));
    }

    @Override
    public IIcon getIconFromDamage(int par1)
    {
        return BlockStabilizer.instance.getBlockTextureFromSide(0);
    }

    @Override
    public int getMetadata(int par1)
    {
        return par1;
    }
}
