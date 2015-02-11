package enhancedportals.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import enhancedportals.EnhancedPortals;
import enhancedportals.tile.TileDimensionalBridgeStabilizer;
import enhancedportals.util.ConnectedTexturesDetailed;

public class BlockDimensionalBridgeStabilizer extends BlockContainer {
    ConnectedTexturesDetailed connectedTextures;
    
    protected BlockDimensionalBridgeStabilizer(String n) {
        super(Material.rock);
        setCreativeTab(EnhancedPortals.proxy.creativeTab);
        setHardness(5);
        setResistance(2000);
        setBlockName(n);
        setStepSound(soundTypeStone);
        connectedTextures = new ConnectedTexturesDetailed("enhancedportals:bridge/%s", this, -1);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileDimensionalBridgeStabilizer();
    }
    
    @Override
    public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int m) {
        return connectedTextures.getBaseIcon();
    }
    
    @Override
    public IIcon getIcon(int side, int meta) {
        return connectedTextures.getBaseIcon();
    }
    
    @Override
    public void registerBlockIcons(IIconRegister register) {
        connectedTextures.registerIcons(register);
    }
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
        TileEntity t = world.getTileEntity(x, y, z);
        
        if (t instanceof TileDimensionalBridgeStabilizer) {
            return ((TileDimensionalBridgeStabilizer) t).onBlockActivated(player);
        }
        
        return super.onBlockActivated(world, x, y, z, player, p_149727_6_, p_149727_7_, p_149727_8_, p_149727_9_);
    }
    
    @Override
    public void onBlockPreDestroy(World world, int x, int y, int z, int p_149725_5_) {
        TileEntity t = world.getTileEntity(x, y, z);
        
        if (t instanceof TileDimensionalBridgeStabilizer) {
            ((TileDimensionalBridgeStabilizer) t).onPreDestroy();
        } 
    }
}
