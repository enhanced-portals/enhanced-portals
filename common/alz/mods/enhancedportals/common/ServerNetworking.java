package alz.mods.enhancedportals.common;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import alz.mods.enhancedportals.common.Reference;
import alz.mods.enhancedportals.common.TileEntityPortalModifier;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;

public class ServerNetworking
{
	public static void SendBlockUpdateToClient(int freq, int colour, int x, int y, int z, int dim, EntityPlayer player)
	{
		
	}
}
