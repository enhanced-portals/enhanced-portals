package enhancedportals.script;

public class World
{
    public static boolean isDaytime(int worldID) throws ScriptException
    {
        return ScriptCommon.getWorld(worldID).isDaytime();
    }
    
    public static boolean isRaining(int worldID) throws ScriptException
    {
        return ScriptCommon.getWorld(worldID).isRaining();
    }
    
    public static boolean isThundering(int worldID) throws ScriptException
    {
        return ScriptCommon.getWorld(worldID).isThundering();
    }
    
    public static long getTime(int worldID) throws ScriptException
    {
        return ScriptCommon.getWorld(worldID).getWorldTime();
    }
}
