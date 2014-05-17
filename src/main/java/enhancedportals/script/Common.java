package enhancedportals.script;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class Common
{  
    public static DamageSource damageSource = new DamageSource("programmableInterface").setDamageBypassesArmor();

    public static String getFullProgram(String s)
    {
        String stripped = s;
        
        // TODO
        
        return stripped;
    }
}
