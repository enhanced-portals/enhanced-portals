package uk.co.shadeddimensions.ep3.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import uk.co.shadeddimensions.ep3.tileentity.TileScanner;

public class ContainerScanner extends Container
{
    public ContainerScanner(TileScanner scanner, EntityPlayer player)
    {
        
    }
    
    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return true;
    }
}
