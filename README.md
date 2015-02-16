![logo](http://media-curse.cursecdn.com/attachments/133/259/6f29ad907748ceb667c287e29d57c0a3.png)

Useful Links:
* [License](docs/LICENSE)
* [Downloads](http://www.curse.com/mc-mods/minecraft/225921-enhanced-portals-3#t1:other-downloads)
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