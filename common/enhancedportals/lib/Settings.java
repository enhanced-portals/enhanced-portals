package enhancedportals.lib;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Settings
{
    public static boolean AllowPortalModifiers;
    public static boolean AllowDialHomeDevices;

    public static boolean DisableModifierRecipe;
    public static boolean DisableDHDRecipe;

    public static int PigmenLevel;

    @SideOnly(Side.CLIENT)
    public static int SoundLevel;
    @SideOnly(Side.CLIENT)
    public static int ParticleLevel;
    @SideOnly(Side.CLIENT)
    public static boolean RenderPortalEffect;
}
