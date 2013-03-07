package alz.mods.enhancedportals.reference;

import java.util.logging.Level;

public class Logger
{
	public static java.util.logging.Logger Logger;
	
	public static void LogData(String data)
	{
		LogData(data, false);
	}
	
	public static void LogData(String data, boolean isRemote)
	{
		if (Settings.PrintPortalMessages && !isRemote)
		Logger.log(Level.INFO, data);
	}
}
