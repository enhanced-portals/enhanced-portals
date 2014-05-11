package enhancedportals.script;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class ScriptCommon
{  
    public static DamageSource damageSource = new DamageSource("programmableInterface").setDamageBypassesArmor();
    public static MinecraftServer server;
    
    public static String stripHarmfulThings(String s)
    {
        String stripped = s;
        
        // TODO
        
        return stripped;
    }
    
    public static String getFullProgram(String s, World world)
    {
        return "importClass(Packages.enhancedportals.script.Entity); importClass(Packages.enhancedportals.script.World); var world=" + world.provider.dimensionId + "; " + stripHarmfulThings(s);
    }
    
    public static World getWorld(int worldID) throws ScriptException
    {
        World w = DimensionManager.getWorld(worldID);
        
        if (w == null)
        {
            throw new ScriptException("Could not load world");
        }
        
        return w;
    }
}
