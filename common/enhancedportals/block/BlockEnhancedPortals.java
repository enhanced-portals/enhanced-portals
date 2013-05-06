package enhancedportals.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class BlockEnhancedPortals extends BlockContainer
{
    private boolean canRotate = false;
    
    protected BlockEnhancedPortals(int id, Material material)
    {
        super(id, material);
    }

    public void setCanRotate()
    {
        canRotate = true;
    }
    
    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return null;
    }
    
    @Override
    public ForgeDirection[] getValidRotations(World worldObj, int x, int y, int z)
    {
        return null;
    }
    
    @Override
    public boolean onBlockActivated(World worldObj, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        if (canRotate)
        {        
            ForgeDirection nextRotation = getNextRotation(worldObj, x, y, z);
            
            if (nextRotation == null || player.inventory.mainInventory[player.inventory.currentItem] != null)
            {
                return false;
            }
            
            rotateBlock(worldObj, x, y, z, nextRotation);      
        }
        
        return false;
    }
    
    private ForgeDirection getNextRotation(World worldObj, int x, int y, int z)
    {
        if (canRotate)
        {
            ForgeDirection[] validRotations = getValidRotations(worldObj, x, y, z);
            ForgeDirection currentRotation = ForgeDirection.getOrientation(worldObj.getBlockMetadata(x, y, z));
            
            if (validRotations == null)
            {
                return null;
            }
            
            for (int i = 0; i < validRotations.length; i++)
            {
                if (validRotations[i] == currentRotation)
                {
                    if (i + 1 < validRotations.length)
                    {
                        return validRotations[i + 1];
                    }
                    else
                    {
                        return validRotations[0];
                    }
                }
            }
        }
        
        return null;
    }
    
    @Override
    public boolean rotateBlock(World worldObj, int x, int y, int z, ForgeDirection axis)
    {
        if (canRotate)
        {
            worldObj.setBlockMetadataWithNotify(x, y, z, axis.ordinal(), 2);
        }
        
        return true;
    }
}
