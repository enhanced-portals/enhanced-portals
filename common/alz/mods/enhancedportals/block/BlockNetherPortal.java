package alz.mods.enhancedportals.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import alz.mods.enhancedportals.client.TextureNetherPortalEntityFX;
import alz.mods.enhancedportals.common.WorldLocation;
import alz.mods.enhancedportals.helpers.EntityHelper;
import alz.mods.enhancedportals.helpers.WorldHelper;
import alz.mods.enhancedportals.portals.PortalHandler;
import alz.mods.enhancedportals.portals.PortalShape;
import alz.mods.enhancedportals.portals.PortalTexture;
import alz.mods.enhancedportals.reference.Localizations;
import alz.mods.enhancedportals.reference.Reference;
import alz.mods.enhancedportals.reference.Settings;
import alz.mods.enhancedportals.reference.Strings;
import alz.mods.enhancedportals.teleportation.TeleportData;
import alz.mods.enhancedportals.tileentity.TileEntityNetherPortal;
import alz.mods.enhancedportals.tileentity.TileEntityPortalModifier;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockNetherPortal extends BlockContainer
{
    public BlockNetherPortal()
    {
        super(Reference.BlockIDs.NetherPortal, Material.portal);
        setHardness(-1.0F);
        setStepSound(soundGlassFootstep);
        setLightValue(0.75F);
        setUnlocalizedName("portal");
        setTickRandomly(true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addBlockDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer)
    {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addBlockHitEffects(World worldObj, MovingObjectPosition target, EffectRenderer effectRenderer)
    {
        return true;
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
        return ((TileEntityNetherPortal) blockAccess.getBlockTileEntity(x, y, z)).Texture.getPortalIcon();
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
        if (Settings.CanDyePortals)
        {
            WorldLocation location = new WorldLocation(world, x, y, z);
            TileEntityNetherPortal netherPortal = (TileEntityNetherPortal) location.getBlockTileEntity();
            PortalTexture newTexture = PortalTexture.getPortalTextureFromItem(player.inventory.mainInventory[player.inventory.currentItem], netherPortal.Texture, Settings.DoesDyingCost && !player.capabilities.isCreativeMode, player);
            
            if (newTexture != PortalTexture.UNKNOWN && !world.isRemote)
            {
                PortalHandler.Data.floodUpdateTexture(location, newTexture, netherPortal.Texture, true);
            }
        }

        return true;
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {
        if (world.isRemote)
        {
            if (Settings.RenderPortalEffects)
            {
                entity.setInPortal();
            }
            
            return;
        }

        WorldLocation location = new WorldLocation(world, x, y, z);
        TileEntityNetherPortal netherPortal = (TileEntityNetherPortal) location.getBlockTileEntity();
        
        if (Settings.CanDyePortals && Settings.CanDyeByThrowing && entity instanceof EntityItem)
        {
            PortalTexture newTexture = PortalTexture.getPortalTextureFromItem(((EntityItem) entity).getEntityItem(), netherPortal.Texture, Settings.DoesDyingCost, null);

            if (newTexture != PortalTexture.UNKNOWN)
            {
                PortalHandler.Data.floodUpdateTexture(location, newTexture, netherPortal.Texture, true);
                return;
            }
        }

        if (Settings.AllowTeleporting)
        {       
            TileEntityPortalModifier modifier = (TileEntityPortalModifier) netherPortal.ParentModifier.getBlockTileEntity();
                
            if (modifier == null || modifier.getNetwork() == "undefined")
            {
                if (world.provider.dimensionId == 0 || world.provider.dimensionId == -1)
                {
                    entity.setInPortal();
                }
            }
            else
            {
                if (EntityHelper.canEntityTravel(entity))
                {
                    List<TeleportData> validExits = Reference.ServerHandler.ModifierNetwork.getFrequencyExcluding(modifier.getNetwork(), new TeleportData(modifier.xCoord, modifier.yCoord, modifier.zCoord, world.provider.dimensionId));
    
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
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int id)
    {
        if (world.isRemote || id == 0)
        {
            return;
        }

        if (PortalShape.getPortalShape(new WorldLocation(world, x, y, z)) == PortalShape.UNKNOWN)
        {
            PortalHandler.Remove.removePortal(new WorldLocation(world, x, y, z), PortalShape.UNKNOWN);
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
            if (Settings.SoundLevel > 0 && par5Random.nextInt(100) <= Settings.SoundLevel)
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

            if (Settings.ParticleLevel > 0 && par5Random.nextInt(100) <= Settings.ParticleLevel)
            {
                if (Settings.CanDyePortals)
                {
                    WorldLocation location = new WorldLocation(par1World, par2, par3, par4);
                    TileEntityNetherPortal netherPortal = (TileEntityNetherPortal) location.getBlockTileEntity();

                    FMLClientHandler.instance().getClient().effectRenderer.addEffect(new TextureNetherPortalEntityFX(par1World, PortalTexture.swapColours(netherPortal.Texture.ordinal()), var7, var9, var11, var13, var15, var17));
                }
                else
                {
                    par1World.spawnParticle("portal", var7, var9, var11, var13, var15, var17);
                }
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        PortalTexture.registerTextures(iconRegister);
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
        PortalShape portalShape = PortalShape.getPortalShape(new WorldLocation(world, x, y, z));

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

        PortalShape portalShape = PortalShape.getPortalShape(new WorldLocation(world, x, y, z).getOffset(ForgeDirection.getOrientation(side).getOpposite()));
        
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
        return PortalHandler.Create.createPortal(new WorldLocation(world, x, y, z));
    }

    @Override
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        super.updateTick(par1World, par2, par3, par4, par5Random);

        if (Settings.PigmenSpawnChance > 0 && par5Random.nextInt(100) <= Settings.PigmenSpawnChance)
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
