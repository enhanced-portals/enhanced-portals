package enhancedportals.network;

import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import enhancedportals.EnhancedPortals;

public class EventHooks
{
    @SideOnly(Side.CLIENT)
    @ForgeSubscribe
    public void registerIcons(TextureStitchEvent.Pre event)
    {
        EnhancedPortals.proxy.registerIcons(event);
    }

    @ForgeSubscribe
    public void worldSave(WorldEvent.Save event)
    {
        if (!event.world.isRemote)
        {
            EnhancedPortals.proxy.ModifierNetwork.saveData();
        }
    }
}
