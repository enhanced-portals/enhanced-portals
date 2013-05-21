package enhancedportals.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import enhancedportals.lib.Localization;

public class BlockEnhancedPortals extends BlockContainer
{
    private boolean canRotate = false, canRotateVertically = false;

    protected BlockEnhancedPortals(int id, Material material)
    {
        super(id, material);
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return null;
    }

    @Override
    public String getLocalizedName()
    {
        return Localization.localizeString(getUnlocalizedName() + ".name");
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
    public ForgeDirection[] getValidRotations(World worldObj, int x, int y, int z)
    {
        return null;
    }

    @Override
    public boolean onBlockActivated(World worldObj, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        if (canRotate && player.isSneaking())
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

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityLiving, ItemStack itemStack)
    {
        if (canRotate)
        {
            int direction = 0;
            int facing = MathHelper.floor_double(entityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

            if (facing == 0)
            {
                direction = ForgeDirection.NORTH.ordinal();
            }
            else if (facing == 1)
            {
                direction = ForgeDirection.EAST.ordinal();
            }
            else if (facing == 2)
            {
                direction = ForgeDirection.SOUTH.ordinal();
            }
            else if (facing == 3)
            {
                direction = ForgeDirection.WEST.ordinal();
            }

            if (canRotateVertically)
            {
                if (entityLiving.rotationPitch > 65 && entityLiving.rotationPitch <= 90)
                {
                    direction = ForgeDirection.UP.ordinal();
                }
                else if (entityLiving.rotationPitch < -65 && entityLiving.rotationPitch >= -90)
                {
                    direction = ForgeDirection.DOWN.ordinal();
                }
            }

            world.setBlockMetadataWithNotify(x, y, z, direction, 0);
        }
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

    public void setCanRotate()
    {
        canRotate = true;
    }

    public void setCanRotateVertically()
    {
        canRotateVertically = true;
    }
}
