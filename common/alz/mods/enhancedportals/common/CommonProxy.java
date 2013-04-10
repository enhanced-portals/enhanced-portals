package alz.mods.enhancedportals.common;

import alz.mods.enhancedportals.reference.Reference;
import alz.mods.enhancedportals.reference.Settings;

public class CommonProxy
{
    public void SetupConfiguration()
    {
        Settings.AllowTeleporting = Settings.GetFromConfig("AllowTeleporting", Settings.AllowTeleporting_Default);
        Settings.CanDyePortals = Settings.GetFromConfig("CanDyePortals", Settings.CanDyePortals_Default);
        Settings.DoesDyingCost = Settings.GetFromConfig("DoesDyingCost", Settings.DoesDyingCost_Default);
        Settings.CanDyeByThrowing = Settings.GetFromConfig("CanDyeByThrowing", Settings.CanDyeByThrowing_Default);
        Settings.AllowModifiers = Settings.GetFromConfig("AllowModifiers", Settings.AllowModifiers_Default);
        Settings.AllowObsidianStairs = Settings.GetFromConfig("AllowObsidianStairs", Settings.AllowObsidianStairs_Default);
        Settings.AllowDialDevice = Settings.GetFromConfig("AllowDialDevice", Settings.AllowDialDevice_Default);
        Settings.PrintPortalMessages = Settings.GetFromConfig("PrintPortalCreationMessages", Settings.PrintPortalMessages_Default);

        Settings.MaximumPortalSize = Settings.GetFromConfig("MaximumPortalSize", Settings.MaximumPortalSize_Default);
        Settings.PigmenSpawnChance = Settings.GetFromConfig("PigmenSpawnChance", 100, 0, 100);

        Reference.BlockIDs.ObsidianStairs = Settings.GetFromConfig("ObsidianStairsID", Reference.BlockIDs.ObsidianStairs_Default, true);
        Reference.BlockIDs.PortalModifier = Settings.GetFromConfig("PortalModifierID", Reference.BlockIDs.PortalModifier_Default, true);
        Reference.BlockIDs.DialDevice = Settings.GetFromConfig("DialDeviceID", Reference.BlockIDs.DialDevice_Default, true);

        Reference.ItemIDs.PortalModifierUpgrade = Settings.GetFromConfig("PortalModifierUpgradeID", Reference.ItemIDs.PortalModifierUpgrade_Default, false);
        Reference.ItemIDs.ItemScroll = Settings.GetFromConfig("ItemScrollID", Reference.ItemIDs.ItemScroll_Default, false);

        Settings.AddToBorderBlocks(Settings.GetFromConfig("CustomBorderBlocks", ""));
        Settings.AddToDestroyBlocks(Settings.GetFromConfig("CustomDestroyBlocks", "8,9,10,11,6,18,30,31,32,37,38,39,40,78,104,105,106,111,115"));

        Settings.ConfigFile.save();
    }
}
