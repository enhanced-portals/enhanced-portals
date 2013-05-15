package enhancedportals.network;

import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;
import enhancedportals.EnhancedPortals;

public class EventHooks
{
    @ForgeSubscribe
    public void worldSave(WorldEvent.Save event)
    {
        if (!event.world.isRemote)
        {
            EnhancedPortals.proxy.ModifierNetwork.saveData();
            EnhancedPortals.proxy.DialDeviceNetwork.saveData();
        }
    }
}
