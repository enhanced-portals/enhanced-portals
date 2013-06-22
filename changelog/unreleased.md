###EnhancedPortals 1.x.y
* Added the ability to customize what blocks are in the Nether and Resourceful frame upgrades in the config file.
* Massively overhauled the way portals are rendered. Now a wider range of effects can be implemented.
* Can now use any liquid container as a valid source of a texture.
 * Can use any liquid registered through the LiquidDictionary (should be all...) as a valid portal texture.
 * Tech addon will either be removed or repurposed for actual tech-based stuff.
* Fixed issue where the camo upgrade would not affect portal modifiers in vertical parts of the portal frame.
* Particles now get their colour from the portal texture.
 * I'm unsure on how this will affect lower-end systems, so it is only enabled when fancy graphics is. Though, it should be fine and it looks awesome.
 * Added config options to toggle if fancy is required, and if these particles should be used.