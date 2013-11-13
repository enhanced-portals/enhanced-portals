package cofh.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyHandler;

public class EnergyHelper {

	private EnergyHelper() {

	}

	public static boolean chargeStorageWithContainer(World world, IEnergyHandler handler, EntityPlayer player) {

		ItemStack container = player.getCurrentEquippedItem();

		if (isEnergyContainerItem(container)) {

		}
		return false;
	}

	public static boolean chargeContainerFromStorage(World world, IEnergyHandler handler, EntityPlayer player, int energy) {

		ItemStack container = player.getCurrentEquippedItem();

		if (isEnergyContainerItem(container)) {

		}
		return false;
	}

	public static int chargeAdjacentEnergyHandler(TileEntity tile, int from, int energy, boolean simulate) {

		TileEntity handler = BlockHelper.getAdjacentTileEntity(tile, from);

		if (handler instanceof IEnergyHandler) {
			return ((IEnergyHandler) handler).receiveEnergy(ForgeDirection.VALID_DIRECTIONS[from].getOpposite(), energy, simulate);
		}
		return 0;
	}

	public static boolean isEnergyContainerItem(ItemStack container) {

		return container != null && container.getItem() instanceof IEnergyContainerItem;
	}

	public static boolean isAdjacentEnergyHandler(TileEntity tile, int from) {

		return BlockHelper.getAdjacentTileEntity(tile, from) instanceof IEnergyHandler;
	}

	public static boolean isAdjacentEnergyHandlerFromSide(TileEntity tile, int from) {

		TileEntity handler = BlockHelper.getAdjacentTileEntity(tile, from);

		if (handler instanceof IEnergyHandler) {
			return ((IEnergyHandler) handler).canInterface(ForgeDirection.VALID_DIRECTIONS[from].getOpposite());
		}
		return false;
	}

	public static boolean isEnergyHandlerFromSide(TileEntity tile, ForgeDirection from) {

		if (tile instanceof IEnergyHandler) {
			return ((IEnergyHandler) tile).canInterface(from);
		}
		return false;
	}

}
