package uk.co.shadeddimensions.enhancedportals.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import uk.co.shadeddimensions.enhancedportals.client.gui.GuiPortalController;
import uk.co.shadeddimensions.enhancedportals.container.ContainerPortalController;
import uk.co.shadeddimensions.enhancedportals.lib.Identifiers;
import uk.co.shadeddimensions.enhancedportals.network.packet.MainPacket;
import uk.co.shadeddimensions.enhancedportals.network.packet.PacketPortalFrameControllerData;
import uk.co.shadeddimensions.enhancedportals.tileentity.TilePortalFrameController;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class GuiHandler implements IGuiHandler
{
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (tile instanceof TilePortalFrameController && tile instanceof TilePortalFrameController)
        {
            PacketDispatcher.sendPacketToPlayer(MainPacket.makePacket(new PacketPortalFrameControllerData((TilePortalFrameController) tile)), (Player) player);
            return new ContainerPortalController(player.inventory, (TilePortalFrameController) tile);
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        if (ID == Identifiers.Gui.FRAME_CONTROLLER && tile instanceof TilePortalFrameController)
        {
            return new GuiPortalController(player, (TilePortalFrameController) tile);
        }

        return null;
    }
}
