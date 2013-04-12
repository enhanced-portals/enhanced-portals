package com.alz.enhancedportals.computercraft.tileentity;

import java.util.ArrayList;
import java.util.List;

import com.alz.enhancedportals.core.tileentity.TileEntityDialHomeDevice;

import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IPeripheral;

public class TileEntityDialHomeDeviceCC extends TileEntityDialHomeDevice implements IPeripheral
{
    List<IComputerAccess> computers = new ArrayList<IComputerAccess>();

    @Override
    public void attach(IComputerAccess computer)
    {
        computers.add(computer);
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
    public void detach(IComputerAccess computer)
    {
        computers.remove(computer);
    }

    @Override
    public String[] getMethodNames()
    {
        return new String[] { "activate", "deactivate", "selectEntry", "addEntry", "removeEntry" };
    }

    @Override
    public String getType()
    {
        return "dialHomeDevice";
    }
}
