package alz.mods.enhancedportals.common;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;

public class EventHooks
{
	@ForgeSubscribe
	public void worldLoad(WorldEvent.Load event)
	{
		if (!event.world.isRemote)
			Reference.LinkData = new LinkData(FMLCommonHandler.instance().getMinecraftServerInstance().getServer());
	}
	
	@ForgeSubscribe
	public void worldSave(WorldEvent.Save event)
	{
		if (!event.world.isRemote)
			Reference.LinkData.saveWorldData();
	}
}
