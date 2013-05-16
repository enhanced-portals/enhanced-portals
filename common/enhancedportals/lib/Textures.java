package enhancedportals.lib;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;

public class Textures
{
    public static List<IIconRegister> IconsToRegister = new ArrayList<IIconRegister>();
    public static final int[] PARTICLE_COLOURS = { 1973019, 11743532, 3887386, 5320730, 2437522, 8073150, 2651799, 11250603, 4408131, 14188952, 4312372, 14602026, 6719955, 12801229, 15435844, 15790320, 15435844, 2437522, 15435844, 2437522, 0 };
    public static Icon[] TEXTURE_ICONS = new Icon[16];
    public static Icon[] PORTAL_MODIFIER_ICONS = new Icon[16];
    
    public static void registerTextures(IconRegister iconRegister)
    {
        TEXTURE_ICONS = new Icon[16];
        PORTAL_MODIFIER_ICONS = new Icon[16];

        for (int i = 0; i < 16; i++)
        {
            TEXTURE_ICONS[i] = iconRegister.registerIcon(Reference.MOD_ID + ":netherPortal_" + i);
            PORTAL_MODIFIER_ICONS[i] = iconRegister.registerIcon(Reference.MOD_ID + ":portalModifier_active_" + i);
        }
        
        for (IIconRegister icon : IconsToRegister)
        {
            icon.registerIcons(iconRegister);
        }
    }
}
