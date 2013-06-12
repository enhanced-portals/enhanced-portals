package enhancedportals.computercraft;

import java.util.HashMap;

import net.minecraft.item.ItemStack;
import dan200.computer.api.IComputerAccess;
import enhancedcore.computercraft.ComputerManager.IMethod;
import enhancedportals.lib.Reference;

public class SharedMethods
{
    public static IMethod getGlyphs = new IMethod()
    {
            @Override
            public String getMethodName()
            {
                return "getGlyphs";
            }
    
            @Override
            public Object[] execute(IComputerAccess computer, Object[] arguments) throws Exception
            {
                HashMap<Integer, String> map = new HashMap<Integer, String>();
                int counter = 0;
                
                for (ItemStack i : Reference.glyphItems)
                {
                    map.put(counter, i.getDisplayName());
                    counter++;
                }
                                
                return new Object[] { map };
            }
        };
        
        public static IMethod getGlyph = new IMethod()
        {
            @Override
            public String getMethodName()
            {
                return "getGlyph";
            }

            @Override
            public Object[] execute(IComputerAccess computer, Object[] arguments) throws Exception
            {
                if (arguments.length != 1 || !(arguments[0] instanceof Double))
                {
                    throw new Exception("Invalid arguments");
                }
                
                int num = (int) Math.round(Double.parseDouble(arguments[0].toString()));
                
                if (num < 0 || num >= Reference.glyphItems.size())
                {
                    throw new Exception("Invalid arguments");
                }
                
                return new Object[] { Reference.glyphItems.get(num).getDisplayName() };
            }
        };
}
