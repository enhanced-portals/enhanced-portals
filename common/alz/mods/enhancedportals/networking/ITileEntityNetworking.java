package alz.mods.enhancedportals.networking;

public interface ITileEntityNetworking
{
    public PacketTileUpdate getUpdatePacket();

    public void parseUpdatePacket(PacketTileUpdate packet);
}
