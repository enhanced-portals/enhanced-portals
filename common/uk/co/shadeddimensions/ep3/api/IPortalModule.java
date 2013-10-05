package uk.co.shadeddimensions.ep3.api;

import net.minecraft.item.ItemStack;
import uk.co.shadeddimensions.ep3.client.particle.PortalFX;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileModuleManipulator;

public interface IPortalModule
{
    /***
     * Gets whether or not the upgrade can be installed into this module manipulator.
     * 
     * @param moduleManipulator
     * @param installedUpgrades
     *            All the upgrades currently installed.
     * @param upgrade
     * @return True if it can, false if it can't.
     */
    public boolean canInstallUpgrade(TileModuleManipulator moduleManipulator, IPortalModule[] installedUpgrades, ItemStack upgrade);

    /***
     * Gets whether or not the upgrade can be removed from this module manipulator.
     * 
     * @param moduleManipulator
     * @param installedUpgrades
     *            All the upgrades currently installed. Includes the upgrade calling this.
     * @param upgrade
     * @return True if it can, false if it can't.
     */
    public boolean canRemoveUpgrade(TileModuleManipulator moduleManipulator, IPortalModule[] installedUpgrades, ItemStack upgrade);

    /***
     * Return true to disable particles from being created.
     * 
     * @param moduleManipulator
     * @param upgrade
     * @return True to disable particles.
     */
    public boolean disableParticles(TileModuleManipulator moduleManipulator, ItemStack upgrade);

    /***
     * Return true to stop the portal from being renderered.
     * 
     * @param moduleManipulator
     * @param upgrade
     * @return True to disable rendering.
     */
    public boolean disablePortalRendering(TileModuleManipulator modulemanipulator, ItemStack upgrade);

    /***
     * Gets the ID for this upgrade. There is no validation for this -- Multiple upgrades can have the same ID, but may cause issues.
     * 
     * @param upgrade
     *            The upgrade itself.
     * @return Identifier
     */
    public String getID(ItemStack upgrade);

    /***
     * Called when a single particle gets created.
     * 
     * @param moduleManipulator
     * @param upgrade
     */
    public void onParticleCreated(TileModuleManipulator moduleManipulator, ItemStack upgrade, PortalFX particle);

    /***
     * Called when a portal gets created.
     * 
     * @param moduleManipulator
     * @param upgrade
     */
    public void onPortalCreated(TileModuleManipulator moduleManipulator, ItemStack upgrade);

    /***
     * Called when a portal gets removed.
     * 
     * @param moduleManipulator
     * @param upgrade
     */
    public void onPortalRemoved(TileModuleManipulator moduleManipulator, ItemStack upgrade);

    /***
     * Called when this upgrade gets installed.
     * 
     * @param moduleManipulator
     * @param upgrade
     */
    public void onUpgradeInstalled(TileModuleManipulator moduleManipulator, ItemStack upgrade);

    /***
     * Called when this upgrade gets removed.
     * 
     * @param moduleManipulator
     * @param upgrade
     */
    public void onUpgradeRemoved(TileModuleManipulator moduleManipulator, ItemStack upgrade);
}
