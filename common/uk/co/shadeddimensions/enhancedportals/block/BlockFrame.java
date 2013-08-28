package uk.co.shadeddimensions.enhancedportals.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeDirection;
import uk.co.shadeddimensions.enhancedportals.tileentity.TileEP;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrame;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameController;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameRedstone;
import uk.co.shadeddimensions.enhancedportals.util.PortalUtils;

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

    private void blockDestroyed(World world, int x, int y, int z)
    {
        PortalUtils.removePortalAround((WorldServer) world, x, y, z);
    }

    @Override
    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int meta)
    {
        if (world.isRemote)
        {
            return;
        }

        blockDestroyed(world, x, y, z);
    }

    @Override
    public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion par5Explosion)
    {
        if (world.isRemote)
        {
            return;
        }

        blockDestroyed(world, x, y, z);
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
        return super.colorMultiplier(par1iBlockAccess, par2, par3, par4);
    }
}
