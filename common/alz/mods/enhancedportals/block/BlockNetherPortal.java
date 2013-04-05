package alz.mods.enhancedportals.block;

import alz.mods.enhancedportals.client.TextureNetherPortalEntityFX;
import alz.mods.enhancedportals.helpers.EntityHelper;
import alz.mods.enhancedportals.helpers.PortalHelper;
import alz.mods.enhancedportals.helpers.WorldHelper;
import alz.mods.enhancedportals.helpers.PortalHelper.PortalShape;
import alz.mods.enhancedportals.portals.PortalTexture;
import alz.mods.enhancedportals.reference.Localizations;
import alz.mods.enhancedportals.reference.Reference;
import alz.mods.enhancedportals.reference.Strings;
import alz.mods.enhancedportals.teleportation.TeleportData;
import alz.mods.enhancedportals.tileentity.TileEntityNetherPortal;
import alz.mods.enhancedportals.tileentity.TileEntityPortalModifier;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockNetherPortal extends BlockContainer
{
	Icon[] textures;
	
    public BlockNetherPortal()
    {
        super(Reference.BlockIDs.NetherPortal, Material.portal);
		setHardness(-1.0F);
		setStepSound(soundGlassFootstep);
		setLightValue(0.75F);
		setUnlocalizedName("portal");
        this.setTickRandomly(true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int par5)
    {
    	PortalTexture texture = ((TileEntityNetherPortal)blockAccess.getBlockTileEntity(x, y, z)).Texture;
    	
    	return textures[texture.ordinal()];
    }
    
    @Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		textures = new Icon[18];

		for (int i = 0; i < 16; i++)
		{
			textures[i] = iconRegister.registerIcon(String.format(Strings.NetherPortal_Icon, i));
		}
		
		textures[16] = Block.lavaMoving.getBlockTextureFromSide(0);
		textures[17] = Block.waterMoving.getBlockTextureFromSide(0);
	}
    
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
    	super.updateTick(par1World, par2, par3, par4, par5Random);
    	
		if (Reference.Settings.PigmenSpawnChance > 0 && par5Random.nextInt(100) <= Reference.Settings.PigmenSpawnChance)
		{
			if (par1World.provider.isSurfaceWorld() && par5Random.nextInt(2000) < par1World.difficultySetting)
			{
				int var6;

				for (var6 = par3; !par1World.doesBlockHaveSolidTopSurface(par2, var6, par4) && var6 > 0; --var6)
				{
					;
				}

				if (var6 > 0 && !par1World.isBlockNormalCube(par2, var6 + 1, par4))
				{
					Entity var7 = ItemMonsterPlacer.spawnCreature(par1World, 57, par2 + 0.5D, var6 + 1.1D, par4 + 0.5D);

					if (var7 != null)
					{
						var7.timeUntilPortal = var7.getPortalCooldown();
					}
				}
			}
		}
    }

    
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return null;
    }

    
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
    	PortalHelper.PortalShape portalShape = PortalHelper.getPortalShape(world, x, y, z);

		if (portalShape == PortalShape.X)
		{
			this.setBlockBounds(0.0F, 0.0F, 0.375F, 1.0F, 1.0F, 0.625F);
		}
		else if (portalShape == PortalShape.Z)
		{
			this.setBlockBounds(0.375F, 0.0F, 0.0F, 0.625F, 1.0F, 1.0F);
		}
		else if (portalShape == PortalShape.HORIZONTAL)
		{
			this.setBlockBounds(0.0F, 0.375F, 0.0F, 1.0F, 0.625F, 1.0F);
		}
    }

    
    public boolean isOpaqueCube()
    {
        return false;
    }

    
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    
    public boolean tryToCreatePortal(World world, int x, int y, int z)
    {
    	if (world.getBlockId(x, y, z) == Reference.BlockIDs.Obsidian)
		{
			y += 1;
		}

		return PortalHelper.createPortal(world, x, y, z, 0);
    }

    public void onNeighborBlockChange(World world, int x, int y, int z, int id)
    {
    	if (world.isRemote || id == 0)
			return;

		// Lets see if the portal is still intact
		if (PortalHelper.getPortalShape(world, x, y, z) == PortalShape.INVALID)
		{
			PortalHelper.removePortal(world, x, y, z, PortalShape.INVALID); // If it's not, deconstruct it
		}
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
    {
    	if (side == 0)
		{
			y++;
		}
		else if (side == 1)
		{
			y--;
		}
		else if (side == 2)
		{
			z++;
		}
		else if (side == 3)
		{
			z--;
		}
		else if (side == 4)
		{
			x++;
		}
		else if (side == 5)
		{
			x--;
		}

		PortalHelper.PortalShape portalShape = PortalHelper.getPortalShape(world, x, y, z);

		if (portalShape == PortalShape.HORIZONTAL)
		{
			if (side == 1 && world.getBlockId(x, y + 1, z) == blockID)
				return false;
			if (side == 0 && world.getBlockId(x, y - 1, z) == blockID)
				return false;
			if (side == 0 || side == 1)
				return true;

			return false;
		}
		else if (portalShape == PortalShape.X)
		{
			if (side == 2 && world.getBlockId(x, y, z - 1) == blockID)
				return false;
			if (side == 3 && world.getBlockId(x, y, z + 1) == blockID)
				return false;
			if (side == 2 || side == 3)
				return true;

			return false;
		}
		else if (portalShape == PortalShape.Z)
		{
			if (side == 4 && world.getBlockId(x - 1, y, z) == blockID)
				return false;
			if (side == 5 && world.getBlockId(x + 1, y, z) == blockID)
				return false;
			if (side == 4 || side == 5)
				return true;

			return false;
		}

		return false;
    }

    public int quantityDropped(Random par1Random)
    {
        return 0;
    }

    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {
    	if (world.isRemote)
		{
			entity.setInPortal(); // Make the magical effects
			return; // There's nothing else we need to do on the client side here.
		}

		if (Reference.Settings.CanDyePortals && Reference.Settings.CanDyeByThrowing && entity instanceof EntityItem)
		{
			ItemStack item = ((EntityItem) entity).getEntityItem();

			if (item.itemID == Item.dyePowder.itemID)
			{
				int damage = item.getItemDamage();

				if (damage == 0)
				{
					damage = 5;
				}
				else if (damage == 5)
				{
					damage = 0;
				}

				if (damage == world.getBlockMetadata(x, y, z))
					return;

				WorldHelper.floodUpdateMetadata(world, x, y, z, blockID, damage, true);

				if (Reference.Settings.DoesDyingCost)
				{
					item.stackSize--;
				}

				return;
			}
		}

		if (!Reference.Settings.AllowTeleporting)
			return;

		int[] firstModifier = WorldHelper.findBestAttachedModifier(world, x, y, z, blockID, Reference.BlockIDs.PortalModifier, world.getBlockMetadata(x, y, z));
		TileEntityPortalModifier modifier = null;

		if (firstModifier != null)
		{
			modifier = (TileEntityPortalModifier) world.getBlockTileEntity(firstModifier[0], firstModifier[1], firstModifier[2]);
		}

		if (modifier == null || modifier.getFrequency() == 0)
		{
			if (world.provider.dimensionId == 0 || world.provider.dimensionId == -1)
			{
				entity.setInPortal(); // There's no Portal Modifier or it's frequency isn't set.
			}
		}
		else
		{
			if (EntityHelper.canEntityTravel(entity))
			{
				List<TeleportData> validExits = Reference.LinkData.getFrequencyExcluding(modifier.getFrequency(), new TeleportData(modifier.xCoord, modifier.yCoord, modifier.zCoord, world.provider.dimensionId));

				if (validExits == null || validExits.isEmpty())
				{
					Reference.LogData(String.format(Localizations.getLocalizedString(Strings.Console_NoExitFound), entity.getEntityName()));
					EntityHelper.sendMessage(entity, Localizations.getLocalizedString(Strings.Portal_NoExitFound));
				}
				else
				{
					TeleportData selectedExit = validExits.remove(world.rand.nextInt(validExits.size()));
					boolean isValidExit = true;

					while (!WorldHelper.isValidExitPortal(world, selectedExit, modifier, entity, true))
					{
						if (validExits.isEmpty())
						{
							isValidExit = false;
							break;
						}

						selectedExit = validExits.remove(world.rand.nextInt(validExits.size()));
					}

					if (!isValidExit)
					{
						Reference.LogData(String.format(Localizations.getLocalizedString(Strings.Console_NoExitFound), entity.getEntityName()));
						EntityHelper.sendMessage(entity, Localizations.getLocalizedString(Strings.Portal_NoExitFound));
					}
					else
					{
						EntityHelper.teleportEntity(entity, selectedExit);
					}
				}
			}

			EntityHelper.setCanEntityTravel(entity, false);
		}
    }

    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass()
    {
        return 1;
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
    	if (par5Random.nextInt(100) == 0)
		{
			if (Reference.Settings.SoundLevel > 0 && par5Random.nextInt(100) <= Reference.Settings.SoundLevel)
			{
				par1World.playSound(par2 + 0.5D, par3 + 0.5D, par4 + 0.5D, "portal.portal", 0.5F, par5Random.nextFloat() * 0.4F + 0.8F, false);
			}
		}

		for (int var6 = 0; var6 < 4; ++var6)
		{
			double var7 = par2 + par5Random.nextFloat();
			double var9 = par3 + par5Random.nextFloat();
			double var11 = par4 + par5Random.nextFloat();
			double var13 = 0.0D;
			double var15 = 0.0D;
			double var17 = 0.0D;
			int var19 = par5Random.nextInt(2) * 2 - 1;
			var13 = (par5Random.nextFloat() - 0.5D) * 0.5D;
			var15 = (par5Random.nextFloat() - 0.5D) * 0.5D;
			var17 = (par5Random.nextFloat() - 0.5D) * 0.5D;

			if (par1World.getBlockId(par2 - 1, par3, par4) != blockID && par1World.getBlockId(par2 + 1, par3, par4) != blockID)
			{
				var7 = par2 + 0.5D + 0.25D * var19;
				var13 = par5Random.nextFloat() * 2.0F * var19;
			}
			else
			{
				var11 = par4 + 0.5D + 0.25D * var19;
				var17 = par5Random.nextFloat() * 2.0F * var19;
			}

			if (Reference.Settings.ParticleLevel > 0 && par5Random.nextInt(100) <= Reference.Settings.ParticleLevel)
			{
				if (Reference.Settings.CanDyePortals)
				{
					FMLClientHandler.instance().getClient().effectRenderer.addEffect(new TextureNetherPortalEntityFX(par1World, par1World.getBlockMetadata(par2, par3, par4), var7, var9, var11, var13, var15, var17));
				}
				else
				{
					par1World.spawnParticle("portal", var7, var9, var11, var13, var15, var17);
				}
			}
		}
    }

    @SideOnly(Side.CLIENT)
    public int idPicked(World par1World, int par2, int par3, int par4)
    {
        return 0;
    }
    
    @Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
	{
		if (!Reference.Settings.CanDyePortals)
			return false;

		ItemStack item = player.inventory.mainInventory[player.inventory.currentItem];

		if (item != null && item.itemID == Item.dyePowder.itemID)
		{
			int colour = item.getItemDamage();

			if (colour == 0)
			{
				colour = 5;
			}
			else if (colour == 5)
			{
				colour = 0;
			}

			WorldHelper.floodUpdateMetadata(world, x, y, z, blockID, colour, true);

			if (!player.capabilities.isCreativeMode && Reference.Settings.DoesDyingCost)
			{
				item.stackSize--;
			}

			return true;
		}

		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileEntityNetherPortal();
	}
}
