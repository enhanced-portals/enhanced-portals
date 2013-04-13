package com.alz.enhancedportals.core.blocks;

import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import com.alz.enhancedportals.core.client.texturefx.TextureNetherPortalEntityFX;
import com.alz.enhancedportals.core.tileentity.TileEntityNetherPortal;
import com.alz.enhancedportals.core.tileentity.TileEntityPortalModifier;
import com.alz.enhancedportals.helpers.WorldLocation;
import com.alz.enhancedportals.reference.BlockIds;
import com.alz.enhancedportals.reference.Localizations;
import com.alz.enhancedportals.reference.Log;
import com.alz.enhancedportals.reference.Reference;
import com.alz.enhancedportals.reference.Settings;
import com.alz.enhancedportals.reference.Strings;
import com.alz.enhancedportals.portals.EntityHandler;
import com.alz.enhancedportals.portals.PortalHandler;
import com.alz.enhancedportals.portals.PortalShape;
import com.alz.enhancedportals.portals.PortalTexture;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockNetherPortal extends BlockEnhancedPortalsContainer
{
    public BlockNetherPortal()
    {
        super(BlockIds.NETHER_PORTAL, Material.portal);
        setHardness(-1.0F);
        setStepSound(soundGlassFootstep);
        setLightValue(0.75F);
        setUnlocalizedName("portal");
        setTickRandomly(true);
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileEntityNetherPortal();
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int par5)
    {
        return ((TileEntityNetherPortal) blockAccess.getBlockTileEntity(x, y, z)).PortalTexture.getPortalIcon();
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
        return PortalTexture.getPortalIcon(0);
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass()
    {
        return 1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int idPicked(World par1World, int par2, int par3, int par4)
    {
        return 0;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        if (Settings.Server.CAN_DYE_PORTALS)
        {
            WorldLocation location = new WorldLocation(x, y, z, world);
            TileEntityNetherPortal netherPortal = (TileEntityNetherPortal) location.getTileEntity();
            PortalTexture newTexture = PortalTexture.getPortalTextureFromItem(player.inventory.mainInventory[player.inventory.currentItem], netherPortal.PortalTexture, Settings.Server.DOES_DYING_COST && !player.capabilities.isCreativeMode, player);

            if (newTexture != PortalTexture.UNKNOWN && !world.isRemote)
            {
                PortalHandler.floodUpdateTexture(location, newTexture, netherPortal.PortalTexture, true);
            }
        }

        return true;
    }
    
    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {
        if (world.isRemote)
        {
            if (Settings.Client.RENDER_PORTAL_EFFECT)
            {
                entity.setInPortal();
            }

            return;
        }

        WorldLocation location = new WorldLocation(x, y, z, world);
        TileEntityNetherPortal netherPortal = (TileEntityNetherPortal) location.getTileEntity();

        if (Settings.Server.CAN_DYE_PORTALS && Settings.Server.CAN_DYE_PORTALS_BY_THROWING_DYE && entity instanceof EntityItem)
        {
            PortalTexture newTexture = PortalTexture.getPortalTextureFromItem(((EntityItem) entity).getEntityItem(), netherPortal.PortalTexture, Settings.Server.DOES_DYING_COST, null);

            if (newTexture != PortalTexture.UNKNOWN)
            {
                PortalHandler.floodUpdateTexture(location, newTexture, netherPortal.PortalTexture, true);
                return;
            }
        }

        if (Settings.Server.CAN_TELEPORT)
        {
            if (netherPortal.ParentModifier == null || netherPortal.ParentModifier.getTileEntity() == null)
            {
                if (world.provider.dimensionId == 0 || world.provider.dimensionId == -1)
                {
                    entity.setInPortal();
                }
                
                return;
            }
            
            TileEntityPortalModifier modifier = (TileEntityPortalModifier) netherPortal.ParentModifier.getTileEntity();

            if (modifier.getNetwork() == "undefined")
            {
                if (world.provider.dimensionId == 0 || world.provider.dimensionId == -1)
                {
                    entity.setInPortal();
                }
            }
            else
            {                
                if (EntityHandler.canEntityTravel(entity))
                {
                    List<WorldLocation> validExits = Reference.NetworkManager.PMNetwork.getFrequencyExcluding(modifier.getNetwork(), new WorldLocation(modifier.xCoord, modifier.yCoord, modifier.zCoord, world));

                    if (validExits == null || validExits.isEmpty())
                    {
                        Log.log(Level.INFO, String.format(Localizations.getLocalizedString(Strings.Console_NoExitFound), entity.getEntityName()));
                        EntityHandler.sendMessage(entity, Localizations.getLocalizedString(Strings.Portal_NoExitFound));
                    }
                    else
                    {
                        WorldLocation selectedExit = validExits.remove(world.rand.nextInt(validExits.size()));
                        boolean isValidExit = true;

                        while (!PortalHandler.isValidExitPortal(world, selectedExit, modifier, entity, true))
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
                            Log.log(Level.INFO, String.format(Localizations.getLocalizedString(Strings.Console_NoExitFound), entity.getEntityName()));
                            EntityHandler.sendMessage(entity, Localizations.getLocalizedString(Strings.Portal_NoExitFound));
                        }
                        else
                        {
                            EntityHandler.teleportEntity(entity, selectedExit);
                        }
                    }
                }

                EntityHandler.setCanEntityTravel(entity, false);
            }
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int id)
    {
        if (world.isRemote || id == 0)
        {
            return;
        }

        if (PortalShape.getPortalShape(new WorldLocation(x, y, z, world)) == PortalShape.UNKNOWN)
        {
            PortalHandler.removePortal(new WorldLocation(x, y, z, world), PortalShape.UNKNOWN);
        }
    }

    @Override
    public int quantityDropped(Random par1Random)
    {
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        if (par5Random.nextInt(100) == 0)
        {
            if (Settings.Client.SOUND_LEVEL > 0 && par5Random.nextInt(100) <= Settings.Client.SOUND_LEVEL)
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

            if (Settings.Client.PARTICLE_LEVEL > 0 && par5Random.nextInt(100) <= Settings.Client.PARTICLE_LEVEL)
            {
                if (Settings.Server.CAN_DYE_PORTALS)
                {
                    WorldLocation location = new WorldLocation(par2, par3, par4, par1World);
                    TileEntityNetherPortal netherPortal = (TileEntityNetherPortal) location.getTileEntity();

                    FMLClientHandler.instance().getClient().effectRenderer.addEffect(new TextureNetherPortalEntityFX(par1World, netherPortal.PortalTexture.ordinal(), var7, var9, var11, var13, var15, var17));
                    
                }
                else
                {
                    par1World.spawnParticle("portal", var7, var9, var11, var13, var15, var17);
                }
            }
        }
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
        PortalShape portalShape = PortalShape.getPortalShape(new WorldLocation(x, y, z, world));

        if (portalShape == PortalShape.X)
        {
            setBlockBounds(0.0F, 0.0F, 0.375F, 1.0F, 1.0F, 0.625F);
        }
        else if (portalShape == PortalShape.Z)
        {
            setBlockBounds(0.375F, 0.0F, 0.0F, 0.625F, 1.0F, 1.0F);
        }
        else if (portalShape == PortalShape.HORIZONTAL)
        {
            setBlockBounds(0.0F, 0.375F, 0.0F, 1.0F, 0.625F, 1.0F);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
    {
        if (world.getBlockMaterial(x, y, z) == Material.portal)
        {
            return false;
        }

        PortalShape portalShape = PortalShape.getPortalShape(new WorldLocation(x, y, z, world).getOffset(ForgeDirection.getOrientation(side).getOpposite()));

        if (portalShape == PortalShape.X)
        {
            if (side == 2 || side == 3)
            {
                return true;
            }
        }
        else if (portalShape == PortalShape.Z)
        {
            if (side == 4 || side == 5)
            {
                return true;
            }
        }
        else if (portalShape == PortalShape.HORIZONTAL)
        {
            if (side == 0 || side == 1)
            {
                return true;
            }
        }

        return false;
    }

    public boolean tryToCreatePortal(World world, int x, int y, int z)
    {
        return PortalHandler.createPortal(new WorldLocation(x, y, z, world));
    }

    @Override
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        super.updateTick(par1World, par2, par3, par4, par5Random);

        if (Settings.Server.PIGMEN_SPAWN_CHANCE > 0 && par5Random.nextInt(100) <= Settings.Server.PIGMEN_SPAWN_CHANCE)
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
}
