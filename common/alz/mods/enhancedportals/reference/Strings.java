package alz.mods.enhancedportals.reference;

public class Strings
{
	// Block Names
	public static final String PortalModifier_Name = "portalModifier";
	public static final String ObsidianStairs_Name = "stairsObsidian";
	public static final String DialDevice_Name = "dialDevice";

	// Block Icons
	public static final String PortalModifier_Icon_Side = Reference.MOD_ID + ":" + PortalModifier_Name + "_side";
	public static final String PortalModifier_Icon_Active = Reference.MOD_ID + ":" + PortalModifier_Name + "_active_%s";
	public static final String NetherPortal_Icon = Reference.MOD_ID + ":netherPortal_%s";
	public static final String DialDevice_Icon_Side = Reference.MOD_ID + ":" + DialDevice_Name + "_side";
	public static final String DialDevice_Icon_Top = Reference.MOD_ID + ":" + DialDevice_Name + "_top";
	public static final String DialDevice_Icon_Bottom = Reference.MOD_ID + ":" + DialDevice_Name + "_bottom";

	// Item Names
	public static final String PortalModifierUpgrade_Name = "portalModifierUpgrade";

	public static final String PortalModifierUpgrade_MultiplePortals_Name = "item." + PortalModifierUpgrade_Name + ".multiplePortals";
	public static final String PortalModifierUpgrade_Dimensional_Name = "item." + PortalModifierUpgrade_Name + ".dimensional";
	public static final String PortalModifierUpgrade_AdvancedDimensional_Name = "item." + PortalModifierUpgrade_Name + ".advancedDimensional";
	public static final String PortalModifierUpgrade_ModifierCamo_Name = "item." + PortalModifierUpgrade_Name + ".modifierCamo";
	public static final String PortalModifierUpgrade_Computer_Name = "item." + PortalModifierUpgrade_Name + ".computer";
	public static final String PortalModifierUpgrade_Scroll_Name = "item." + PortalModifierUpgrade_Name + ".scroll";
	public static final String ItemScroll_Name = "teleportScroll";

	// Item Icons
	public static final String PortalModifierUpgrade_Icon = Reference.MOD_ID + ":" + PortalModifier_Name + "Upgrade_%s";
	public static final String ItemScroll_Icon = Reference.MOD_ID + ":" + ItemScroll_Name;

	// Tile Entities
	public static final String PortalModifier_TE_Name = "epmodif";
	public static final String DialDevice_TE_Name = DialDevice_Name;

	// Portal
	public static final String Portal_NoExitFound = "portal.noExitFound";
	public static final String Portal_MissingUpgrade = "portal.missingUpgrade";
	public static final String Portal_ExitBlocked = "portal.exitBlocked";

	// Console
	public static final String Console_NoExitFound = "console.portal.noExitFound";
	public static final String Console_MissingUpgrade = "console.portal.missingUpgrade";
	public static final String Console_ExitBlocked = "console.portal.exitBlocked";
	public static final String Console_sizeFail = "console.portal.sizeFail";
	public static final String Console_success = "console.portal.success";
	public static final String Console_fail = "console.portal.fail";

	// GUI
	public static final String GUI_ClearCharacter = "gui.portalModifier.clearCharacter"; // C
	public static final String GUI_SaveCharacter = "gui.portalModifier.saveCharacter"; // S
	public static final String GUI_ClearFrequency = "gui.portalModifier.clearFrequency"; // Clear the frequency
	public static final String GUI_SaveFrequency = "gui.portalModifier.saveFrequency"; // Save the frequency
	public static final String GUI_Frequency = "gui.portalModifier.frequency"; // Frequency

	public static final String GUI_Accept = "gui.accept"; // Accept
	public static final String GUI_Cancel = "gui.cancel"; // Cancel
	public static final String GUI_Name = "gui.name"; // Name
	public static final String GUI_Add = "gui.add"; // Add
	public static final String GUI_Remove = "gui.remove"; // Remove
	public static final String GUI_Edit = "gui.edit"; // Edit
	public static final String GUI_Close = "gui.close"; // Close

	public static final String GUI_DialDevice_Selected = "gui.dialDevice.selected"; // Selected
	public static final String GUI_DialDevice_NotSelected = "gui.dialDevice.notSelected"; // Double click to select
	public static final String GUI_DialDevice_Coords = "gui.dialDevice.coords"; // %s - X: %s Y: %s Z: %s
	public static final String GUI_DialDevice_Scroll = "gui.dialDevice.scroll"; // Scroll
	public static final String GUI_DialDevice_Modifier = "gui.dialDevice.modifier"; // Modifier
	public static final String GUI_DialDevice_Invalid1a = "gui.dialDevice.invalid.1a"; // The teleportation scroll is invalid
	public static final String GUI_DialDevice_Invalid1b = "gui.dialDevice.invalid.1b"; // An upgrade is required
	public static final String GUI_DialDevice_Invalid2a = "gui.dialDevice.invalid.2a"; // A teleportation scroll is required
	public static final String GUI_DialDevice_Invalid2b = "gui.dialDevice.invalid.2b"; // 
}
