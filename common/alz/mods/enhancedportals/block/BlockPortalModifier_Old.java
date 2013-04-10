package alz.mods.enhancedportals.block;

/*import java.util.Random;

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
import alz.mods.enhancedportals.EnhancedPortals;
import alz.mods.enhancedportals.common.WorldLocation;
import alz.mods.enhancedportals.helpers.WorldHelper;
import alz.mods.enhancedportals.portals.PortalHandler;
import alz.mods.enhancedportals.portals.PortalTexture;
import alz.mods.enhancedportals.reference.Reference;
import alz.mods.enhancedportals.reference.Settings;
import alz.mods.enhancedportals.reference.Strings;
import alz.mods.enhancedportals.teleportation.TeleportData;
import alz.mods.enhancedportals.tileentity.TileEntityPortalModifier;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;*/

public class BlockPortalModifier_Old //extends BlockContainer
{
    /*private Icon[] activeFace;
    private Icon sideFace;

    public BlockPortalModifier_Old()
    {
        super(Reference.BlockIDs.PortalModifier, Material.rock);
        setHardness(50.0F);
        setResistance(2000.0F);
        setStepSound(soundStoneFootstep);
        setUnlocalizedName(Strings.PortalModifier_Name);
        setCreativeTab(CreativeTabs.tabBlock);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int par5, int par6)
    {
        if (!world.isRemote)
        {
            TileEntityPortalModifier tileEntity = (TileEntityPortalModifier) world.getBlockTileEntity(x, y, z);

            if (tileEntity != null && tileEntity.getFrequency() != 0)
            {
                Reference.ServerHandler.ModifierNetwork.removeFromNetwork("" + tileEntity.getFrequency(), new TeleportData(x, y, z, world.provider.dimensionId));
            }
        }

        dropItems(world, x, y, z);
        super.breakBlock(world, x, y, z, par5, par6);
    }

    @Override
    public TileEntity createNewTileEntity(World var1)
    {
        return new TileEntityPortalModifier();
    }

    void dropItems(World world, int x, int y, int z)
    {
        Random rand = new Random();

        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

        if (!(tileEntity instanceof IInventory))
        {
            return;
        }

        IInventory inventory = (IInventory) tileEntity;

        for (int i = 0; i < inventory.getSizeInventory(); i++)
        {
            ItemStack item = inventory.getStackInSlot(i);

            if (item == null || item.stackSize == 0)
            {
                return;
            }

            float rx = rand.nextFloat() * 0.8F + 0.1F, ry = rand.nextFloat() * 0.8F + 0.1F, rz = rand.nextFloat() * 0.8F + 0.1F;

            EntityItem entityItem = new EntityItem(world, x + rx, y + ry, z + rz, new ItemStack(item.itemID, item.stackSize, item.getItemDamage()));

            if (item.hasTagCompound())
            {
                entityItem.getEntityData().setCompoundTag(item.getTagCompound().getName(), (NBTTagCompound) item.getTagCompound().copy());
            }

            float factor = 0.05F;
            entityItem.motionX = rand.nextGaussian() * factor;
            entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
            entityItem.motionZ = rand.nextGaussian() * factor;

            world.spawnEntityInWorld(entityItem);
            item.stackSize = 0;
        }
    }

    // Placed in world
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side)
    {
        TileEntityPortalModifier modifier = (TileEntityPortalModifier) world.getBlockTileEntity(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);

        if (modifier.hasUpgrade(3))
        {
            return Block.blocksList[Reference.BlockIDs.Obsidian].getBlockTextureFromSide(0);
        }
        else if (modifier.hasUpgrade(0))
        {
            return activeFace[modifier.getColour()];
        }

        return side == meta ? activeFace[modifier.getColour()] : sideFace;
    }

    // For inventory
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getBlockTextureFromSideAndMetadata(int side, int meta)
    {
        return side == 1 ? activeFace[0] : sideFace;
    }

    @Override
    public ForgeDirection[] getValidRotations(World world, int x, int y, int z)
    {
        return ForgeDirection.VALID_DIRECTIONS;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        TileEntityPortalModifier modifier = (TileEntityPortalModifier) world.getBlockTileEntity(x, y, z);
        ItemStack currentItem = player.inventory.mainInventory[player.inventory.currentItem];

        if (currentItem != null || !player.isSneaking())
        {
            if (Settings.CanDyePortals && currentItem != null)
            {
                if (currentItem.itemID == Item.dyePowder.itemID)
                {
                    PortalHandler.Data.floodUpdateTextureModifier(modifier, PortalTexture.getPortalTexture(PortalTexture.swapColours(currentItem.getItemDamage())), true);

                    if (!player.capabilities.isCreativeMode && Settings.DoesDyingCost)
                    {
                        currentItem.stackSize--;
                    }

                    return true;
                }
                else if (currentItem.itemID == Item.bucketLava.itemID)
                {
                    PortalHandler.Data.floodUpdateTextureModifier(modifier, PortalTexture.LAVA, true);

                    if (!player.capabilities.isCreativeMode && Settings.DoesDyingCost)
                    {
                        currentItem.stackSize--;
                        player.inventory.addItemStackToInventory(new ItemStack(Item.bucketEmpty));
                    }

                    return true;
                }
                else if (currentItem.itemID == Item.bucketWater.itemID)
                {
                    PortalHandler.Data.floodUpdateTextureModifier(modifier, PortalTexture.WATER, true);

                    if (!player.capabilities.isCreativeMode && Settings.DoesDyingCost)
                    {
                        currentItem.stackSize--;
                        player.inventory.addItemStackToInventory(new ItemStack(Item.bucketEmpty));
                    }

                    return true;
                }
            }
        }
        else if (currentItem == null && player.isSneaking())
        {
            int nextRotation = world.getBlockMetadata(x, y, z) + 1;

            if (nextRotation >= ForgeDirection.VALID_DIRECTIONS.length)
            {
                nextRotation = 0;
            }

            rotateBlock(world, x, y, z, ForgeDirection.VALID_DIRECTIONS[nextRotation]);
            world.markBlockForRenderUpdate(x, y, z);

            return true;
        }

        player.openGui(EnhancedPortals.instance, Reference.GuiIDs.PortalModifier, world, x, y, z);
        return true;
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
    public void onNeighborBlockChange(World world, int x, int y, int z, int id)
    {
        TileEntityPortalModifier modifier = (TileEntityPortalModifier) world.getBlockTileEntity(x, y, z);
        boolean gettingPower = world.isBlockIndirectlyGettingPowered(x, y, z), hadPower = modifier.HadPower;
        boolean hasMultiUpgrade = modifier.hasUpgrade(0);

        ForgeDirection direction = WorldHelper.getBlockDirection(world, x, y, z);
        int[] blockToTest = WorldHelper.offsetDirectionBased(world, x, y, z, direction);
        int blockID = world.getBlockId(blockToTest[0], blockToTest[1], blockToTest[2]), meta = world.getBlockMetadata(blockToTest[0], blockToTest[1], blockToTest[2]);

        if (gettingPower && !hadPower)
        {
            if (hasMultiUpgrade)
            {
                PortalHandler.Create.createPortalAroundBlock(new WorldLocation(x, y, z), modifier.PortalData.Texture);
            }
            else if (WorldHelper.isBlockPortalRemovable(blockID))
            {
                PortalHandler.Create.createPortalFromModifier(modifier);
            }
        }
        else if (!gettingPower && hadPower)
        {
            if (hasMultiUpgrade)
            {
                PortalHandler.Remove.removePortalAround(new WorldLocation(world, x, y, z));
            }
            else if (blockID == Reference.BlockIDs.NetherPortal && meta == modifier.getColour())
            {
                PortalHandler.Remove.removePortalModifier(modifier);
            }
        }

        modifier.HadPower = gettingPower;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        sideFace = iconRegister.registerIcon(Strings.PortalModifier_Icon_Side);
        activeFace = new Icon[18];

        for (int i = 0; i < 16; i++)
        {
            activeFace[i] = iconRegister.registerIcon(String.format(Strings.PortalModifier_Icon_Active, i));
        }

        activeFace[16] = activeFace[14];
        activeFace[17] = activeFace[4];
    }

    @Override
    public boolean rotateBlock(World world, int x, int y, int z, ForgeDirection axis)
    {
        return world.setBlockMetadataWithNotify(x, y, z, axis.ordinal(), 0);
    }*/
}