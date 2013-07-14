###Upcoming (1.0.8)
* Rewrote remaining GUI screens.
 * Added ability to reorganize entries in the dial device.
 * Added ability to edit entries in the dial device.

###1.0.7
* Updated to 1.6.2.
* Some GUIs have been rewritten to take advantage of new features.
 * Tabs for Redstone Control.
 * Upgrades now get removed when you click, instead of all being removed at once when you closed the GUI.
 * Minor tweaks and improvements.
* Support for the new Fluid system in Forge.
 * Modifiers using the old Liquid system will be incompatible, you must re-add any liquid textures you want applied to a portal.
* Updated Teleporter to look for both Vanilla portal blocks and EP2 portal blocks. Should hopefully resolve any issues remaining with the current system.

###1.0.6
* ZeroLevels: Optimized image files. Saved 268.50KB (56.26%)
* Now uses the OreDictionary for dyes, can use any mod dye to colour portals and use in recipes.
* Fixed rare infinite loop in the dialling devices.

###1.0.5
* Fixed issue where vanilla portals would be created instead of using custom portals when travelling to and from the Nether.

###1.0.4
* Fixed issue where portals could get left open (and could not be terminated) if you logged out while the dialling device is active.
* Fixed crash issue relating to the above.

###1.0.3
* Fix for crashing issue with dialling devices.
* Fixed crash issue if you destroyed the portal modifier while it was active with a dialling device.

###1.0.2
* Added the ability to customize what blocks are in the Nether and Resourceful frame upgrades in the config file.
* Massively overhauled the way portals are rendered. Now a wider range of effects can be implemented.
* Can now use any liquid container as a valid source of a texture.
 * Can use any liquid registered through the LiquidDictionary (should be all...) as a valid portal texture.
 * Tech addon will either be removed or repurposed for actual tech-based stuff.
* Fixed issue where the camo upgrade would not affect portal modifiers in vertical parts of the portal frame.
* Particles now get their colour from the portal texture.
 * Is only active when fancy graphics is enabled, unless changed in the config.
 * Added config options to toggle if fancy is required, and if these particles should be used.
 * Screenshots: http://imgur.com/a/nhorR
* New textures for the active side of the portal modifier.
* Added a new Redstone Control button for portal modifiers, will ignore all redstone input.
* Updated the easter eggs for the new rendering system.

###1.0.1
* Merged Advanced Dimensional and Dimensional upgrades.
 * All Adv. Dim. upgrades can be turned into Dimensional upgrades in the crafting table.
 * All Adv. Dim. upgrades in Portal Modifiers should get turned into Dimensional upgrades when loaded.
* Added a config option for alternate peaceful recipes.
* Fixed issue with the Dial Device's GUI and being unable to dial the first time in that session.
* Upgrades now stack up to 16.

###1.0
* Fixed issue with being unable to terminate the connection with a dialling device if you selected a different identifier.
* Added min/max portal limit options to config file. Defaults to unlimited.
* Finished off the commands. /ep or /enhancedportals. Can be used by anyone, used to toggle various clientside settings.
* Fixed issue where the basic dial device would not sync it's active state, resulting in the GUI not showing whether or not it is active.
* Added support for the following mods:
 * Buildcraft: Allows use of Oil and Fuel as a portal texture. *(Part of the tech addon)*
 * Thermal Expansion: Allows use of Liquid Redstone/Glowstone/Ender as a portal texture. *(Part of the tech addon)*
 * Computercraft: Allows interactivity with Portal Modifiers and both Dialling Devices. See the forum thread for methods and more info. *(seperate addon)*

###Beta 3g
* Added a lot of new configuration options. (Including the ability to turn off the second flint & steel)
* Added a new Adventure mode limitation, will stop players in adventure mode from adding/removing networks on the dial device, stop them from accessing the portal modifier, etc. Can be toggled in the config options.
* Updated localization, should have full coverage now. (There are no alternate language files, however)
* Fixed issue with Immibis' micro blocks.
* Added dependencies to latest version of Core (2c) and Forge 700. Will fix crashes due to not having the correct versions.

###Beta 3f
* Fixed issue with basic dialling devices being unable to open a portal for longer than 2.5 seconds.
* Added Kinetic upgrade, allows entities to keep their momentum when moving through portals.
* Changed the cooldown period on boats & minecarts to match the player's cooldown.

###Beta 3e_2
* Fix to random crash issue with a mod.

###Beta 3e
* Fixed issue where an exit portal was unable to be created if you had a different portal frame.
* Fixed issue where the network/identifier would overlap certain upgrade descriptions.
* Added some easter eggs.
* When a portal is created by a dialling device, the dialling device will terminate the connection if the portal is destroyed.
* Other bugfixes.

###Beta 3d
* Fix issue where you couldn't teleport to the Nether with a default portal in Survival.

###Beta 3c
* Fix relating to serverside packets.
* Attempted fix on an issue where portals would not get created.
* Updated some localization, specifically error messages when entering portals.
* Removed ComputerCraft upgrade, instead going to just have them naturally interact with computers.
* When in Creative, Portal Modifier Upgrades will not be consumed when used.

###Beta 2b
**Old saves are incompatible, you will either have to break and replace all existing modifiers and dial devices, or create a new world to continue testing.**
* Fixed not being able to teleport without a Portal Modifier.
* Overhauled portal texture system.
* Changed how the network data is saved to disk. (Now uses NBT for those interested)
* Now uses reflection to change the Obsidian block - increasing compatibility with other mods.
* Changes to texture registering.
* You can not activate a dialling device on an active portal.
* Portal Modifiers will no longer consume items they cannot use.
* Shift-Clicking on an unusable texture block in the dial device menu will no longer reset the texture.
* You can now right-click on the texture area in the dial device menu to reset the texture.
* All blocks should have their names properly displayed in the Dial Device/Portal Modifier.
* Fixed crashing issue in the dial device menu.

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