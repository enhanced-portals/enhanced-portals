package alz.mods.enhancedportals.common;

import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;
import alz.mods.enhancedportals.reference.Reference;
import alz.mods.enhancedportals.server.LinkData;
import cpw.mods.fml.common.FMLCommonHandler;

public class EventHooks
{
	@ForgeSubscribe
	public void worldLoad(WorldEvent.Load event)
	{
		if (!event.world.isRemote)
		{
			Reference.LinkData = new LinkData(FMLCommonHandler.instance().getMinecraftServerInstance());
		}
	}

	@ForgeSubscribe
	public void worldSave(WorldEvent.Save event)
	{
		if (!event.world.isRemote)
		{
			Reference.LinkData.saveWorldData();
		}
	}
}
