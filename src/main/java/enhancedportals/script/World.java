package enhancedportals.script;

import org.luaj.vm3.LuaBoolean;
import org.luaj.vm3.LuaNumber;
import org.luaj.vm3.LuaValue;

public class World
{
    net.minecraft.world.World world;

    public World(net.minecraft.world.World w)
    {
        world = w;
    }

    public LuaNumber getTime()
    {
        return LuaValue.valueOf(world.getWorldTime());
    }

    public LuaBoolean isDaytime()
    {
        return LuaValue.valueOf(world.isDaytime());
    }

    public LuaBoolean isRaining()
    {
        return LuaValue.valueOf(world.isRaining());
    }

    public LuaBoolean isThundering()
    {
        return LuaValue.valueOf(world.isThundering());
    }
}
