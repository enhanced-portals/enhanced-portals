package alz.mods.enhancedportals.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import alz.mods.enhancedportals.portals.TeleportData;
import alz.mods.enhancedportals.reference.Reference;
import alz.mods.enhancedportals.reference.Strings;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemScroll extends Item
{
	Icon Texture;

	public ItemScroll()
	{
		super(Reference.ItemIDs.ItemScroll);
		this.setMaxDamage(0);
		this.setCreativeTab(CreativeTabs.tabMisc);
		setUnlocalizedName(Strings.ItemScroll_Name);
		maxStackSize = 1;
		hasSubtypes = true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateIcons(IconRegister IconRegister)
	{
		Texture = IconRegister.registerIcon(Strings.ItemScroll_Icon);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int par1)
	{
		return Texture;
	}

	public void setLocationData(ItemStack Stack, TeleportData Data)
	{
		NBTTagCompound Tag = Stack.stackTagCompound;

		if (Tag == null)
		{
			Tag = new NBTTagCompound();
		}

		Tag.setInteger("x", Data.GetX());
		Tag.setInteger("y", Data.GetY());
		Tag.setInteger("z", Data.GetZ());
		Tag.setInteger("d", Data.GetDimension());
		Stack.stackTagCompound = Tag;
		Stack.setItemDamage(1);
	}

	public TeleportData getLocationData(ItemStack Stack)
	{
		NBTTagCompound Tag = Stack.stackTagCompound;

		if (Tag == null)
			return null;

		int x = Tag.getInteger("x");
		int y = Tag.getInteger("y");
		int z = Tag.getInteger("z");
		int d = Tag.getInteger("d");

		return new TeleportData(x, y, z, d);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		if (par1ItemStack.getItemDamage() == 1)
		{
			TeleportData Data = getLocationData(par1ItemStack);
			String dimensionString = "Overworld";

			if (Data == null)
				return;

			switch (Data.GetDimension())
			{
				case 0:
					dimensionString = "Overworld";
					break;

				case 1:
					dimensionString = "The End";
					break;

				case -1:
					dimensionString = "The Nether";
					break;

				default:
					dimensionString = "Unknown";
					break;
			}

			par3List.add("X: " + Data.GetX());
			par3List.add("Y: " + Data.GetY());
			par3List.add("Z: " + Data.GetZ());
			par3List.add(dimensionString);
			par3List.add(Data.GetLinksToModifier() ? "Modifier" : "Block");
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack par1ItemStack)
	{
		return par1ItemStack.getItemDamage() == 1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack par1ItemStack)
	{
		return EnumRarity.rare;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer)
	{
		if (itemStack.getItemDamage() > 0)
		{
			return itemStack;
		}
		
		int x = MathHelper.floor_double(entityPlayer.posX),
			y = MathHelper.floor_double(entityPlayer.posY),
			z = MathHelper.floor_double(entityPlayer.posZ);
		
		if (world.getBlockId(x, y - 1, z) == Reference.BlockIDs.PortalModifier)
		{
			setLocationData(itemStack, new TeleportData(x, y - 1, z, world.provider.dimensionId, true));
		}
		else
		{
			setLocationData(itemStack, new TeleportData(x, y, z, world.provider.dimensionId, false));
		}
		
		return itemStack;
	}
}
