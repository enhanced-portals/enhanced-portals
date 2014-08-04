package enhancedportals.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import enhancedportals.EnhancedPortals;

public class onLogOn
{
	@SubscribeEvent
	public void onLogIn(PlayerEvent.PlayerLoggedInEvent login)
	{
		String curVers = EnhancedPortals.VERS;
		
		if (login.player != null)
		{
			try
			{
				URL versionIn = new URL("https://raw.githubusercontent.com/SkyNetAB/DaBells/master/vers");
				BufferedReader in = new BufferedReader(new InputStreamReader(versionIn.openStream()));
				String newVers = in.readLine();
				
				if (!newVers.equals(curVers))
				{
					EntityPlayer player = login.player;
					String lateVers = CommonProxy.lateVers;
					CommonProxy.Notify(player, lateVers);
				}
				else
				{
	    			System.out.println("DaBells is up-to-date :: v" + newVers);
				}
			}
			catch (Exception e) 
	        {
	        	System.out.println("Unable to find latest version of EP");
	            throw new RuntimeException(e);
	        } 
	        finally {}
		}
		else
		{
			System.out.println("Something is wrong::Player is NULL");
		}
	}
}
