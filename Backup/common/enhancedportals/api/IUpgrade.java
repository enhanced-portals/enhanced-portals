package enhancedportals.api;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import enhancedcore.util.BlockMetaPair;

public interface IUpgrade
{
    /***
     * Returns a list of any additional blocks to add to the border list.
     */
    public abstract List<BlockMetaPair> getAdditionalBorderBlocks();

    /***
     * Returns a list of any additional blocks to add to the destroy list.
     */
    public abstract List<BlockMetaPair> getAdditionalDestroyBlocks();

    /***
     * Returns the text when the upgrade is being hovered over in the inventory.
     */
    public abstract List<String> getHoverText();

    /***
     * Returns the item icon for this upgrade.
     */
    public abstract Icon getIcon();

    /***
     * Returns the ItemStack to display when the upgrade is installed in the
     * modifier.
     */
    public abstract ItemStack getInstalledItemStack();

    /***
     * Returns the localized name of the upgrade.
     */
    public abstract String getLocalizedName();

    /***
     * Must be unique to all upgrades added by this mod. Identifier will be
     * prefixed by the Mod ID of the mod adding the upgrade.
     */
    public abstract String getUniqueIdentifier();

    /***
     * Called when the upgrade is first installed in the modifier.
     */
    public abstract void onInstalled(int x, int y, int z);

    /***
     * Called when a portal has been successfully created.
     */
    public abstract void onPortalActivated(int x, int y, int z);

    /***
     * Called when a portal has been successfully removed.
     */
    public abstract void onPortalRemoved(int x, int y, int z);

    /***
     * Called before this upgrade gets installed.
     * 
     * @param upgradeIdentifiers
     *            A list of all the identifiers of the currently installed
     *            upgrades.
     * @return true if you want to cancel the upgrade from being installed.
     */
    public abstract boolean onPreInstalled(int x, int y, int z, List<String> upgradeIdentifiers);

    /***
     * Called before this upgrade gets removed.
     * 
     * @param upgradeIdentifiers
     *            A list of all the identifiers of the currently installed
     *            upgrades (contains self).
     * @return true if you want to cancel the upgrade from being removed.
     */
    public abstract boolean onPreRemoved(int x, int y, int z, List<String> upgradeIdentifiers);

    /***
     * Called when the upgrade is removed from the modifier.
     */
    public abstract void onRemoved(int x, int y, int z);
}
