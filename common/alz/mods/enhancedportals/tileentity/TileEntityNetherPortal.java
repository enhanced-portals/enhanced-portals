package alz.mods.enhancedportals.tileentity;

import alz.mods.enhancedportals.networking.ITileEntityNetworking;
import alz.mods.enhancedportals.networking.PacketTileUpdate;
import alz.mods.enhancedportals.portals.PortalTexture;
import net.minecraft.tileentity.TileEntity;

public class TileEntityNetherPortal extends TileEntity implements ITileEntityNetworking
{
	public PortalTexture Texture;
	
	public TileEntityNetherPortal()
	{
		Texture = PortalTexture.PURPLE;
	}

	@Override
	public PacketTileUpdate getUpdatePacket()
	{
		PacketTileUpdate packet = new PacketTileUpdate();
		packet.xCoord = xCoord;
		packet.yCoord = yCoord;
		packet.zCoord = zCoord;
		packet.Dimension = worldObj.provider.dimensionId;
		packet.data = new int[] { Texture.ordinal() };
		
		return null;
	}

	@Override
	public void parseUpdatePacket(PacketTileUpdate packet)
	{
		Texture = PortalTexture.getPortalTexture(packet.data[0]);
	}
}
