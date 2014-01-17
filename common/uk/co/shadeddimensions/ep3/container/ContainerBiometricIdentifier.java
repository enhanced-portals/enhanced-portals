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
        addSlotToContainer(new SlotBiometricIdentifier(t, 0, 8, 206));

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 88 + j * 18, 148 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++)
        {
            addSlotToContainer(new Slot(player.inventory, i, 88 + i * 18, 206));
        }
    }
}
