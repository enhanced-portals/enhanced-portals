package enhancedportals.tileentity;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class TilePortalPart extends TileEP
{
    ChunkCoordinates portalController;
    TileController cachedController;

    public boolean activate(EntityPlayer player, ItemStack stack)
    {
        return false;
    }

    public abstract void addDataToPacket(NBTTagCompound tag);

    public void breakBlock(Block b, int oldMeta)
    {
        TileController controller = getPortalController();

        if (controller != null)
        {
            controller.connectionTerminate();
        }
    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound tag = new NBTTagCompound();

        if (portalController != null)
        {
            tag.setInteger("PortalControllerX", portalController.posX);
            tag.setInteger("PortalControllerY", portalController.posY);
            tag.setInteger("PortalControllerZ", portalController.posZ);
        }

        addDataToPacket(tag);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tag);
    }

    public TileController getPortalController()
    {
        if (cachedController != null)
        {
            return cachedController;
        }

        TileEntity tile = worldObj.getTileEntity(portalController.posX, portalController.posY, portalController.posZ);

        if (tile != null && tile instanceof TileController)
        {
            cachedController = (TileController) tile;
            return cachedController;
        }

        return null;
    }

    /**
     * Called when this block is placed in the world.
     * 
     * @param entity
     * @param stack
     */
    public void onBlockPlaced(EntityLivingBase entity, ItemStack stack)
    {
        for (int i = 0; i < 6; i++)
        {
            TileEntity tile = getWorldCoordinates().offset(ForgeDirection.getOrientation(i)).getTileEntity();

            if (tile != null && tile instanceof TilePortalPart)
            {
                ((TilePortalPart) tile).onNeighborPlaced(entity, xCoord, yCoord, zCoord);
            }
        }
    }

    public abstract void onDataPacket(NBTTagCompound tag);

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        NBTTagCompound tag = pkt.func_148857_g();

        portalController = null;
        cachedController = null;

        if (tag.hasKey("PortalControllerX"))
        {
            portalController = new ChunkCoordinates(tag.getInteger("PortalControllerX"), tag.getInteger("PortalControllerY"), tag.getInteger("PortalControllerZ"));
        }

        onDataPacket(tag);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    /**
     * Called when a portal part gets placed next to this one. Is used to notify the Portal Controller to dismantle the structure.
     * 
     * @param x
     * @param y
     * @param z
     */
    public void onNeighborPlaced(EntityLivingBase entity, int x, int y, int z)
    {
        TileController controller = getPortalController();

        if (controller != null)
        {
            controller.deconstruct();
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);

        if (compound.hasKey("Controller"))
        {
            NBTTagCompound controller = compound.getCompoundTag("Controller");
            portalController = new ChunkCoordinates(controller.getInteger("X"), controller.getInteger("Y"), controller.getInteger("Z"));
        }
    }

    /**
     * Sets the Portal Controller to the specified coordinates, and sends an update packet.
     * 
     * @param c
     */
    public void setPortalController(ChunkCoordinates c)
    {
        portalController = c;
        cachedController = null;
        markDirty();
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        if (portalController != null)
        {
            NBTTagCompound controller = new NBTTagCompound();
            controller.setInteger("X", portalController.posX);
            controller.setInteger("Y", portalController.posY);
            controller.setInteger("Z", portalController.posZ);
            compound.setTag("Controller", controller);
        }
    }
}
