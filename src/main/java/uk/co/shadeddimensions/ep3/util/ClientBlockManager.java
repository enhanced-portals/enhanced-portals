package uk.co.shadeddimensions.ep3.util;

import java.io.DataInputStream;
import java.io.IOException;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientBlockManager extends BlockManager
{
    int frame, portal, redstone;
    boolean network, dial, biometric;

    @Override
    public int getFrameCount()
    {
        return frame;
    }

    @Override
    public boolean getHasBiometricIdentifier()
    {
        return biometric;
    }

    @Override
    public boolean getHasDialDevice()
    {
        return dial;
    }

    @Override
    public boolean getHasNetworkInterface()
    {
        return network;
    }

    @Override
    public int getPortalCount()
    {
        return portal;
    }

    @Override
    public int getRedstoneInterfaceCount()
    {
        return redstone;
    }

    @Override
    public BlockManager readFromPacket(DataInputStream stream) throws IOException
    {
        frame = stream.readInt();
        portal = stream.readInt();
        redstone = stream.readInt();
        network = stream.readBoolean();
        dial = stream.readBoolean();
        biometric = stream.readBoolean();
        moduleManipulator = GeneralUtils.readChunkCoord(stream);
        return this;
    }
}
