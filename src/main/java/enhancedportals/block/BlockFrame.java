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
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.block.IDismantleable;
import cpw.mods.fml.common.Optional.Interface;
import cpw.mods.fml.common.Optional.InterfaceList;
import cpw.mods.fml.common.Optional.Method;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import enhancedportals.EnhancedPortals;
import enhancedportals.common.ISidedBlockTexture;
import enhancedportals.network.ClientProxy;
import enhancedportals.tileentity.TileController;
import enhancedportals.tileentity.TileDiallingDevice;
import enhancedportals.tileentity.TileFrame;
import enhancedportals.tileentity.TileFrameBasic;
import enhancedportals.tileentity.TileFrameTransfer;
import enhancedportals.tileentity.TileModuleManipulator;
import enhancedportals.tileentity.TileNetworkInterface;
import enhancedportals.tileentity.TilePortalPart;
import enhancedportals.tileentity.TileProgrammableInterface;
import enhancedportals.tileentity.TileRedstoneInterface;
import enhancedportals.tileentity.TileTransferEnergy;
import enhancedportals.tileentity.TileTransferFluid;
import enhancedportals.tileentity.TileTransferItem;
import enhancedportals.utility.ConnectedTexturesDetailed;

@InterfaceList(value = { @Interface(iface = "dan200.computercraft.api.peripheral.IPeripheralProvider", modid = EnhancedPortals.MODID_COMPUTERCRAFT) })
public class BlockFrame extends BlockContainer implements IDismantleable, IPeripheralProvider
{
    public static BlockFrame instance;
    public static ConnectedTexturesDetailed connectedTextures;
    public static IIcon[] overlayIcons;
    public static int PORTAL_CONTROLLER = 1, REDSTONE_INTERFACE = 2, NETWORK_INTERFACE = 3, DIALLING_DEVICE = 4, PROGRAMMABLE_INTERFACE = 5, MODULE_MANIPULATOR = 6, TRANSFER_FLUID = 7, TRANSFER_ITEM = 8, TRANSFER_ENERGY = 9;
    public static int FRAME_TYPES = 10;
    static IIcon[] fullIcons;

    public BlockFrame(String n)
    {
        super(Material.rock);
        instance = this;
        setCreativeTab(EnhancedPortals.creativeTab);
        setHardness(5);
        setResistance(2000);
        setBlockName(n);
        setStepSound(soundTypeStone);
        connectedTextures = new ConnectedTexturesDetailed("enhancedportals:frame/%s", this, -1);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int unknown)
    {
        TileEntity t = world.getTileEntity(x, y, z);

        if (t != null && t instanceof TileFrame)
        {
            ((TileFrame) t).breakBlock(block, unknown);
        }

        super.breakBlock(world, x, y, z, block, unknown);
    }

    @Override
    public boolean canDismantle(EntityPlayer player, World world, int x, int y, int z)
    {
        return true;
    }

    @Override
    public boolean canProvidePower()
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
    public int colorMultiplier(IBlockAccess blockAccess, int x, int y, int z)
    {
        TileEntity tile = blockAccess.getTileEntity(x, y, z);

        if (tile instanceof TileFrame)
        {
            return ((TileFrame) tile).getColour();
        }

        return 0xFFFFFF;
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
        else if (metadata == PROGRAMMABLE_INTERFACE)
        {
            return new TileProgrammableInterface();
        }
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
            return ((ISidedBlockTexture) tile).getBlockTexture(side, ClientProxy.renderPass);
        }

        return fullIcons[0];
    }

    @Override
    public IIcon getIcon(int side, int meta)
    {
        return fullIcons[MathHelper.clamp_int(meta, 0, FRAME_TYPES)];
    }

    @Override
    @Method(modid = EnhancedPortals.MODID_COMPUTERCRAFT)
    public IPeripheral getPeripheral(World world, int x, int y, int z, int side)
    {
        TileEntity t = world.getTileEntity(x, y, z);

        if (t != null && (t instanceof TileController || t instanceof TileNetworkInterface || t instanceof TileDiallingDevice || t instanceof TileTransferEnergy || t instanceof TileTransferFluid || t instanceof TileTransferItem))
        {
            return (IPeripheral) t;
        }

        return null;
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
    public boolean isBlockSolid(IBlockAccess p_149747_1_, int p_149747_2_, int p_149747_3_, int p_149747_4_, int p_149747_5_)
    {
        return true;
    }

    @Override
    public boolean isNormalCube()
    {
        return true;
    }

    @Override
    public boolean isNormalCube(IBlockAccess world, int x, int y, int z)
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

    @Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side)
    {
        return true;
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
        TileEntity tile = world.getTileEntity(x, y, z);

        if (tile instanceof TilePortalPart)
        {
            ((TilePortalPart) tile).onBlockPlaced(entity, stack);
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block b)
    {
        TileEntity tile = world.getTileEntity(x, y, z);

        if (tile instanceof TileRedstoneInterface)
        {
            ((TileRedstoneInterface) tile).onNeighborBlockChange(b);
        }
        else if (tile instanceof TileFrameTransfer)
        {
            ((TileFrameTransfer) tile).onNeighborChanged();
        }
    }

    @Override
    public void registerBlockIcons(IIconRegister register)
    {
        overlayIcons = new IIcon[FRAME_TYPES];
        fullIcons = new IIcon[FRAME_TYPES];

        for (int i = 0; i < overlayIcons.length; i++)
        {
            overlayIcons[i] = register.registerIcon("enhancedportals:frame_" + i);
            fullIcons[i] = register.registerIcon("enhancedportals:frame_" + i + "b");
        }

        connectedTextures.registerIcons(register);
        int counter = 0;
        ClientProxy.customFrameTextures.clear();

        while (ClientProxy.resourceExists("textures/blocks/customFrame/" + String.format("%02d", counter) + ".png"))
        {
            EnhancedPortals.logger.info("Registered custom frame Icon: " + String.format("%02d", counter) + ".png");
            ClientProxy.customFrameTextures.add(register.registerIcon("enhancedportals:customFrame/" + String.format("%02d", counter)));
            counter++;
        }
    }
}
