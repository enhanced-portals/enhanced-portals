package cofh.util.fluid;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public final class DispenserEmptyBucketHandler extends BehaviorDefaultDispenseItem {

	private final BehaviorDefaultDispenseItem defaultDispenserItemBehavior = new BehaviorDefaultDispenseItem();

	@Override
	public ItemStack dispenseStack(IBlockSource blockSource, ItemStack stackBucket) {

		EnumFacing facing = BlockDispenser.getFacing(blockSource.getBlockMetadata());
		World world = blockSource.getWorld();

		int x = blockSource.getXInt() + facing.getFrontOffsetX();
		int y = blockSource.getYInt() + facing.getFrontOffsetY();
		int z = blockSource.getZInt() + facing.getFrontOffsetZ();

		ItemStack filledBucket = BucketHandler.fillBucket(world, x, y, z);

		if (filledBucket == null) {
			int bMeta = world.getBlockMetadata(x, y, z);

			if (bMeta == 0) {
				int bId = world.getBlockId(x, y, z);

				if (bId == Block.waterStill.blockID || bId == Block.waterMoving.blockID) {
					filledBucket = new ItemStack(Item.bucketWater);
					world.setBlockToAir(x, y, z);
				} else if (bId == Block.lavaStill.blockID || bId == Block.lavaMoving.blockID) {
					filledBucket = new ItemStack(Item.bucketLava);
					world.setBlockToAir(x, y, z);
				}
			}
		}
		if (filledBucket == null) {
			return defaultDispenserItemBehavior.dispense(blockSource, stackBucket);
		}
		if (--stackBucket.stackSize == 0) {
			stackBucket = filledBucket.copy();
		} else if (((TileEntityDispenser) blockSource.getBlockTileEntity()).addItem(filledBucket) < 0) {
			return defaultDispenserItemBehavior.dispense(blockSource, stackBucket);
		}
		return stackBucket;
	}

}
