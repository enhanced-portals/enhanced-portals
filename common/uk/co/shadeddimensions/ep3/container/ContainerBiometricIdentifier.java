package uk.co.shadeddimensions.ep3.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileBiometricIdentifier;

public class ContainerBiometricIdentifier extends ContainerEnhancedPortals
{
    public ContainerBiometricIdentifier(TileBiometricIdentifier t, EntityPlayer player)
    {
        super(t.getSizeInventory(), t);
        
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 70 + j * 18, 118 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++)
        {
            addSlotToContainer(new Slot(player.inventory, i, 70 + i * 18, 176));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return ((TileBiometricIdentifier) tile).isUseableByPlayer(entityplayer);
    }
}
