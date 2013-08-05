package uk.co.shadeddimensions.enhancedportals.tileentity;

import net.minecraft.tileentity.TileEntity;
import uk.co.shadeddimensions.enhancedportals.util.Texture;

public class TilePortal extends TileEntity
{    
    public Texture texture;
    
    public TilePortal()
    {
        texture = new Texture();
    }
}
