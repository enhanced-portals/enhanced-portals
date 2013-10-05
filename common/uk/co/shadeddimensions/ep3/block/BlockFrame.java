package uk.co.shadeddimensions.ep3.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.tileentity.TilePortalFrame;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileBiometricIdentifier;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileDiallingDevice;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileModuleManipulator;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileNetworkInterface;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileRedstoneInterface;
import uk.co.shadeddimensions.ep3.util.ChunkCoordinateUtils;
import uk.co.shadeddimensions.ep3.util.ConnectedTextures;

public class BlockFrame extends BlockEP
{
    enum FrameTypes
    {
        PORTAL_CONTROLLER(TilePortalController.class), REDSTONE_INTERFACE(TileRedstoneInterface.class), NETWORK_INTERFACE(TileNetworkInterface.class), DIALLING_DEVICE(TileDiallingDevice.class), BIOMETRIC_IDENTIFIER(TileBiometricIdentifier.class), MODULE_MANIPULATOR(TileModuleManipulator.class);

        public static FrameTypes getFrameType(int meta)
        {
            return values()[meta - 1];
        }

        private Class<? extends TilePortalFrame> c;

        private FrameTypes(Class<? extends TilePortalFrame> clas)
        {
            c = clas;
        }

        public int getID()
        {
            return ordinal() + 1;
        }

        public TilePortalFrame getTileEntity()
        {
            try
            {
                return c.newInstance();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return null;
        }
    }

    static final int FRAME_TYPES = 7;
    public static Icon[] typeOverlayIcons = new Icon[FRAME_TYPES];

    static ConnectedTextures connectedTextures;

    public BlockFrame(int id, String name)
    {
        super(id, Material.rock, true);
        setHardness(10);
        setResistance(2000);
        setUnlocalizedName(name);
        setStepSound(soundStoneFootstep);
        connectedTextures = new ConnectedTextures("enhancedportals:frame/portalFrame_%s", blockID, -1);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addCreativeItems(ArrayList list)
    {
        for (int i = 0; i < FRAME_TYPES; i++)
        {
            list.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int par5, int par6)
    {
        ((TilePortalFrame) world.getBlockTileEntity(x, y, z)).selfBroken();
        super.breakBlock(world, x, y, z, par5, par6);
    }

    @Override
    public boolean canBeReplacedByLeaves(World world, int x, int y, int z)
    {
        return false;
    }

    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, World world, int x, int y, int z)
    {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return null;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata)
    {
        if (metadata == 0)
        {
            return new TilePortalFrame();
        }
        else if (metadata <= FrameTypes.values().length)
        {
            return FrameTypes.getFrameType(metadata).getTileEntity();
        }

        return null;
    }

    @Override
    public int damageDropped(int par1)
    {
        return par1;
    }

    @Override
    public Icon getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        return connectedTextures.getIconForFace(blockAccess, x, y, z, side);
    }

    @Override
    public Icon getIcon(int side, int meta)
    {
        return connectedTextures.getNormalIcon();
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
        return new ItemStack(CommonProxy.blockFrame.blockID, 1, world.getBlockMetadata(x, y, z));
    }

    @Override
    public int getRenderType()
    {
        return -1;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubBlocks(int par1, CreativeTabs creativeTab, List list)
    {
        for (int i = 0; i < FRAME_TYPES; i++)
        {
            list.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public boolean isBlockNormalCube(World world, int x, int y, int z)
    {
        return false;
    }

    @Override
    public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side)
    {
        return true;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return true;
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        return ((TilePortalFrame) blockAccess.getBlockTileEntity(x, y, z)).isProvidingStrongPower(side);
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        return ((TilePortalFrame) blockAccess.getBlockTileEntity(x, y, z)).isProvidingWeakPower(side);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        return ((TilePortalFrame) world.getBlockTileEntity(x, y, z)).activate(player);
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z)
    {
        if (world.isRemote)
        {
            return;
        }

        for (int i = 0; i < 6; i++)
        {
            ChunkCoordinates c = ChunkCoordinateUtils.offset(new ChunkCoordinates(x, y, z), ForgeDirection.getOrientation(i));

            if (world.getBlockId(c.posX, c.posY, c.posZ) == CommonProxy.blockFrame.blockID)
            {
                ((TilePortalFrame) world.getBlockTileEntity(c.posX, c.posY, c.posZ)).selfBroken();
            }
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int id)
    {
        ((TilePortalFrame) world.getBlockTileEntity(x, y, z)).neighborChanged(id);
    }

    @Override
    public void registerIcons(IconRegister register)
    {
        for (int i = 0; i < typeOverlayIcons.length; i++)
        {
            typeOverlayIcons[i] = register.registerIcon("enhancedportals:portalFrame_" + i);
        }

        connectedTextures.registerIcons(register);
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random)
    {
        ((TilePortalFrame) world.getBlockTileEntity(x, y, z)).scheduledTick(random);
    }
}
