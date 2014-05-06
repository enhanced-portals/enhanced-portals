package enhancedportals.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cofh.api.block.IDismantleable;
import cofh.api.tileentity.ISidedBlockTexture;
import enhancedportals.EnhancedPortals;
import enhancedportals.network.ClientProxy;
import enhancedportals.tileentity.portal.TileController;
import enhancedportals.tileentity.portal.TileDiallingDevice;
import enhancedportals.tileentity.portal.TileFrame;
import enhancedportals.tileentity.portal.TileFrameBasic;
import enhancedportals.tileentity.portal.TileFrameTransfer;
import enhancedportals.tileentity.portal.TileModuleManipulator;
import enhancedportals.tileentity.portal.TileNetworkInterface;
import enhancedportals.tileentity.portal.TilePortalPart;
import enhancedportals.tileentity.portal.TileRedstoneInterface;
import enhancedportals.tileentity.portal.TileTransferEnergy;
import enhancedportals.tileentity.portal.TileTransferFluid;
import enhancedportals.tileentity.portal.TileTransferItem;
import enhancedportals.utility.ConnectedTextures;
import enhancedportals.utility.ConnectedTexturesDetailed;

public class BlockFrame extends BlockContainer implements IDismantleable
{
    public static BlockFrame instance;

    public static int PORTAL_CONTROLLER = 1, REDSTONE_INTERFACE = 2, NETWORK_INTERFACE = 3, DIALLING_DEVICE = 4, BIOMETRIC_IDENTIFIER = 5, MODULE_MANIPULATOR = 6, TRANSFER_FLUID = 7, TRANSFER_ITEM = 8, TRANSFER_ENERGY = 9;
    public static int FRAME_TYPES = 10;

    static IIcon[] fullIcons;
    public static IIcon[] overlayIcons;
    public static ConnectedTextures connectedTextures;

    public BlockFrame()
    {
        super(Material.rock);
        instance = this;
        setCreativeTab(EnhancedPortals.creativeTab);
        setHardness(5);
        setResistance(2000);
        setBlockName("frame");
        setStepSound(soundTypeStone);
        connectedTextures = new ConnectedTexturesDetailed("enhancedportals:frame/%s", this, -1);
    }

    @Override
    public boolean canDismantle(EntityPlayer player, World world, int x, int y, int z)
    {
        return true;
    }

    @Override
    public boolean canRenderInPass(int pass)
    {
        ClientProxy.renderPass = pass;
        return pass < 2;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        if (metadata == 0)
        {
            return new TileFrameBasic();
        }
        else if (metadata == PORTAL_CONTROLLER)
        {
            return new TileController();
        }
        else if (metadata == REDSTONE_INTERFACE)
        {
            return new TileRedstoneInterface();
        }
        else if (metadata == NETWORK_INTERFACE)
        {
            return new TileNetworkInterface();
        }
        else if (metadata == DIALLING_DEVICE)
        {
            return new TileDiallingDevice();
        }
        /*else if (metadata == BIOMETRIC_IDENTIFIER) // TODO
        {
            return new TileBiometricIdentifier();
        }*/
        else if (metadata == MODULE_MANIPULATOR)
        {
            return new TileModuleManipulator();
        }
        else if (metadata == TRANSFER_FLUID)
        {
            return new TileTransferFluid();
        }
        else if (metadata == TRANSFER_ITEM)
        {
            return new TileTransferItem();
        }
        else if (metadata == TRANSFER_ENERGY)
        {
            return new TileTransferEnergy();
        }

        return null;
    }

    @Override
    public int damageDropped(int par1)
    {
        return par1;
    }

