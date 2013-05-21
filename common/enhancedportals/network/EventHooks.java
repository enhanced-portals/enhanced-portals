package enhancedportals.network;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;
import enhancedportals.EnhancedPortals;

public class EventHooks
{
    @ForgeSubscribe
    public void worldSave(WorldEvent.Save event)
    {
        if (!event.world.isRemote)
        {
            EnhancedPortals.proxy.ModifierNetwork.saveData();
        }
    }
    
    @SideOnly(Side.CLIENT)
    @ForgeSubscribe
    public void registerIcons(TextureStitchEvent.Pre event)
    {        
        EnhancedPortals.proxy.registerIcons(event);
    }
}
