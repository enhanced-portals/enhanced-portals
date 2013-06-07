package enhancedportals.world;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import enhancedportals.lib.BlockIds;
import enhancedportals.tileentity.TileEntityDialDeviceBasic;

public class DialDeviceChunkCallback implements ForgeChunkManager.OrderedLoadingCallback
{
    @Override
    public void ticketsLoaded(List<Ticket> tickets, World world)
    {
        for (Ticket ticket : tickets)
        {
            int x, y, z;

            if (ticket.getModData().hasKey("basicDialX"))
            {
                x = ticket.getModData().getInteger("basicDialX");
                y = ticket.getModData().getInteger("basicDialY");
                z = ticket.getModData().getInteger("basicDialZ");

                if (world.getBlockId(x, y, z) == BlockIds.DialHomeDeviceBasic)
                {
                    TileEntityDialDeviceBasic dialDevice = (TileEntityDialDeviceBasic) world.getBlockTileEntity(x, y, z);

                    if (dialDevice != null)
                    {
                        dialDevice.loadChunk(ticket);
                    }
                }
            }
            else
            {
                x = ticket.getModData().getInteger("dialX");
                y = ticket.getModData().getInteger("dialY");
                z = ticket.getModData().getInteger("dialZ");

                if (world.getBlockId(x, y, z) == BlockIds.DialHomeDevice)
                {

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
            int x, y, z;

            if (ticket.getModData().hasKey("basicDialX"))
            {
                x = ticket.getModData().getInteger("basicDialX");
                y = ticket.getModData().getInteger("basicDialY");
                z = ticket.getModData().getInteger("basicDialZ");

                if (world.getBlockId(x, y, z) == BlockIds.DialHomeDeviceBasic)
                {
                    validTickets.add(ticket);
                }
            }
            else
            {
                x = ticket.getModData().getInteger("dialX");
                y = ticket.getModData().getInteger("dialY");
                z = ticket.getModData().getInteger("dialZ");

                if (world.getBlockId(x, y, z) == BlockIds.DialHomeDevice)
                {
                    validTickets.add(ticket);
                }
            }
        }

        return validTickets;
    }
}
