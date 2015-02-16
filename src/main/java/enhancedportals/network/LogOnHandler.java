package enhancedportals.network;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import enhancedportals.EnhancedPortals;

public class LogOnHandler
{
    @SubscribeEvent
    public void onLogIn(PlayerEvent.PlayerLoggedInEvent login)
    {
        if (login.player != null && !CommonProxy.lateVers.equals(EnhancedPortals.VERSION))
        {
            EntityPlayer player = login.player;
            String lateVers = CommonProxy.lateVers;
            CommonProxy.Notify(player, lateVers);
        }
    }
}
