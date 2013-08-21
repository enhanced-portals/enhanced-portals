package uk.co.shadeddimensions.enhancedportals.tileentity;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;
import uk.co.shadeddimensions.enhancedportals.network.CommonProxy;
import uk.co.shadeddimensions.enhancedportals.network.packet.MainPacket;
import uk.co.shadeddimensions.enhancedportals.network.packet.PacketRequestData;
import uk.co.shadeddimensions.enhancedportals.util.Texture;
import cpw.mods.fml.common.network.PacketDispatcher;

public class TilePortalFrame extends TileEP
{
    public Texture texture;
    public ChunkCoordinates controller;

    public TilePortalFrame()
    {
        texture = new Texture();
    }

    @Override
    public Icon getTexture(int side, int renderpass)
    {
        if (renderpass == 1)
        {
            ForgeDirection d = ForgeDirection.getOrientation(side);
            
            if (worldObj.getBlockId(xCoord + d.offsetX, yCoord + d.offsetY, zCoord + d.offsetZ) == CommonProxy.blockPortal.blockID)
            {
                return Block.blockGold.getBlockTextureFromSide(side);
            }
        }
        
        return null;
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);

        texture.writeToNBT(tagCompound);

        if (controller != null)
        {
            tagCompound.setInteger("ControllerX", controller.posX);
            tagCompound.setInteger("ControllerY", controller.posY);
            tagCompound.setInteger("ControllerZ", controller.posZ);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);

        texture = new Texture(tagCompound);

        if (tagCompound.hasKey("ControllerX"))
        {
            controller = new ChunkCoordinates(tagCompound.getInteger("ControllerX"), tagCompound.getInteger("ControllerY"), tagCompound.getInteger("ControllerZ"));
        }
    }

    @Override
    public void validate()
    {
        super.validate();

        if (worldObj.isRemote)
        {
            PacketDispatcher.sendPacketToServer(MainPacket.makePacket(new PacketRequestData(this)));
        }
    }

    public boolean checkController()
    {
        if (controller != null)
        {
            if (controller.posY == -1 || !(worldObj.getBlockTileEntity(controller.posX, controller.posY, controller.posZ) instanceof TilePortalController))
            {
                controller = new ChunkCoordinates(0, -1, 0);
                return false;
            }

            return true;
        }

        controller = new ChunkCoordinates(0, -1, 0);
        return false;
    }
}
