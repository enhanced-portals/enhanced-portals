package alz.mods.enhancedportals.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import alz.mods.enhancedportals.portals.PortalHandler;
import alz.mods.enhancedportals.portals.PortalTexture;
import alz.mods.enhancedportals.reference.Reference;
import alz.mods.enhancedportals.reference.Settings;
import alz.mods.enhancedportals.reference.Strings;
import alz.mods.enhancedportals.teleportation.TeleportData;
import alz.mods.enhancedportals.tileentity.TileEntityPortalModifier;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class BlockPortalModifier extends BlockContainer
{
    private Icon sideFace;
    
    protected BlockPortalModifier()
    {
        super(Reference.BlockIDs.PortalModifier, Material.rock);
        setHardness(50.0F);
        setResistance(2000.0F);
        setStepSound(soundStoneFootstep);
        setUnlocalizedName(Strings.PortalModifier_Name);
        setCreativeTab(CreativeTabs.tabBlock);
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileEntityPortalModifier();
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        sideFace = iconRegister.registerIcon(Strings.PortalModifier_Icon_Side);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        int meta = blockAccess.getBlockMetadata(x, y, z);
        TileEntityPortalModifier modifier = (TileEntityPortalModifier) blockAccess.getBlockTileEntity(x, y, z);
        
        if (modifier.hasUpgrade(3))
        {
            return Block.blocksList[Reference.BlockIDs.Obsidian].getBlockTextureFromSide(side);
        }
        else if (modifier.hasUpgrade(0))
        {
            return modifier.getTexture().getModifierIcon();
        }
        
        return side == meta ? modifier.getTexture().getModifierIcon() : sideFace;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getBlockTextureFromSideAndMetadata(int side, int meta)
    {
        return side == 1 ? PortalTexture.getModifierIcon(meta) : sideFace;
    }
    
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityLiving, ItemStack itemStack)
    {
        int direction = 0;
        int facing = MathHelper.floor_double(entityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

        if (facing == 0)
        {
            direction = ForgeDirection.NORTH.ordinal();
        }
        else if (facing == 1)
        {
            direction = ForgeDirection.EAST.ordinal();
        }
        else if (facing == 2)
        {
            direction = ForgeDirection.SOUTH.ordinal();
        }
        else if (facing == 3)
        {
            direction = ForgeDirection.WEST.ordinal();
        }

        if (entityLiving.rotationPitch > 65 && entityLiving.rotationPitch <= 90)
        {
            direction = ForgeDirection.UP.ordinal();
        }
        else if (entityLiving.rotationPitch < -65 && entityLiving.rotationPitch >= -90)
        {
            direction = ForgeDirection.DOWN.ordinal();
        }

        world.setBlockMetadataWithNotify(x, y, z, direction, 0);
    }
    
    @Override
    public void breakBlock(World world, int x, int y, int z, int par5, int par6)
    {
        if (!world.isRemote)
        {
            TileEntityPortalModifier tileEntity = (TileEntityPortalModifier) world.getBlockTileEntity(x, y, z);

            if (tileEntity != null && tileEntity.getNetwork() != "undefined")
            {
                Reference.ServerHandler.ModifierNetwork.removeFromNetwork(tileEntity.getNetwork(), new TeleportData(x, y, z, world.provider.dimensionId));
            }
        }
        
        super.breakBlock(world, x, y, z, par5, par6);
    }
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        ItemStack item = player.inventory.mainInventory[player.inventory.currentItem];
        
        if (player.isSneaking())
        {
            if (item == null)
            {
                ForgeDirection nextRotation = ForgeDirection.getOrientation(world.getBlockMetadata(x, y, z));
                
                if (nextRotation.ordinal() + 1 < ForgeDirection.VALID_DIRECTIONS.length)
                {
                    nextRotation = ForgeDirection.getOrientation(nextRotation.ordinal() + 1);
                }
                else
                {
                    nextRotation = ForgeDirection.getOrientation(0);
                }
                
                rotateBlock(world, x, y, z, nextRotation);
                return true;
            }
            
            return false;
        }
        else
        {
            if (item == null)
            {
                return false;
            }
            else
            {
                TileEntityPortalModifier modifier = (TileEntityPortalModifier) world.getBlockTileEntity(x, y, z);
                PortalTexture texture = null;
                boolean isBucket = false;
                
                if (item.itemID == Item.dyePowder.itemID)
                {
                    texture = PortalTexture.getPortalTexture(PortalTexture.swapColours(item.getItemDamage()));
                }
                else if (item.itemID == Item.bucketLava.itemID)
                {
                    texture = PortalTexture.LAVA;
                    
                    if (modifier.getTexture() == texture)
                    {
                        texture = PortalTexture.LAVA_STILL;
                    }
                }
                else if (item.itemID == Item.bucketWater.itemID)
                {
                    texture = PortalTexture.WATER;
                    
                    if (modifier.getTexture() == texture)
                    {
                        texture = PortalTexture.WATER_STILL;
                    }
                }
                
                if (texture != null && modifier.getTexture() != texture)
                {
                    if (!player.capabilities.isCreativeMode && Settings.DoesDyingCost)
                    {
                        item.stackSize--;
                        
                        if (isBucket)
                        {
                            player.inventory.addItemStackToInventory(new ItemStack(Item.bucketEmpty));
                        }
                    }
                    
                    PortalHandler.Data.floodUpdateTextureModifier(modifier, texture, true);
                }
                
                return true;
            }
        }
    }
    
    @Override
    public boolean rotateBlock(World world, int x, int y, int z, ForgeDirection axis)
    {
        boolean success = world.setBlockMetadataWithNotify(x, y, z, axis.ordinal(), 0);
        
        if (world.isRemote && success)
        {
            world.markBlockForRenderUpdate(x, y, z);
        }
        
        return success;
    }
    
    @Override
    public ForgeDirection[] getValidRotations(World worldObj, int x, int y, int z)
    {
        return ForgeDirection.VALID_DIRECTIONS;
    }
    
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int id)
    {
        TileEntityPortalModifier modifier = (TileEntityPortalModifier) world.getBlockTileEntity(x, y, z);
        boolean gettingPower = world.isBlockIndirectlyGettingPowered(x, y, z), hadPower = modifier.HadPower;
        
        if (gettingPower && !hadPower)
        {
            PortalHandler.Create.createPortalFromModifier(modifier);
        }
        else if (!gettingPower && hadPower)
        {
            PortalHandler.Remove.removePortalModifier(modifier);
        }

        modifier.HadPower = gettingPower;
    }
}
