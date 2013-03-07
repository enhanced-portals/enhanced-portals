package alz.mods.enhancedportals.item;

import java.util.List;

import alz.mods.enhancedportals.reference.IO;
import alz.mods.enhancedportals.reference.ItemID;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemUpgrade extends Item
{
	String[] text = new String[] { "Multiple Portals", "Dimensional", "Advanced Dimensional" };

	public ItemUpgrade()
	{
		super(ItemID.ModifierUpgrade);
		hasSubtypes = true;
		this.setMaxDamage(0);
        this.setCreativeTab(CreativeTabs.tabMisc);
        setItemName("modifierUpgrade");
        maxStackSize = 1;
	}
	
	@SideOnly(Side.CLIENT)
    public int getIconFromDamage(int damage)
    {
        return damage;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack par1ItemStack)
	{
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack par1ItemStack)
	{
		return EnumRarity.uncommon;
	}
	
	@SideOnly(Side.CLIENT)
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int var4 = 0; var4 < text.length; var4++)
            par3List.add(new ItemStack(par1, 1, var4));
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, EntityPlayer par2EntityPlayer, List list, boolean par4)
	{
		list.add(text[itemStack.getItemDamage()]);
	}
	
	@Override
	public String getTextureFile()
	{
		return IO.ItemsPath;
	}
}
