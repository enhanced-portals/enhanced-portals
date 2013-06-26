package enhancedportals.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enhancedcore.world.BlockPosition;
import enhancedcore.world.WorldHelper;
import enhancedcore.world.WorldPosition;
import enhancedportals.EnhancedPortals;
import enhancedportals.lib.BlockIds;
import enhancedportals.lib.GuiIds;
import enhancedportals.lib.ItemIds;
import enhancedportals.lib.Localization;
import enhancedportals.lib.Reference;
import enhancedportals.portal.Portal;
import enhancedportals.portal.upgrades.Upgrade;
import enhancedportals.portal.upgrades.modifier.UpgradeCamouflage;
import enhancedportals.tileentity.TileEntityPortalModifier;

public class BlockPortalModifier extends BlockEnhancedPortals
{
    Icon sideTexture, frontTexture, frontTextureActive;

    public BlockPortalModifier()
    {
        super(BlockIds.PortalModifier, Material.rock);
        setCreativeTab(Reference.CREATIVE_TAB);
        setCanRotate();
        setCanRotateVertically();
        setHardness(25.0F);
        setResistance(2000.0F);
        setStepSound(soundStoneFootstep);
        setUnlocalizedName(Localization.PortalModifier_Name);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int par5, int par6)
    {
        if (world.isRemote)
        {
            return;
        }

        TileEntityPortalModifier modifier = (TileEntityPortalModifier) world.getBlockTileEntity(x, y, z);

        if (modifier.isRemotelyControlled())
        {
            if (!modifier.dialDeviceNetwork.equals(""))
            {
                EnhancedPortals.proxy.DialDeviceNetwork.removeFromAllNetworks(new WorldPosition(x, y, z, world.provider.dimensionId));
            }
        }
        else
        {
            if (!modifier.modifierNetwork.equals(""))
            {
                EnhancedPortals.proxy.ModifierNetwork.removeFromAllNetworks(new WorldPosition(x, y, z, world.provider.dimensionId));
            }
        }

        for (Upgrade u : modifier.upgradeHandler.getUpgrades())
        {
            if (u.getItemStack() != null)
            {
                EntityItem entity = new EntityItem(modifier.worldObj, modifier.xCoord + 0.5, modifier.yCoord + 0.5, modifier.zCoord + 0.5, u.getItemStack());
                modifier.worldObj.spawnEntityInWorld(entity);
            }
        }

        super.breakBlock(world, x, y, z, par5, par6);
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata)
    {
        return new TileEntityPortalModifier();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        TileEntityPortalModifier modifier = (TileEntityPortalModifier) blockAccess.getBlockTileEntity(x, y, z);
        BlockPosition position = new BlockPosition(x, y, z);

        if (modifier.upgradeHandler.hasUpgrade(new UpgradeCamouflage()))
        {
            if (new Portal().isBlockFrame(WorldHelper.getBlockId(blockAccess, position.getOffset(ForgeDirection.NORTH)), modifier.customBorderBlocks()))
            {
                return Block.blocksList[WorldHelper.getBlockId(blockAccess, position.getOffset(ForgeDirection.NORTH))].getIcon(side, WorldHelper.getMetadata(position.getOffset(ForgeDirection.NORTH), blockAccess));
            }
            else if (new Portal().isBlockFrame(WorldHelper.getBlockId(blockAccess, position.getOffset(ForgeDirection.SOUTH)), modifier.customBorderBlocks()))
            {
                return Block.blocksList[WorldHelper.getBlockId(blockAccess, position.getOffset(ForgeDirection.SOUTH))].getIcon(side, WorldHelper.getMetadata(position.getOffset(ForgeDirection.SOUTH), blockAccess));
            }
            else if (new Portal().isBlockFrame(WorldHelper.getBlockId(blockAccess, position.getOffset(ForgeDirection.EAST)), modifier.customBorderBlocks()))
            {
                return Block.blocksList[WorldHelper.getBlockId(blockAccess, position.getOffset(ForgeDirection.EAST))].getIcon(side, WorldHelper.getMetadata(position.getOffset(ForgeDirection.EAST), blockAccess));
            }
            else if (new Portal().isBlockFrame(WorldHelper.getBlockId(blockAccess, position.getOffset(ForgeDirection.WEST)), modifier.customBorderBlocks()))
            {
                return Block.blocksList[WorldHelper.getBlockId(blockAccess, position.getOffset(ForgeDirection.WEST))].getIcon(side, WorldHelper.getMetadata(position.getOffset(ForgeDirection.WEST), blockAccess));
            }
            else if (new Portal().isBlockFrame(WorldHelper.getBlockId(blockAccess, position.getOffset(ForgeDirection.UP)), modifier.customBorderBlocks()))
            {
                return Block.blocksList[WorldHelper.getBlockId(blockAccess, position.getOffset(ForgeDirection.UP))].getIcon(side, WorldHelper.getMetadata(position.getOffset(ForgeDirection.UP), blockAccess));
            }
            else if (new Portal().isBlockFrame(WorldHelper.getBlockId(blockAccess, position.getOffset(ForgeDirection.DOWN)), modifier.customBorderBlocks()))
            {
                return Block.blocksList[WorldHelper.getBlockId(blockAccess, position.getOffset(ForgeDirection.DOWN))].getIcon(side, WorldHelper.getMetadata(position.getOffset(ForgeDirection.DOWN), blockAccess));
            }
        }

        return side == blockAccess.getBlockMetadata(x, y, z) ? modifier.isActive() ? frontTextureActive : frontTexture : sideTexture;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta)
    {
        return side == 1 ? frontTexture : sideTexture;
    }

    @Override
    public ForgeDirection[] getValidRotations(World worldObj, int x, int y, int z)
    {
        return ForgeDirection.VALID_DIRECTIONS;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        if (!player.isSneaking())
        {
            if (player.inventory.getCurrentItem() != null && (player.inventory.getCurrentItem().itemID == ItemIds.PortalModifierUpgrade + 256 || player.inventory.getCurrentItem().itemID == ItemIds.NetworkCard + 256))
            {
                return false;
            }
            else if (player.inventory.getCurrentItem() != null && (player.inventory.getCurrentItem().itemID == Item.flintAndSteel.itemID || player.inventory.getCurrentItem().itemID == ItemIds.EnhancedFlintAndSteel + 256))
            {
                TileEntityPortalModifier modifier = (TileEntityPortalModifier) world.getBlockTileEntity(x, y, z);

                if (modifier.createPortal(player.inventory.getCurrentItem()))
                {
                    player.inventory.getCurrentItem().damageItem(1, player);
                }

                return true;
            }

            player.openGui(EnhancedPortals.instance, GuiIds.PortalModifier, world, x, y, z);
            return true;
        }

        return super.onBlockActivated(world, x, y, z, player, par6, par7, par8, par9);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int blockID)
    {
        ((TileEntityPortalModifier) world.getBlockTileEntity(x, y, z)).handleRedstoneChanges(world.getStrongestIndirectPower(x, y, z));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        sideTexture = iconRegister.registerIcon(Reference.MOD_ID + ":" + Localization.PortalModifier_Name + "_side");
        frontTexture = iconRegister.registerIcon(Reference.MOD_ID + ":" + Localization.PortalModifier_Name + "_front");
        frontTextureActive = iconRegister.registerIcon(Reference.MOD_ID + ":" + Localization.PortalModifier_Name + "_frontActive");
    }
}
