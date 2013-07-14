package enhancedportals.computercraft.tileentity;

import java.util.HashMap;

import net.minecraft.item.ItemStack;
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
import enhancedportals.lib.Reference;
import enhancedportals.network.packet.PacketCreatePortal;
import enhancedportals.network.packet.PacketEnhancedPortals;
import enhancedportals.network.packet.PacketPortalModifierUpdate;
import enhancedportals.portal.upgrades.Upgrade;
import enhancedportals.tileentity.TileEntityEnhancedPortals;
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
        computerManager.registerMethod(new IMethod()
        {
            @Override
            public Object[] execute(IComputerAccess computer, Object[] arguments) throws Exception
            {
                if (FMLCommonHandler.instance().getSide() == Side.CLIENT && FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
                {
                    PacketDispatcher.sendPacketToServer(PacketEnhancedPortals.makePacket(new PacketCreatePortal((TileEntityEnhancedPortals) worldObj.getBlockTileEntity(xCoord, yCoord, zCoord), true)));
                }
                else
                {
                    createPortal();
                }

                return null;
            }

            @Override
            public String getMethodName()
            {
                return "createPortal";
            }
        });

        // removePortal
        computerManager.registerMethod(new IMethod()
        {
            @Override
            public Object[] execute(IComputerAccess computer, Object[] arguments) throws Exception
            {
                if (FMLCommonHandler.instance().getSide() == Side.CLIENT && FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
                {
                    PacketDispatcher.sendPacketToServer(PacketEnhancedPortals.makePacket(new PacketCreatePortal((TileEntityEnhancedPortals) worldObj.getBlockTileEntity(xCoord, yCoord, zCoord), false)));
                }
                else
                {
                    removePortal();
                }

                return null;
            }

            @Override
            public String getMethodName()
            {
                return "removePortal";
            }
        });

        // getTexture
        computerManager.registerMethod(new IMethod()
        {
            @Override
            public Object[] execute(IComputerAccess computer, Object[] arguments) throws Exception
            {
                return new Object[] { texture.equals("") ? "C:5" : texture };
            }

            @Override
            public String getMethodName()
            {
                return "getTexture";
            }
        });

        // getNetwork
        computerManager.registerMethod(new IMethod()
        {
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

            @Override
            public String getMethodName()
            {
                return "getNetwork";
            }
        });

        // setNetwork
        computerManager.registerMethod(new IMethod()
        {
            @Override
            public Object[] execute(IComputerAccess computer, Object[] arguments) throws Exception
            {
                if (isRemotelyControlled())
                {
                    throw new Exception("You must use setIdentifier()");
                }

                if (arguments[0] instanceof String)
                {
                    modifierNetwork = GlyphString.getGlyphStringFromIdString(arguments[0].toString());

                    if (FMLCommonHandler.instance().getSide() == Side.CLIENT && FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
                    {
                        PacketDispatcher.sendPacketToServer(PacketEnhancedPortals.makePacket(new PacketPortalModifierUpdate((TileEntityPortalModifier) worldObj.getBlockTileEntity(xCoord, yCoord, zCoord))));
                    }

                    return new Object[] { true };
                }
                else
                {
                    throw new Exception("Invalid type");
                }
            }

            @Override
            public String getMethodName()
            {
                return "setNetwork";
            }
        });

        // getIdentifier
        computerManager.registerMethod(new IMethod()
        {
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

            @Override
            public String getMethodName()
            {
                return "getIdentifier";
            }
        });

        // setIdentifier
        computerManager.registerMethod(new IMethod()
        {
            @Override
            public Object[] execute(IComputerAccess computer, Object[] arguments) throws Exception
            {
                if (!isRemotelyControlled())
                {
                    throw new Exception("You must use setNetwork()");
                }

                if (arguments[0] instanceof String)
                {
                    dialDeviceNetwork = GlyphString.getGlyphStringFromIdString(arguments[0].toString());

                    if (FMLCommonHandler.instance().getSide() == Side.CLIENT && FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
                    {
                        PacketDispatcher.sendPacketToServer(PacketEnhancedPortals.makePacket(new PacketPortalModifierUpdate((TileEntityPortalModifier) worldObj.getBlockTileEntity(xCoord, yCoord, zCoord))));
                    }

                    return new Object[] { true };
                }
                else
                {
                    throw new Exception("Invalid type");
                }
            }

            @Override
            public String getMethodName()
            {
                return "setIdentifier";
            }
        });

        // getThickness
        computerManager.registerMethod(new IMethod()
        {
            @Override
            public Object[] execute(IComputerAccess computer, Object[] arguments) throws Exception
            {
                return new Object[] { thickness };
            }

            @Override
            public String getMethodName()
            {
                return "getThickness";
            }
        });

        // setThickness
        computerManager.registerMethod(new IMethod()
        {
            @Override
            public Object[] execute(IComputerAccess computer, Object[] arguments) throws Exception
            {
                if (arguments[0] instanceof Double)
                {
                    double obj = Double.parseDouble(arguments[0].toString());

                    if (obj > 0 && obj < 5)
                    {
                        thickness = (byte) (obj - 1);

                        if (FMLCommonHandler.instance().getSide() == Side.CLIENT && FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
                        {
                            PacketDispatcher.sendPacketToServer(PacketEnhancedPortals.makePacket(new PacketPortalModifierUpdate((TileEntityPortalModifier) worldObj.getBlockTileEntity(xCoord, yCoord, zCoord))));
                        }

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

            @Override
            public String getMethodName()
            {
                return "setThickness";
            }
        });

        // getUpgrades
        computerManager.registerMethod(new IMethod()
        {
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

            @Override
            public String getMethodName()
            {
                return "getUpgrades";
            }
        });

        // getUpgradeName
        computerManager.registerMethod(new IMethod()
        {
            @Override
            public Object[] execute(IComputerAccess computer, Object[] arguments) throws Exception
            {
                if (arguments.length != 1 || !(arguments[0] instanceof Double))
                {
                    throw new Exception("Invalid arguments.");
                }

                int num = (int) Math.round(Double.parseDouble(arguments[0].toString()));

                if (num < 0 || num >= Upgrade.getAllUpgrades().length)
                {
                    throw new Exception("Invalid arguments");
                }

                return new Object[] { Upgrade.getUpgrade(num).getItemStack().getDisplayName() };
            }

            @Override
            public String getMethodName()
            {
                return "getUpgradeName";
            }
        });

        // getSelfActive
        computerManager.registerMethod(new IMethod()
        {
            @Override
            public Object[] execute(IComputerAccess computer, Object[] arguments) throws Exception
            {
                return new Object[] { isActive() };
            }

            @Override
            public String getMethodName()
            {
                return "getSelfActive";
            }
        });

        // getAnyActive
        computerManager.registerMethod(new IMethod()
        {
            @Override
            public Object[] execute(IComputerAccess computer, Object[] arguments) throws Exception
            {
                return new Object[] { isAnyActive() };
            }

            @Override
            public String getMethodName()
            {
                return "getAnyActive";
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
