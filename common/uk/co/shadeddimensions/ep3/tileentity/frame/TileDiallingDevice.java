package uk.co.shadeddimensions.ep3.tileentity.frame;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatMessageComponent;
import uk.co.shadeddimensions.ep3.EnhancedPortals;
import uk.co.shadeddimensions.ep3.lib.GuiIds;
import uk.co.shadeddimensions.ep3.lib.Reference;
import uk.co.shadeddimensions.ep3.portal.GlyphIdentifier;
import uk.co.shadeddimensions.ep3.tileentity.TilePortalPart;
import uk.co.shadeddimensions.ep3.util.PortalTextureManager;

public class TileDiallingDevice extends TilePortalPart
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
        
        if (controller != null && controller.getUniqueIdentifier() == null)
        {
            if (!worldObj.isRemote)
            {
                player.sendChatToPlayer(ChatMessageComponent.createFromTranslationKey(Reference.SHORT_ID + ".chat.error.noUidSet"));
            }

            return false;
        }
        else if (controller == null || !controller.hasConfigured)
        {
            return false;
        }
        else
        {
            player.openGui(EnhancedPortals.instance, GuiIds.DIALLING_DEVICE, worldObj, xCoord, yCoord, zCoord);
            return true;
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
    public void usePacket(DataInputStream stream) throws IOException
    {
        super.usePacket(stream);
        int max = stream.readInt();
        
        for (int i = 0; i < max; i++)
        {
            glyphList.add(new GlyphElement(stream.readUTF(), new GlyphIdentifier(stream.readUTF())));
        }
    }
}
