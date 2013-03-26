package alz.mods.enhancedportals.block;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import alz.mods.enhancedportals.EnhancedPortals;
import alz.mods.enhancedportals.client.ClientProxy;
import alz.mods.enhancedportals.helpers.PortalHelper;
import alz.mods.enhancedportals.helpers.WorldHelper;
import alz.mods.enhancedportals.reference.Reference;
import alz.mods.enhancedportals.tileentity.TileEntityPortalModifier;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class BlockPortalModifier extends BlockContainer
{
	private Icon[] activeFace;
	private Icon sideFace;
	
	public BlockPortalModifier()
	{
		super(Reference.BlockIDs.PortalModifier, Material.rock);
		setHardness(50.0F);
		setResistance(2000.0F);
		setStepSound(soundStoneFootstep);
		setUnlocalizedName(Reference.Strings.PortalModifier_Name);
		setCreativeTab(CreativeTabs.tabBlock);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		sideFace = iconRegister.registerIcon(Reference.Strings.PortalModifier_Icon_Side);
		
		activeFace = new Icon[16];
		
		for (int i = 0; i < 16; i++)
		{
			activeFace[i] = iconRegister.registerIcon(String.format(Reference.Strings.PortalModifier_Icon_Active, i));
		}
	}
	
	// Placed in world
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side)
	{
		TileEntityPortalModifier modifier = (TileEntityPortalModifier)world.getBlockTileEntity(x, y, z);
		int meta = world.getBlockMetadata(x, y, z);
		
		if (modifier.hasUpgrade(3))
		{
			return Block.blocksList[Reference.BlockIDs.Obsidian].getBlockTextureFromSide(0);
		}
		else if (modifier.hasUpgrade(0))
		{
			return activeFace[modifier.Colour];
		}
				
		return side == meta ? activeFace[modifier.Colour] : sideFace;
	}
	
	// For inventory
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTextureFromSideAndMetadata(int side, int meta)
	{
		return side == 1 ? activeFace[0] : sideFace;
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
						
			if (currentItem != null && Reference.Settings.CanDyePortals && currentItem.itemID == Item.dyePowder.itemID)
			{
				int colour = currentItem.getItemDamage();
				
				if (colour == 0)
					colour = 5;
			    else if (colour == 5)
			    	colour = 0;
				
				tileEntity.Colour = colour;
				
				if (!player.capabilities.isCreativeMode && Reference.Settings.DoesDyingCost)
					currentItem.stackSize--;
				
				if (world.getBlockId(x, y + 1, z) == Reference.BlockIDs.NetherPortal)
					WorldHelper.floodUpdateMetadata(world, x, y + 1, z, Reference.BlockIDs.NetherPortal, colour);
				else
					world.markBlockForRenderUpdate(x, y, z);
								
				if (world.isRemote)
					ClientProxy.SendBlockUpdate(tileEntity);
				else if (!world.isRemote)
					Reference.LinkData.sendUpdatePacketToNearbyClients(tileEntity);
								
				return true;
			}
		}
		else
			return true;
						
		player.openGui(EnhancedPortals.instance, Reference.GuiIDs.PortalModifier, world, x, y, z);
		return true;
	}
		
	@Override
	public TileEntity createNewTileEntity(World var1)
	{
		return new TileEntityPortalModifier();
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
		boolean hasMultiUpgrade = modifier.hasUpgrade(0);
		
		ForgeDirection direction = WorldHelper.getBlockDirection(world, x, y, z);
		int[] blockToTest = WorldHelper.offsetDirectionBased(world, x, y, z, direction);
		int blockID = world.getBlockId(blockToTest[0], blockToTest[1], blockToTest[2]),
			meta = world.getBlockMetadata(blockToTest[0], blockToTest[1], blockToTest[2]);
		
		if (gettingPower && !hadPower)
		{			
			if (hasMultiUpgrade)
				PortalHelper.createPortalAround(world, x, y, z, modifier.Colour);
			else
				if (WorldHelper.isBlockPortalRemovable(blockID))
					PortalHelper.createPortal(world, blockToTest[0], blockToTest[1], blockToTest[2], modifier.Colour);
		}
		else if (!gettingPower && hadPower)
		{
			if (hasMultiUpgrade)
				PortalHelper.removePortalAround(world, x, y, z);
			else
				if (blockID == Reference.BlockIDs.NetherPortal && meta == modifier.Colour)
					PortalHelper.removePortal(world, blockToTest[0], blockToTest[1], blockToTest[2]);
		}
		
		modifier.HadPower = gettingPower;
	}
	
	@Override	
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityLiving, ItemStack itemStack)
	{
		int direction = 0;
        int facing = MathHelper.floor_double((double)(entityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (facing == 0)
            direction = ForgeDirection.NORTH.ordinal();
        else if (facing == 1) 
            direction = ForgeDirection.EAST.ordinal();
        else if (facing == 2) 
            direction = ForgeDirection.SOUTH.ordinal();
        else if (facing == 3)
            direction = ForgeDirection.WEST.ordinal();
                
        if (entityLiving.rotationPitch > 65 && entityLiving.rotationPitch <= 90)
        	direction = ForgeDirection.UP.ordinal();
        else if (entityLiving.rotationPitch < -65 && entityLiving.rotationPitch >= -90)
        	direction = ForgeDirection.DOWN.ordinal();
        
        world.setBlockMetadataWithNotify(x, y, z, direction, 0);
	}
}