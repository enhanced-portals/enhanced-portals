package uk.co.shadeddimensions.enhancedportals.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import uk.co.shadeddimensions.enhancedportals.tileentity.frame.TileModuleManipulator;

public class PacketModuleManipulator extends MainPacket
{
    ChunkCoordinates coord, controller;
    int[] installedUpgradeIDs, installedUpgradeMetas;

    public PacketModuleManipulator()
    {
        installedUpgradeIDs = new int[9];
        installedUpgradeMetas = new int[9];
    }

    public PacketModuleManipulator(TileModuleManipulator frame)
    {
        coord = frame.getChunkCoordinates();
        controller = frame.controller;
        
        installedUpgradeIDs = new int[9];
        installedUpgradeMetas = new int[9];
        
        for (int i = 0; i < installedUpgradeIDs.length; i++)
        {
            if (frame.getStackInSlot(i) != null)
            {
                installedUpgradeIDs[i] = frame.getStackInSlot(i).itemID;
                installedUpgradeMetas[i] = frame.getStackInSlot(i).getItemDamage();
            }
        }
    }

    @Override
    public MainPacket consumePacket(DataInputStream stream) throws IOException
    {
        coord = readChunkCoordinates(stream);
        controller = readChunkCoordinates(stream);
        
        for (int i = 0; i < 9; i++)
        {
            installedUpgradeIDs[i] = stream.readInt();
            installedUpgradeMetas[i] = stream.readInt();
        }

        return this;
    }

    @Override
    public void execute(INetworkManager network, EntityPlayer player)
    {
        World world = player.worldObj;
        TileEntity tile = world.getBlockTileEntity(coord.posX, coord.posY, coord.posZ);

        if (tile != null && tile instanceof TileModuleManipulator)
        {
            TileModuleManipulator module = (TileModuleManipulator) tile;

            module.controller = controller;
            
            for (int i = 0; i < 9; i++)
            {
                if (installedUpgradeIDs[i] > 0)
                {
                    module.setInventorySlotContents(i, new ItemStack(installedUpgradeIDs[i], 1, installedUpgradeMetas[i]));
                }
            }
        }
    }

    @Override
    public void generatePacket(DataOutputStream stream) throws IOException
    {
        writeChunkCoordinates(coord, stream);
        writeChunkCoordinates(controller, stream);
        
        for (int i = 0; i < 9; i++)
        {
            stream.writeInt(installedUpgradeIDs[i]);
            stream.writeInt(installedUpgradeMetas[i]);
        }
    }
}
