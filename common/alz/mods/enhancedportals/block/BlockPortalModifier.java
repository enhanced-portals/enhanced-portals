package alz.mods.enhancedportals.block;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import alz.mods.enhancedportals.client.ClientNetworking;
import alz.mods.enhancedportals.common.EnhancedPortals;
import alz.mods.enhancedportals.common.Reference;
import alz.mods.enhancedportals.common.TileEntityPortalModifier;
import alz.mods.enhancedportals.helpers.PortalHelper;
import alz.mods.enhancedportals.helpers.WorldHelper;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPortalModifier extends BlockContainer
{
	public BlockPortalModifier()
	{
		super(Reference.IDPortalModifier, 0, Material.rock);
		setHardness(50.0F);
		setResistance(2000.0F);
		setStepSound(soundStoneFootstep);
		setBlockName("blockPortalModifier");
		setCreativeTab(CreativeTabs.tabBlock);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
	{
		TileEntityPortalModifier tileEntity = (TileEntityPortalModifier)world.getBlockTileEntity(x, y, z);
		
		if (player.isSneaking() || tileEntity == null)
			return false;
		
		if (!PortalHelper.createPortalAround(world, x, y, z, tileEntity.Colour, player))
		{
			ItemStack currentItem = player.inventory.mainInventory[player.inventory.currentItem];
						
			if (currentItem != null && Reference.canDyePortals && currentItem.itemID == Item.dyePowder.itemID)
			{
				int colour = currentItem.getItemDamage();
				
				if (colour == 0)
					colour = 5;
			    else if (colour == 5)
			    	colour = 0;
				
				tileEntity.Colour = colour;
				
				if (!player.capabilities.isCreativeMode && Reference.doesDyingCost)
					currentItem.stackSize--;
				
				if (world.getBlockId(x, y + 1, z) == EnhancedPortals.instance.blockNetherPortal.blockID)
					WorldHelper.floodUpdateMetadata(world, x, y, z, EnhancedPortals.instance.blockNetherPortal.blockID, colour);
				else
					world.markBlockForRenderUpdate(x, y, z);
				
				if (world.isRemote)
					ClientNetworking.SendBlockUpdate(-1, colour, x, y, z, world.provider.dimensionId);
				
				return true;
			}
			else if (currentItem != null && currentItem.itemID == EnhancedPortals.instance.itemUpgrade.itemID)
			{
				if (tileEntity.hasFreeSpace() && tileEntity.insertUpgrade(currentItem))
				{
					currentItem.stackSize = 0;
					world.markBlockForRenderUpdate(x, y, z);
				
					return true;
				}
			}
		}
		else
			return true;
						
		player.openGui(EnhancedPortals.instance, Reference.IDPortalModifierGui, world, x, y, z);
		return true;
	}
	
	// Placed in world
	@Override
	@SideOnly(Side.CLIENT)
	public int getBlockTexture(IBlockAccess world, int x, int y, int z, int side)
	{
		TileEntityPortalModifier modifier = (TileEntityPortalModifier)world.getBlockTileEntity(x, y, z);
		
		if (modifier.hasUpgrade(0))
		{
			return 16 + modifier.Colour;
		}
		
		return side == 1 ? 16 + modifier.Colour : 32;
	}

	// For inventory
	@Override
	public int getBlockTextureFromSide(int side)
	{		
		return side == 1 ? 16 : 32;
	}
	
	@Override
	public TileEntity createNewTileEntity(World var1)
	{
		return new TileEntityPortalModifier();
	}
	
	@Override
	public String getTextureFile()
	{
		return Reference.textureLocation;
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6)
	{
		if (!world.isRemote)
		{
			TileEntityPortalModifier tileEntity = (TileEntityPortalModifier)world.getBlockTileEntity(x, y, z);
			
			if (tileEntity != null && tileEntity.Frequency != 0)
				Reference.LinkData.RemoveFromFrequency(tileEntity.Frequency, x, y, z, world.provider.dimensionId);
		}
		
		dropItems(world, x, y, z);
		super.breakBlock(world, x, y, z, par5, par6);
	}
	
	void dropItems(World world, int x, int y, int z)
	{
		Random rand = new Random();
		
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		
		if (!(tileEntity instanceof IInventory))
			return;
		
		IInventory inventory = (IInventory)tileEntity;
		
		for (int i = 0; i < inventory.getSizeInventory(); i++)
		{
			ItemStack item = inventory.getStackInSlot(i);
			
			if (item == null || item.stackSize == 0)
				return;
			
			float rx = rand.nextFloat() * 0.8F + 0.1F,
				  ry = rand.nextFloat() * 0.8F + 0.1F,
				  rz = rand.nextFloat() * 0.8F + 0.1F;
			
			EntityItem entityItem = new EntityItem(world, x + rx, y + ry, z + rz, new ItemStack(item.itemID, item.stackSize, item.getItemDamage()));
			
			if (item.hasTagCompound())
				entityItem.getEntityData().setCompoundTag(item.getTagCompound().getName(), (NBTTagCompound)item.getTagCompound().copy());
			
			float factor = 0.05F;
			entityItem.motionX = rand.nextGaussian() * factor;
			entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
			entityItem.motionZ = rand.nextGaussian() * factor;
			
			world.spawnEntityInWorld(entityItem);
			item.stackSize = 0;
		}
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int id)
	{
		TileEntityPortalModifier modifier = (TileEntityPortalModifier) world.getBlockTileEntity(x, y, z);
		boolean gettingPower = world.isBlockIndirectlyGettingPowered(x, y, z), hadPower = modifier.HadPower;
		int blockIDAbove = world.getBlockId(x, y + 1, z), metaAbove = world.getBlockMetadata(x, y + 1, z);
		boolean hasMultiUpgrade = modifier.hasUpgrade(0);
		
		if (gettingPower && !hadPower)
		{			
			if (hasMultiUpgrade)
				PortalHelper.createPortalAround(world, x, y, z, modifier.Colour);
			else
				if (WorldHelper.isBlockPortalRemovable(blockIDAbove))
					PortalHelper.createPortal(world, x, y + 1, z, modifier.Colour);
		}
		else if (!gettingPower && hadPower)
		{
			if (hasMultiUpgrade)
				PortalHelper.removePortalAround(world, x, y, z);
			else
				if (blockIDAbove == EnhancedPortals.instance.blockNetherPortal.blockID && metaAbove == modifier.Colour)
					PortalHelper.removePortal(world, x, y + 1, z);
		}
		
		modifier.HadPower = gettingPower;
	}
}