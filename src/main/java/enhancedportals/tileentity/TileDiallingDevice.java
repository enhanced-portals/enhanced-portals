package enhancedportals.tileentity;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;

import li.cil.oc.api.network.Arguments;
import li.cil.oc.api.network.Callback;
import li.cil.oc.api.network.Context;
import li.cil.oc.api.network.SimpleComponent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;
import cpw.mods.fml.common.Optional.Interface;
import cpw.mods.fml.common.Optional.InterfaceList;
import cpw.mods.fml.common.Optional.Method;
import cpw.mods.fml.common.network.ByteBufUtils;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import enhancedportals.EnhancedPortals;
import enhancedportals.network.GuiHandler;
import enhancedportals.portal.GlyphElement;
import enhancedportals.portal.GlyphIdentifier;
import enhancedportals.portal.PortalTextureManager;
import enhancedportals.utility.ComputerUtils;

@InterfaceList(value = { @Interface(iface="dan200.computercraft.api.peripheral.IPeripheral", modid=EnhancedPortals.MODID_COMPUTERCRAFT), @Interface(iface="li.cil.oc.api.network.SimpleComponent", modid=EnhancedPortals.MODID_OPENCOMPUTERS) })
public class TileDiallingDevice extends TileFrame implements IPeripheral, SimpleComponent
{
    public ArrayList<GlyphElement> glyphList = new ArrayList<GlyphElement>();

    @Override
    public boolean activate(EntityPlayer player, ItemStack stack)
    {
        TileController controller = getPortalController();

        if (worldObj.isRemote)
        {
            return controller != null;
        }

        if (controller != null && controller.isFinalized())
        {
            if (controller.getIdentifierUnique() == null)
            {
                player.addChatComponentMessage(new ChatComponentText(EnhancedPortals.localizeError("noUidSet")));
            }
            else
            {
                if (!player.isSneaking())
                {
                    GuiHandler.openGui(player, this, GuiHandler.DIALLING_DEVICE_A);
                }
                else
                {
                    if (controller.isPortalActive())
                    {
                        controller.connectionTerminate();
                    }
                    else
                    {
                        GuiHandler.openGui(player, this, GuiHandler.DIALLING_DEVICE_B);
                    }
                }
            }

            return true;
        }

        return false;
    }

    @Override
    public void packetGuiFill(ByteBuf buffer)
    {
        buffer.writeInt(glyphList.size());

        for (int i = 0; i < glyphList.size(); i++)
        {
            ByteBufUtils.writeUTF8String(buffer, glyphList.get(i).name);
            ByteBufUtils.writeUTF8String(buffer, glyphList.get(i).identifier.getGlyphString());
        }
    }

