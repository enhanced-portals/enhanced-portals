package enhancedportals.computercraft;

import enhancedportals.lib.Reference;

public class GlyphString
{
    public static boolean isValidGlyph(int id)
    {
        return id >= 0 && id < Reference.glyphItems.size();
    }
    
    public static String convertGlyphIdToString(int id)
    {
        return Reference.glyphItems.get(id).getItemName().replace("item.", "");
    }
    
    public static int convertGlyphStringToId(String str)
    {
        for (int i = 0; i < Reference.glyphItems.size(); i++)
        {
            if (Reference.glyphItems.get(i).getItemName().replace("item.", "").equals(str))
            {
                return i;
            }
        }
        
        return -1;
    }
    
    public static String getGlyphStringFromIdString(String str) throws Exception
    {        
        if (str.contains(" "))
        {
            String[] strs = str.split(" ");
            String dialString = "";
            
            for (String s : strs)
            {
                if (isValidGlyph(Integer.parseInt(s)))
                {
                    dialString = dialString + " " + convertGlyphIdToString(Integer.parseInt(s));
                }
            }
            
            return dialString.substring(1);
        }
        else if (isValidGlyph(Integer.parseInt(str)))
        {
            return convertGlyphIdToString(Integer.parseInt(str));
        }
        else
        {
            throw new Exception("Invalid input string");
        }
    }
}
