package uk.co.shadeddimensions.enhancedportals.block;

import java.util.List;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import uk.co.shadeddimensions.enhancedportals.EnhancedPortals;
import uk.co.shadeddimensions.enhancedportals.item.ItemTextureDuplicator;
import uk.co.shadeddimensions.enhancedportals.lib.Identifiers;
import uk.co.shadeddimensions.enhancedportals.network.CommonProxy;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrame;
import uk.co.shadeddimensions.enhancedportals.util.PortalUtils;
import uk.co.shadeddimensions.enhancedportals.util.Texture;

public class BlockFrame extends BlockContainer
{
    public BlockFrame()
    {
        super(Identifiers.Block.PORTAL_FRAME, Material.rock);
        setHardness(10);
        setResistance(2000);
        setUnlocalizedName("ep2.portalFrame");
        setStepSound(soundStoneFootstep);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubBlocks(int par1, CreativeTabs creativeTab, List list)
    {
        list.add(new ItemStack(this, 0));
        list.add(new ItemStack(this, 1));
    }

    @Override
    public Icon getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        TilePortalFrame frame = (TilePortalFrame) blockAccess.getBlockTileEntity(x, y, z);

        if (!frame.texture.Texture.equals(""))
        {
            return Texture.getTexture(frame.texture.Texture, side);
        }

        return super.getBlockTexture(blockAccess, x, y, z, side);
    }

    @Override
    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int meta)
    {
        if (world.isRemote)
        {
            return;
        }

        PortalUtils.removePortalAround((WorldServer) world, x, y, z);
    }

    @Override
    public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion par5Explosion)
    {
        if (world.isRemote)
        {
            return;
        }

        PortalUtils.removePortalAround((WorldServer) world, x, y, z);
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TilePortalFrame();
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

        if (world.getBlockMetadata(x, y, z) == 1)
        {
            player.openGui(EnhancedPortals.instance, Identifiers.Gui.FRAME_CONTROLLER, world, x, y, z);
            return true;
        }

        return false;
    }
}
