package enhancedportals.computercraft.tileentity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IPeripheral;
import enhancedportals.lib.Reference;
import enhancedportals.network.packet.PacketCreatePortal;
import enhancedportals.network.packet.PacketEnhancedPortals;
import enhancedportals.network.packet.PacketPortalModifierUpdate;
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
        if ((method == 0 || method == 1 || method == 2 || method == 5 || method == 7 || method == 9 || method == 10 || method == 11) && arguments.length != 0)
        {
            throw new Exception("Invalid number of arguments. Expecting 0.");
        }
        else if ((method == 3 || method == 4 || method == 6 || method == 8) && arguments.length != 1)
        {
            throw new Exception("Invalid number of arguments. Expecting 1.");
        }

        if (method == 0) // createPortal
        {
            if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
            {
                PacketDispatcher.sendPacketToServer(PacketEnhancedPortals.makePacket(new PacketCreatePortal(this, true)));
            }
            else
            {
                createPortal();
            }
            
            return null;
        }
        else if (method == 1) // removePortal
        {
            if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
            {
                PacketDispatcher.sendPacketToServer(PacketEnhancedPortals.makePacket(new PacketCreatePortal(this, false)));
            }
            else
            {
                removePortal();
            }
            
            return null;
        }
        else if (method == 2) // getTexture
        {
            return new Object[] { texture };
        }
        else if (method == 3) // getNetwork
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
        else if (method == 4) // setNetwork
        {
            if (isRemotelyControlled())
            {
                throw new Exception("You must use setIdentifier()");
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
                
                modifierNetwork = theNetwork.substring(1);
                PacketDispatcher.sendPacketToServer(PacketEnhancedPortals.makePacket(new PacketPortalModifierUpdate(this)));
                
                return new Object[] { true };
            }
            else
            {
                throw new Exception("Invalid type");
            }
        }
        else if (method == 5) // getIdentifier
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
        else if (method == 6) // setIdentifier
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
                PacketDispatcher.sendPacketToServer(PacketEnhancedPortals.makePacket(new PacketPortalModifierUpdate(this)));
                
                return new Object[] { true };
            }
            else
            {
                throw new Exception("Invalid type");
            }
        }
        else if (method == 7) // getThickness
        {
            return new Object[] { thickness };
        }
        else if (method == 8) // setThickness
        {
            if (arguments[0] instanceof Double)
            {
                double obj = Double.parseDouble(arguments[0].toString());
                
                if (obj > 0 && obj < 5)
                {
                    thickness = (byte) (obj - 1);
                    PacketDispatcher.sendPacketToServer(PacketEnhancedPortals.makePacket(new PacketPortalModifierUpdate(this)));
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
        else if (method == 9) // getUpgrades
        {
            HashMap<Integer, Integer> test = new HashMap<Integer, Integer>();
            byte[] upgrades = upgradeHandler.getInstalledUpgrades();

            for (int i = 0; i < upgradeHandler.getInstalledUpgrades().length; i++)
            {
                test.put(i + 1, (int) upgrades[i]);
            }

            return new Object[] { test };
        }
        else if (method == 10) // isSelfActive
        {
            return new Object[] { isActive() };
        }
        else if (method == 11) // isAnyActive
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
        return new String[] { "createPortal", "removePortal", "getTexture", "getNetwork", "setNetwork", "getIdentifier", "setIdentifier", "getThickness", "setThickness", "getUpgrades", "isSelfActive", "isAnyActive" };
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
