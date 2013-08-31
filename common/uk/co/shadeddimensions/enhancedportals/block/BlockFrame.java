package uk.co.shadeddimensions.enhancedportals.block;

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
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import uk.co.shadeddimensions.enhancedportals.network.CommonProxy;
import uk.co.shadeddimensions.enhancedportals.tileentity.TileEP;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrame;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameController;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameRedstone;

public class BlockFrame extends BlockEP
{
    int renderPass = 0;
    public static Icon controllerOverlay;
    public static Icon redstoneOverlay;

    public BlockFrame(int id, String name)
    {
        super(id, Material.rock, true);
        setHardness(10);
        setResistance(2000);
        setUnlocalizedName(name);
        setStepSound(soundStoneFootstep);
        setConnectedTexture();
    }

    @Override
    public void registerIcons(IconRegister register)
    {
        controllerOverlay = register.registerIcon("enhancedportals:portalFrame_controller");
        redstoneOverlay = register.registerIcon("enhancedportals:portalFrame_redstone");

        for (int i = 0; i < textures.length; i++)
        {
            textures[i] = register.registerIcon("enhancedportals:frame/portalFrame_" + i);
        }
    }

    @Override
    public Icon getIcon(int side, int meta)
    {
        if (renderPass == 1)
        {
            if (meta == 1)
            {
                return controllerOverlay;
            }
            else if (meta == 2)
            {
                return redstoneOverlay;
            }
        }

        return textures[0];
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addCreativeItems(ArrayList list)
    {
        for (int i = 0; i < 3; i++)
        {
            list.add(new ItemStack(this, 1, i));
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubBlocks(int par1, CreativeTabs creativeTab, List list)
    {
        for (int i = 0; i < 3; i++)
        {
            list.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public Icon getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        TileEP frame = (TileEP) blockAccess.getBlockTileEntity(x, y, z);
        Icon frameIcon = frame != null ? frame.getTexture(side, renderPass) : null;

        return frameIcon == null ? super.getBlockTexture(blockAccess, x, y, z, side) : frameIcon;
    }

    @Override
    public boolean canRenderInPass(int pass)
    {
        renderPass = pass;
        return pass < 2;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int par5, int par6)
    {
        ((TilePortalFrame) world.getBlockTileEntity(x, y, z)).selfBroken();
        super.breakBlock(world, x, y, z, par5, par6);
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
        else if (metadata == 1)
        {
            return new TilePortalFrameController();
        }
        else if (metadata == 2)
        {
            return new TilePortalFrameRedstone();
        }

        return null;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int id)
    {
        ((TilePortalFrame) world.getBlockTileEntity(x, y, z)).neighborChanged(id);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {        
        return ((TilePortalFrame) world.getBlockTileEntity(x, y, z)).activate(player);
    }

    @Override
    public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side)
    {
        return true;
    }

    @Override
    public boolean isBlockNormalCube(World world, int x, int y, int z)
    {
        return false;
    }

    @Override
    public int getRenderBlockPass()
    {
        return 1;
    }

    @Override
    public int colorMultiplier(IBlockAccess par1iBlockAccess, int par2, int par3, int par4)
    {
        TilePortalFrame frame = (TilePortalFrame) par1iBlockAccess.getBlockTileEntity(par2, par3, par4);
        TilePortalFrameController controller = frame.getControllerValidated();
                
        if (controller != null)
        {
            return controller.frameTexture.TextureColour;
        }
        else if (frame instanceof TilePortalFrameController)
        {
            return ((TilePortalFrameController) frame).frameTexture.TextureColour;
        }
        
        return super.colorMultiplier(par1iBlockAccess, par2, par3, par4);
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
    public void updateTick(World world, int x, int y, int z, Random random)
    {
        ((TilePortalFrame) world.getBlockTileEntity(x, y, z)).scheduledTick(random);
    }

    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, World world, int x, int y, int z)
    {
        return false;
    }

    @Override
    public boolean canBeReplacedByLeaves(World world, int x, int y, int z)
    {
        return false;
    }

    @Override
    public int damageDropped(int par1)
    {
        return par1;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
        return new ItemStack(CommonProxy.blockFrame.blockID, 1, world.getBlockMetadata(x, y, z));
    }
}
