package enhancedportals.lib;

import net.minecraft.util.EnumChatFormatting;

public enum Strings
{
    Accept("gui.accept"),
    Cancel("gui.cancel"),
    ChatAdvDimAlreadyInstalled("chat.advDimUpgradeAlreadyInstalled", EnumChatFormatting.RED),
    ChatDimAlreadyInstalled("chat.dimUpgradeAlreadyInstalled", EnumChatFormatting.RED),
    ChatMaxUpgradesInstalled("chat.maxUpgradesInstalled", EnumChatFormatting.RED),
    ChatUpgradeInstalled("chat.upgradeAlreadyInstalled", EnumChatFormatting.RED),
    Clear("gui.clear"),
    ClickToSetIdentifier("gui.setIdentifier"),
    ClickToSetNetwork("gui.setNetwork"),
    Dial("gui.dial"),
    Facade("gui.facade"),
    FullBlock("gui.fullBlock"),
    Glyphs("gui.glyphs"),
    IdentifierSelection("gui.selectIdentifier"),
    Inverted("gui.inverted"),
    Modifications("gui.modifications"),
    ModifierActive("gui.activeModifier"),
    Network("gui.network"),
    NetworkSelection("gui.networkSelection"),
    Normal("gui.normal"),
    PortalModifierUpgrade("upgrade.portalModifier", EnumChatFormatting.GOLD),
    Precise("gui.precise"),
    Random("gui.random"),
    RedstoneControl("gui.redstoneControl"),
    RemoveUpgrade("gui.upgrade.remove", EnumChatFormatting.DARK_GRAY),
    RightClickToReset("gui.rightClickToReset"),
    Thick("gui.thick"),
    Thicker("gui.thicker"),
    Thickness("gui.thickness"),
    UniqueIdentifier("gui.uniqueIdentifier"),
    Upgrades("gui.upgrades"),
    Save("gui.save");

    private EnumChatFormatting chatFormatting;
    private String             text;

    private Strings(String str)
    {
        text = str;
        chatFormatting = null;
    }

    private Strings(String str, EnumChatFormatting formatting)
    {
        text = str;
        chatFormatting = formatting;
    }

    @Override
    public String toString()
    {
        return (chatFormatting != null ? chatFormatting.toString() : "") + Localization.localizeString(text);
    }
}
