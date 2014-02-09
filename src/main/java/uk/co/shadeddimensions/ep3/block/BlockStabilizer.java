package uk.co.shadeddimensions.ep3.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import uk.co.shadeddimensions.ep3.lib.Reference;
import uk.co.shadeddimensions.ep3.tileentity.TileStabilizer;
import uk.co.shadeddimensions.ep3.tileentity.TileStabilizerMain;
import uk.co.shadeddimensions.library.ct.ConnectedTextures;

public class BlockStabilizer extends BlockContainer
{
    public static int ID;
    public static BlockStabilizer instance;
    
    static ConnectedTextures connectedTextures;

    public BlockStabilizer()
    {
        super(ID, Material.rock);
        instance = this;
        setHardness(5);
        setResistance(2000);
        setUnlocalizedName("stabilizer");
        setStepSound(soundStoneFootstep);
        setCreativeTab(Reference.creativeTab);
        connectedTextures = new ConnectedTextures("enhancedportals:bridge/bridge_%s", ID, -1);
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
            return new TileStabilizer();
        }
        else if (metadata == 1)
        {
            return new TileStabilizerMain();
        }

        return null;
    }

    @Override
    public Icon getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        TileEntity tile = blockAccess.getBlockTileEntity(x, y, z);

        if (tile instanceof TileStabilizer)
        {
            return ((TileStabilizer) tile).isFormed ? connectedTextures.getIconForSide(blockAccess, x, y, z, side) : connectedTextures.getBaseIcon();
        }

        return connectedTextures.getIconForSide(blockAccess, x, y, z, side);
    }

    @Override
    public Icon getIcon(int par1, int par2)
    {
        return connectedTextures.getBaseIcon();
    }

    @Override
    public void registerIcons(IconRegister iconRegister)
    {
        connectedTextures.registerIcons(iconRegister);
    }
    
    @Override
    public void breakBlock(World world, int x, int y, int z, int oldID, int newID)
    {
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        
        if (tile instanceof TileStabilizer)
        {
            ((TileStabilizer) tile).breakBlock(oldID, newID);
        }
        else if (tile instanceof TileStabilizerMain)
        {
            ((TileStabilizerMain) tile).breakBlock(oldID, newID);
        }
         
        super.breakBlock(world, x, y, z, oldID, newID);
    }
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        
        if (tile instanceof TileStabilizer)
        {
            return ((TileStabilizer) tile).activate(player);
        }
        else if (tile instanceof TileStabilizerMain)
        {
            return ((TileStabilizerMain) tile).activate(player);
        }
        
        return false;
    }
}
