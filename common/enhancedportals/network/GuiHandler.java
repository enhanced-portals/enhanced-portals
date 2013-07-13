package enhancedportals.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import enhancedportals.client.gui.GuiAutomaticDiallerSimple;
import enhancedportals.client.gui.GuiDialDevice;
import enhancedportals.client.gui.GuiDialDeviceAddNetwork;
import enhancedportals.client.gui.GuiDialDeviceBasic;
import enhancedportals.client.gui.GuiModifier;
import enhancedportals.client.gui.GuiPortalModifierNetwork;
import enhancedportals.client.gui.container.ContainerModifier;
import enhancedportals.container.ContainerDialDeviceAddNetwork;
import enhancedportals.lib.GuiIds;
import enhancedportals.lib.Settings;
import enhancedportals.network.packet.PacketAutomaticDiallerUpdate;
import enhancedportals.network.packet.PacketBasicDialDeviceUpdate;
import enhancedportals.network.packet.PacketDialDeviceUpdate;
import enhancedportals.network.packet.PacketDialEntry;
import enhancedportals.network.packet.PacketEnhancedPortals;
import enhancedportals.network.packet.PacketPortalModifierUpdate;
import enhancedportals.network.packet.PacketPortalModifierUpgrade;
import enhancedportals.portal.network.DialDeviceNetworkObject;
import enhancedportals.tileentity.TileEntityAutomaticDialler;
import enhancedportals.tileentity.TileEntityDialDevice;
import enhancedportals.tileentity.TileEntityDialDeviceBasic;
import enhancedportals.tileentity.TileEntityPortalModifier;

public class GuiHandler implements IGuiHandler
{
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

        if (ID == GuiIds.PortalModifier && Settings.canUse(player))
        {
            if (tileEntity instanceof TileEntityPortalModifier)
            {
            	return new GuiModifier(new ContainerModifier(player.inventory, (TileEntityPortalModifier) tileEntity), (IInventory) tileEntity);
                //return new GuiPortalModifier(player.inventory, (TileEntityPortalModifier) tileEntity);
            }
        }
        else if (ID == GuiIds.PortalModifierNetwork && Settings.canUse(player))
        {
            if (tileEntity instanceof TileEntityPortalModifier)
            {
                return new GuiPortalModifierNetwork((TileEntityPortalModifier) tileEntity);
            }
        }
        else if (ID == GuiIds.DialDevice)
        {
            if (tileEntity instanceof TileEntityDialDevice)
            {
                return new GuiDialDevice((TileEntityDialDevice) tileEntity);
            }
        }
        else if (ID == GuiIds.DialDeviceBasic)
        {
            if (tileEntity instanceof TileEntityDialDeviceBasic)
            {
                return new GuiDialDeviceBasic((TileEntityDialDeviceBasic) tileEntity);
            }
        }
        else if (ID == GuiIds.AutoDiallerBasic && Settings.canUse(player))
        {
            if (tileEntity instanceof TileEntityAutomaticDialler)
            {
                return new GuiAutomaticDiallerSimple((TileEntityAutomaticDialler) tileEntity);
            }
        }
        else if (ID == GuiIds.DialDeviceAdd && Settings.canUse(player))
        {
            if (tileEntity instanceof TileEntityDialDevice)
            {
                return new GuiDialDeviceAddNetwork(player.inventory, (TileEntityDialDevice) tileEntity);
            }
        }

        return null;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

        if (ID == GuiIds.PortalModifier && Settings.canUse(player))
        {
            if (tileEntity instanceof TileEntityPortalModifier)
            {
                PacketDispatcher.sendPacketToPlayer(PacketEnhancedPortals.makePacket(new PacketPortalModifierUpdate((TileEntityPortalModifier) tileEntity)), (Player) player);
                PacketDispatcher.sendPacketToPlayer(PacketEnhancedPortals.makePacket(new PacketPortalModifierUpgrade((TileEntityPortalModifier) tileEntity)), (Player) player);

                return new ContainerModifier(player.inventory, (TileEntityPortalModifier) tileEntity);
                //return new ContainerPortalModifier(player.inventory, (TileEntityPortalModifier) tileEntity);
            }
        }
        else if (ID == GuiIds.PortalModifierNetwork)
        {

        }
        else if (ID == GuiIds.DialDevice)
        {
            if (tileEntity instanceof TileEntityDialDevice)
            {
                TileEntityDialDevice dial = (TileEntityDialDevice) tileEntity;
                PacketDispatcher.sendPacketToPlayer(PacketEnhancedPortals.makePacket(new PacketDialDeviceUpdate(dial)), (Player) player);

                for (DialDeviceNetworkObject obj : dial.destinationList)
                {
                    PacketDispatcher.sendPacketToPlayer(PacketEnhancedPortals.makePacket(new PacketDialEntry(dial, (byte) 0, obj)), (Player) player);
                }
            }
        }
        else if (ID == GuiIds.DialDeviceBasic)
        {
            if (tileEntity instanceof TileEntityDialDeviceBasic)
            {
                PacketDispatcher.sendPacketToPlayer(PacketEnhancedPortals.makePacket(new PacketBasicDialDeviceUpdate((TileEntityDialDeviceBasic) tileEntity)), (Player) player);
            }
        }
        else if (ID == GuiIds.AutoDiallerBasic && Settings.canUse(player))
        {
            if (tileEntity instanceof TileEntityAutomaticDialler)
            {
                PacketDispatcher.sendPacketToPlayer(PacketEnhancedPortals.makePacket(new PacketAutomaticDiallerUpdate((TileEntityAutomaticDialler) tileEntity)), (Player) player);
            }
        }
        else if (ID == GuiIds.DialDeviceAdd && Settings.canUse(player))
        {
            if (tileEntity instanceof TileEntityDialDevice)
            {
                return new ContainerDialDeviceAddNetwork(player.inventory);
            }
        }

        return null;
    }
}