    @Override
    public ItemStack dismantleBlock(EntityPlayer player, World world, int x, int y, int z, boolean returnBlock)
    {
        ItemStack dropBlock = new ItemStack(this, 1, world.getBlockMetadata(x, y, z));

        TileEntity tile = world.getTileEntity(x, y, z);

        if (tile instanceof TileFrame)
        {
            ((TileFrame) tile).onBlockDismantled();
        }

        world.setBlockToAir(x, y, z);

        if (dropBlock != null && !returnBlock)
        {
            float f = 0.3F;
            double x2 = world.rand.nextFloat() * f + (1.0F - f) * 0.5D;
            double y2 = world.rand.nextFloat() * f + (1.0F - f) * 0.5D;
            double z2 = world.rand.nextFloat() * f + (1.0F - f) * 0.5D;
            EntityItem item = new EntityItem(world, x + x2, y + y2, z + z2, dropBlock);
            item.delayBeforeCanPickup = 10;
            world.spawnEntityInWorld(item);
        }

        return dropBlock;
    }

    @Override
    public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        TileEntity tile = blockAccess.getTileEntity(x, y, z);

        if (tile instanceof TileFrame)
        {
            return ((ISidedBlockTexture) blockAccess.getTileEntity(x, y, z)).getBlockTexture(side, ClientProxy.renderPass);
        }

        return fullIcons[0];
    }

    @Override
    public IIcon getIcon(int side, int meta)
    {
        return fullIcons[MathHelper.clamp_int(meta, 0, FRAME_TYPES)];
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
        return new ItemStack(this, 1, world.getBlockMetadata(x, y, z));
    }

    @Override
    public int getRenderBlockPass()
    {
        return 1;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubBlocks(Item par1, CreativeTabs creativeTab, List list)
    {
        for (int i = 0; i < FRAME_TYPES; i++)
        {
            list.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public void registerBlockIcons(IIconRegister register)
    {
        overlayIcons = new IIcon[FRAME_TYPES];
        fullIcons = new IIcon[FRAME_TYPES];

        for (int i = 0; i < overlayIcons.length; i++)
        {
            overlayIcons[i] = register.registerIcon("enhancedportals:portalFrame_" + i);
            fullIcons[i] = register.registerIcon("enhancedportals:portalFrameFull_" + i);
        }

        connectedTextures.registerIcons(register);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int unknown)
    {
    	((TileFrame) world.getTileEntity(x, y, z)).breakBlock(block, unknown);
    	
    	super.breakBlock(world, x, y, z, block, unknown);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        TileEntity tile = world.getTileEntity(x, y, z);

        if (tile instanceof TileFrame)
        {
            return ((TileFrame) tile).activate(player, player.inventory.getCurrentItem());
        }

        return false;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack)
    {
        ((TilePortalPart) world.getTileEntity(x, y, z)).onBlockPlaced(entity, stack);
    }

    @Override
    public int colorMultiplier(IBlockAccess blockAccess, int x, int y, int z)
    {
        TileEntity tile = blockAccess.getTileEntity(x, y, z);

        if (tile instanceof TileFrame)
        {
            return ((TileFrame) blockAccess.getTileEntity(x, y, z)).getColour();
        }

        return 0xFFFFFF;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block b)
    {
        TileEntity tile = world.getTileEntity(x, y, z);

        if (tile instanceof TileRedstoneInterface)
        {
            ((TileRedstoneInterface) tile).onNeighborBlockChange(b);
        }
        /*else if (tile instanceof TileBiometricIdentifier) // TODO
        {
            ((TileBiometricIdentifier) tile).onNeighborBlockChange(b);
        }*/
        else if (tile instanceof TileFrameTransfer)
        {
            ((TileFrameTransfer) tile).onNeighborChanged();
        }
    }

    @Override
    public boolean isBlockSolid(IBlockAccess p_149747_1_, int p_149747_2_, int p_149747_3_, int p_149747_4_, int p_149747_5_) {
    	return true;
    }

    @Override
    public boolean canProvidePower()
    {
        return true;
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        TileEntity tile = blockAccess.getTileEntity(x, y, z);

        if (tile instanceof TileRedstoneInterface)
        {
            return ((TileRedstoneInterface) tile).isProvidingPower(side);
        }

        return 0;
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        TileEntity tile = blockAccess.getTileEntity(x, y, z);

        if (tile instanceof TileRedstoneInterface)
        {
            return ((TileRedstoneInterface) tile).isProvidingPower(side);
        }

        return 0;
    }
}
