package alz.mods.enhancedportals.common;

import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class WorldLocation
{
	public World worldObj;
	public int xCoord, yCoord, zCoord;
		
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
		return ((xCoord == location.xCoord) && (yCoord == location.yCoord) && (zCoord == location.zCoord) && (worldObj == location.worldObj));
	}
	
	public static boolean equals(WorldLocation location, WorldLocation location2)
	{
		return ((location2.xCoord == location.xCoord) && (location2.yCoord == location.yCoord) && (location2.zCoord == location.zCoord) && (location2.worldObj == location.worldObj));
	}
	
	public int getBlockId()
	{
		if (worldObj == null)
		{
			return 0;
		}
		
		return worldObj.getBlockId(xCoord, yCoord, zCoord);
	}
	
	public TileEntity getBlockTileEntity()
	{
		if (worldObj == null)
		{
			return null;
		}
		
		return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord);
	}
	
	public void setBlock(int id)
	{
		if (worldObj == null)
		{
			return;
		}
		
		if (id == 0)
		{
			worldObj.setBlockToAir(xCoord, yCoord, zCoord);
		}
		else
		{
			worldObj.setBlock(xCoord, yCoord, zCoord, id);
		}
	}
	
	public void markBlockForUpdate()
	{
		if (worldObj == null)
		{
			return;
		}
		
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
}
