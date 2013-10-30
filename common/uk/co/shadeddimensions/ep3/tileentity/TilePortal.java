package uk.co.shadeddimensions.ep3.tileentity;

import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.entity.Entity;
import uk.co.shadeddimensions.ep3.tileentity.frame.TilePortalController;

public class TilePortal extends TilePortalPart
{        
    @Override
    public void breakBlock(int oldBlockID, int oldMetadata)
    {
        
    }
    
    @Override
    public void usePacket(DataInputStream stream) throws IOException
    {
        super.usePacket(stream);
        
        worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
    }
    
    @Override
    public void onEntityCollidedWithBlock(Entity entity)
    {
        if (entity.worldObj.isRemote)
        {
            return;
        }
        
        TilePortalController controller = getPortalController();
        
        if (controller != null && controller.hasConfigured && controller.isPortalActive)
        {
            controller.onEntityEnterPortal(entity, this);
        }
    }
}
