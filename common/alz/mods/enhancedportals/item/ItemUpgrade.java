package alz.mods.enhancedportals.item;

import java.util.List;

import alz.mods.enhancedportals.reference.ItemID;
import alz.mods.enhancedportals.reference.ModData;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public class ItemUpgrade extends Item
{
	String[] text = new String[] { "Multiple Portals", "Dimensional", "Advanced Dimensional", "Modifier Camouflage" };
	Icon[] textures;
	
	public ItemUpgrade()
	{
		super(ItemID.ModifierUpgrade);
		hasSubtypes = true;
		this.setMaxDamage(0);
        this.setCreativeTab(CreativeTabs.tabMisc);
        setUnlocalizedName("modifierUpgrade");
        maxStackSize = 1;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void func_94581_a(IconRegister iconRegister)
	{
		textures = new Icon[text.length];
		
		for (int i = 0; i < textures.length; i++)
		{
			textures[i] = iconRegister.func_94245_a(ModData.ID + ":modifierUpgrade_" + i);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int damage)
	{
		return textures[damage];
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@SideOnly(Side.CLIENT)
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int var4 = 0; var4 < text.length; var4++)
            par3List.add(new ItemStack(par1, 1, var4));
    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, EntityPlayer par2EntityPlayer, List list, boolean par4)
	{
		list.add(text[itemStack.getItemDamage()]);
	}
}
