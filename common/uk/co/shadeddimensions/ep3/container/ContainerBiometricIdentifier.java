package uk.co.shadeddimensions.ep3.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import uk.co.shadeddimensions.ep3.client.gui.slot.SlotBiometricIdentifier;
import uk.co.shadeddimensions.ep3.tileentity.frame.TileBiometricIdentifier;
import uk.co.shadeddimensions.library.container.ContainerBase;

public class ContainerBiometricIdentifier extends ContainerBase
{
    public ContainerBiometricIdentifier(TileBiometricIdentifier t, EntityPlayer player)
    {
        super(t);

        addSlotToContainer(new SlotBiometricIdentifier(t, 0, 8, 176));
        addSlotToContainer(new SlotBiometricIdentifier(t, 1, 276, 176));

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
}
