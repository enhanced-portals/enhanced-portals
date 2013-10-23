package uk.co.shadeddimensions.ep3.portal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.nbt.NBTTagCompound;

public class GlyphIdentifier
{
    public static final String GLYPH_SEPERATOR = " ";
    int[] glyphs;

    public GlyphIdentifier()
    {
        glyphs = new int[9];
    }

    public GlyphIdentifier(int[] Glyphs)
    {
        glyphs = Glyphs;
    }

    public GlyphIdentifier(String s)
    {
        glyphs = parseGlyphString(s);
        
        if (glyphs == null)
        {
            glyphs = new int[9];
        }
    }

    public GlyphIdentifier(DataInputStream s) throws IOException
    {
        this(s.readUTF());
    }
    
    public GlyphIdentifier(NBTTagCompound tag)
    {
        this(tag.getString("GlyphIdentifier"));
    }
    
    public static int[] parseGlyphString(String str)
    {
        if (str == null || str.length() == 0)
        {
            return null;
        }
        
        int[] Glyphs = new int[9];

        try
        {
            if (str.contains(GLYPH_SEPERATOR))
            {
                String[] sGlyphs = str.split(GLYPH_SEPERATOR);
                
                for (int i = 0; i < sGlyphs.length; i++)
                {
                    Glyphs[i] = Integer.parseInt(str);
                }
            }
            else
            {
                Glyphs[0] = Integer.parseInt(str);
            }
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
            return null;
        }

        return Glyphs;
    }
    
    public String getGlyphString()
    {
        return getGlyphString(glyphs);
    }
    
    public static String getGlyphString(int[] glyph)
    {
        String s = "";
        
        for (int i : glyph)
        {
            s += GLYPH_SEPERATOR + i;
        }
        
        return s.substring(GLYPH_SEPERATOR.length());
    }

    public int[] getGlyps()
    {
        return glyphs;
    }

    public void setGlyphs(int[] glyph)
    {
        glyphs = glyph;
    }
    
    public void setGlyphs(String str)
    {
        glyphs = parseGlyphString(str);
    }

    public int getGlyph(int id)
    {
        return glyphs[id];
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof GlyphIdentifier)
        {            
            return glyphs.equals(((GlyphIdentifier) obj).glyphs);
        }

        return false;
    }

    @Override
    public String toString()
    {
        return String.format("GlyphIdentifier (%s)", glyphs);
    }
    
    public void writeToNBT(NBTTagCompound tag)
    {
        tag.setString("GlyphIdentifier", getGlyphString());
    }
    
    public void readFromNBT(NBTTagCompound tag)
    {
        setGlyphs(tag.getString("GlyphIdentifier"));
    }
    
    public void writeToStream(DataOutputStream stream) throws IOException
    {
        stream.writeUTF(getGlyphString());
    }
    
    public void readFromStream(DataInputStream stream) throws IOException
    {
        setGlyphs(stream.readUTF());
    }
}
