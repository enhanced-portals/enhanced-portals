package alz.mods.enhancedportals.portals;

import net.minecraft.item.ItemDye;
import net.minecraft.util.StringTranslate;

public enum PortalTexture
{
	PURPLE(0),
	RED(1),
	GREEN(2),
	BROWN(3),
	BLUE(4),
	BLACK(5),
	CYAN(6),
	LIGHT_GRAY(7),
	GRAY(8),
	PINK(9),
	LIME(10),
	YELLOW(11),
	LIGHT_BLUE(12),
	MAGENTA(13),
	ORANGE(14),
	WHITE(15),
	LAVA(16),
	WATER(17),
	UNKNOWN(18);
	
	private static PortalTexture[] VALID_TEXTURES = { PURPLE, RED, GREEN, BROWN, BLUE, BLACK, CYAN, LIGHT_GRAY, GRAY, PINK, LIME, YELLOW, LIGHT_BLUE, MAGENTA, ORANGE, WHITE, LAVA, WATER };
	private static int[] PARTICLE_COLOURS = { 1973019, 11743532, 3887386, 5320730, 2437522, 8073150, 2651799, 11250603, 4408131, 14188952, 4312372, 14602026, 6719955, 12801229, 15435844, 15790320, 15435844, 2437522 };
	public final int portalColour;
	
	private PortalTexture(int id)
	{
		portalColour = id;
		ordinal();
	}
	
	public static int getParticleColour(int id)
	{
		if (id < 0)
		{
			id = 0;
		}
		else if (id > PARTICLE_COLOURS.length)
		{
			id = PARTICLE_COLOURS.length;
		}
		
		return PARTICLE_COLOURS[id];
	}
	
	public static int SwapColours(int id)
	{
		if (id == 0)
		{
			id = 5;
		}
		else if (id == 5)
		{
			id = 0;
		}
		
		return id;
	}
	
	public int GetSwapped()
	{
		return SwapColours(ordinal());
	}
	
	public int Get()
	{
		return ordinal();
	}
	
	public static PortalTexture getPortalTexture(int id)
	{
		if (id >= 0 && id < VALID_TEXTURES.length)
		{
			return VALID_TEXTURES[id];
		}
		
		return UNKNOWN;
	}
		
	public static String GetLocalizedName(int id)
	{
		return GetLocalizedName(getPortalTexture(id));
	}
	
	public static String GetLocalizedName(PortalTexture texture)
	{
		StringTranslate translate = StringTranslate.getInstance();
				
		if (texture == LAVA)
		{
			return translate.translateKey("tile.lava.name");
		}
		else if (texture == WATER)
		{
			return translate.translateKey("tile.water.name");
		}
		else if (texture != UNKNOWN)
		{
			return translate.translateKey("item.fireworksCharge." + ItemDye.dyeColorNames[SwapColours(texture.ordinal())]);
		}
		
		return "Unknown";
	}
	
	public String GetLocalizedName()
	{
		return GetLocalizedName(ordinal());
	}
}
