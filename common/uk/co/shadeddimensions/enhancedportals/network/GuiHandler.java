package uk.co.shadeddimensions.enhancedportals.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import uk.co.shadeddimensions.enhancedportals.container.ContainerNetworkInterface;
import uk.co.shadeddimensions.enhancedportals.container.ContainerPortalFrameController;
import uk.co.shadeddimensions.enhancedportals.container.ContainerPortalFrameRedstone;
import uk.co.shadeddimensions.enhancedportals.container.ContainerPortalFrameTexture;
import uk.co.shadeddimensions.enhancedportals.container.ContainerPortalTexture;
import uk.co.shadeddimensions.enhancedportals.gui.GuiPortalFrameNetworkInterface;
import uk.co.shadeddimensions.enhancedportals.gui.GuiPortalFrameController;
import uk.co.shadeddimensions.enhancedportals.gui.GuiPortalFrameRedstone;
import uk.co.shadeddimensions.enhancedportals.gui.GuiPortalFrameTexture;
import uk.co.shadeddimensions.enhancedportals.gui.GuiPortalTexture;
import uk.co.shadeddimensions.enhancedportals.lib.GuiIds;
import uk.co.shadeddimensions.enhancedportals.network.packet.MainPacket;
import uk.co.shadeddimensions.enhancedportals.network.packet.PacketNetworkInterfaceData;
import uk.co.shadeddimensions.enhancedportals.network.packet.PacketPortalFrameControllerData;
import uk.co.shadeddimensions.enhancedportals.network.packet.PacketPortalFrameRedstoneData;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortal;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrame;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameController;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameNetworkInterface;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameRedstone;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class GuiHandler implements IGuiHandler
{
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (ID == GuiIds.PORTAL_CONTROLLER && tile instanceof TilePortalFrameController)
        {
            PacketDispatcher.sendPacketToPlayer(MainPacket.makePacket(new PacketPortalFrameControllerData((TilePortalFrameController) tile)), (Player) player);
            return new ContainerPortalFrameController((TilePortalFrameController) tile);
        }
        else if (ID == GuiIds.PORTAL_REDSTONE && tile instanceof TilePortalFrameRedstone)
        {
            PacketDispatcher.sendPacketToPlayer(MainPacket.makePacket(new PacketPortalFrameRedstoneData((TilePortalFrameRedstone) tile)), (Player) player);
            return new ContainerPortalFrameRedstone((TilePortalFrameRedstone) tile);
        }
        else if (ID == GuiIds.PORTAL_FRAME_TEXTURE && tile instanceof TilePortalFrame)
        {
            TilePortalFrameController c = null;

            if (tile instanceof TilePortalFrameController)
            {
                c = (TilePortalFrameController) tile;
            }
            else
            {
                c = ((TilePortalFrame) tile).getControllerValidated();
            }

            return c == null ? null : new ContainerPortalFrameTexture(player, c);
        }
        else if (ID == GuiIds.PORTAL_TEXTURE && tile instanceof TilePortal)
        {
            TilePortalFrameController c = ((TilePortal) tile).getControllerValidated();

            return c == null ? null : new ContainerPortalTexture(player, c);
        }
        else if (ID == GuiIds.NETWORK_INTERFACE && tile instanceof TilePortalFrameNetworkInterface)
        {
            if (((TilePortalFrame) tile).getControllerValidated() == null)
            {
                return null;
            }
            else if (((TilePortalFrame) tile).getControllerValidated().UniqueIdentifier.equals(""))
            {
                player.sendChatToPlayer(ChatMessageComponent.createFromText(EnumChatFormatting.RED + StatCollector.translateToLocal("chat.ep2.uniqueIdentifierNotSet")));
                return null;
            }
            
            PacketDispatcher.sendPacketToPlayer(MainPacket.makePacket(new PacketNetworkInterfaceData((TilePortalFrameNetworkInterface) tile)), (Player) player);
            return new ContainerNetworkInterface((TilePortalFrameNetworkInterface) tile);
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (ID == GuiIds.PORTAL_CONTROLLER && tile instanceof TilePortalFrameController)
        {
            return new GuiPortalFrameController(player, (TilePortalFrameController) tile);
        }
        else if (ID == GuiIds.PORTAL_REDSTONE && tile instanceof TilePortalFrameRedstone)
        {
            return new GuiPortalFrameRedstone(player, (TilePortalFrameRedstone) tile);
        }
        else if (ID == GuiIds.PORTAL_FRAME_TEXTURE && tile instanceof TilePortalFrame)
        {
            TilePortalFrameController c = null;

            if (tile instanceof TilePortalFrameController)
            {
                c = (TilePortalFrameController) tile;
            }
            else
            {
                c = ((TilePortalFrame) tile).getControllerValidated();
            }

            return c == null ? null : new GuiPortalFrameTexture(player, c);
        }
        else if (ID == GuiIds.PORTAL_TEXTURE && tile instanceof TilePortal)
        {
            TilePortalFrameController c = ((TilePortal) tile).getControllerValidated();

            return c == null ? null : new GuiPortalTexture(player, c);
        }
        else if (ID == GuiIds.NETWORK_INTERFACE && tile instanceof TilePortalFrameNetworkInterface)
        {
            if (((TilePortalFrame) tile).getControllerValidated() == null)
            {
                return null;
            }
            else if (((TilePortalFrame) tile).getControllerValidated().UniqueIdentifier.equals(""))
            {
                return null;
            }
            
            return new GuiPortalFrameNetworkInterface((TilePortalFrameNetworkInterface) tile);
        }

        return null;
    }
}
