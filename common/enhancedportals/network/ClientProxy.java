package enhancedportals.network;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;
import enhancedportals.network.packet.PacketTEUpdate;
import enhancedportals.tileentity.TileEntityEnhancedPortals;

public class ClientProxy extends CommonProxy
{
    @Override
    public void loadSettings()
    {
        super.loadSettings();
    }
    
    @Override
    public void loadTileEntities()
    {
        super.loadTileEntities();
    }
    
    @Override
    public MinecraftServer getMinecraftServer()
    {
        return FMLClientHandler.instance().getServer();
    }
    
    @Override
    public World getClientWorld()
    {
        return FMLClientHandler.instance().getClient().theWorld;
    }
    
    @Override
    public void parseTileEntityUpdate(PacketTEUpdate update)
    {
        World world = getClientWorld();
                
        if (update.tileEntityExists(world))
        {
            ((TileEntityEnhancedPortals) update.getTileEntity(world)).parsePacketData(update.packetData);
        }
        else
        {
            System.out.println(String.format("Could not find enhanced portals tile entity at %s, %s, %s", update.xCoord, update.yCoord, update.zCoord));
        }
    }
}