    @Override
    public void packetGuiUse(ByteBuf buffer)
    {
        int max = buffer.readInt();
        glyphList.clear();

        for (int i = 0; i < max; i++)
        {
            glyphList.add(new GlyphElement(ByteBufUtils.readUTF8String(buffer), new GlyphIdentifier(ByteBufUtils.readUTF8String(buffer))));
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        NBTTagList list = tag.getTagList("glyphList", 10);

        for (int i = 0; i < list.tagCount(); i++)
        {
            NBTTagCompound t = (NBTTagCompound) list.getCompoundTagAt(i);
            String name = t.getString("Name"), glyph = t.getString("Identifier");

            if (t.hasKey("texture"))
            {
                PortalTextureManager tex = new PortalTextureManager();
                tex.readFromNBT(t, "texture");

                glyphList.add(new GlyphElement(name, new GlyphIdentifier(glyph), tex));
            }
            else
            {
                glyphList.add(new GlyphElement(name, new GlyphIdentifier(glyph)));
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        NBTTagList list = new NBTTagList();

        for (int i = 0; i < glyphList.size(); i++)
        {
            NBTTagCompound t = new NBTTagCompound();
            GlyphElement e = glyphList.get(i);
            t.setString("Name", e.name);
            t.setString("Identifier", e.identifier.getGlyphString());

            if (e.hasSpecificTexture())
            {
                e.texture.writeToNBT(t, "texture");
            }

            list.appendTag(t);
        }

        tag.setTag("glyphList", list);
    }

    @Override
    @Method(modid=EnhancedPortals.MODID_COMPUTERCRAFT)
    public String getType()
    {
        return "dialling_device";
    }

    @Override
    @Method(modid=EnhancedPortals.MODID_COMPUTERCRAFT)
    public String[] getMethodNames()
    {
        return new String[] { "dial", "terminate", "dialStored", "getStoredName", "getStoredGlyph", "getStoredCount" };
    }

    @Override
    @Method(modid=EnhancedPortals.MODID_COMPUTERCRAFT)
    public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws Exception
    {
        if (method == 0) // dial
        {
            if (arguments.length == 1)
            {
                String s = arguments[0].toString();
                s = s.replace(" ", GlyphIdentifier.GLYPH_SEPERATOR);

                String error = ComputerUtils.verifyGlyphArguments(s);
                if(error != null) throw new Exception(error);

                getPortalController().connectionDial(new GlyphIdentifier(s), null, null);
            }
            else
            {
                throw new Exception("Invalid arguments");
            }
        }
        else if (method == 1) // terminate
        {
            getPortalController().connectionTerminate();
        }
        else if (method == 2) // dialStored
        {
            int num = getSelectedEntry(arguments);

            if (num >= 0 && num < glyphList.size())
            {
                getPortalController().connectionDial(glyphList.get(num).identifier, null, null);
            }
        }
        else if (method == 3) // getStoredName
        {
            int num = getSelectedEntry(arguments);
            GlyphElement entry = glyphList.get(num);

            if (entry != null)
            {
                return new Object[] { entry.name };
            }
            else
            {
                throw new Exception("Entry not found");
            }
        }
        else if (method == 4) // getStoredGlyph
        {
            int num = getSelectedEntry(arguments);
            GlyphElement entry = glyphList.get(num);

            if (entry != null)
            {
                return new Object[] { entry.identifier.getGlyphString() };
            }
            else
            {
                throw new Exception("Entry not found");
            }
        }
        else if (method == 5) // getStoredCount
        {
            return new Object[] { glyphList.size() };
        }

        return null;
    }

    int getSelectedEntry(Object[] arguments) throws Exception
    {
        try
        {
            if (arguments.length == 1)
            {
                if (arguments[0].toString().contains("."))
                {
                    arguments[0] = arguments[0].toString().substring(0, arguments[0].toString().indexOf("."));
                }

                int i = Integer.parseInt(arguments[0].toString());

                if (i < 0 || i >= glyphList.size())
                {
                    throw new Exception("There is no entry in location " + i);
                }

                return i;
            }
        }
        catch (NumberFormatException e)
        {
            throw new Exception(arguments[0].toString() + " is not an integer.");
        }

        throw new Exception("Invalid number of arguments.");
    }

    @Override
    @Method(modid=EnhancedPortals.MODID_COMPUTERCRAFT)
    public void attach(IComputerAccess computer)
    {

    }

    @Override
    @Method(modid=EnhancedPortals.MODID_COMPUTERCRAFT)
    public void detach(IComputerAccess computer)
    {

    }

    @Override
    public void addDataToPacket(NBTTagCompound tag)
    {

    }

    @Override
    public void onDataPacket(NBTTagCompound tag)
    {

    }

    @Override
    @Method(modid=EnhancedPortals.MODID_OPENCOMPUTERS)
    public String getComponentName()
    {
        return "ep_dialling_device";
    }

    @Callback
    @Method(modid=EnhancedPortals.MODID_OPENCOMPUTERS)
    public Object[] dial(Context context, Arguments args) throws Exception
    {
        if(args.count() < 1)
        {
            return null;
        }
        
        return callMethod(null, null, 0, ComputerUtils.argsToArray(args));
    }

    @Callback
    @Method(modid=EnhancedPortals.MODID_OPENCOMPUTERS)
    public Object[] terminate(Context context, Arguments args)
    {
        getPortalController().connectionTerminate();
        return new Object[]{ true };
    }

    @Callback
    @Method(modid=EnhancedPortals.MODID_OPENCOMPUTERS)
    public Object[] dialStored(Context context, Arguments args) throws Exception
    {
        return callMethod(null, null, 2, ComputerUtils.argsToArray(args));
    }

    @Callback(direct = true)
    @Method(modid=EnhancedPortals.MODID_OPENCOMPUTERS)
    public Object[] getStoredName(Context context, Arguments args) throws Exception
    {
        return callMethod(null, null, 3, ComputerUtils.argsToArray(args));
    }

    @Callback(direct = true)
    @Method(modid=EnhancedPortals.MODID_OPENCOMPUTERS)
    public Object[] getStoredGlyph(Context context, Arguments args) throws Exception
    {
        return callMethod(null, null, 4, ComputerUtils.argsToArray(args));
    }

    @Callback(direct = true)
    @Method(modid=EnhancedPortals.MODID_OPENCOMPUTERS)
    public Object[] getStoredCount(Context context, Arguments args)
    {
        return new Object[] { glyphList.size() };
    }

    @Override
    @Method(modid=EnhancedPortals.MODID_COMPUTERCRAFT)
    public boolean equals(IPeripheral other)
    {
        return other == this;
    }
}
