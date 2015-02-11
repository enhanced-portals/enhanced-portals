package enhancedportals.portal;

import java.util.Arrays;

public class GlyphSequence {
    int[] glyphs = new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1 };
    
    @Override
    public String toString() {
        String s = "";
        
        for (int i = 0; i < 9; i++) {
            if (glyphs[i] == -1) break;
            s = s + "-" + glyphs[i];
        }
        
        if (s.startsWith("-"))
            s = s.substring(1);
        
        return s;
    }
    
    public boolean isEmpty() {
        return glyphs[0] == -1;
    }
    
    public int getLength() {
        int length = 0;
        
        for (int i = 0; i < 9; i++) {
            if (glyphs[i] == -1) break;
            length++;
        }
        
        return length;
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(glyphs);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GlyphSequence) {
            GlyphSequence o = (GlyphSequence) obj;
            
            if (o.getLength() == getLength()) {
                try {
                    return getGlyphAt(1) == o.getGlyphAt(1) && getGlyphAt(2) == o.getGlyphAt(2) && getGlyphAt(3) == o.getGlyphAt(3) && getGlyphAt(4) == o.getGlyphAt(4) && getGlyphAt(5) == o.getGlyphAt(5) && getGlyphAt(6) == o.getGlyphAt(6) && getGlyphAt(7) == o.getGlyphAt(7) && getGlyphAt(8) == o.getGlyphAt(8) && getGlyphAt(9) == o.getGlyphAt(9);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        return false;
    }
    
    public int getGlyphAt(int p) throws Exception {
        if (p <= 0 || p > 9) throw new Exception("Invalid position. Found: " + p + " Expected: 1 - 9.");
        p -= 1;
        return glyphs[p];
    }
    
    public void setGlyph(int p, int g) throws Exception {
        if (p <= 0 || p > 9) throw new Exception("Invalid position. Found: " + p + " Expected: 1 - 9.");
        p -= 1;
        glyphs[p] = g;
    }
    
    public void setGlyphs(int[] g) {
        for (int i = 0; i < 9; i++) {
            if (g.length <= i)
                glyphs[i] = -1;
            else
                glyphs[i] = g[i];
        }
    }
    
    public void clearGlyph(int p) throws Exception {
        if (p <= 0 || p > 9) throw new Exception("Invalid position. Found: " + p + " Expected: 1 - 9.");
        p -= 1;
        glyphs[p] = -1;
    }
}
