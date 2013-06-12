package enhancedportals.computercraft.tileentity;

import java.util.HashMap;

import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IPeripheral;
import enhancedcore.computercraft.ComputerManager;
import enhancedcore.computercraft.ComputerManager.IMethod;
import enhancedportals.computercraft.SharedMethods;
import enhancedportals.lib.Reference;
import enhancedportals.network.packet.PacketCreatePortal;
import enhancedportals.network.packet.PacketEnhancedPortals;
import enhancedportals.network.packet.PacketPortalModifierUpdate;
import enhancedportals.tileentity.TileEntityPortalModifier;

public class TileEntityPortalModifier_cc extends TileEntityPortalModifier implements IPeripheral
{
    ComputerManager computerManager;

    public TileEntityPortalModifier_cc()
    {
        super();
        computerManager = new ComputerManager();
        addMethods();
    }

    private void addMethods()
    {
        // createPortal
        computerManager.registerMethod(new IMethod() {
            @Override
            public String getMethodName()
            {
                return "createPortal";
            }

            @Override
            public Object[] execute(IComputerAccess computer, Object[] arguments) throws Exception
            {
                if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
                {
                    PacketDispatcher.sendPacketToServer(PacketEnhancedPortals.makePacket(new PacketCreatePortal(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord), true)));
                }
                else
                {
                    createPortal();
                }
                
                return null;
            }
        });
        
