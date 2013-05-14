##EnhancedPortals 2
[Minecraft Forums Thread](http://www.minecraftforum.net/topic/1301217-)

**Latest Beta**: [1.0 Beta 1b](http://goo.gl/VVjDa).

**Release Notes**:
* Changed Quartz upgrade to Nether Frame Upgrade. Now allows you to use Quartz, Nether Brick and Glowstone.
* Added Resourceful frame upgrade. Allows you to use Iron, Gold and Diamond blocks.
* You can now shift-click upgrades into the portal modifier from the GUI.
* Fixed issue where you could get stuck in the ground or appear on the wrong face of the portal modifier.
* Attempted fix for the issue where it can't find an exit modifier. Added some debug code that will print extra information if this happens to you, please report it back.
* Fixed crash when shift-clicking on the top left most 8 inventory slots.

###Setting up the development environment
The build scripts are set up in the exact same way as the Equivalent Exchange 3 scripts.

When setting up your development environment, follow the guide in [Equivalent Exchange 3's readme file](https://github.com/pahimar/Equivalent-Exchange-3/blob/master/README.md), but obviously replace ee3 with enhanced-portals.
When done, your directory structure should look like this:

***
	development
	\mcp
		\forge\
		\jars\
		\mcp files
	\source
		\enhanced-portals\
			\common\
			\resources\
			\README.md
			\other files
		\computercraft-api\
			\*
		\thermalexpansion-api\
			\*
***