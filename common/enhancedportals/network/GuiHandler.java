package enhancedportals.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import enhancedportals.client.gui.GuiDialDeviceBasic;
import enhancedportals.client.gui.GuiPortalModifier;
import enhancedportals.client.gui.GuiPortalModifierNetwork;
import enhancedportals.container.ContainerPortalModifier;
import enhancedportals.lib.GuiIds;
import enhancedportals.network.packet.PacketTEUpdate;
import enhancedportals.tileentity.TileEntityDialDeviceBasic;
import enhancedportals.tileentity.TileEntityPortalModifier;

public class GuiHandler implements IGuiHandler
{
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

        if (ID == GuiIds.PortalModifier)
        {
            if (tileEntity instanceof TileEntityPortalModifier)
            {
                return new GuiPortalModifier(player.inventory, (TileEntityPortalModifier) tileEntity);
            }
        }
        else if (ID == GuiIds.PortalModifierNetwork)
        {
            if (tileEntity instanceof TileEntityPortalModifier)
            {
                return new GuiPortalModifierNetwork((TileEntityPortalModifier) tileEntity);
            }
        }
        else if (ID == GuiIds.DialDevice)
        {

        }
        else if (ID == GuiIds.DialDeviceBasic)
        {
            if (tileEntity instanceof TileEntityDialDeviceBasic)
            {
                return new GuiDialDeviceBasic((TileEntityDialDeviceBasic) tileEntity);
            }
        }

        return null;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

        if (ID == GuiIds.PortalModifier)
        {
            if (tileEntity instanceof TileEntityPortalModifier)
            {
                PacketDispatcher.sendPacketToPlayer(new PacketTEUpdate((TileEntityPortalModifier) tileEntity).getPacket(), (Player) player);

                return new ContainerPortalModifier(player.inventory, (TileEntityPortalModifier) tileEntity);
            }
        }
        else if (ID == GuiIds.PortalModifierNetwork)
        {

        }
        else if (ID == GuiIds.DialDevice)
        {

        }
        else if (ID == GuiIds.DialDeviceBasic)
        {
            if (tileEntity instanceof TileEntityDialDeviceBasic)
            {
                PacketDispatcher.sendPacketToPlayer(new PacketTEUpdate((TileEntityDialDeviceBasic) tileEntity).getPacket(), (Player) player);
            }
        }

        return null;
    }
}
