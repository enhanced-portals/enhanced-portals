package enhancedportals.utility;

import li.cil.oc.api.network.Arguments;
import enhancedportals.portal.GlyphIdentifier;

public class ComputerUtils
{
    public static Object[] argsToArray(Arguments args)
    {
        Object[] data = new Object[args.count()];

        for (int i = 0; i < data.length; i++)
        {
            // The explicit check for strings is required because OC returns
            // them as byte arrays otherwise (because that's what they are in
            // Lua). Which is usually not what we wan when auto-converting.
            if (args.isString(i))
            {
                data[i] = args.checkString(i);
            }
            else
            {
                data[i] = args.checkAny(i);
            }
        }

        return data;
    }

    public static String verifyGlyphArguments(String s)
    {
        if (s.length() == 0)
        {
            return "Glyph ID is not formatted correctly";
        }

        if (s.contains(GlyphIdentifier.GLYPH_SEPERATOR))
        {
            String[] nums = s.split(GlyphIdentifier.GLYPH_SEPERATOR);

            if (nums.length > 9)
            {
                return "Glyph ID is too long. Must be a maximum of 9 IDs";
            }

            for (String num : nums)
            {

                int n = Integer.parseInt(num);

                if (n < 0 || n > 27)
                {
                    return "Glyph ID must be between 0 and 27 (inclusive)";
                }
            }
        }
        else
        {
            int n = Integer.parseInt(s);

            if (n < 0 || n > 27)
            {
                return "Glyph ID must be between 0 and 27 (inclusive)";
            }
        }

        return null; // All OK
    }
}
