package uk.co.shadeddimensions.ep3.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import uk.co.shadeddimensions.ep3.item.base.ItemEnhancedPortals;
import uk.co.shadeddimensions.ep3.lib.Localization;

public class ItemEntityCard extends ItemEnhancedPortals
{
    public ItemEntityCard(int par1, String name)
    {
        super(par1, true);
        setUnlocalizedName(name);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addInformation(ItemStack stack, EntityPlayer par2EntityPlayer, List list, boolean par4)
    {
        NBTTagCompound tag = stack.getTagCompound();
        
        if (tag != null)
        {
            list.add(EnumChatFormatting.GRAY + Localization.getItemString("contains"));
            
            NBTTagList tagList = tag.getTagList("entities");
            
            for (int i = 0; i < 5; i++)
            {
                if (i >= tagList.tagCount())
                {
                    break;
                }
                
                NBTTagCompound t = (NBTTagCompound) tagList.tagAt(i);
                String s = t.getString("Name");
                
                if (s.contains("item.item."))
                {
                    s = StatCollector.translateToLocal(s.replace("item.item.", "item.") + ".name");
                }
                else if (s.contains("item.tile."))
                {
                    s = StatCollector.translateToLocal(s.replace("item.tile.", "tile.") + ".name");
                }
                
                list.add(EnumChatFormatting.DARK_GRAY + " " + s);
            }
            
            if (tagList.tagCount() > 5)
            {
                list.add(EnumChatFormatting.GRAY + String.format(Localization.getItemString("andMore"), tagList.tagCount() - 5));
            }
        }
    }
}
