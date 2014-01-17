package uk.co.shadeddimensions.ep3.tileentity.frame;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.Icon;
import uk.co.shadeddimensions.ep3.block.BlockFrame;
import uk.co.shadeddimensions.ep3.lib.GUIs;
import uk.co.shadeddimensions.ep3.lib.Localization;
import uk.co.shadeddimensions.ep3.network.CommonProxy;
import uk.co.shadeddimensions.ep3.portal.GlyphIdentifier;
import uk.co.shadeddimensions.ep3.tileentity.TilePortalFrame;
import uk.co.shadeddimensions.ep3.util.GuiPayload;
import uk.co.shadeddimensions.ep3.util.PortalTextureManager;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.ILuaContext;
import dan200.computer.api.IPeripheral;

public class TileDiallingDevice extends TilePortalFrame implements IPeripheral
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

    public ArrayList<GlyphElement> glyphList;

    public TileDiallingDevice()
    {
        super();
        glyphList = new ArrayList<GlyphElement>();
    }

    @Override
    public boolean activate(EntityPlayer player)
    {
        TilePortalController controller = getPortalController();
        
        if (worldObj.isRemote)
        {
            return controller != null;
        }
        
        if (controller != null && controller.isFullyInitialized())
        {
            if (controller.getUniqueIdentifier() == null)
            {
                player.sendChatToPlayer(ChatMessageComponent.createFromText(Localization.getChatString("noUidSet")));
            }
            else if (!player.isSneaking())
            {
                CommonProxy.openGui(player, GUIs.DiallingDevice, this);
            }

            return true;
        }

        return false;
    }
    
    void dial(GlyphIdentifier id) throws Exception
    {
        TilePortalController controller = getPortalController();
        
        if (controller == null)
        {
            throw new Exception("Can't find portal controller");
        }
        
        controller.dialRequest(id, null, null);
    }
    
    void dialStored(int i) throws Exception
    {
        TilePortalController controller = getPortalController();
        
        if (controller == null)
        {
            throw new Exception("Can't find portal controller");
        }
        
        controller.dialRequest(glyphList.get(i).identifier, glyphList.get(i).texture, null);
    }
    
    void terminate() throws Exception
    {
        TilePortalController controller = getPortalController();
        
        if (controller == null)
        {
            throw new Exception("Can't find portal controller");
        }
        
        controller.removePortal();
    }

    public ArrayList<GlyphElement> copyGlyphList()
    {
        ArrayList<GlyphElement> list = new ArrayList<GlyphElement>();

        for (GlyphElement e : glyphList)
        {
            list.add(new GlyphElement(e.name, e.identifier, e.texture));
        }

        return list;
    }

    @Override
    public void fillPacket(DataOutputStream stream) throws IOException
    {
        super.fillPacket(stream);
        stream.writeInt(glyphList.size());

        for (int i = 0; i < glyphList.size(); i++)
        {
            stream.writeUTF(glyphList.get(i).name);
            stream.writeUTF(glyphList.get(i).identifier.getGlyphString());
        }
    }

    @Override
    public Icon getBlockTexture(int side, int pass)
    {
        if (pass == 0)
        {
            return super.getBlockTexture(side, pass);
        }

        return BlockFrame.overlayIcons[4];
    }

    @Override
    public void guiActionPerformed(GuiPayload payload, EntityPlayer player)
    {
        super.guiActionPerformed(payload, player);

        if (payload.data.hasKey("DialRequest"))
        {
            String id = payload.data.getString("DialRequest");

            for (GlyphElement el : glyphList) // Check to see if this is in the list of stored addresses, use its texture if it is.
            {
                if (el.identifier.getGlyphString().equals(id))
                {
                    getPortalController().dialRequest(new GlyphIdentifier(id), el.texture, player);
                    return;
                }
            }

            getPortalController().dialRequest(new GlyphIdentifier(id), null, player); // If not, use no texture
            return;
        }
        else if (payload.data.hasKey("DialTerminateRequest"))
        {
            getPortalController().removePortal();
            return;
        }

        if (payload.data.hasKey("SetDialTexture") && payload.data.hasKey("TextureData"))
        {
            PortalTextureManager ptm = new PortalTextureManager();
            ptm.readFromNBT(payload.data, "TextureData");
            glyphList.get(payload.data.getInteger("SetDialTexture")).texture = ptm;
            CommonProxy.sendUpdatePacketToAllAround(this);
        }

        if (payload.data.hasKey("GlyphName") && payload.data.hasKey("Glyphs"))
        {
            glyphList.add(new GlyphElement(payload.data.getString("GlyphName"), new GlyphIdentifier(payload.data.getString("Glyphs"))));
            CommonProxy.sendUpdatePacketToAllAround(this);
        }

        if (payload.data.hasKey("DeleteGlyph"))
        {
            int g = payload.data.getInteger("DeleteGlyph");

            if (glyphList.size() > g)
            {
                glyphList.remove(g);
                CommonProxy.sendUpdatePacketToAllAround(this);
            }
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
    public void usePacket(DataInputStream stream) throws IOException
    {
        super.usePacket(stream);
        int max = stream.readInt();
        glyphList.clear();

        for (int i = 0; i < max; i++)
        {
            glyphList.add(new GlyphElement(stream.readUTF(), new GlyphIdentifier(stream.readUTF())));
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
    
    /* IPeripheral */
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

                dial(new GlyphIdentifier(s));
            }
            else
            {
                throw new Exception("Invalid arguments");
            }
        }
        else if (method == 1) // terminate
        {
            terminate();
        }
        else if (method == 2) // dialStored
        {
            int num = getSelectedEntry(arguments);
            dialStored(num);
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
}
