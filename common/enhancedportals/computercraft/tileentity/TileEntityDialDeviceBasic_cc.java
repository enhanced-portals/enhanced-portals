package enhancedportals.computercraft.tileentity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.item.ItemStack;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IPeripheral;
import enhancedportals.lib.Reference;
import enhancedportals.tileentity.TileEntityDialDeviceBasic;

public class TileEntityDialDeviceBasic_cc extends TileEntityDialDeviceBasic implements IPeripheral
{
    List<IComputerAccess> computerList;
    
    public TileEntityDialDeviceBasic_cc()
    {
        super();
        
        computerList = new ArrayList<IComputerAccess>();
    }

    @Override
    public String getType()
    {
        return "automaticDialler";
    }

    @Override
    public String[] getMethodNames()
    {
        return new String[] { "dial", "terminate", "getGlyphs" };
    }

    @Override
    public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception
    {
        if (method == 0 && arguments.length != 1)
        {
            throw new Exception("Invalid number of arguments. Expecting 1");
        }
        else if ((method == 1 || method == 2) && arguments.length != 0)
        {
            throw new Exception("Invalid number of arguments. Expecting 0");
        }
        
        if (method == 0) // dial
        {
            
        }
        else if (method == 1) // terminate
        {
            
        }
        else if (method == 2) // getGlyphs
        {
            HashMap<String, Integer> map = new HashMap<String, Integer>();
            int counter = 0;
            
            for (ItemStack i : Reference.glyphItems)
            {
                map.put(i.getDisplayName(), counter);
                counter++;
            }
            
            return new Object[] { map };
        }
        
        return null;
    }

    @Override
    public boolean canAttachToSide(int side)
    {
        return true;
    }

    @Override
    public void attach(IComputerAccess computer)
    {
        computerList.add(computer);
    }

    @Override
    public void detach(IComputerAccess computer)
    {
        computerList.remove(computer);
    }
}
