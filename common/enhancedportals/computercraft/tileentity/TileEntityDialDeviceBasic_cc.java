package enhancedportals.computercraft.tileentity;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.ILuaContext;
import dan200.computer.api.IPeripheral;
import enhancedcore.computercraft.ComputerManager;
import enhancedcore.computercraft.ComputerManager.IMethod;
import enhancedportals.computercraft.GlyphString;
import enhancedportals.computercraft.SharedMethods;
import enhancedportals.network.packet.PacketDialRequest;
import enhancedportals.network.packet.PacketEnhancedPortals;
import enhancedportals.tileentity.TileEntityDialDeviceBasic;
import enhancedportals.tileentity.TileEntityEnhancedPortals;

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
        // dial
        computerManager.registerMethod(new IMethod()
        {
            @Override
            public Object[] execute(IComputerAccess computer, Object[] arguments) throws Exception
            {
                if (active)
                {
                    throw new Exception("Cannot dial when there's already an active connection.");
                }

                if (arguments.length != 1 || !(arguments[0] instanceof String))
                {
                    throw new Exception("Invalid arguments");
                }

                String dialString = GlyphString.getGlyphStringFromIdString(arguments[0].toString());

                if (FMLCommonHandler.instance().getSide() == Side.CLIENT && FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
                {
                    PacketDispatcher.sendPacketToServer(PacketEnhancedPortals.makePacket(new PacketDialRequest((TileEntityEnhancedPortals) worldObj.getBlockTileEntity(xCoord, yCoord, zCoord), dialString)));
                }
                else
                {
                    ((TileEntityDialDeviceBasic) worldObj.getBlockTileEntity(xCoord, yCoord, zCoord)).processDiallingRequest(dialString, null);
                }

                return null;
            }

            @Override
            public String getMethodName()
            {
                return "dial";
            }
        });

        // isActive
        computerManager.registerMethod(new IMethod()
        {
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
    public void attach(IComputerAccess computer)
    {
        computerManager.addComputer(computer);
    }

    @Override
    public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws Exception
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
