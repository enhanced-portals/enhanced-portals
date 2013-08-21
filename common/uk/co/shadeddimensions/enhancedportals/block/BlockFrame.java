package uk.co.shadeddimensions.enhancedportals.block;

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
import uk.co.shadeddimensions.enhancedportals.item.ItemTextureDuplicator;
import uk.co.shadeddimensions.enhancedportals.network.ClientProxy;
import uk.co.shadeddimensions.enhancedportals.network.CommonProxy;
import uk.co.shadeddimensions.enhancedportals.tileentity.TileEP;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalController;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrame;
import uk.co.shadeddimensions.enhancedportals.util.IConnectedTexture;
import uk.co.shadeddimensions.enhancedportals.util.PortalUtils;
import uk.co.shadeddimensions.enhancedportals.util.Texture;

public class BlockFrame extends BlockEP implements IConnectedTexture
{
    int renderPass = 0;
    public static Icon portalControllerOverlay;
    
    public BlockFrame(int id, String name)
    {
        super(id, Material.rock);
        setHardness(10);
        setResistance(2000);
        setUnlocalizedName(name);
        setStepSound(soundStoneFootstep);
    }
    
    @Override
    public void registerIcons(IconRegister register)
    {
        portalControllerOverlay = register.registerIcon("enhancedportals:portalController");
        
        for (int i = 0; i < textures.length; i++)
        {
            textures[i] = register.registerIcon("enhancedportals:frame/portalFrame_" + i);
        }
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubBlocks(int par1, CreativeTabs creativeTab, List list)
    {
        list.add(new ItemStack(this, 0, 0));
        list.add(new ItemStack(this, 0, 1));
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
            return new TilePortalController();
        }
        
        return null;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        ItemStack stack = player.inventory.getCurrentItem();

        if (stack != null)
        {
            if (stack.itemID == CommonProxy.itemTextureDuplicator.itemID)
            {
                TilePortalFrame frame = (TilePortalFrame) world.getBlockTileEntity(x, y, z);

                if (ItemTextureDuplicator.hasStoredTexture(player.inventory.getCurrentItem()))
                {
                    frame.texture = new Texture(ItemTextureDuplicator.getStoredTexture(player.inventory.getCurrentItem()), 0xFFFFFF, 0xFFFFFF, 0);
                    world.markBlockForRenderUpdate(x, y, z);
                }
                else if (!frame.texture.Texture.equals(""))
                {
                    ItemTextureDuplicator.setStoredTexture(player.inventory.getCurrentItem(), frame.texture.Texture);
                }

                return true;
            }
            else if (stack.itemID == CommonProxy.itemNetherQuartzIgniter.itemID || stack.itemID == CommonProxy.itemWrench.itemID)
            {
                return false;
            }
        }

        return false;
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
        if (ClientProxy.isWearingGoggles)
        {
            TileEP tile = (TileEP) par1iBlockAccess.getBlockTileEntity(par2, par3, par4);
            
            if (tile instanceof TilePortalFrame)
            {
                TilePortalFrame frame = (TilePortalFrame) tile;
                
                if (frame.controller == null || frame.controller.posY == -1)
                {
                    return 0xFF0000;
                }
            }
        }
        
        return super.colorMultiplier(par1iBlockAccess, par2, par3, par4);
    }
}
