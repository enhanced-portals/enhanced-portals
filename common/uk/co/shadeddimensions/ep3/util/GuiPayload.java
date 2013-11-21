package uk.co.shadeddimensions.ep3.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

public class GuiPayload
{
    public NBTTagCompound data;

    public GuiPayload()
    {
        data = new NBTTagCompound();
    }

    public GuiPayload(DataInputStream s) throws IOException
    {
        short length = s.readShort();
        byte[] compressed = new byte[length];
        s.readFully(compressed);
        data = CompressedStreamTools.decompress(compressed);
    }

    public GuiPayload(NBTTagCompound tag)
    {
        data = tag;
    }

    public void writeData(NBTTagCompound tag)
    {
        if (data != null)
        {
            tag.setTag("guiPayload", data);
        }
    }

    public void readData(NBTTagCompound tag)
    {
        data = tag.hasKey("guiPayload") ? (NBTTagCompound) tag.getTag("guiPayload") : null;
    }

    public void write(DataOutputStream s) throws IOException
    {
        byte[] compressed = CompressedStreamTools.compress(data);
        s.writeShort(compressed.length);
        s.write(compressed);
    }
}
