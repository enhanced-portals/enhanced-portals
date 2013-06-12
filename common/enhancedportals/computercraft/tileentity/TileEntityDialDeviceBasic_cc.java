package enhancedportals.computercraft.tileentity;

import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IPeripheral;
import enhancedcore.computercraft.ComputerManager;
import enhancedportals.tileentity.TileEntityDialDeviceBasic;

public class TileEntityDialDeviceBasic_cc extends TileEntityDialDeviceBasic implements IPeripheral
{
    ComputerManager computerManager;

    public TileEntityDialDeviceBasic_cc()
    {
        super();

        computerManager = new ComputerManager();
        addMethods();
    }

    private void addMethods()
    {

    }

    @Override
    public void attach(IComputerAccess computer)
    {
        computerManager.addComputer(computer);
    }

    @Override
    public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception
    {
        return computerManager.execute(computer, method, arguments);
    }

    @Override
    public boolean canAttachToSide(int side)
    {
        return true;
    }

    @Override
    public void detach(IComputerAccess computer)
    {
        computerManager.removeComputer(computer);
    }

    @Override
    public String[] getMethodNames()
    {
        return computerManager.getAllMethodNames();
    }

    @Override
    public String getType()
    {
        return "automaticDialler";
    }
}
