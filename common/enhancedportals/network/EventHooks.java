package enhancedportals.network;

import enhancedportals.EnhancedPortals;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;

public class EventHooks
{
    @ForgeSubscribe
    public void worldSave(WorldEvent.Save event)
    {
        if (!event.world.isRemote)
        {
            EnhancedPortals.proxy.ModifierNetwork.saveData();
        }
    }
}
