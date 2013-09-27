package uk.co.shadeddimensions.enhancedportals.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import uk.co.shadeddimensions.enhancedportals.container.ContainerModuleManipulator;
import uk.co.shadeddimensions.enhancedportals.container.ContainerNetworkInterface;
import uk.co.shadeddimensions.enhancedportals.container.ContainerPortalFrameController;
import uk.co.shadeddimensions.enhancedportals.container.ContainerPortalFrameRedstone;
import uk.co.shadeddimensions.enhancedportals.container.ContainerPortalFrameTexture;
import uk.co.shadeddimensions.enhancedportals.container.ContainerPortalTexture;
import uk.co.shadeddimensions.enhancedportals.gui.GuiModuleManipulator;
import uk.co.shadeddimensions.enhancedportals.gui.GuiPortalFrameController;
import uk.co.shadeddimensions.enhancedportals.gui.GuiPortalFrameNetworkInterface;
import uk.co.shadeddimensions.enhancedportals.gui.GuiPortalFrameRedstone;
import uk.co.shadeddimensions.enhancedportals.gui.GuiPortalFrameTexture;
import uk.co.shadeddimensions.enhancedportals.gui.GuiPortalTexture;
import uk.co.shadeddimensions.enhancedportals.lib.GuiIds;
import uk.co.shadeddimensions.enhancedportals.network.packet.MainPacket;
import uk.co.shadeddimensions.enhancedportals.network.packet.PacketNetworkInterfaceData;
import uk.co.shadeddimensions.enhancedportals.network.packet.PacketPortalControllerData;
import uk.co.shadeddimensions.enhancedportals.network.packet.PacketRedstoneInterfaceData;
import uk.co.shadeddimensions.enhancedportals.tileentity.frame.TileModuleManipulator;
import uk.co.shadeddimensions.enhancedportals.tileentity.frame.TileNetworkInterface;
import uk.co.shadeddimensions.enhancedportals.tileentity.frame.TilePortalController;
import uk.co.shadeddimensions.enhancedportals.tileentity.frame.TileRedstoneInterface;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class GuiHandler implements IGuiHandler
{
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (ID == GuiIds.PORTAL_CONTROLLER && tile instanceof TilePortalController)
        {
            return new GuiPortalFrameController(player, (TilePortalController) tile);
        }
        else if (ID == GuiIds.PORTAL_REDSTONE && tile instanceof TileRedstoneInterface)
        {
            return new GuiPortalFrameRedstone(player, (TileRedstoneInterface) tile);
        }
        else if (ID == GuiIds.PORTAL_FRAME_TEXTURE && tile instanceof TilePortalController)
        {
            return new GuiPortalFrameTexture(player, (TilePortalController) tile);
        }
        else if (ID == GuiIds.PORTAL_TEXTURE && tile instanceof TilePortalController)
        {
            return new GuiPortalTexture(player, (TilePortalController) tile);
        }
        else if (ID == GuiIds.NETWORK_INTERFACE && tile instanceof TileNetworkInterface)
        {
            return new GuiPortalFrameNetworkInterface((TileNetworkInterface) tile);
        }
        else if (ID == GuiIds.MODULE_MANIPULATOR && tile instanceof TileModuleManipulator)
        {
            return new GuiModuleManipulator(player, (TileModuleManipulator) tile);
        }

        return null;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (ID == GuiIds.PORTAL_CONTROLLER && tile instanceof TilePortalController)
        {
            PacketDispatcher.sendPacketToPlayer(MainPacket.makePacket(new PacketPortalControllerData((TilePortalController) tile)), (Player) player);
            return new ContainerPortalFrameController((TilePortalController) tile);
        }
        else if (ID == GuiIds.PORTAL_REDSTONE && tile instanceof TileRedstoneInterface)
        {
            PacketDispatcher.sendPacketToPlayer(MainPacket.makePacket(new PacketRedstoneInterfaceData((TileRedstoneInterface) tile)), (Player) player);
            return new ContainerPortalFrameRedstone((TileRedstoneInterface) tile);
        }
        else if (ID == GuiIds.PORTAL_FRAME_TEXTURE && tile instanceof TilePortalController)
        {
            return new ContainerPortalFrameTexture(player, (TilePortalController) tile);
        }
        else if (ID == GuiIds.PORTAL_TEXTURE && tile instanceof TilePortalController)
        {
            return new ContainerPortalTexture(player, (TilePortalController) tile);
        }
        else if (ID == GuiIds.NETWORK_INTERFACE && tile instanceof TileNetworkInterface)
        {
            PacketDispatcher.sendPacketToPlayer(MainPacket.makePacket(new PacketNetworkInterfaceData((TileNetworkInterface) tile)), (Player) player);
            return new ContainerNetworkInterface((TileNetworkInterface) tile);
        }
        else if (ID == GuiIds.MODULE_MANIPULATOR && tile instanceof TileModuleManipulator)
        {
            return new ContainerModuleManipulator(player, (TileModuleManipulator) tile);
        }

        return null;
    }
}
