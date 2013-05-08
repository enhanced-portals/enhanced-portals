package enhancedportals.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import enhancedportals.container.ContainerPortalModifier;
import enhancedportals.lib.GuiIds;
import enhancedportals.network.packet.PacketTEUpdate;
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
        else if (ID == GuiIds.DialDevice)
        {

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
        else if (ID == GuiIds.DialDevice)
        {

        }

        return null;
    }
}
