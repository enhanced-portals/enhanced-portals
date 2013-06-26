package enhancedportals.world;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import enhancedportals.lib.BlockIds;
import enhancedportals.tileentity.TileEntityDialDevice;
import enhancedportals.tileentity.TileEntityDialDeviceBasic;

public class DialDeviceChunkCallback implements ForgeChunkManager.OrderedLoadingCallback
{
    @Override
    public void ticketsLoaded(List<Ticket> tickets, World world)
    {
        for (Ticket ticket : tickets)
        {
            int x = ticket.getModData().getInteger("dialX"), y = ticket.getModData().getInteger("dialY"), z = ticket.getModData().getInteger("dialZ");

            if (world.getBlockId(x, y, z) == BlockIds.DialDevice)
            {
                TileEntityDialDevice dialDevice = (TileEntityDialDevice) world.getBlockTileEntity(x, y, z);

                if (dialDevice != null)
                {
                    dialDevice.loadChunk(ticket);
                }
            }
            else if (world.getBlockId(x, y, z) == BlockIds.DialDeviceBasic)
            {
                TileEntityDialDeviceBasic dialDevice = (TileEntityDialDeviceBasic) world.getBlockTileEntity(x, y, z);

                if (dialDevice != null)
                {
                    dialDevice.loadChunk(ticket);
                }
            }
        }
    }

    @Override
    public List<Ticket> ticketsLoaded(List<Ticket> tickets, World world, int maxTicketCount)
    {
        List<Ticket> validTickets = new ArrayList<Ticket>();

        for (Ticket ticket : tickets)
        {
            int x = ticket.getModData().getInteger("dialX"), y = ticket.getModData().getInteger("dialY"), z = ticket.getModData().getInteger("dialZ");

            if (world.getBlockId(x, y, z) == BlockIds.DialDevice)
            {
                validTickets.add(ticket);
            }
            else if (world.getBlockId(x, y, z) == BlockIds.DialDeviceBasic)
            {
                validTickets.add(ticket);
            }
        }

        return validTickets;
    }
}
