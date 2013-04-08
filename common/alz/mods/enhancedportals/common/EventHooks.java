package alz.mods.enhancedportals.common;

import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;
import alz.mods.enhancedportals.reference.Reference;

public class EventHooks
{
	@ForgeSubscribe
	public void worldSave(WorldEvent.Save event)
	{
		if (!event.world.isRemote && event.world.provider.dimensionId == 0)
		{
			Reference.ServerHandler.saveAllData();
		}
	}
}