        // removePortal
        computerManager.registerMethod(new IMethod() {
            @Override
            public String getMethodName()
            {
                return "removePortal";
            }

            @Override
            public Object[] execute(IComputerAccess computer, Object[] arguments) throws Exception
            {
                if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
                {
                    PacketDispatcher.sendPacketToServer(PacketEnhancedPortals.makePacket(new PacketCreatePortal(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord), false)));
                }
                else
                {
                    removePortal();
                }
                
                return null;
            }
        });
        
        // getTexture
        computerManager.registerMethod(new IMethod() {
            @Override
            public String getMethodName()
            {
                return "getTexture";
            }

            @Override
            public Object[] execute(IComputerAccess computer, Object[] arguments) throws Exception
            {
                return new Object[] { texture.equals("") ? "C:5" : texture };
            }
        });
        
        // getNetwork
        computerManager.registerMethod(new IMethod() {
            @Override
            public String getMethodName()
            {
                return "getNetwork";
            }

            @Override
            public Object[] execute(IComputerAccess computer, Object[] arguments) throws Exception
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
        });
        
        // setNetwork
        computerManager.registerMethod(new IMethod() {
            @Override
            public String getMethodName()
            {
                return "setNetwork";
            }

            @Override
            public Object[] execute(IComputerAccess computer, Object[] arguments) throws Exception
            {
                if (isRemotelyControlled())
                {
                    throw new Exception("You must use setIdentifier()");
                }
                
                if (arguments[0] instanceof String)
                {
                    if (arguments[0].toString().length() == 0)
                    {
                        throw new Exception("Invalid arguments");
                    }
                    
                    String[] str = arguments[0].toString().split(" ");
                    String theNetwork = "";
                    
                    if (str.length > 9)
                    {
                        throw new Exception("Too many values");
                    }
                    else if (str.length == 0)
                    {
                        throw new Exception("Invalid arguments");
                    }
                    
                    for (String s : str)
                    {
                        if (s.equals(""))
                        {
                            continue;
                        }
                        
                        int i = Integer.parseInt(s);
                        
                        if (i >= 0 && i < Reference.glyphItems.size())
                        {
                            theNetwork = theNetwork + " " + Reference.glyphItems.get(i).getItemName().replace("item.", "");
                        }
                    }
                    
                    modifierNetwork = theNetwork.substring(1);
                    PacketDispatcher.sendPacketToServer(PacketEnhancedPortals.makePacket(new PacketPortalModifierUpdate((TileEntityPortalModifier) worldObj.getBlockTileEntity(xCoord, yCoord, zCoord))));
                    
                    return new Object[] { true };
                }
                else
                {
                    throw new Exception("Invalid type");
                }
            }
        });
        
        // getIdentifier
        computerManager.registerMethod(new IMethod() {
            @Override
            public String getMethodName()
            {
                return "getIdentifier";
            }

            @Override
            public Object[] execute(IComputerAccess computer, Object[] arguments) throws Exception
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
        });
        
        // setIdentifier
        computerManager.registerMethod(new IMethod() {
            @Override
            public String getMethodName()
            {
                return "setIdentifier";
            }

            @Override
            public Object[] execute(IComputerAccess computer, Object[] arguments) throws Exception
            {
                if (!isRemotelyControlled())
                {
                    throw new Exception("You must use setNetwork()");
                }
                
                if (arguments[0] instanceof String)
                {
                    if (!arguments[0].toString().contains(" "))
                    {
                        throw new Exception("Invalid arguments");
                    }
                    
                    String[] str = arguments[0].toString().split(" ");
                    String theNetwork = "";
                    
                    if (str.length > 9)
                    {
                        throw new Exception("Too many values");
                    }
                    else if (str.length == 0)
                    {
                        throw new Exception("Invalid arguments");
                    }
                    
                    for (String s : str)
                    {
                        if (s.equals(""))
                        {
                            continue;
                        }
                        
                        int i = Integer.parseInt(s);
                        
                        if (i >= 0 && i < Reference.glyphItems.size())
                        {
                            theNetwork = theNetwork + " " + Reference.glyphItems.get(i).getItemName().replace("item.", "");
                        }
                    }
                    
                    dialDeviceNetwork = theNetwork.substring(1);
                    PacketDispatcher.sendPacketToServer(PacketEnhancedPortals.makePacket(new PacketPortalModifierUpdate((TileEntityPortalModifier) worldObj.getBlockTileEntity(xCoord, yCoord, zCoord))));
                    
                    return new Object[] { true };
                }
                else
                {
                    throw new Exception("Invalid type");
                }
            }
        });
        
        // getThickness
        computerManager.registerMethod(new IMethod() {
            @Override
            public String getMethodName()
            {
                return "getThickness";
            }

            @Override
            public Object[] execute(IComputerAccess computer, Object[] arguments) throws Exception
            {
                return new Object[] { thickness };
            }
        });
        
        // setThickness
        computerManager.registerMethod(new IMethod() {
            @Override
            public String getMethodName()
            {
                return "setThickness";
            }

            @Override
            public Object[] execute(IComputerAccess computer, Object[] arguments) throws Exception
            {
                if (arguments[0] instanceof Double)
                {
                    double obj = Double.parseDouble(arguments[0].toString());
                    
                    if (obj > 0 && obj < 5)
                    {
                        thickness = (byte) (obj - 1);
                        PacketDispatcher.sendPacketToServer(PacketEnhancedPortals.makePacket(new PacketPortalModifierUpdate((TileEntityPortalModifier) worldObj.getBlockTileEntity(xCoord, yCoord, zCoord))));
                        return new Object[] { true };
                    }
                    else
                    {
                        throw new Exception("Invalid integer. Must be within 0 and 5");
                    }
                }
                else
                {
                    throw new Exception("Invalid type. Must be an integer between 0 and 5");
                }
            }
        });
        
        // getUpgrades
        computerManager.registerMethod(new IMethod() {
            @Override
            public String getMethodName()
            {
                return "getUpgrades";
            }

            @Override
            public Object[] execute(IComputerAccess computer, Object[] arguments) throws Exception
            {
                HashMap<Integer, Integer> test = new HashMap<Integer, Integer>();
                byte[] upgrades = upgradeHandler.getInstalledUpgrades();

                for (int i = 0; i < upgradeHandler.getInstalledUpgrades().length; i++)
                {
                    test.put(i + 1, (int) upgrades[i]);
                }

                return new Object[] { test };
            }
        });
        
        // getSelfActive
        computerManager.registerMethod(new IMethod() {
            @Override
            public String getMethodName()
            {
                return "getSelfActive";
            }

            @Override
            public Object[] execute(IComputerAccess computer, Object[] arguments) throws Exception
            {
                return new Object[] { isActive() };
            }
        });
        
        // getAnyActive
        computerManager.registerMethod(new IMethod() {
            @Override
            public String getMethodName()
            {
                return "getAnyActive";
            }

            @Override
            public Object[] execute(IComputerAccess computer, Object[] arguments) throws Exception
            {
                return new Object[] { isAnyActive() };
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
    public boolean createPortal()
    {
        if (super.createPortal())
        {
            computerManager.queueEvent("create_portal", null);
            return true;
        }

        return false;
    }

    @Override
    public boolean createPortal(ItemStack stack)
    {
        if (super.createPortal(stack))
        {
            computerManager.queueEvent("create_portal", new Object[] { stack.getItem().itemID });
            return true;
        }

        return false;
    }

    @Override
    public boolean createPortalFromDialDevice()
    {
        if (super.createPortalFromDialDevice())
        {
            computerManager.queueEvent("create_portal", new Object[] { "basicDialDevice" });
            return true;
        }

        return false;
    }

    @Override
    public boolean createPortalFromDialDevice(String texture, byte thickness)
    {
        if (super.createPortalFromDialDevice(texture, thickness))
        {
            computerManager.queueEvent("create_portal", new Object[] { "dialDevice", texture, thickness });
            return true;
        }

        return false;
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
        return "portalModifier";
    }

    @Override
    public boolean removePortal()
    {
        if (super.removePortal())
        {
            computerManager.queueEvent("remove_portal", null);
            return true;
        }

        return false;
    }
}
