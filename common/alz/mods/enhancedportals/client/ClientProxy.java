package alz.mods.enhancedportals.client;

import cpw.mods.fml.common.network.PacketDispatcher;
import alz.mods.enhancedportals.common.CommonProxy;
import alz.mods.enhancedportals.tileentity.TileEntityPortalModifier;

public class ClientProxy extends CommonProxy
{	
	public static void SendBlockUpdate(TileEntityPortalModifier modifier)
	{
		PacketDispatcher.sendPacketToServer(modifier.getUpdatePacket().getClientPacket());
	}
}
