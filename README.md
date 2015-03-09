![logo](http://mods.atomicbase.com/enhancedportals/forum_files/ep_banner.png)
[![Build Status](https://travis-ci.org/enhanced-portals/enhanced-portals.svg?branch=master)](https://travis-ci.org/enhanced-portals/enhanced-portals)

Useful Links:
* [License](docs/LICENSE)
* [Downloads](http://www.curse.com/mc-mods/minecraft/225921-enhanced-portals-3#t1:other-downloads)
* [Changelog](docs/changelog/)
* [Minecraft Forums Thread](http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/1292751)


###Compiling

1. Clone EnhancedPortals into any directory.
2. Open up a command line or terminal window and navigate to that directory.
3. Execute `gradlew build`.
4. If `BUILD SUCCESSFUL` appears, you'll find the `EnhancedPortals-{mcversion}-{version}.jar` in `build\libs\`.


###Developing

1. Fork EnhancedPortals and clone it into any directory.
2. Open up a command line or terminal window and navigate to that directory.
3. Execute `gradlew setupDecompWorkspace eclipse` or `gradlew setupDecompWorkspace idea`.
4. Open up your IDE and add `./src/api/java/` as a Source Folder.