package enhancedportals.lib;

public class Commands
{
    // GENERAL
    public static final String ON = "on";
    public static final String OFF = "off";
    public static final String ADD = "add";
    public static final String REMOVE = "remove";
    public static final String RESET = "reset";
    public static final String CLEAR = "clear";
    public static final String LIST = "list";

    public static final String USAGE = "ep [effects/blocks/control]";
    public static final String ONOFF_USAGE = "ep %s [on/off]";
    public static final String BLOCKID_USAGE = "ep %s [blockid]";

    // EFFECTS
    public static final String EFFECTS = "effects";
    public static final String EFFECTS_USAGE = "ep effects [sounds/particles/portalEffects]";
    public static final String SOUNDS = "sounds";
    public static final String PARTICLES = "particles";
    public static final String PORTAL_EFFECTS = "portalEffects";

    // BLOCKS
    public static final String BLOCKS = "blocks";
    public static final String BLOCKS_USAGE = "ep blocks [border/destroy]";
    public static final String BORDER_BLOCKS = "border";
    public static final String BORDER_BLOCKS_USAGE = "ep blocks border [add/remove/reset/clear/list]";
    public static final String DESTROY_BLOCKS = "destroy";
    public static final String DESTROY_BLOCKS_USAGE = "ep blocks destroy [add/remove/reset/clear/list]";

    // CONTROL
    public static final String CONTROL = "control";

    // Localization strings
    public static final String PARTICLES_TURNED_ON = "chat.particles.on";
    public static final String PARTICLES_TURNED_OFF = "chat.particles.off";
    public static final String SOUNDS_TURNED_ON = "chat.sounds.on";
    public static final String SOUNDS_TURNED_OFF = "chat.sounds.off";
    public static final String PORTAL_EFFECTS_TURNED_ON = "chat.portalEffects.on";
    public static final String PORTAL_EFFECTS_TURNED_OFF = "chat.portalEffects.off";

    public static final String DESTROY_BLOCKS_RESET = "chat.destroyBlocks.reset";
    public static final String BORDER_BLOCKS_RESET = "chat.borderBlocks.reset";
    public static final String DESTROY_BLOCKS_CLEAR = "chat.destroyBlocks.clear";
    public static final String BORDER_BLOCKS_CLEAR = "chat.borderBlocks.clear";
    public static final String BORDER_BLOCKS_ADD_SUCCESS = "chat.borderBlocks.addSuccess";
    public static final String BORDER_BLOCKS_REMOVE_SUCCESS = "chat.borderBlocks.removeSuccess";
}
