package alz.mods.enhancedportals.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import alz.mods.enhancedportals.client.gui.GuiDialDevice;
import alz.mods.enhancedportals.client.gui.GuiDialDeviceAdd;
import alz.mods.enhancedportals.client.gui.GuiPortalModifier;
import alz.mods.enhancedportals.reference.Reference;
import alz.mods.enhancedportals.tileentity.TileEntityDialDevice;
import alz.mods.enhancedportals.tileentity.TileEntityPortalModifier;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.Player;

public class GuiHandler implements IGuiHandler
{
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

		if (ID == Reference.GuiIDs.PortalModifier)
		{
			if (tileEntity instanceof TileEntityPortalModifier)
			{
				TileEntityPortalModifier modifier = (TileEntityPortalModifier) tileEntity;

				if (Reference.LinkData != null)
				{
					Reference.LinkData.sendUpdatePacketToPlayer(modifier, (Player) player);
				}

				return new ContainerPortalModifier(player.inventory, modifier);
			}
		}
		else if (ID == Reference.GuiIDs.DialDevice + 1)
		{
			if (tileEntity instanceof TileEntityDialDevice)
			{
				TileEntityDialDevice dialDevice = (TileEntityDialDevice) tileEntity;

				return new ContainerDialDeviceAdd(player.inventory, dialDevice);
			}
		}

		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == Reference.GuiIDs.PortalModifier)
		{
			TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

			if (tileEntity instanceof TileEntityPortalModifier)
				return new GuiPortalModifier(player.inventory, (TileEntityPortalModifier) tileEntity);
		}
		else if (ID == Reference.GuiIDs.DialDevice)
		{
			TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

			if (tileEntity instanceof TileEntityDialDevice)
				return new GuiDialDevice(player, (TileEntityDialDevice) tileEntity);
		}
		else if (ID == Reference.GuiIDs.DialDevice + 1)
		{
			TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

			if (tileEntity instanceof TileEntityDialDevice)
				return new GuiDialDeviceAdd(player, (TileEntityDialDevice) tileEntity);
		}

		return null;
	}
}
