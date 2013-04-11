package com.alz.enhancedportals.computercraft.tileentity;

import java.util.ArrayList;
import java.util.List;

import com.alz.enhancedportals.core.tileentity.TileEntityPortalModifier;

import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IPeripheral;

public class TileEntityPortalModifierCC extends TileEntityPortalModifier implements IPeripheral
{
    List<IComputerAccess> computers = new ArrayList<IComputerAccess>();
    
    @Override
    public String getType()
    {
        return "portalModifier";
    }

    @Override
    public String[] getMethodNames()
    {
        return new String[] { "activate", "deactivate", "getNetwork", "setNetwork", "getTexture", "setTexture" };
    }

    @Override
    public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception
    {
        return null;
    }

    @Override
    public boolean canAttachToSide(int side)
    {
        // only if has upgrade
        
        return true;
    }

    @Override
    public void attach(IComputerAccess computer)
    {
        computers.add(computer);
    }

    @Override
    public void detach(IComputerAccess computer)
    {
        computers.remove(computer);
    }
}
