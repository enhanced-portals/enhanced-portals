package uk.co.shadeddimensions.ep3.client.renderer;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import uk.co.shadeddimensions.ep3.tileentity.TileStabilizer;

public class TileStabilizerRenderer extends TileEntitySpecialRenderer
{    
    public TileStabilizerRenderer()
    {
        
    }
    
    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f)
    {
        TileStabilizer stabilizer = (TileStabilizer) tile;
        
        if (stabilizer.hasConfigured && stabilizer.getMainBlock().getChunkCoordinates().equals(stabilizer.getChunkCoordinates()))
        {
            
        }
    }
}
