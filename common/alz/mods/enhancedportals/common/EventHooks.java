package alz.mods.enhancedportals.common;

import alz.mods.enhancedportals.reference.IO;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;

public class EventHooks
{
	@ForgeSubscribe
	public void worldLoad(WorldEvent.Load event)
	{
		if (!event.world.isRemote)
			IO.LinkData = new LinkData(FMLCommonHandler.instance().getMinecraftServerInstance());
	}
	
	@ForgeSubscribe
	public void worldSave(WorldEvent.Save event)
	{
		if (!event.world.isRemote)
			IO.LinkData.saveWorldData();
	}
}
