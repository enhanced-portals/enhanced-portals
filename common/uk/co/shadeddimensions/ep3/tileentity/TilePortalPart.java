package uk.co.shadeddimensions.ep3.tileentity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.ForgeDirection;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;
import uk.co.shadeddimensions.ep3.util.GeneralUtils;

public class TilePortalPart extends TileEnhancedPortals
{
    public ChunkCoordinates portalController;

    public void breakBlock(int oldBlockID, int oldMetadata)
    {
        if (!worldObj.isRemote)
        {
            if (oldBlockID == worldObj.getBlockId(xCoord, yCoord, zCoord))
            {
                return;
            }

            TilePortalController controller = getPortalController();

            if (controller != null)
            {
                controller.partBroken(false);
            }
        }
    }

    @Override
    public void fillPacket(DataOutputStream stream) throws IOException
    {
        super.fillPacket(stream);
        GeneralUtils.writeChunkCoord(stream, portalController);
    }

    public TilePortalController getPortalController()
    {
        if (portalController != null)
        {
            TileEntity tile = worldObj.getBlockTileEntity(portalController.posX, portalController.posY, portalController.posZ);

            if (tile instanceof TilePortalController)
            {
                return (TilePortalController) tile;
            }

            portalController = null;
        }

        return null;
    }

    public void onBlockPlacedBy(EntityLivingBase entity, ItemStack stack)
    {
        if (!worldObj.isRemote)
        {
            for (int i = 0; i < 6; i++)
            {
                ForgeDirection d = ForgeDirection.getOrientation(i);
                TileEntity tile = worldObj.getBlockTileEntity(xCoord + d.offsetX, yCoord + d.offsetY, zCoord + d.offsetZ);

                if (tile != null && tile instanceof TilePortalPart)
                {
                    ((TilePortalPart) tile).breakBlock(0, 0);
                }
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        portalController = GeneralUtils.loadChunkCoord(tag, "Controller");
    }

    @Override
    public void usePacket(DataInputStream stream) throws IOException
    {
        super.usePacket(stream);
        portalController = GeneralUtils.readChunkCoord(stream);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        GeneralUtils.saveChunkCoord(tag, portalController, "Controller");
    }
    
    public int getColourMultiplier()
    {
        TilePortalController controller = getPortalController();

        if (controller != null)
        {
            return controller.activeTextureData.getFrameColour();
        }

        return 0xFFFFFF;
    }
}
