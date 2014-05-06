package enhancedportals.portal;

public class GlyphElement
{
    public String name;
    public GlyphIdentifier identifier;
    public PortalTextureManager texture;

    public GlyphElement(String n, GlyphIdentifier i)
    {
        name = n;
        identifier = i;
        texture = null;
    }

    public GlyphElement(String n, GlyphIdentifier i, PortalTextureManager t)
    {
        this(n, i);
        texture = t;
    }

    public boolean hasSpecificTexture()
    {
        return texture != null;
    }
}
