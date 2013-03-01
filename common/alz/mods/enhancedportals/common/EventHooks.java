package alz.mods.enhancedportals.common;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;

public class EventHooks
{
	@ForgeSubscribe
	public void worldLoad(WorldEvent.Load event)
	{
		if (!event.world.isRemote)
			//System.out.println("Server world? " + FMLCommonHandler.instance().getSide());
		
		//if (FMLCommonHandler.instance().getSide() == Side.SERVER)
			Reference.LinkData = new LinkData(FMLCommonHandler.instance().getMinecraftServerInstance().getServer());
	}
	
	@ForgeSubscribe
	public void worldSave(WorldEvent.Save event)
	{
		if (!event.world.isRemote)
		//if (FMLCommonHandler.instance().getSide() == Side.SERVER)
			Reference.LinkData.saveWorldData();
	}
}
