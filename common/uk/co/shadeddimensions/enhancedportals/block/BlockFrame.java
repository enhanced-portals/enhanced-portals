package uk.co.shadeddimensions.enhancedportals.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
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
        setUnlocalizedName("portalFrame");
        setStepSound(soundStoneFootstep);
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
        
        PortalUtils.removePortal((WorldServer) world, x, y, z);
    }
    
    @Override
    public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion par5Explosion)
    {
        if (world.isRemote)
        {
            return;
        }
        
        PortalUtils.removePortal((WorldServer) world, x, y, z);
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TilePortalFrame();
    }
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        if (player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().itemID == CommonProxy.itemTextureDuplicator.itemID)
        {
            TilePortalFrame frame = (TilePortalFrame) world.getBlockTileEntity(x, y, z);
            
            if (ItemTextureDuplicator.hasStoredTexture(player.inventory.getCurrentItem()))
            {
                frame.texture = new Texture(ItemTextureDuplicator.getStoredTexture(player.inventory.getCurrentItem()), 0xFFFFFF, 0xFFFFFF);
                world.markBlockForRenderUpdate(x, y, z);
            }
            else
            {
                ItemTextureDuplicator.setStoredTexture(player.inventory.getCurrentItem(), frame.texture.Texture);
            }
            
            return true;
        }
        
        return false;
    }
}
