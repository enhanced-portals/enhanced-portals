package uk.co.shadeddimensions.enhancedportals.tileentity;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;
import uk.co.shadeddimensions.enhancedportals.block.BlockFrame;
import uk.co.shadeddimensions.enhancedportals.network.ClientProxy;
import uk.co.shadeddimensions.enhancedportals.network.CommonProxy;
import uk.co.shadeddimensions.enhancedportals.network.packet.MainPacket;
import uk.co.shadeddimensions.enhancedportals.network.packet.PacketRequestData;
import uk.co.shadeddimensions.enhancedportals.util.Texture;
import cpw.mods.fml.common.network.PacketDispatcher;

public class TilePortalController extends TileEP
{
    public Texture texture;
        
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
            
            if (ClientProxy.isWearingGoggles)
            {
                return BlockFrame.portalControllerOverlay;
            }
        }
        
        return null;
    }
    
    public TilePortalController()
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
}
