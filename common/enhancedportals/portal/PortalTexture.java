package enhancedportals.portal;

import net.minecraft.block.Block;
import net.minecraft.util.Icon;
import enhancedportals.lib.Textures;

public class PortalTexture
{
    public static Icon getDefaultModifierTexture()
    {
        return Textures.getTexture("").getModifierTexture();
    }

    public static int getDefaultParticleColour()
    {
        return Textures.getTexture("").getParticleColour();
    }

    public static Icon getDefaultPortalTexture()
    {
        return Textures.getTexture("").getPortalTexture();
    }

    private String ID;

    private Icon portalTexture;

    private Icon modifierTexture;

    private int particleColour;

    public PortalTexture(String id)
    {
        ID = id;
        portalTexture = null;
        modifierTexture = null;
        particleColour = -1;
    }

    public PortalTexture(String id, Icon portaltexture)
    {
        ID = id;
        portalTexture = portaltexture;
        modifierTexture = null;
        particleColour = -1;
    }

    public PortalTexture(String id, Icon portaltexture, Icon modifiertexture)
    {
        ID = id;
        portalTexture = portaltexture;
        modifierTexture = modifiertexture;
        particleColour = -1;
    }

    public PortalTexture(String id, Icon portaltexture, Icon modifiertexture, int particlecolour)
    {
        ID = id;
        portalTexture = portaltexture;
        modifierTexture = modifiertexture;
        particleColour = particlecolour;
    }

    public PortalTexture(String id, Icon portaltexture, int particlecolour)
    {
        ID = id;
        portalTexture = portaltexture;
        modifierTexture = null;
        particleColour = particlecolour;
    }

    public String getID()
    {
        return ID;
    }

    public Icon getModifierTexture()
    {
        return modifierTexture != null ? modifierTexture : getDefaultModifierTexture();
    }

    public Icon getModifierTexture(Object... objects)
    {
        return getModifierTexture();
    }

    public int getParticleColour()
    {
        return particleColour != -1 ? particleColour : getDefaultParticleColour();
    }

    public Icon getPortalTexture()
    {
        if (ID.startsWith("B:") && portalTexture == null)
        {
            int id = Integer.parseInt(ID.substring(2).split(":")[0]), meta = Integer.parseInt(ID.substring(2).split(":")[1]);

            return getPortalTexture(id, meta);
        }
        else if (portalTexture != null)
        {
            return portalTexture;
        }

        return getDefaultPortalTexture();
    }

    public Icon getPortalTexture(Object... objects)
    {
        if (objects.length == 2)
        {
            if (objects[0] instanceof Integer && objects[1] instanceof Integer)
            {
                return Block.blocksList[Integer.parseInt(objects[0].toString())].getIcon(2, Integer.parseInt(objects[1].toString()));
            }
        }

        return getDefaultPortalTexture();
    }
}
