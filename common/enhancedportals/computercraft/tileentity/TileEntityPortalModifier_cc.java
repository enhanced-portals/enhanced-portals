package enhancedportals.computercraft.tileentity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.item.ItemStack;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IPeripheral;
import enhancedportals.lib.Reference;
import enhancedportals.tileentity.TileEntityPortalModifier;

public class TileEntityPortalModifier_cc extends TileEntityPortalModifier implements IPeripheral
{
    List<IComputerAccess> computerList;

    public TileEntityPortalModifier_cc()
    {
        super();

        computerList = new ArrayList<IComputerAccess>();
    }

    @Override
    public void attach(IComputerAccess computer)
    {
        computerList.add(computer);
    }

    @Override
    public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception
    {
        if ((method == 0 || method == 1 || method == 2 || method == 4 || method == 6 || method == 8 || method == 10 || method == 11 || method == 12) && arguments.length != 0)
        {
            throw new Exception("Invalid number of arguments. Expecting 0.");
        }
        else if ((method == 3 || method == 5 || method == 7 || method == 9) && arguments.length != 1)
        {
            throw new Exception("Invalid number of arguments. Expecting 1.");
        }
        
        if (method == 0) // createPortal
        {
            return new Object[] { createPortal() };
        }
        else if (method == 1) // removePortal
        {
            return new Object[] { removePortal() };
        }
        else if (method ==  2) // getTexture
        {
            return new Object[] { texture };
        }
        else if (method == 3) // setTexture
        {
            
        }
        else if (method == 4) // getNetwork
        {
            HashMap<Integer, Integer> values = new HashMap<Integer, Integer>();
            int count = 1;
            
            for (String str : modifierNetwork.split(" "))
            {
                for (int i = 0; i < Reference.glyphItems.size(); i++)
                {
                    if (str.equals(Reference.glyphItems.get(i).getItemName().replace("item.", "")))
                    {
                        values.put(count, i);
                        count++;
                        break;
                    }
                }
            }
            
            return new Object[] { values };
        }
        else if (method == 5) // setNetwork
        {
            
        }
        else if (method == 6) // getIdentifier
        {
            HashMap<Integer, Integer> values = new HashMap<Integer, Integer>();
            int count = 1;
            
            for (String str : dialDeviceNetwork.split(" "))
            {
                for (int i = 0; i < Reference.glyphItems.size(); i++)
                {
                    if (str.equals(Reference.glyphItems.get(i).getItemName().replace("item.", "")))
                    {
                        values.put(count, i);
                        count++;
                        break;
                    }
                }
            }
            
            return new Object[] { values };
        }
        else if (method == 7) // setIdentifier
        {
            
        }
        else if (method == 8) // getThickness
        {
            return new Object[] { thickness };
        }
        else if (method == 9) // setThickness
        {
            
        }
        else if (method == 10) // getUpgrades
        {
            HashMap<Integer, Integer> test = new HashMap<Integer, Integer>();
            byte[] upgrades = upgradeHandler.getInstalledUpgrades();
            
            for (int i = 0; i < upgradeHandler.getInstalledUpgrades().length; i++)
            {
                test.put(i + 1, (int) upgrades[i]);
            }
            
            return new Object[] { test };
        }
        else if (method == 11) // isSelfActive
        {
            return new Object[] { isActive() };
        }
        else if (method == 12) // isAnyActive
        {
            return new Object[] { isAnyActive() };
        }
        
        return null;
    }

    @Override
    public boolean canAttachToSide(int side)
    {
        return true;
    }

    @Override
    public boolean createPortal()
    {        
        if (super.createPortal())
        {
            queueEvent("create_portal", null);
            return true;
        }

        return false;
    }

    @Override
    public boolean createPortal(ItemStack stack)
    {
        if (super.createPortal(stack))
        {
            queueEvent("create_portal", new Object[] { stack.getItem().itemID });
            return true;
        }

        return false;
    }

    @Override
    public boolean createPortalFromDialDevice()
    {
        if (super.createPortalFromDialDevice())
        {
            queueEvent("create_portal", new Object[] { "basicDialDevice" });
            return true;
        }

        return false;
    }

    @Override
    public boolean createPortalFromDialDevice(String texture, byte thickness)
    {
        if (super.createPortalFromDialDevice(texture, thickness))
        {
            queueEvent("create_portal", new Object[] { "dialDevice", texture, thickness });
            return true;
        }

        return false;
    }

    @Override
    public void detach(IComputerAccess computer)
    {
        computerList.remove(computer);
    }

    @Override
    public String[] getMethodNames()
    {
        return new String[] { "createPortal", "removePortal", "getTexture", "setTexture", "getNetwork", "setNetwork", "getIdentifier", "setIdentifier", "getThickness", "setThickness", "getUpgrades", "isSelfActive", "isAnyActive" };
    }

    @Override
    public String getType()
    {
        return "portalModifier";
    }

    private void queueEvent(String event, Object[] arguments)
    {
        for (IComputerAccess computer : computerList)
        {
            computer.queueEvent(event, arguments);
        }
    }

    @Override
    public boolean removePortal()
    {        
        if (super.removePortal())
        {
            queueEvent("remove_portal", null);
            return true;
        }
        
        return false;
    }
}
