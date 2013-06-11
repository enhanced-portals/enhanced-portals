package enhancedportals.computercraft.tileentity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IPeripheral;
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
            queueEvent("createPortal", null);
            return true;
        }

        return false;
    }

    @Override
    public boolean createPortal(ItemStack stack)
    {
        if (super.createPortal(stack))
        {
            queueEvent("createPortal", new Object[] { stack.getItem().itemID });
            return true;
        }

        return false;
    }

    @Override
    public boolean createPortalFromDialDevice()
    {
        if (super.createPortalFromDialDevice())
        {
            queueEvent("createPortal", new Object[] { "basicDialDevice" });
            return true;
        }

        return false;
    }

    @Override
    public boolean createPortalFromDialDevice(String texture, byte thickness)
    {
        if (super.createPortalFromDialDevice(texture, thickness))
        {
            queueEvent("createPortal", new Object[] { "dialDevice", texture, thickness });
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
        return new String[] { "" };
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
    public void removePortal()
    {
        super.removePortal();

        queueEvent("removePortal", null);
    }
}
