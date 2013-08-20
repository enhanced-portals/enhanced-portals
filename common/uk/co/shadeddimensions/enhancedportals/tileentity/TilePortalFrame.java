package uk.co.shadeddimensions.enhancedportals.tileentity;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Icon;
import uk.co.shadeddimensions.enhancedportals.network.ClientProxy;
import uk.co.shadeddimensions.enhancedportals.network.packet.MainPacket;
import uk.co.shadeddimensions.enhancedportals.network.packet.PacketRequestData;
import uk.co.shadeddimensions.enhancedportals.util.Texture;
import cpw.mods.fml.common.network.PacketDispatcher;

public class TilePortalFrame extends TileEP
{
    public Texture texture;
    public ChunkCoordinates controller;
    public boolean[] activeSide;
    
    public TilePortalFrame()
    {
        texture = new Texture();
        activeSide = new boolean[6];
    }
    
    public boolean hasActiveSide()
    {
        return !(activeSide[0] == false && activeSide[1] == false && activeSide[2] == false && activeSide[3] == false && activeSide[4] == false && activeSide[5] == false);
    }
    
    @Override
    public Icon getTexture(int side, int renderpass)
    {
            if (renderpass == 1 && ClientProxy.isWearingGoggles && activeSide[side])
            {
                return Block.glass.getBlockTextureFromSide(side);
            }
        
        return Block.obsidian.getBlockTextureFromSide(side);
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
        
        for (int i = 0; i < activeSide.length; i++)
        {
            tagCompound.setBoolean("ActiveSide" + i, activeSide[i]);
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
        
        for (int i = 0; i < activeSide.length; i++)
        {
            activeSide[i] = tagCompound.getBoolean("ActiveSide" + i);
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
