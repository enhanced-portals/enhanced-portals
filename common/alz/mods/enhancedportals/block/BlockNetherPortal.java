package alz.mods.enhancedportals.block;

import java.util.List;
import java.util.Random;

import alz.mods.enhancedportals.client.TextureNetherPortalEntityFX;
import alz.mods.enhancedportals.common.EnhancedPortals;
import alz.mods.enhancedportals.common.Reference;
import alz.mods.enhancedportals.common.TileEntityPortalModifier;
import alz.mods.enhancedportals.helpers.EntityHelper;
import alz.mods.enhancedportals.helpers.PortalHelper;
import alz.mods.enhancedportals.helpers.PortalHelper.PortalShape;
import alz.mods.enhancedportals.helpers.WorldHelper;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.BlockPortal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockNetherPortal extends BlockPortal
{
	public BlockNetherPortal()
	{
		super(90, 14);
		setHardness(-1.0F);
		setStepSound(soundGlassFootstep);
		setLightValue(0.75F);
		setBlockName("portal");
	}
		
	@SideOnly(Side.CLIENT)
	public String getTextureFile()
	{
		return Reference.textureLocation;
	}
	
	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
	{
		if (world.isRemote)
		{
			entity.setInPortal(); // Make the magical effects
			return; // There's nothing else we need to do on the client side here.
		}
		
		if (!Reference.allowTeleporting)
			return;
		
		// TODO Redo BEGIN
		
		//int[] firstModifier = WorldHelper.findFirstAttachedBlock(world, x, y, z, this.blockID, EnhancedPortals.instance.blockPortalModifier.blockID);
		int[] firstModifier = WorldHelper.findBestAttachedModifier(world, x, y, z, this.blockID, EnhancedPortals.instance.blockPortalModifier.blockID, world.getBlockMetadata(x, y, z));
		TileEntityPortalModifier modifier = null;
		
		if (firstModifier != null)
			modifier = (TileEntityPortalModifier)world.getBlockTileEntity(firstModifier[0], firstModifier[1], firstModifier[2]);
		
		if (modifier == null || modifier.Frequency == 0)
		{
			if (world.provider.dimensionId == 0 || world.provider.dimensionId == -1)
				entity.setInPortal(); // There's no Portal Modifier or it's frequency isn't set.
		}
		else
		{
			if (EntityHelper.canEntityTravel(entity))
			{				
				List<int[]> validExits = Reference.LinkData.getFrequencyExcluding(modifier.Frequency, new int[] { modifier.xCoord, modifier.yCoord, modifier.zCoord, world.provider.dimensionId });
				
				if (validExits == null || validExits.isEmpty())
				{
					Reference.LogData(String.format("Couldn't teleport entity (%s) - No valid exit found.", entity.getEntityName()));
					EntityHelper.sendMessage(entity, Reference.STR_NoExit);
				}
				else
				{
					int[] selectedExit = validExits.get(world.rand.nextInt(validExits.size()));					
					double exitX = selectedExit[0] + 0.5, exitY = selectedExit[1] + 1.0, exitZ = selectedExit[2] + 0.5;
					int exitD = selectedExit[3];
					boolean isValidPortal = false, canTeleport = true;
					World testWorld = world;
					
					if (exitD == world.provider.dimensionId)
						canTeleport = modifier.hasUpgrade(1) || modifier.hasUpgrade(2);
					else if ((exitD == 0 && world.provider.dimensionId == -1) || (exitD == -1 && world.provider.dimensionId == 0))
						canTeleport = true;
					else
						canTeleport = modifier.hasUpgrade(2);
					
					if (exitD != testWorld.provider.dimensionId)
						testWorld = WorldHelper.getWorld(exitD);
					
					if (testWorld.getBlockId(selectedExit[0], selectedExit[1] + 1, selectedExit[2]) == EnhancedPortals.instance.blockNetherPortal.blockID)
						isValidPortal = true;
					else if (testWorld.isAirBlock(selectedExit[0], selectedExit[1] + 1, selectedExit[2]))
						isValidPortal = PortalHelper.createPortal(testWorld, selectedExit[0], selectedExit[1] + 1, selectedExit[2], modifier.Colour);
					
					if (isValidPortal && canTeleport)
					{
						if (entity instanceof EntityMinecart)
							exitY += 0.5;
						
						EntityHelper.sendEntityToDimensionAndLocation(entity, exitD, exitX, exitY, exitZ);
					}
					else if (!canTeleport)
					{
						Reference.LogData(String.format("Couldn't teleport entity (%s) - Modifier does not have the required upgrade.", entity.getEntityName()));
						EntityHelper.sendMessage(entity, Reference.STR_NoUpgrade);
					}
					else
					{
						Reference.LogData(String.format("Couldn't teleport entity (%s) - Exit is blocked.", entity.getEntityName()));
						EntityHelper.sendMessage(entity, Reference.STR_ExitBlocked);
					}
				}
			}
			
			EntityHelper.setCanEntityTravel(entity, false);
		}
		
		// TODO Redo END
	}
	
	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int meta)
	{
		return meta;
	}
	
	@Override
	public boolean tryToCreatePortal(World world, int x, int y, int z)
	{
		if (world.getBlockId(x, y, z) == EnhancedPortals.instance.blockObsidian.blockID)
			y += 1;
		
		return PortalHelper.createPortal(world, x, y, z, 0);
	}
		
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		PortalHelper.PortalShape portalShape = PortalHelper.getPortalShape(world, x, y, z);
		
		if (portalShape == PortalShape.X)
			this.setBlockBounds(0.0F, 0.0F, 0.375F, 1.0F, 1.0F, 0.625F);
		else if (portalShape == PortalShape.Z)
			this.setBlockBounds(0.375F, 0.0F, 0.0F, 0.625F, 1.0F, 1.0F);
		else if (portalShape == PortalShape.HORIZONTAL)
			this.setBlockBounds(0.0F, 0.375F, 0.0F, 1.0F, 0.625F, 1.0F);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
	{
		if (side == 0)
			y++;
		else if (side == 1)
		      y--;
		else if (side == 2)
		      z++;
	    else if (side == 3)
		      z--;
	    else if (side == 4)
		      x++;
	    else if (side == 5)
		      x--;
		
		PortalHelper.PortalShape portalShape = PortalHelper.getPortalShape(world, x, y, z);
		
	    if (portalShape == PortalShape.HORIZONTAL)
	    {	    	
	    	if ((side == 1) && (world.getBlockId(x, y + 1, z) == this.blockID))
	    		return false;
	    	if ((side == 0) && (world.getBlockId(x, y - 1, z) == this.blockID))
	    		return false;
	    	if ((side == 0) || (side == 1))
	    		return true;
		    
	    	return false;
	    }
	    else if (portalShape == PortalShape.X)
	    {
	    	if ((side == 2) && (world.getBlockId(x, y, z - 1) == this.blockID))
	    		return false;
	    	if ((side == 3) && (world.getBlockId(x, y, z + 1) == this.blockID))
	    		return false;
	    	if ((side == 2) || (side == 3))
	    		return true;
	    	
	    	return false;
	    }
	    else if (portalShape == PortalShape.Z)
	    {
	    	if ((side == 4) && (world.getBlockId(x - 1, y, z) == this.blockID))
	    		return false;
	    	if ((side == 5) && (world.getBlockId(x + 1, y, z) == this.blockID))
	    		return false;
	    	if ((side == 4) || (side == 5)) 
	    		return true;
	    
	    	return false;
	    }

	    return false;
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int id)
	{
		// If the ID isn't 0, the portal wasn't destroyed
		if (id != 0)
			return;
				
		// Lets see if the portal is still intact
		if (PortalHelper.getPortalShape((World)world, x, y, z) == PortalShape.INVALID)
			PortalHelper.removePortal(world, x, y, z); // If it's not, deconstruct it
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random)
	{
		if (par5Random.nextInt(100) == 0)
        {
			if (Reference.soundLevel > 0 && par5Random.nextInt(100) <= Reference.soundLevel)
				par1World.playSound((double)par2 + 0.5D, (double)par3 + 0.5D, (double)par4 + 0.5D, "portal.portal", 0.5F, par5Random.nextFloat() * 0.4F + 0.8F, false);
        }

        for (int var6 = 0; var6 < 4; ++var6)
        {
            double var7 = (double)((float)par2 + par5Random.nextFloat());
            double var9 = (double)((float)par3 + par5Random.nextFloat());
            double var11 = (double)((float)par4 + par5Random.nextFloat());
            double var13 = 0.0D;
            double var15 = 0.0D;
            double var17 = 0.0D;
            int var19 = par5Random.nextInt(2) * 2 - 1;
            var13 = ((double)par5Random.nextFloat() - 0.5D) * 0.5D;
            var15 = ((double)par5Random.nextFloat() - 0.5D) * 0.5D;
            var17 = ((double)par5Random.nextFloat() - 0.5D) * 0.5D;

            if (par1World.getBlockId(par2 - 1, par3, par4) != this.blockID && par1World.getBlockId(par2 + 1, par3, par4) != this.blockID)
            {
                var7 = (double)par2 + 0.5D + 0.25D * (double)var19;
                var13 = (double)(par5Random.nextFloat() * 2.0F * (float)var19);
            }
            else
            {
                var11 = (double)par4 + 0.5D + 0.25D * (double)var19;
                var17 = (double)(par5Random.nextFloat() * 2.0F * (float)var19);
            }

            if (Reference.particleLevel > 0 && par5Random.nextInt(100) <= Reference.particleLevel)
            {
            	if (Reference.canDyePortals)
            		FMLClientHandler.instance().getClient().effectRenderer.addEffect(new TextureNetherPortalEntityFX(par1World, par1World.getBlockMetadata(par2, par3, par4), var7, var9, var11, var13, var15, var17));
            	else
            		par1World.spawnParticle("portal", var7, var9, var11, var13, var15, var17);
            }
        }
	}
	
	@Override
	public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
	{
		super.updateTick(par1World, par2, par3, par4, par5Random);

		if (Reference.pigmenSpawnChance > 0 && par5Random.nextInt(100) <= Reference.pigmenSpawnChance)
	        if (par1World.provider.isSurfaceWorld() && par5Random.nextInt(2000) < par1World.difficultySetting)
	        {
	            int var6;
	
	            for (var6 = par3; !par1World.doesBlockHaveSolidTopSurface(par2, var6, par4) && var6 > 0; --var6)
	            {
	                ;
	            }
	
	            if (var6 > 0 && !par1World.isBlockNormalCube(par2, var6 + 1, par4))
	            {
	                Entity var7 = ItemMonsterPlacer.spawnCreature(par1World, 57, (double)par2 + 0.5D, (double)var6 + 1.1D, (double)par4 + 0.5D);
	
	                if (var7 != null)
	                {
	                    var7.timeUntilPortal = var7.getPortalCooldown();
	                }
	            }
	        }
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
	{
		if (!Reference.canDyePortals)
			return false;
		
		ItemStack item = player.inventory.mainInventory[player.inventory.currentItem];
		
		if (item != null && item.itemID == Item.dyePowder.itemID)
		{
			int colour = item.getItemDamage();
			
			if (colour == 0)
				colour = 5;
		    else if (colour == 5)
		    	colour = 0;
			
			WorldHelper.floodUpdateMetadata(world, x, y, z, this.blockID, colour);
			
			if (!player.capabilities.isCreativeMode && Reference.doesDyingCost)
				item.stackSize--;
			
			return true;
		}
		
		return false;
	}
}
