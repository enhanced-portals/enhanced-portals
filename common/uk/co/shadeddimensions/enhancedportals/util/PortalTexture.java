package uk.co.shadeddimensions.enhancedportals.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.nbt.NBTTagCompound;

public class PortalTexture extends Texture
{
    public int ParticleColour;
    public int ParticleType;
    
    public PortalTexture()
    {
        ParticleColour = 0x000000;
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

    public void writeTextureToStream(DataOutputStream stream) throws IOException
    {
        stream.writeUTF(Texture);
        stream.writeInt(TextureColour);
        stream.writeInt(ParticleColour);
        stream.writeInt(ParticleType);
    }
}
