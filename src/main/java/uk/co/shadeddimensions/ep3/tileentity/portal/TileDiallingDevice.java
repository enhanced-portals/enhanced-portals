package uk.co.shadeddimensions.ep3.tileentity.portal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatMessageComponent;
import uk.co.shadeddimensions.ep3.lib.Localization;
import uk.co.shadeddimensions.ep3.network.GuiHandler;
import uk.co.shadeddimensions.ep3.network.PacketHandlerServer;
import uk.co.shadeddimensions.ep3.portal.GlyphIdentifier;
import uk.co.shadeddimensions.ep3.util.PortalTextureManager;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.ILuaContext;
import dan200.computer.api.IPeripheral;

public class TileDiallingDevice extends TileFrame implements IPeripheral
{
    public class GlyphElement
    {
        public String name;
        public GlyphIdentifier identifier;
        public PortalTextureManager texture;

        public GlyphElement(String n, GlyphIdentifier i)
        {
            name = n;
            identifier = i;
            texture = null;
        }

        public GlyphElement(String n, GlyphIdentifier i, PortalTextureManager t)
        {
            this(n, i);
            texture = t;
        }

        public boolean hasSpecificTexture()
        {
            return texture != null;
        }
    }

    public ArrayList<GlyphElement> glyphList = new ArrayList<GlyphElement>();

    @Override
    public void packetGui(NBTTagCompound tag, EntityPlayer player)
    {
        if (tag.hasKey("DialRequest"))
        {
            String id = tag.getString("DialRequest");
            
            for (GlyphElement el : glyphList) // Check to see if this is in the list of stored addresses, use its texture if it is.
            {
                if (el.identifier.getGlyphString().equals(id))
                {
                    getPortalController().connectionDial(new GlyphIdentifier(id), el.texture, player);
                    return;
                }
            }
            
            getPortalController().connectionDial(new GlyphIdentifier(id), null, player);
            return;
        }
        else if (tag.hasKey("DialTerminateRequest"))
        {
            getPortalController().connectionTerminate();
            return;
        }

        if (tag.hasKey("SetDialTexture") && tag.hasKey("TextureData"))
        {
            PortalTextureManager ptm = new PortalTextureManager();
            ptm.readFromNBT(tag, "TextureData");
            glyphList.get(tag.getInteger("SetDialTexture")).texture = ptm;
            
            PacketHandlerServer.sendGuiPacketToPlayer(this, player);
        }

        if (tag.hasKey("GlyphName") && tag.hasKey("Glyphs"))
        {
            glyphList.add(new GlyphElement(tag.getString("GlyphName"), new GlyphIdentifier(tag.getString("Glyphs"))));
            
            PacketHandlerServer.sendGuiPacketToPlayer(this, player);
        }

        if (tag.hasKey("DeleteGlyph"))
        {
            int g = tag.getInteger("DeleteGlyph");

            if (glyphList.size() > g)
            {
                glyphList.remove(g);
                
                PacketHandlerServer.sendGuiPacketToPlayer(this, player);
            }
        }
        
        onInventoryChanged();
    }
    
    @Override
    public boolean activate(EntityPlayer player, ItemStack stack)
    {
    	if (player.isSneaking())
		{
			return false;
		}
    	
        TileController controller = getPortalController();
        
        if (worldObj.isRemote)
        {
            return controller != null;
        }
        
        if (controller != null && controller.isFinalized())
        {
            if (controller.getIdentifierUnique() == null)
            {
                player.sendChatToPlayer(ChatMessageComponent.createFromText(Localization.getChatString("noUidSet")));
            }
            else if (!player.isSneaking())
            {
                GuiHandler.openGui(player, this, GuiHandler.DIALLING_DEVICE);
            }

            return true;
        }

        return false;
    }
    
    @Override
    public void packetGuiFill(DataOutputStream stream) throws IOException
    {
        stream.writeInt(glyphList.size());

        for (int i = 0; i < glyphList.size(); i++)
        {
            stream.writeUTF(glyphList.get(i).name);
            stream.writeUTF(glyphList.get(i).identifier.getGlyphString());
        }
    }
    
    @Override
    public void packetGuiUse(DataInputStream stream) throws IOException
    {
        int max = stream.readInt();
        glyphList.clear();

        for (int i = 0; i < max; i++)
        {
            glyphList.add(new GlyphElement(stream.readUTF(), new GlyphIdentifier(stream.readUTF())));
        }
    }
    
    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        NBTTagList list = tag.getTagList("glyphList");

        for (int i = 0; i < list.tagCount(); i++)
        {
            NBTTagCompound t = (NBTTagCompound) list.tagAt(i);
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
    public String getType()
    {
        return "Dialling Device";
    }

    @Override
    public String[] getMethodNames()
    {
        return new String[] { "dial", "terminate", "dialStored", "getStoredName", "getStoredGlyph", "getStoredCount" };
    }

    @Override
    public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws Exception
    {
        if (method == 0) // dial
        {
            if (arguments.length == 1)
            {
                String s = arguments[0].toString();
                s = s.replace(" ", GlyphIdentifier.GLYPH_SEPERATOR);

                if (s.length() == 0)
                {
                    throw new Exception("Glyph ID is not formatted correctly");
                }
                
                try
                {
                    if (s.contains(GlyphIdentifier.GLYPH_SEPERATOR))
                    {
                        String[] nums = s.split(GlyphIdentifier.GLYPH_SEPERATOR);

                        if (nums.length > 9)
                        {
                            throw new Exception("Glyph ID is too long. Must be a maximum of 9 IDs");
                        }

                        for (String num : nums)
                        {

                            int n = Integer.parseInt(num);

                            if (n < 0 || n > 27)
                            {
                                throw new Exception("Glyph ID must be between 0 and 27 (inclusive)");
                            }
                        }
                    }
                    else
                    {
                        int n = Integer.parseInt(s);

                        if (n < 0 || n > 27)
                        {
                            throw new Exception("Glyph ID must be between 0 and 27 (inclusive)");
                        }
                    }
                }
                catch (NumberFormatException ex)
                {
                    throw new Exception("Glyph ID is not formatted correctly");
                }

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
            
            if (num > 0 && num < glyphList.size())
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
    public boolean canAttachToSide(int side)
    {
        return true;
    }

    @Override
    public void attach(IComputerAccess computer)
    {
        
    }

    @Override
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
}
