package uk.co.shadeddimensions.ep3.tileentity;

import net.minecraft.tileentity.TileEntity;
import uk.co.shadeddimensions.ep3.util.WorldCoordinates;

public class TileScannerFrame extends TileEnhancedPortals
{
    WorldCoordinates scanner;
    
    public TileScanner getScanner()
    {
        if (scanner == null)
        {
            return null;
        }
        
        TileEntity tile = scanner.getBlockTileEntity();
                
        return tile instanceof TileScanner ? (TileScanner) tile : null;
    }
}
