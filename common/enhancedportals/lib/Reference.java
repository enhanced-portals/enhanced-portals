package enhancedportals.lib;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import enhancedportals.creativetab.CreativeTabEP;

public class Reference
{
    public static final String MOD_ID = "EP2";
    public static final String MOD_NAME = "EnhancedPortals 2";
    public static final String MOD_VERSION = "@VERSION@ (Build: @BUILD_NUMBER@)";

    public static final String PROXY_CLIENT = "enhancedportals.network.ClientProxy";
    public static final String PROXY_COMMON = "enhancedportals.network.CommonProxy";

    public static CreativeTabs CREATIVE_TAB = new CreativeTabEP();

    public static final String RESOURCE_NAME = MOD_ID.toLowerCase();
    public static final String RESOURCE_LOCATION = "/assets/" + RESOURCE_NAME + "/";
    public static final String LOCALE_LOCATION = RESOURCE_LOCATION + "lang/";
    public static final String TEXTURE_LOCATION = RESOURCE_LOCATION + "textures/";
    public static final String GUI_LOCATION = TEXTURE_LOCATION + "gui/";

    public static final List<ItemStack> glyphItems = new ArrayList<ItemStack>();
    public static final String glyphSeperator = " ";

    public static Logger log = Logger.getLogger("EnhancedPortals 2");
}
