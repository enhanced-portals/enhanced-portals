package alz.mods.enhancedportals.common;

import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class WorldLocation
{
	public World worldObj;
	public IBlockAccess blockAccess;
	public int xCoord, yCoord, zCoord;

	public WorldLocation()
	{

	}

	public WorldLocation(IBlockAccess world, int x, int y, int z)
	{
		blockAccess = world;
		xCoord = x;
		yCoord = y;
		zCoord = z;
	}

	public WorldLocation(World world, int x, int y, int z)
	{
		worldObj = world;
		xCoord = x;
		yCoord = y;
		zCoord = z;
	}

	public WorldLocation(int dimID, int x, int y, int z)
	{
		worldObj = MinecraftServer.getServer().worldServerForDimension(dimID);
		xCoord = x;
		yCoord = y;
		zCoord = z;
	}

	public WorldLocation(TileEntity tileEntity)
	{
		worldObj = tileEntity.worldObj;
		xCoord = tileEntity.xCoord;
		yCoord = tileEntity.yCoord;
		zCoord = tileEntity.zCoord;
	}

	public WorldLocation(int x, int y, int z)
	{
		xCoord = x;
		yCoord = y;
		zCoord = z;
	}

	public boolean equals(WorldLocation location)
	{
		return xCoord == location.xCoord && yCoord == location.yCoord && zCoord == location.zCoord && worldObj == location.worldObj;
	}

	public static boolean equals(WorldLocation location, WorldLocation location2)
	{
		return location2.xCoord == location.xCoord && location2.yCoord == location.yCoord && location2.zCoord == location.zCoord && location2.worldObj == location.worldObj;
	}

	public int getBlockId()
	{
		if (worldObj == null)
			return 0;

		return worldObj.getBlockId(xCoord, yCoord, zCoord);
	}

	public int getBlockMeta()
	{
		if (worldObj == null)
			return 0;

		return worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
	}

	public TileEntity getBlockTileEntity()
	{
		if (worldObj == null)
		{
			if (blockAccess == null)
				return null;
			else
				return blockAccess.getBlockTileEntity(xCoord, yCoord, zCoord);
		}

		return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord);
	}

	public void setBlock(int id)
	{
		if (worldObj == null)
			return;

		if (id == 0)
		{
			worldObj.setBlockToAir(xCoord, yCoord, zCoord);
		}
		else
		{
			worldObj.setBlock(xCoord, yCoord, zCoord, id);
		}
	}

	public void setBlockToAir()
	{
		if (worldObj == null)
			return;

		worldObj.setBlock(xCoord, yCoord, zCoord, 0, 0, 2);
	}

	public void markBlockForUpdate()
	{
		if (worldObj == null)
			return;

		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	public WorldLocation getOffset(ForgeDirection direction)
	{
		WorldLocation newLocation = new WorldLocation();
		newLocation.worldObj = worldObj;
		newLocation.blockAccess = blockAccess;
		newLocation.xCoord = xCoord + direction.offsetX;
		newLocation.yCoord = yCoord + direction.offsetY;
		newLocation.zCoord = zCoord + direction.offsetZ;

		return newLocation;
	}
}
