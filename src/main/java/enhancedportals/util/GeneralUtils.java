package enhancedportals.util;

import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.util.ForgeDirection;

public class GeneralUtils {

    public static ChunkCoordinates offset(ChunkCoordinates c, ForgeDirection f) {
        return new ChunkCoordinates(c.posX + f.offsetX, c.posY + f.offsetY, c.posZ + f.offsetZ);
    }

}
