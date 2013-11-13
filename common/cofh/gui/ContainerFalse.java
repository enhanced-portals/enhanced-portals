package cofh.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public final class ContainerFalse extends Container {

	@Override
	public boolean canInteractWith(EntityPlayer player) {

		return false;
	}

}
