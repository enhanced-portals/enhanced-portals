package enhancedportals.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import enhancedportals.EnhancedPortals;

public class LogOnHandler
{
	@SubscribeEvent
	public void onLogIn(PlayerEvent.PlayerLoggedInEvent login)
	{
		String curVers = EnhancedPortals.VERS;
		String newVers = null;
		
		if (login.player != null)
		{
			try
			{
				URL versionIn = new URL(EnhancedPortals.UPDATE_URL);
				BufferedReader in = new BufferedReader(new InputStreamReader(versionIn.openStream()));
				newVers = in.readLine();
				
				if (!newVers.equals(curVers))
				{
					EntityPlayer player = login.player;
					String lateVers = CommonProxy.lateVers;
					CommonProxy.Notify(player, lateVers);
				}
				else
				{
	    			EnhancedPortals.logger.info("You're using the latest version :: v" + newVers);
				}
			}
			catch (Exception e) 
	        {
	        	EnhancedPortals.logger.warn("Unable to get the latest version information");
	            newVers = curVers;
	        } 
	        finally {}
		}
		else
		{
			EnhancedPortals.logger.warn("Something is wrong :: Player is NULL");
		}
	}
}
