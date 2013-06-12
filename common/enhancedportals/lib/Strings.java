package enhancedportals.lib;

import net.minecraft.util.EnumChatFormatting;

public enum Strings
{
    Accept("gui.accept"),
    Add("gui.add"),
    Cancel("gui.cancel"),
    ChatAdvDimAlreadyInstalled("chat.advDimUpgradeAlreadyInstalled", EnumChatFormatting.RED),
    ChatDialActivePortal("chat.noDialActivePortal", EnumChatFormatting.RED),
    ChatDialSuccess("chat.dialSuccessA", EnumChatFormatting.GREEN),
    ChatDialSuccess2("chat.dialSuccessB", EnumChatFormatting.GREEN),
    ChatDimAlreadyInstalled("chat.dimUpgradeAlreadyInstalled", EnumChatFormatting.RED),
    ChatInvalidDestination("chat.invalidDestination", EnumChatFormatting.RED),
    ChatMaxUpgradesInstalled("chat.maxUpgradesInstalled", EnumChatFormatting.RED),
    ChatMissingUpgrade("chat.missingUpgrade", EnumChatFormatting.RED),
    ChatModifierActive("chat.modifierActive", EnumChatFormatting.RED),
    ChatNetworkSuccessful("chat.networkSuccessful", EnumChatFormatting.GREEN),
    ChatNoConnection("chat.noConnection", EnumChatFormatting.RED),
    ChatNoLinkedPortals("chat.noLinkedPortals", EnumChatFormatting.RED),
    ChatNoModifiers("chat.noModifiers", EnumChatFormatting.RED),
    ChatNotLinked("chat.notLinked", EnumChatFormatting.RED),
    ChatUpgradeInstalled("chat.upgradeAlreadyInstalled", EnumChatFormatting.RED),
    Clear("gui.clear"),
    ClickToSetIdentifier("gui.setIdentifier"),
    ClickToSetNetwork("gui.setNetwork"),
    Dial("gui.dial"),
    DontShutdownAuto("gui.dontShutdownAuto"),
    Facade("gui.facade"),
    FullBlock("gui.fullBlock"),
    Glyphs("gui.glyphs"),
    IdentifierInUse("gui.identifierInUse"),
    IdentifierSelection("gui.selectIdentifier"),
    Inverted("gui.inverted"),
    Modifications("gui.modifications"),
    ModifierActive("gui.activeModifier"),
    Network("gui.network"),
    NetworkNotSet("gui.networkNotSet"),
    NetworkSelection("gui.networkSelection"),
    Normal("gui.normal"),
    PortalModifierUpgrade("upgrade.portalModifier", EnumChatFormatting.GOLD),
    Precise("gui.precise"),
    Random("gui.random"),
    RedstoneControl("gui.redstoneControl"),
    Remove("gui.remove"),
    RemoveUpgrade("gui.upgrade.remove", EnumChatFormatting.DARK_GRAY),
    RightClickToReset("gui.rightClickToReset"),
    Save("gui.save"),
    Terminate("gui.terminate"),
    Thick("gui.thick"),
    Thicker("gui.thicker"),
    Thickness("gui.thickness"),
    UniqueIdentifier("gui.uniqueIdentifier"),
    Upgrades("gui.upgrades");

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
