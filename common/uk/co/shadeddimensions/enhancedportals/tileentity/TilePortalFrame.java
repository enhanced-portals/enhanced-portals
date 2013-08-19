package uk.co.shadeddimensions.enhancedportals.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.ForgeDirection;
import uk.co.shadeddimensions.enhancedportals.lib.Identifiers;
import uk.co.shadeddimensions.enhancedportals.network.packet.MainPacket;
import uk.co.shadeddimensions.enhancedportals.network.packet.PacketRequestData;
import uk.co.shadeddimensions.enhancedportals.util.Texture;
import cpw.mods.fml.common.network.PacketDispatcher;

public class TilePortalFrame extends TileEntity
{
    public Texture texture;
    public ChunkCoordinates controller;
    
    public int portalMeta;
    public ForgeDirection creationOffset;

    public TilePortalFrame()
    {
        texture = new Texture();
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);

        texture.writeToNBT(tagCompound);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);

        texture = new Texture(tagCompound);
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
            if (controller.posY == -1)
            {
                return false;
            }

            return worldObj.getBlockId(controller.posX, controller.posY, controller.posZ) == Identifiers.Block.PORTAL_FRAME;
        }

        return false;
    }
}
