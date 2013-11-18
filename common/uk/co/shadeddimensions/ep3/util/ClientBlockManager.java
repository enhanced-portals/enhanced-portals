package uk.co.shadeddimensions.ep3.util;

import java.io.DataInputStream;
import java.io.IOException;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientBlockManager extends BlockManager
{
    int frame, portal, redstone, item, energy, fluid;
    boolean network, dial, biometric;
    
    public int getFrameCount()
    {
        return frame;
    }

    public int getPortalCount()
    {
        return portal;
    }

    public int getRedstoneInterfaceCount()
    {
        return redstone;
    }

    public int getItemCount()
    {
        return item;
    }

    public int getEnergyCount()
    {
        return energy;
    }

    public int getFluidCount()
    {
        return fluid;
    }

    public boolean getHasNetworkInterface()
    {
        return network;
    }

    public boolean getHasDialDevice()
    {
        return dial;
    }

    public boolean getHasBiometricIdentifier()
    {
        return biometric;
    }
    
    @Override
    public BlockManager readFromPacket(DataInputStream stream) throws IOException
    {
        frame = stream.readInt();
        portal = stream.readInt();
        redstone = stream.readInt();
        item = stream.readInt();
        energy = stream.readInt();
        fluid = stream.readInt();
        network = stream.readBoolean();
        dial = stream.readBoolean();
        biometric = stream.readBoolean();
        moduleManipulator = GeneralUtils.readChunkCoord(stream);
        return this;
    }
}
