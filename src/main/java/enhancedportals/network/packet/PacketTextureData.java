package enhancedportals.network.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.ByteBufUtils;
import enhancedportals.EnhancedPortals;
import enhancedportals.client.gui.GuiDiallingDeviceEdit;
import enhancedportals.network.ClientProxy;
import enhancedportals.network.GuiHandler;
import enhancedportals.portal.GlyphElement;
import enhancedportals.portal.GlyphIdentifier;
import enhancedportals.portal.PortalTextureManager;
import enhancedportals.tileentity.portal.TileDiallingDevice;

public class PacketTextureData extends PacketEP
{
    int id, x, y, z;
    PortalTextureManager ptm;
    String name;
    String glyphs;

    public PacketTextureData()
    {
        id = -1;
    }

    public PacketTextureData(int i, int x, int y, int z)
    {
        id = i;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public PacketTextureData(String n, String g, PortalTextureManager t)
    {
        id = -1;
        ptm = t;
        name = n;
        glyphs = g;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
    	if (id > -1)
        {
    		buffer.writeBoolean(true);
            buffer.writeInt(id);
            buffer.writeInt(x);
            buffer.writeInt(y);
            buffer.writeInt(z);
        }
        else
        {
        	buffer.writeBoolean(false);
            NBTTagCompound data = new NBTTagCompound();

            if (ptm != null)
            {
                ptm.writeToNBT(data, "Texture");
            }

            ByteBufUtils.writeTag(buffer, data);
            ByteBufUtils.writeUTF8String(buffer, name);
            ByteBufUtils.writeUTF8String(buffer, glyphs);
        }
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
    	if (buffer.readBoolean())
        {
            id = buffer.readInt();
            x = buffer.readInt();
            y = buffer.readInt();
            z = buffer.readInt();
        }
        else
        {
            short length = buffer.readShort();
            byte[] compressed = new byte[length];
            buffer.readBytes(compressed);
            NBTTagCompound data = ByteBufUtils.readTag(buffer);
            ptm = new PortalTextureManager();
            name = ByteBufUtils.readUTF8String(buffer);
            glyphs = ByteBufUtils.readUTF8String(buffer);

            if (data.hasKey("Texture"))
            {
                ptm.readFromNBT(data, "Texture");
            }

            id = -1;
        }
    }

    @Override
    public void handleClientSide(EntityPlayer player)
    {
    	if (id != -1)
        {
            return;
        }

        ClientProxy.saveName = name;
        ClientProxy.saveGlyph = new GlyphIdentifier(glyphs);
        ClientProxy.saveTexture = ptm;
        ((GuiDiallingDeviceEdit) FMLClientHandler.instance().getClient().currentScreen).receivedData();
    }

    @Override
    public void handleServerSide(EntityPlayer player)
    {
    	TileDiallingDevice dial = (TileDiallingDevice) ((EntityPlayer) player).worldObj.getTileEntity(x, y, z);

        if (dial != null)
        {
        	GuiHandler.openGui((EntityPlayer)player, dial, GuiHandler.TEXTURE_DIALLING_A);
        	GlyphElement e = dial.glyphList.get(id);
            EnhancedPortals.packetPipeline.sendTo(new PacketTextureData(e.name, e.identifier.getGlyphString(), e.texture), (EntityPlayerMP) player);
        }
    }
}