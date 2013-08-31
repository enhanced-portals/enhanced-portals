package uk.co.shadeddimensions.enhancedportals.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class PortalTexture extends Texture
{
    public int ParticleColour;
    public int ParticleType;

    public PortalTexture()
    {
        ParticleColour = 0xB336A1;
        ParticleType = 0;
    }

    public PortalTexture(String texture, int tColour, int pColour, int pType)
    {
        Texture = texture;
        TextureColour = tColour;
        ParticleColour = pColour;
        ParticleType = pType;
    }

    public PortalTexture(NBTTagCompound nbt)
    {
        Texture = nbt.getString("Texture");
        TextureColour = nbt.getInteger("TColour");
        ParticleColour = nbt.getInteger("PColour");
        ParticleType = nbt.getInteger("PType");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        nbt.setString("Texture", Texture);
        nbt.setInteger("TColour", TextureColour);
        nbt.setInteger("PColour", ParticleColour);
        nbt.setInteger("PType", ParticleType);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof PortalTexture)
        {
            PortalTexture tex = (PortalTexture) obj;

            return Texture.equals(tex.Texture) && TextureColour == tex.TextureColour && ParticleColour == tex.ParticleColour && ParticleType == tex.ParticleType;
        }

        return false;
    }

    @Override
    public String toString()
    {
        return "Texture (\"" + Texture + "\" TextureColour: " + TextureColour + " ParticleColour: " + ParticleColour + " ParticleType: " + ParticleType + ")";
    }

    public static PortalTexture getTextureFromStream(DataInputStream stream) throws IOException
    {
        return new PortalTexture(stream.readUTF(), stream.readInt(), stream.readInt(), stream.readInt());
    }

    @Override
    public void writeTextureToStream(DataOutputStream stream) throws IOException
    {
        stream.writeUTF(Texture);
        stream.writeInt(TextureColour);
        stream.writeInt(ParticleColour);
        stream.writeInt(ParticleType);
    }

    public int getParticleIndex()
    {
        switch (ParticleType)
        {
            default:
            case 0:
                return (int) (Math.random() * 8.0D);

            case 1:
                return 32;

            case 2:
                return 48 + (int) (2 * Math.random());

            case 3:
                return 64;

            case 4:
                return 65;

            case 5:
                return 66;

            case 6:
                return 80;

            case 7:
                return 81;

            case 8:
                return 82;

            case 9:
                return 83;

            case 10:
                return 97;

            case 11:
                return 128 + (int) (8 * Math.random());

            case 12:
                return 144 + (int) (8 * Math.random());

            case 13:
                return 160 + (int) (8 * Math.random());
        }
    }

    public int getStaticParticleIndex()
    {
        if (ParticleType == 0)
        {
            return 7;
        }
        else if (ParticleType == 2)
        {
            return 48;
        }
        else if (ParticleType == 11)
        {
            return 129;
        }
        else if (ParticleType == 12)
        {
            return 145;
        }
        else if (ParticleType == 13)
        {
            return 161;
        }

        return getParticleIndex();
    }

    @Override
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

        return new ItemStack(Block.portal);
    }
}
