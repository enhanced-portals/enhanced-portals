package alz.mods.enhancedportals.common;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class ServerCommands extends CommandBase
{
	@Override
	public String getCommandName()
	{
		return "ep";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args)
	{
		if (args.length > 0)
		{			
			if (args[0].contains("list"))
			{				
				if (args[1].contains("frequencies"))
				{
					sender.sendChatToPlayer(String.format("Found %s frequencies:", Reference.LinkData.LinkData.size()));
					
					for (int item : Reference.LinkData.LinkData.keySet())
					{
						sender.sendChatToPlayer("  " + item + " : " + Reference.LinkData.LinkData.get(item).size());
					}
				}
				else if (args[1].contains("infrequency"))
				{					
					if (args.length >= 2 && args[2] != "" && args[2] != null)
					{
						int freq = Integer.parseInt(args[2]);
						
						if (Reference.LinkData.GetFrequency(freq) == null || Reference.LinkData.GetFrequency(freq).isEmpty())
						{
							sender.sendChatToPlayer("Invalid frequency.");
							return;
						}
						
						for (int[] item : Reference.LinkData.GetFrequency(freq))
						{
							sender.sendChatToPlayer(String.format("%s, %s, %s : %s", item[0], item[1], item[2], item[3]));
						}
					}
				}
			}
		}

		//for (String str : args)
		//{
		//	sender.sendChatToPlayer(str);
		//}
	}
}
