package uk.co.shadeddimensions.enhancedportals.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import uk.co.shadeddimensions.enhancedportals.network.CommonProxy;

public class Texture
{
    public String Texture;
    public int TextureColour;

    public Texture()
    {
        Texture = "";
        TextureColour = 0xFFFFFF;
    }

    public Texture(String t, int c)
    {
        Texture = t;
        TextureColour = c;
    }

    public Texture(NBTTagCompound nbt)
    {
        Texture = nbt.getString("Texture");
        TextureColour = nbt.getInteger("TColour");
    }

    public void writeToNBT(NBTTagCompound nbt)
    {
        nbt.setString("Texture", Texture);
        nbt.setInteger("TColour", TextureColour);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Texture)
        {
            Texture tex = (Texture) obj;

            return Texture.equals(tex.Texture) && TextureColour == tex.TextureColour;
        }

        return false;
    }

    @Override
    public String toString()
    {
        return "Texture (\"" + Texture + "\" TextureColour: " + TextureColour + ")";
    }

    public static String getTextureName(String texture)
    {
        if (texture.startsWith("B:") || texture.startsWith("I:"))
        {
            texture = texture.substring(2);
            return new ItemStack(Integer.parseInt(texture.split(":")[0]), 1, Integer.parseInt(texture.split(":")[1])).getDisplayName();
        }

        return texture;
    }

    public static Icon getTexture(String texture, int side)
    {
        if (texture.startsWith("B:"))
        {
            texture = texture.substring(2);
            return Block.blocksList[Integer.parseInt(texture.split(":")[0])].getIcon(side, Integer.parseInt(texture.split(":")[1]));
        }

        return null;
    }

    public static Texture getTextureFromStream(DataInputStream stream) throws IOException
    {
        return new Texture(stream.readUTF(), stream.readInt());
    }

    public void writeTextureToStream(DataOutputStream stream) throws IOException
    {
        stream.writeUTF(Texture);
        stream.writeInt(TextureColour);
    }

    public ItemStack getItemStack()
    {
        if (Texture.startsWith("B:") || Texture.startsWith("I:"))
        {
            String tex = Texture.replace("B:", "");

            return new ItemStack(Integer.parseInt(tex.split(":")[0]), 1, Integer.parseInt(tex.split(":")[1]));
        }
        else if (Texture.startsWith("C:"))
        {
            // String tex = Texture.replace("B:", "");

            // return new ItemStack();
        }

        return new ItemStack(CommonProxy.blockFrame);
    }
}
