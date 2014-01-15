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

public class TileDiallingDevice extends TilePortalFrame
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
            else
            {
                CommonProxy.openGui(player, GUIs.DiallingDevice, this);
            }

            return true;
        }

        return false;
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
}
