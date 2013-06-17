##EnhancedPortals 2
[Minecraft Forums Thread](http://www.minecraftforum.net/topic/1301217-)

###Setting up the development environment
Your directory structure should look something like this. If you have everything set up in a different place, you will need to edit the `build.properties` file to reflect your setup.

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
			\*
		\enhanced-core\
			\common\
			\*
		\computercraft-api\
			\*
***

An example `build.properties` file:

***
dir.mcp=../../mcp
dir.mcp.src=../../mcp/src/minecraft
dir.mcp.reobf=../../mcp/reobf/minecraft
dir.main.source=../../source/enhanced-portals/common
dir.main.resources=../../source/enhanced-portals/resources
dir.core.source=../../source/enhanced-core/common
dir.core.resources=../../source/enhanced-core/resources
dir.ccapi=../../source/computercraft-api
dir.release=../../releases
version.mc=1.5.2
version.main=1.0.1
version.cc=1.0
version.tech=1.0.1
***