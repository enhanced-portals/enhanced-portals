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
import enhancedportals.tileentity.TileDiallingDevice;

public class PacketTextureData extends PacketEP
{
    PortalTextureManager ptm;
    String name;
    String glyphs;

    public PacketTextureData()
    {

    }

    public PacketTextureData(String n, String g, PortalTextureManager t)
    {
        ptm = t;
        name = n;
        glyphs = g;
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        NBTTagCompound data = ByteBufUtils.readTag(buffer);
        ptm = new PortalTextureManager();

        if (data.hasKey("Texture"))
        {
            ptm.readFromNBT(data, "Texture");
        }
        
        name = data.getString("name");
        glyphs = data.getString("glyphs");
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        NBTTagCompound data = new NBTTagCompound();

        if (ptm != null)
        {
            ptm.writeToNBT(data, "Texture");
        }
        
        data.setString("name", name);
        data.setString("glyphs", glyphs);

        ByteBufUtils.writeTag(buffer, data);

    }

    @Override
    public void handleClientSide(EntityPlayer player)
    {
        ClientProxy.saveName = name;
        ClientProxy.saveGlyph = new GlyphIdentifier(glyphs);
        ClientProxy.saveTexture = ptm;
        ((GuiDiallingDeviceEdit) FMLClientHandler.instance().getClient().currentScreen).receivedData();
    }

    @Override
    public void handleServerSide(EntityPlayer player)
    {
        
    }
}
