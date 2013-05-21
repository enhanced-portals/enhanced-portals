##EnhancedPortals 2 - Changelog

####To Finish Before Final Release:
* Client-side commands.
* Server op commands.
* Localization.
* Rename en_US language to en_GB and create US version, spellings etc.

####Post-Release
* EnhancedPortals 2 - World
 * Naturally spawning ruins containing loot and portals
 * Portals will have a chance of being connected to others
 * Extremely rare 'portal hubs' will spawn, they will be connected to up to five different smaller areas via portals
 * Not all Overworld portals will link to the Overworld...
* Forestry, Thermal Expansion addons
 * Ability to use their liquids as a portal texture
 * Restrictions on stupid-looking blocks for portal textures

###Beta 2b (Upcoming)
**Old saves are incompatible, you will either have to break and replace all existing modifiers and dial devices, or create a new world to continue testing.**
* Fixed not being able to teleport without a Portal Modifier.
* Overhauled portal texture system.
* Changed how the network data is saved to disk. (Now uses NBT for those interested)
* Now uses reflection to change the Obsidian block - increasing compatibility with other mods.
* Changes to texture registering.

###Beta 2a
* Networking improvements
* Added Modifier Camouflage upgrade.
* Added two types of Dialling Devices.
 * **Basic Dial Device**
  * Requires manual selection of the Glyphs every time you want to open a portal.
  * Stays open for 38 seconds.
  * Unable to modify the portals textures or effects via the device. It will use all the data currently stored within the Portal Modifier.
  * **Dial Device**
  * Stores a list of network destinations.
  * Able to modify the portals texture/effects, seperately for each destination.
* Added Automatic Dialler.
 * Must be directly attached to a basic dial device.
 * Able to store one network.
 * When recieving a redstone signal, will dial the selected network.
* Network Cards can now be used on the Automatic Dialler.

###Beta 1c
* Fixed potion Glyph - now is Potion of Healing.
* Added language support.
* You can now right-click on the Portal Modifier with a Flint & Steel to create a portal.
* You can now right-click on Obsidian Stairs with a Flint & Steel to create a portal.
* Fixed issue where invalid data would appear in the data files.
* Added new item: Network Card. Allows you to copy the network in a Portal Modifier and transfer it to another.
* A few system changes:
 * Mod ID has been slightly changed.
 * Configuration file is now 'EnhancedPortals 2.cfg'.
 * If you're upgrading you will get 'mismatched ID' errors, that's normal.
* Fixed crashing issue with TMI & NEI.

###Beta 1b
* Changed Quartz upgrade to Nether Frame Upgrade. Now allows you to use Quartz, Nether Brick and Glowstone.
* Added Resourceful frame upgrade. Allows you to use Iron, Gold and Diamond blocks.
* You can now shift-click upgrades into the portal modifier from the GUI.
* Fixed issue where you could get stuck in the ground or appear on the wrong face of the portal modifier.
* Attempted fix for the issue where it can't find an exit modifier. Added some debug code that will print extra information if this happens to you, please report it back.
* Fixed crash when shift-clicking on the top left most 8 inventory slots.

###Beta 1a
* Portal Modifiers and their new Networking system using Glyphs.
* Overhauled portal systems.
* Ability to change the texture of the portal modifiers to blocks.(Using a Portal Modifier)
* Ability to change the thickness of the portal blocks. (Using a Portal Modifier)
* New upgrades for the Portal Modifier.
 * Particle Control. Allows you to toggle the particles of the portal.
 * Sound Control. Allows you to toggle the sound of the portal.
 * Quartz Upgrade. Allows you to use Nether Quartz (all variations) as a portal frame.