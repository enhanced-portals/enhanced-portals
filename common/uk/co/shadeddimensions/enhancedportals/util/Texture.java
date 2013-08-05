package uk.co.shadeddimensions.enhancedportals.util;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;

public class Texture
{
    public String Texture;
    public int TextureColour;
    public int ParticleColour;
    
    public Texture()
    {
        Texture = "";
        TextureColour = 0xFFFFFF;
        ParticleColour = 0xFFFFFF;
    }
    
    public Texture(String texture, int tColour, int pColour)
    {
        Texture = texture;
        TextureColour = tColour;
        ParticleColour = pColour;
    }
    
    public Texture(NBTTagCompound nbt)
    {
        Texture = nbt.getString("Texture");
        TextureColour = nbt.getInteger("TColour");
        ParticleColour = nbt.getInteger("PColour");
    }

    public void writeToNBT(NBTTagCompound nbt)
    {
        nbt.setString("Texture", Texture);
        nbt.setInteger("TColour", TextureColour);
        nbt.setInteger("PColour", ParticleColour);
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Texture)
        {
            Texture tex = (Texture) obj;
            
            return Texture.equals(tex.Texture) && TextureColour == tex.TextureColour && ParticleColour == tex.ParticleColour;
        }
        
        return false;
    }
    
    @Override
    public String toString()
    {
        return "Texture (\"" + Texture + "\" TextureColour: " + TextureColour + " ParticleColour: " + ParticleColour + ")";
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
}
