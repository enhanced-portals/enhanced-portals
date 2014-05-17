package enhancedportals.script;

import org.luaj.vm3.LuaNumber;
import org.luaj.vm3.LuaValue;

import enhancedportals.tileentity.portal.TileController;

public class Portal
{
    TileController controller;
    
    public Portal(TileController c)
    {
        controller = c;
    }
    
    public LuaNumber getInstabilityAmount()
    {
        return LuaValue.valueOf(controller.instability);
    }
    
    public void terminate()
    {
        controller.connectionTerminate();
    }
}
