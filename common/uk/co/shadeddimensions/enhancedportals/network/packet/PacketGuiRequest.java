package uk.co.shadeddimensions.enhancedportals.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.network.INetworkManager;
import uk.co.shadeddimensions.enhancedportals.EnhancedPortals;
import uk.co.shadeddimensions.enhancedportals.container.ContainerPortalFrameController;
import uk.co.shadeddimensions.enhancedportals.network.CommonProxy;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameController;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class PacketGuiRequest extends MainPacket
{
    int id;

    public PacketGuiRequest()
    {

    }

    public PacketGuiRequest(int guiID)
    {
        id = guiID;
    }

    @Override
    public MainPacket consumePacket(DataInputStream stream) throws IOException
    {
        id = stream.readInt();

        return this;
    }

    @Override
    public void execute(INetworkManager network, EntityPlayer player)
    {
        boolean validRequest = false, sendPacket = player instanceof EntityPlayerMP;
        Container c = player.openContainer;

        if (c instanceof ContainerPortalFrameController)
        {
            TilePortalFrameController controller = ((ContainerPortalFrameController) c).controller;

            if (id == CommonProxy.GuiIds.PORTAL_CONTROLLER_PORTAL_TEXTURE || id == CommonProxy.GuiIds.PORTAL_CONTROLLER_FRAME_TEXTURE || id == CommonProxy.GuiIds.PORTAL_CONTROLLER_NETWORK)
            {
                player.openGui(EnhancedPortals.instance, id, controller.worldObj, controller.xCoord, controller.yCoord, controller.zCoord);
                validRequest = true;
            }
        }

        if (validRequest && sendPacket)
        {
            PacketDispatcher.sendPacketToPlayer(MainPacket.makePacket(this), (Player) player);
        }
    }

    @Override
    public void generatePacket(DataOutputStream stream) throws IOException
    {
        stream.writeInt(id);
    }
}
