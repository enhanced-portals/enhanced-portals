package alz.mods.enhancedportals.common;

import net.minecraft.command.ServerCommandManager;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.server.FMLServerHandler;

public class CommonProxy
{
	public void setupRenderers() {}
	
	public void setupServerCommands()
	{
		((ServerCommandManager)FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager()).registerCommand(new ServerCommands());
	}
}
