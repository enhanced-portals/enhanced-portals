##The Basics
The most basic thing you can do in this mod is create any size/shape portals and dye them any of the sixteen different Minecraft colours. To do this, simply create a frame of Obsidian and light it as you normally would. To change the colour of the Portal, right-click it with that colour dye in your hand. To reset the portal colour to it's default, use a purple dye on the portal.

When creating a portal there are a few restrictions. When using a default Flint & Steel you will be restricted to creating a portal that consists of 6 portal blocks, no more, no less. This is to make it a little more balanced early-game, even though it doesn't take much to get to the Nether for the first time. Once you've been to the Nether, you can then upgrade your Flint & Steel with two pieces of Glowstone. Not only will this double it's durability (to 128 uses), but it will allow you to create a portal of any size.

If you wish, you can also create Obsidian Stairs, crafted as you would expect.

##The Portal Modifier
The Portal Modifier is a block that allows you to modify your portals in various different ways. Simply right-click on it to open up it's GUI. There are a few different areas that I will explain seperately.

**Network**

When you set a network, the portal modifier will link together with all of the other Portal Modifiers on that same network. With the correct upgrades (see below), you will be able to teleport from one portal to another, instead of going directly to the nether and back.

Network Selection Tips:
* You can left-click on a Glyph to add it to the end of the list or right-click to remove the last-added Glyph of that type.
* You can right-click on a Glyph in the selected network area to remove that Glyph in that location.
* You can hold down Ctrl while clicking 'Random' to force the random selection of Glyphs to use all 9 slots.
* You can hold down Shift to see a text representation of the selected Glyphs.
* You can hold down Shift and the random button will turn into clear, clearing the selected Glyphs.

**Upgrades**

There are a number of different upgrades available for the Portal Modifier. When installed, they will change certain aspects of the portal that the Portal Modifier creates. The upgrades (as of Beta 3) are as follows:
* **Particle Control** 
This upgrade stops the portal from creating particles.
* **Sound Control** 
This upgrade stops the portal from playing sounds.
* **Dimensional** 
This upgrade allows the portal to connect to a different portal modifier in any vanilla dimension.
* **Advanced Dimensional** 
This upgrade allows the portal to connect to a different portal modifier in any dimension, or the same dimension.
* **Technological** 
Currently unused, but will be used for ComputerCraft support in the near future.
* **Nether Frame** 
Allows you to use Glowstone, Nether Brick & Quartz in your portal frame.
* **Resourceful Frame** 
Allows you to use Iron, Gold, Diamond and Emerald blocks in your portal frame.
* **Modifier Camouflage** 
Camouflages the Portal Modifier to look like the neighboring frame.
* **Dialling Upgrade** 
Allows the use of Dialling Devices with this portal modifier. When installed, the network will be reset and instead will require a unique identifier. (I'll get into this when I explain Dialling Devices.)

**Modifications**

This section is quite simple, the thickness button will allow you to change the thicknes of the portal, and the facade will allow you to change the texture. The Facade slot works exactly the same as a fake item slot in mods like BuildCraft. You can shift-click an item or pick it up and click on it to change the texture of the portal to that block/item. The item will not be consumed in this process. You can also right-click on it with nothing selected to reset it back to the default purple texture (or use a purple dye on it.)

There are a number of restricted blocks from being portal textures, and most items will not work. The following items work successfully:
* All dyes.
* Lava & Water buckets.

I will be working on a number of different addons that integerate EnhancedPortals with other various mods which will allow you to use some of their special textures, such as liquids, as portal textures. (For example, Fuel, Oil, Liquid Glowstone/Redstone/Ender)

**Redstone Control**

Finally there are three buttons to the right of the GUI which control the state of the redstone, which act as you would expect them to.

##The Dialling Devices
###Basic Dial Device
When used with a Portal Modifier with the Dialling upgrade within a 5-block range, the dialling device can 'dial' another portal without changing the settings of the portal modifier. When using a basic dialling device you cannot change the texture, thickness or duration of the portal, you can only change the destination. The thickness and texture of the portal will be selected by the portal modifier's setting.

###Dial Device
This version of the Dialling Device is the same as the basic dialling device, but more advanced and has more features. It can store an unlimited number of different identifiers (or the same identifier... If you want), and you can change the duration of how long the portal stays open before it will automatically close (or not). To increase the tick delay, left click and to decrease, right click.

For each stored identifier you can customise a seperate thickness and portal texture, regardless of the Portal Modifier's settings.

The maximum amount of time you can have the portal open for automatically is 1200 ticks, or 60 seconds. If you click again after that it will set it to don't automatically shutdown. The minimum amount of time you can set it to is 20 ticks, or 1 second. If you right-click after that then again, it will set itself to not automatically shutting down.

This is fairly self-explanatory, so a few tips:
* You can hold Ctrl and click to increase/decrease the ticks by 20 (1 second).
* You can hold Shift and click to increase/decrease the ticks by 10 (0.5 seconds).
* You can hold Ctrl & Shift and click to increase/decrease the ticks by 100 (5 seconds).