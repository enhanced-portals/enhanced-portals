package enhancedportals.lib;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;

public class PortalTexture
{
    public enum Type
    {
        COLOUR, BLOCK, UNKNOWN;
    }
    
    public enum Colour
    {
        PURPLE, RED, GREEN, BROWN, BLUE, BLACK, CYAN, LIGHT_GRAY, GRAY, PINK, LIME, YELLOW, LIGHT_BLUE, MAGENTA, ORANGE, WHITE;
        
        public static Colour getColour(int id)
        {
            if (id < 0 || id > 15)
            {
                return Colour.PURPLE;
            }
            
            return values()[id];
        }
    }
    
    public int blockID = -1, metaData = -1;
    public Colour colour;
    public Type type;
    
    private static int[] PARTICLE_COLOURS = { 1973019, 11743532, 3887386, 5320730, 2437522, 8073150, 2651799, 11250603, 4408131, 14188952, 4312372, 14602026, 6719955, 12801229, 15435844, 15790320, 15435844, 2437522, 15435844, 2437522, 0 };
    private static Icon[] TEXTURE_ICONS = new Icon[Colour.values().length];
    private static Icon[] PORTAL_MODIFIER_ICONS = new Icon[Colour.values().length];
    
    public PortalTexture(int colourID)
    {
        blockID = -1;
        metaData = -1;
        colour = Colour.getColour(colourID);
        type = Type.COLOUR;
    }
    
    public PortalTexture(Colour color)
    {
        blockID = -1;
        metaData = -1;
        colour = color;
        type = Type.COLOUR;
    }
    
    public PortalTexture(int blockID, int meta)
    {
        colour = null;
        type = Type.BLOCK;
        this.blockID = blockID;
        this.metaData = meta;
    }
    
    public Icon getIcon(int side)
    {        
        if (type == Type.COLOUR)
        {
            return TEXTURE_ICONS[colour.ordinal()];
        }
        else if (type == Type.BLOCK)
        {
            if (blockID == 9 || blockID == 11) // We want to display the still versions of these.
            {
                return Block.blocksList[blockID].getIcon(0, 0);
            }
            else if (blockID == 8 || blockID == 10) // We want to make sure that the flowing texture is visible on all sides.
            {
                return Block.blocksList[blockID].getIcon(2, 2);
            }
            
            return Block.blocksList[blockID].getIcon(side, metaData);
        }
        
        return null;
    }
    
    public Icon getModifierIcon()
    {
        if (type == Type.COLOUR)
        {
            return PORTAL_MODIFIER_ICONS[colour.ordinal()];
        }
        else if (type == Type.BLOCK)
        {
            return PORTAL_MODIFIER_ICONS[0];
        }
        
        return null;
    }
    
    public int getParticleColour()
    {
        if (type == Type.COLOUR)
        {
            return PARTICLE_COLOURS[colour.ordinal()];
        }
        else if (type == Type.BLOCK)
        {
            return PARTICLE_COLOURS[0];
        }
        
        return 0;
    }
    
    public static int swapColours(int integer)
    {
        if (integer == 0)
            integer = 5;
        else if (integer == 5)
            integer = 0;
        
        return integer;
    }
    
    public boolean isEqualTo(PortalTexture texture)
    {
        // TODO
        //System.out.println(String.format("Types: %s, %s. BlockIDs: %s, %s. MetaData: %s, %s. Colour: %s, %s. Match: %s", type, texture.type, blockID, texture.blockID, metaData, texture.metaData, colour, texture.colour, (type == texture.type && blockID == texture.blockID && metaData == texture.metaData && colour == texture.colour)));
        if (type.ordinal() == texture.type.ordinal())
        {
            if (type == Type.BLOCK)
            {
                if (blockID == texture.blockID && metaData == texture.metaData)
                {
                    return true;
                }
            }
            else if (type == Type.COLOUR)
            {
                if (colour.ordinal() == texture.colour.ordinal())
                {
                    return true;
                }
            }
        }
        
        return false;
        
        //return type.ordinal() == texture.type.ordinal() && blockID == texture.blockID && metaData == texture.metaData && colour == texture.colour;
    }

    public static void registerTextures(IconRegister iconRegister)
    {
        TEXTURE_ICONS = new Icon[Colour.values().length];
        PORTAL_MODIFIER_ICONS = new Icon[Colour.values().length];

        for (int i = 0; i < 16; i++)
        {
            TEXTURE_ICONS[i] = iconRegister.registerIcon("enhancedportals:netherPortal_" + i);
            PORTAL_MODIFIER_ICONS[i] = iconRegister.registerIcon("enhancedportals:portalModifier_Active_" + i);
        }
    }
}
