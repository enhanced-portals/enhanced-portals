package uk.co.shadeddimensions.ep3.portal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.nbt.NBTTagCompound;

public class GlyphIdentifier
{
    public static final String GLYPH_SEPERATOR = "-";
    ArrayList<Integer> glyphs;

    public GlyphIdentifier()
    {
        glyphs = new ArrayList<Integer>();
    }
    
    public GlyphIdentifier(GlyphIdentifier i)
    {
        glyphs = new ArrayList<Integer>(i.glyphs.size());
        
        for (int I : i.glyphs)
        {
            glyphs.add(I);
        }
    }

    public GlyphIdentifier(int[] Glyphs)
    {
        this();

        for (int i = 0; i < Glyphs.length; i++)
        {
            glyphs.add(Glyphs[i]);
        }        
    }

    public GlyphIdentifier(String s)
    {
        glyphs = parseGlyphString(s);

        if (glyphs == null)
        {
            glyphs = new ArrayList<Integer>();
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

    public GlyphIdentifier(ArrayList<Integer> list)
    {
        glyphs = list;
    }

    public static ArrayList<Integer> parseGlyphString(String str)
    {
        if (str == null || str.length() == 0)
        {
            return null;
        }

        ArrayList<Integer> Glyphs = new ArrayList<Integer>();

        try
        {
            if (str.contains(GLYPH_SEPERATOR))
            {
                String[] sGlyphs = str.split(GLYPH_SEPERATOR);

                for (int i = 0; i < sGlyphs.length; i++)
                {
                    Glyphs.add(Integer.parseInt(sGlyphs[i]));
                }
            }
            else
            {
                Glyphs.add(Integer.parseInt(str));
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

    public static String getGlyphString(ArrayList<Integer> glyph)
    {
        String s = "";

        for (int i : glyph)
        {
            s += GLYPH_SEPERATOR + i;
        }
        
        return s.length() > 0 ? s.substring(GLYPH_SEPERATOR.length()) : "";
    }

    public ArrayList<Integer> getGlyphs()
    {
        return glyphs;
    }

    public void setGlyphs(ArrayList<Integer> glyph)
    {
        glyphs = glyph;
    }

    public void setGlyphs(String str)
    {
        glyphs = parseGlyphString(str);
    }

    public int getGlyph(int id)
    {
        return glyphs.get(id);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof GlyphIdentifier)
        {
            GlyphIdentifier id = (GlyphIdentifier) obj;
                        
            if (size() == id.size())
            {
                for (int i = 0; i < size(); i++)
                {
                    if (get(i) != id.get(i))
                    {
                        return false;
                    }
                }
                
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString()
    {
        return String.format("GlyphIdentifier (%s)", getGlyphString());
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

    public boolean hasGlyph(int glyph)
    {
        for (int i : glyphs)
        {
            if (i == glyph)
            {
                return true;
            }
        }

        return false;
    }

    public boolean isEmpty()
    {
        return glyphs.isEmpty();
    }

    public void addGlyph(int glyph)
    {
        if (glyphs.size() < 9)
        {
            glyphs.add(glyph);
        }
    }

    public int size()
    {
        return glyphs.size();
    }

    public int get(int id)
    {
        return glyphs.get(id);
    }

    public void remove(int index)
    {
        glyphs.remove(index);
    }

    public void removeLast(int glyph)
    {
        if (hasGlyph(glyph))
        {
            for (int i = size() - 1; i >= 0; i--)
            {
                if (get(i) == glyph)
                {
                    remove(i);
                    break;
                }
            }
        }
    }
}
