package uk.co.shadeddimensions.enhancedportals.network.packet.n;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.INetworkManager;
import uk.co.shadeddimensions.enhancedportals.tileentity.TileEP;
import cpw.mods.fml.common.network.Player;

public class PacketTile extends PacketEP
{
    TileEP Tile;
    
    public PacketTile()
    {
        
    }
    
    public PacketTile(TileEP tile)
    {
        Tile = tile;
    }

    @Override
    public void readData(DataInputStream stream) throws IOException
    {
        
    }

    @Override
    public void writeData(DataOutputStream stream) throws IOException
    {
        
    }

    @Override
    public void execute(INetworkManager networkManager, Player player)
    {
        
    }
}
