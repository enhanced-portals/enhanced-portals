package enhancedportals.computercraft.tileentity;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IPeripheral;
import enhancedcore.computercraft.ComputerManager;
import enhancedcore.computercraft.ComputerManager.IMethod;
import enhancedportals.computercraft.SharedMethods;
import enhancedportals.network.packet.PacketDialDeviceUpdate;
import enhancedportals.network.packet.PacketDialRequest;
import enhancedportals.network.packet.PacketEnhancedPortals;
import enhancedportals.tileentity.TileEntityDialDevice;

public class TileEntityDialDevice_cc extends TileEntityDialDevice implements IPeripheral
{
    ComputerManager computerManager;
    
    public TileEntityDialDevice_cc()
    {
        super();
        
        computerManager = new ComputerManager();
        addMethods();
    }

    private void addMethods()
    {        
        // dial
        computerManager.registerMethod(new IMethod() {
            @Override
            public Object[] execute(IComputerAccess computer, Object[] arguments) throws Exception
            {
                if (active)
                {
                    throw new Exception("Cannot dial when there's already an active connection.");
                }
                
                if (arguments.length != 1 || !(arguments[0] instanceof Double))
                {
                    throw new Exception("Invalid arguments");
                }
                
                int dialNum = (int) Math.round(Double.parseDouble(arguments[0].toString()));
                
                if (destinationList.size() < dialNum || dialNum < 0)
                {
                    throw new Exception("Invalid destination ID");
                }
                
                if (FMLCommonHandler.instance().getSide() == Side.CLIENT && FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
                {
                    PacketDispatcher.sendPacketToServer(PacketEnhancedPortals.makePacket(new PacketDialRequest((TileEntityDialDevice) worldObj.getBlockTileEntity(xCoord, yCoord, zCoord), dialNum + "")));
                }
                else
                {
                    ((TileEntityDialDevice) worldObj.getBlockTileEntity(xCoord, yCoord, zCoord)).processDiallingRequest(dialNum, null);
                }
                
                return null;
            }

            @Override
            public String getMethodName()
            {
                return "dialStored";
            }
        });
        
        // terminate
        computerManager.registerMethod(new IMethod() {
            @Override
            public Object[] execute(IComputerAccess computer, Object[] arguments) throws Exception
            {
                if (!active)
                {
                    throw new Exception("Cannot terminate an inactive connection");
                }
                
                if (FMLCommonHandler.instance().getSide() == Side.CLIENT && FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
                {
                    PacketDispatcher.sendPacketToServer(PacketEnhancedPortals.makePacket(new PacketDialRequest((TileEntityDialDevice) worldObj.getBlockTileEntity(xCoord, yCoord, zCoord), "")));
                }
                else
                {
                    ((TileEntityDialDevice) worldObj.getBlockTileEntity(xCoord, yCoord, zCoord)).processDiallingRequest(0, null);
                }
                
                return null;
            }

            @Override
            public String getMethodName()
            {
                return "terminate";
            }
        });
        
        // setTimeoutTime
        computerManager.registerMethod(new IMethod() {
            @Override
            public Object[] execute(IComputerAccess computer, Object[] arguments) throws Exception
            {                
                if (arguments.length != 1 || !(arguments[0] instanceof Double))
                {
                    throw new Exception("Invalid arguments");
                }
                
                int num = (int) Math.round(Double.parseDouble(arguments[0].toString()));
                
                if (num < 19 || num > 1201)
                {
                    throw new Exception("Must be between 19 and 1201.");
                }
                
                if (FMLCommonHandler.instance().getSide() == Side.CLIENT && FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
                {
                    ((TileEntityDialDevice) worldObj.getBlockTileEntity(xCoord, yCoord, zCoord)).tickTimer = num;
                    PacketDispatcher.sendPacketToServer(PacketEnhancedPortals.makePacket(new PacketDialDeviceUpdate((TileEntityDialDevice) worldObj.getBlockTileEntity(xCoord, yCoord, zCoord))));
                }
                else
                {
                    ((TileEntityDialDevice) worldObj.getBlockTileEntity(xCoord, yCoord, zCoord)).tickTimer = num;
                }
                
                return null;
            }

            @Override
            public String getMethodName()
            {
                return "setTimeoutTime";
            }
        });
                
        // isActive
        computerManager.registerMethod(new IMethod() {
            @Override
            public Object[] execute(IComputerAccess computer, Object[] arguments) throws Exception
            {                
                return new Object[] { active };
            }

            @Override
            public String getMethodName()
            {
                return "isActive";
            }        
        });
        
        // getGlyphs
        computerManager.registerMethod(SharedMethods.getGlyphs);
        
        // getGlyph
        computerManager.registerMethod(SharedMethods.getGlyph);
    }

    @Override
    public String getType()
    {
        return "dialDevice";
    }

    @Override
    public String[] getMethodNames()
    {
        return computerManager.getAllMethodNames();
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
    public void attach(IComputerAccess computer)
    {
        computerManager.addComputer(computer);
    }

    @Override
    public void detach(IComputerAccess computer)
    {
        computerManager.removeComputer(computer);
    }
}
