EnhancedPortals 3
---
Useful Links:
* [Downloads](http://shadeddimensions.co.uk/enhancedportals-3/downloads/)
* [Changelog](http://shadeddimensions.co.uk/enhancedportals-3/changelog/)
* [Minecraft Forums Thread](http://www.minecraftforum.net/topic/2143651-164-enhancedportals-3/)


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